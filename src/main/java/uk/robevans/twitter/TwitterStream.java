package uk.robevans.twitter;

import com.google.gson.Gson;
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

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

    SearchTerms searchGroups;
    List<String> individualSearchTerms;

    Map<String, Integer> indexForSearchTerm;
    BlockingQueue<String> queue;
    List<Integer> positiveCounts;
    List<Integer> negativeCounts;

    // You can only use a single filter for all the search terms you're monitoring.
    // otherwise you will see " Error connecting w/ status code - 420, reason - Enhance Your Calm"
    // .. which is essentially asking you to cut it out because they only allow one stream.
    StatusesFilterEndpoint searchFilter;

    public TwitterStream() {
    }

    @Autowired
    public TwitterStream(@Value("${twitter.api_key}") String apiKey,
                         @Value("${twitter.api_secret}") String apiSecret,
                         @Value("${twitter.access_token}") String accessToken,
                         @Value("${twitter.access_token_secret}") String accessTokenSecret,
                         SearchTerms searchTerms) {
        // Init counters and queues for messages for each of the search terms that come through.

        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.searchGroups = searchTerms;
        init();
    }

    private void init() {
        initDataStructures();
        connect();
        read();
    }

    private void initDataStructures() {

        this.indexForSearchTerm = new HashMap<>();
        this.positiveCounts = new ArrayList<>();
        this.negativeCounts = new ArrayList<>();
        this.searchFilter = new StatusesFilterEndpoint();
        this.individualSearchTerms = searchGroups.individualSearchTerms();
    }

    private void connect() {
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        Authentication hosebirdAuth = new OAuth1(apiKey, apiSecret, accessToken, accessTokenSecret);

        int i = 0;
        StringBuilder builder = new StringBuilder("");

        this.queue = new LinkedBlockingQueue<>(100);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(100);

        List<String> terms = individualSearchTerms;

        for (String term : terms) {
            this.positiveCounts.add(0);
            this.negativeCounts.add(0);

            indexForSearchTerm.put(term, i);
            builder.append(term);
            if (i != terms.size() - 1) {
                builder.append(",");
            }
            i++;
        }

        List<String> allSearchTerms = Arrays.asList(builder.toString());
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

    public void read() {
        while (!hosebirdClient.isDone()) {
            getMessage();
        }
    }

    public String getMessage() {
        String msg = null;

        try {
            msg = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Could not read msg from queue";
        }

        Gson gson = new Gson();
        ATweet aTweet = null;
        try {
            aTweet = gson.fromJson(msg, ATweet.class);
        } catch (Exception e) {
            System.out.print(msg);
            return msg;
        }

        if (msg== null || aTweet == null) {
            return "";
        }

        boolean notFound = true;
        for (int i = 0; i < individualSearchTerms.size(); i++) {
            if (aTweet.getText().toLowerCase().contains(individualSearchTerms.get(i))) {
                Integer counterIndex = indexForSearchTerm.get(individualSearchTerms.get(i));
                if (isPositiveMessage(msg)) {
                    int newCount = positiveCounts.get(counterIndex) + 1;
                    positiveCounts.set(i, newCount);
                    System.out.printf(":) - %s - %d --- %s%n", individualSearchTerms.get(i), newCount, aTweet.getText().replace("\n", ""));
                    notFound = false;
                } else if (isNegativeMessage(msg)) {
                    int newCount = negativeCounts.get(counterIndex) + 1;
                    negativeCounts.set(i, newCount);
                    System.out.printf(":( %d - %s - --- %s%n", individualSearchTerms.get(i), newCount, aTweet.getText().replace("\n", ""));
                    notFound = false;
                }
            }
        }

        if (notFound) {
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

        return aTweet.getText();
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
        return endpoint.locations(Arrays.asList(location)); // This will limit subscription to tweets only from the specified box.
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
}
