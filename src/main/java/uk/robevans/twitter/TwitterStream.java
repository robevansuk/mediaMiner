package uk.robevans.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.robevans.twitter.model.ATweet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Arrays.asList;

/**
 * Created by robevans.uk on 21/07/2017.
 */
@Component
public class TwitterStream {

    final static Logger log = LoggerFactory.getLogger(TwitterStream.class);

    String apiKey;
    String apiSecret;
    String accessToken;
    String accessTokenSecret;

    Client hosebirdClient;

    UserStreams userStreams;
    SearchTerms searchTerms;
    List<String> allSearchTerms;
    List<String> individualSearchTerms;
    List<Long> individualUsersToFollow;

    Map<String, Integer> indexForSearchTerm;
    BlockingQueue<String> queue;
    List<Integer> positiveCounts;
    List<Integer> negativeCounts;

    // You can only use a single filter for all the search terms you're monitoring.
    // otherwise you will see " Error connecting w/ status code - 420, reason - Enhance Your Calm"
    // .. which is essentially asking you to cut it out because they only allow one stream.
    StatusesFilterEndpoint searchFilter;

    /**
     * Constructor for testing - this will not start the client
     */
    public TwitterStream(SearchTerms searchTerms, UserStreams userStreams) {
        this.searchTerms = searchTerms;
        this.userStreams = userStreams;
        initDataStructures();
    }

    @Autowired
    public TwitterStream(@Value("${twitter.api_key}") String apiKey,
                         @Value("${twitter.api_secret}") String apiSecret,
                         @Value("${twitter.access_token}") String accessToken,
                         @Value("${twitter.access_token_secret}") String accessTokenSecret,
                         SearchTerms searchTerms,
                         UserStreams userStreams) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;

        if (apiKey==null || apiKey.equals("")) {
            apiKey = System.getenv("twitter.api_key");
        }
        if (apiSecret==null || apiSecret.equals("")) {
            apiSecret = System.getenv("twitter.api_secret");
        }
        if (accessToken==null || accessToken.equals("")) {
            apiKey = System.getenv("twitter.access_token");
        }
        if (accessTokenSecret==null || accessTokenSecret.equals("")) {
            apiSecret = System.getenv("twitter.access_token_secret");
        }
        log.info(apiKey);
        log.info(apiSecret);
        this.searchTerms = searchTerms;
        this.userStreams = userStreams;
        initDataStructures();
        start();
    }

    private void start() {
        connect();
        read();
    }

    public void initDataStructures() {
        this.indexForSearchTerm = new HashMap<>();
        this.positiveCounts = new ArrayList<>();
        this.negativeCounts = new ArrayList<>();
        this.searchFilter = new StatusesFilterEndpoint();

        this.individualSearchTerms = searchTerms.individualSearchTerms();
        this.allSearchTerms = asList(convertListOfMultipleTermsToListOfSingleTerms(individualSearchTerms));
        this.individualUsersToFollow = userStreams.getUserIds();

        initPositiveCounters(searchTerms.getSearchTerms());
        initNegativeCounters(searchTerms.getSearchTerms());
        initSearchTermIndexList(searchTerms.getSearchTerms());
    }

    private void connect() {
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        Authentication hosebirdAuth = new OAuth1(apiKey, apiSecret, accessToken, accessTokenSecret);

        this.queue = new LinkedBlockingQueue<>(100);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(100);

        searchFilter.trackTerms(allSearchTerms);
        searchFilter.followings(individualUsersToFollow);

        ClientBuilder clientBuilder = new ClientBuilder()
                .name("Hosebird-Client-01")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(searchFilter)
                .processor(new StringDelimitedProcessor(queue))
                .eventMessageQueue(eventQueue);

        hosebirdClient = clientBuilder.build();

        // Attempts to establish a connection.
        hosebirdClient.connect();
    }

    public String convertListOfMultipleTermsToListOfSingleTerms(List<String> terms) {
        StringBuilder builder = new StringBuilder("");
        int i = 0;
        for (String term : terms) {
            builder.append(term);
            if (i != terms.size() - 1) {
                builder.append(",");
            }
            i++;
        }
        return builder.toString();
    }

    /**
     * if we have two search terms in a group - e.g. litecoin,ltc
     * we only want one counter for both terms.
     *
     * @param terms
     */
    public void initPositiveCounters(List<String> terms) {
        for (int i = 0; i < terms.size(); i++) {
            positiveCounts.add(0);
        }
    }

    /**
     * if we have two search terms in a group - e.g. litecoin,ltc
     * we only want one counter for both terms.
     *
     * @param terms
     */
    public void initNegativeCounters(List<String> terms) {
        for (int i = 0; i < terms.size(); i++) {
            negativeCounts.add(0);
        }
    }

    /**
     * if we have two search terms in a group - e.g. litecoin,ltc
     * we only want one index for both terms.
     * <p>
     * Here we need to break apart the search terms and add the same index
     * for each term
     *
     * @param terms
     */
    public void initSearchTermIndexList(List<String> terms) {
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).contains(",")) {
                String[] individualTerms = terms.get(i).split(",");
                for (String individualTerm : individualTerms) {
                    // add the same index for each term
                    indexForSearchTerm.put(individualTerm, i);
                }
            } else {
                // only a single index required.
                indexForSearchTerm.put(terms.get(i), i);
            }
        }
    }

    public void read() {
        while (!hosebirdClient.isDone()) {
            getMessage();
        }
    }

    public void getMessage() {
        final String msg = takeMsgFromQueue(queue);
        final ATweet aTweet = parseJsonToTweet(msg);

        if (aTweet == null || aTweet.getText() == null) {
            return;
        }

        int poi = isPoi(aTweet);
        String tweetText = getTweet(aTweet);
        String twitterUser =  aTweet.getUser().getScreen_name();


        String sentiment = ":|";
        createEntryIfPoi(aTweet, tweetText, twitterUser, sentiment, poi);

        String searchTermsMatched = findMatchedSearchTerms(tweetText);
        checkForPositiveSentiment(tweetText, twitterUser, searchTermsMatched);
        checkForNegativeSentiment(tweetText, twitterUser, searchTermsMatched);

//        boolean found = checkForSearchTerms(tweetText, twitterUserUrl, searchTermsMatched, sentiment, poi);

    }

    private void createEntryIfPoi(ATweet aTweet, String tweetText, String twitterUserUrl, String sentiment, int poi) {
        logIfPoi(tweetText, twitterUserUrl, poi);
        try {
            if (poi == 1) {
                String url = createInsertUrl(tweetText, twitterUserUrl, "", sentiment, poi);
                log.debug(url);
                insertTweet(url);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private int isPoi(ATweet aTweet) {
        return individualUsersToFollow.contains(new Long(aTweet.getUser().getId())) ? 1 : 0;
    }

    private void logIfPoi(String tweetText, String twitterUserUrl, int poi) {
        if (poi == 1) {
            log.info("*** {}", twitterUserUrl);
            log.info("*** {}", tweetText);
            // continue to process it like any other tweet after this.
        }
    }

    private String takeMsgFromQueue(BlockingQueue<String> queue) {
        String msgFromQueue = "";
        try {
            msgFromQueue = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msgFromQueue;
    }

    private ATweet parseJsonToTweet(String msg) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(msg, ATweet.class);
        } catch (JsonSyntaxException e) {
            //e.printStackTrace();
            log.error("Failed to convert message to Tweet: {}", msg);
            Field[] fields = ATweet.class.getDeclaredFields();

            for (Field field : fields) {
                String fieldName = field.getName();
                if (!msg.contains("\"" + fieldName + "\":")) {
                    log.info("Missing: " + fieldName);
                }
            }
            return null;
        }
    }

    public String findMatchedSearchTerms(String tweetText) {
        StringBuilder searchTermsMatched = new StringBuilder("");
        for (int i = 0; i < individualSearchTerms.size(); i++) {
            String keyWord = individualSearchTerms.get(i);
            if (tweetText.toLowerCase().contains(keyWord)) {
                searchTermsMatched.append(keyWord + ",");
            }
        }
        if (searchTermsMatched.length()>0)
            searchTermsMatched.deleteCharAt(searchTermsMatched.length()-1);
        return searchTermsMatched.toString();
    }

    private String getTweet(ATweet aTweet) {
        return aTweet.getText()
                .replace("\n", "")
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("@", "\\@");
    }

    private void checkForPositiveSentiment(String tweetText, String twitterUser, String searchTermsMatched) {
        try {
            if (isPositiveMessage(tweetText)) {
                log.info(":) - {} --- {} | {}", searchTermsMatched, "https://www.twitter.com/" + twitterUser, tweetText);
                String url = createInsertUrl(tweetText, twitterUser, searchTermsMatched, ":)", 0);
                log.debug(url);
                insertTweet(url);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


//        createInsertUrl(tweetText, twitterUserUrl, )
//        int newCount = positiveCounts.get(counterIndex) + 1;
//        positiveCounts.set(counterIndex, newCount);
//        String updatedCounter = newCount + "";

//        if (aTweet.getRetweet_count() >= 10) {
//            log.info("10 :) - {} - {} --- {} | {}", searchTermFound, updatedCounter, tweetMade, twitterUserUrl);
//        } else {
//            log.info(":) - {} - {} --- {} | {}", searchTermFound, updatedCounter, tweetText, twitterUserUrl);
//        }

//        return true;
    }

    private String getTwitterUserUrl(ATweet aTweet) {
        return "https://www.twitter.com/" + aTweet.getUser().getScreen_name();
    }

    private void checkForNegativeSentiment(String tweetText, String twitterUser, String searchTermsMatched) {

        try {
            if (isNegativeMessage(tweetText)) {
                log.info(":( - {} --- {} | {}", searchTermsMatched, "https://www.twitter.com/" + twitterUser, tweetText);
                String url = createInsertUrl(tweetText, twitterUser, searchTermsMatched, ":(", 0);
                log.debug(url);
                insertTweet(url);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        int newCount = negativeCounts.get(searchTermIndexToIncrement) + 1;
//        negativeCounts.set(searchTermIndexToIncrement, newCount);
//        String searchTermFound = individualSearchTerms.get(i);
//        String updatedCounter = newCount + "";
//        String twitterUserUrl = twitterUser;
//        return true;
    }

    private boolean isNegativeMessage(String msg) {
        return msg.contains(" :( ") || msg.contains(" :-( ")
                || msg.contains(" :(( ") || msg.contains(" :-(( ")
                || msg.contains(" :'( ") || msg.contains(" :'-( ")
                || msg.contains(" ): ")
                || msg.contains(" )-: ");
    }

    private boolean isPositiveMessage(String msg) {
        return msg.contains(" :) ") || msg.contains(" :-) ")
                || msg.contains(":D") || msg.contains(" :-D ")
                || msg.contains(":d") || msg.contains(" :-d ")
                || msg.contains("XD") || msg.contains(" X-D ")
                || msg.contains("Xd") || msg.contains(" X-d ")
                || msg.contains(" (: ")
                || msg.contains(" (-: ")
                || msg.contains("😀"); //smiley emoji
    }

    public StatusesFilterEndpoint filterByLocation(
            double leftLong,
            double leftLat,
            double rightLong,
            double rightLat
    ) {
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        Location location = new Location(new Location.Coordinate(leftLong, leftLat), new Location.Coordinate(rightLong, rightLat));
        return endpoint.locations(asList(location)); // This will limit subscription to tweets only from the specified box.
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public void setSearchTerms(SearchTerms searchTerms) {
        this.searchTerms = searchTerms;
    }

    public SearchTerms getSearchTerms() {
        return searchTerms;
    }

    public List<String> getIndividualSearchTerms() {
        return individualSearchTerms;
    }

    public Map<String, Integer> getIndexForSearchTerm() {
        return indexForSearchTerm;
    }

    public List<Integer> getPositiveCounts() {
        return positiveCounts;
    }

    public List<Integer> getNegativeCounts() {
        return negativeCounts;
    }

    // HTTP GET request
    private void insertTweet(String url) {
        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

//            String inputLine;
//            while ((inputLine = in.readLine()) != null)
//               log.info(inputLine);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    private String createInsertUrl(String tweet, String user, String currency, String sentiment, int poi) throws UnsupportedEncodingException {
//        log.info("TWEET: {}", tweet);
//        log.info("USER: {}", user);
//        log.info("COIN: {}", currency);
//        log.info("SENTIMENT: {}", sentiment);
//        log.info("POI: {}", poi);

        return "http://www.robevans.uk/test.php?"
                + "tweet=" + URLEncoder.encode(tweet, StandardCharsets.UTF_8.toString())
                + "&user=" + URLEncoder.encode(user, StandardCharsets.UTF_8.toString())
                + "&coin=" + URLEncoder.encode(currency, StandardCharsets.UTF_8.toString())
                + "&sentiment=" + URLEncoder.encode(sentiment, StandardCharsets.UTF_8.toString())
                + "&poi=" + URLEncoder.encode(poi+"", StandardCharsets.UTF_8.toString());
    }
}
