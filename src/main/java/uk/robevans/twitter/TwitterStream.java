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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.robevans.twitter.model.ATweet;

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

    String apiKey;
    String apiSecret;
    String accessToken;
    String accessTokenSecret;

    Client hosebirdClient;

    SearchTerms searchTerms;
    List<String> allSearchTerms;
    List<String> individualSearchTerms;

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
    public TwitterStream(SearchTerms searchTerms) {
        this.searchTerms = searchTerms;
        initDataStructures();
    }

    @Autowired
    public TwitterStream(@Value("${twitter.api_key}") String apiKey,
                         @Value("${twitter.api_secret}") String apiSecret,
                         @Value("${twitter.access_token}") String accessToken,
                         @Value("${twitter.access_token_secret}") String accessTokenSecret,
                         SearchTerms searchTerms) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;

        this.searchTerms = searchTerms;
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
        int i=0;
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
     *
     * Here we need to break apart the search terms and add the same index
     * for each term
     * @param terms
     */
    public void initSearchTermIndexList(List<String> terms) {
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).contains(",")) {
                String[] individualTerms = terms.get(i).split(",");
                for (String individualTerm : individualTerms){
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
        String msg = null;

        try {
            msg = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        Gson gson = new Gson();
        ATweet aTweet = null;
        try {
            aTweet = gson.fromJson(msg, ATweet.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            System.out.printf("Failed to convert message to Tweet: %s%n", msg);
            return;
        }

        if (msg== null || aTweet == null) {
            return;
        }

        boolean found = true;
        for (int i = 0; i < individualSearchTerms.size(); i++) {
            if (aTweet.getText().toLowerCase().contains(individualSearchTerms.get(i))) {

                Integer counterIndex = indexForSearchTerm.get(individualSearchTerms.get(i));
                if (isPositiveMessage(msg)) {
                    found = updatePositiveCount(aTweet, i, counterIndex);
                } else if (isNegativeMessage(msg)) {
                    found = updateNegativeCounter(aTweet, i, counterIndex);
                }
            }
        }

        if (!found) {
            StringBuilder matchedItems = new StringBuilder("");
            for (int i = 0; i < individualSearchTerms.size(); i++) {
                if (aTweet.getText().toLowerCase().contains(individualSearchTerms.get(i))) {
                    matchedItems.append(individualSearchTerms.get(i));
                    matchedItems.append(",");
                }
            }
            // remove last comma
            if (matchedItems.length()>0) {
                matchedItems.deleteCharAt(matchedItems.length() - 1);
                System.out.printf(":| - %s --- %s%n", matchedItems.toString(), aTweet.getText().replace("\n", ""));
            }
        }
    }

    private boolean updatePositiveCount(ATweet aTweet, int i, Integer counterIndex) {
        int newCount = positiveCounts.get(counterIndex) + 1;
        positiveCounts.set(counterIndex, newCount);
        System.out.printf(":) - %s - %d --- %s%n", individualSearchTerms.get(i), newCount, aTweet.getText().replace("\n", ""));
        return true;
    }

    private boolean updateNegativeCounter(ATweet aTweet, int i, Integer searchTermIndexToIncrement) {
        int newCount = negativeCounts.get(searchTermIndexToIncrement) + 1;
        negativeCounts.set(searchTermIndexToIncrement, newCount);
        System.out.printf(":( %d - %s - --- %s%n", individualSearchTerms.get(i), newCount, aTweet.getText().replace("\n", ""));
        return true;
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
                || msg.contains("XD") || msg.contains(" X-D ")
                || msg.contains(" (: ")
                || msg.contains(" (-: ")
                || msg.contains("😀🤣"); //smiley emoji
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
}
