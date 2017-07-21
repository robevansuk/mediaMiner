package uk.robevans.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
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
    BlockingQueue<String> msgQueue;

    public TwitterStream() {
    }

    @Autowired
    public TwitterStream(@Value("${twitter.api_key}") String apiKey,
                         @Value("${twitter.api_secret}") String apiSecret,
                         @Value("${twitter.access_token}") String accessToken,
                         @Value("${twitter.access_token_secret}") String accessTokenSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        connect();
        read();
    }

    private void connect() {
        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        msgQueue = new LinkedBlockingQueue<>(1000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(1000);

        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        List<String> terms = Lists.newArrayList("litecoin", "ltc");

        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(apiKey, apiSecret, accessToken, accessTokenSecret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);

        hosebirdClient = builder.build();

        // Attempts to establish a connection.
        hosebirdClient.connect();
    }

    public void read() {
        while (!hosebirdClient.isDone()) {
            getMessage();
        }
    }

    public String getMessage() {
        String msg;
        try {
            msg = msgQueue.take();
            System.out.print(msg);
            return msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ""; // throw exception.
        }
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
