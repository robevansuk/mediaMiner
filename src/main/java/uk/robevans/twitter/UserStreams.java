package uk.robevans.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.robevans.twitter.model.TwitterUser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * Created by robevans.uk on 25/07/2017.
 */
@Component
@ConfigurationProperties(prefix="twitter")
public class UserStreams {

    private static Logger log = LoggerFactory.getLogger(UserStreams.class);
    private static final String resourcePath = "1.1/users/lookup.json?";
    private static final String oauthEndpoint = "oauth2/token";

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${twitter.rest_api_url}")
    private String apiBaseUrl;

    @Value("${twitter.api_key}")
    private String consumerKey;

    @Value("${twitter.api_secret}")
    private String consumerSecret;

    private String bearerToken;
    private List<String> userStreams = new ArrayList<>();

    public UserStreams() { }

    public UserStreams(String... userStreams){
        for (String userStream : userStreams) {
            this.userStreams.add(userStream);
        }
    }

    public UserStreams(List<String> userStreams){
        this.userStreams = userStreams;
    }

    public List<String> getUserStreams() {
        return userStreams;
    }

    public List<Long> getUserIds() {
        StringBuilder builder = getUsernamesAsCommaSeparatedListFromSpringConfig();

        setBearerToken();

        try {
            TwitterUser[] twitterUserIds = getTwitterUserIds(builder);

            List<Long> usersIds = Arrays.stream(twitterUserIds)
                    .map(user -> user.getId_str())
                    .map(Long::new)
                    .collect(toList());

            return usersIds;
        } catch (HttpClientErrorException ex) {
            log.error("GET request Failed for '" + resourcePath + "': " + ex.getResponseBodyAsString());
        }

        return emptyList();
    }

    private TwitterUser[] getTwitterUserIds(StringBuilder builder) {
        ResponseEntity<TwitterUser[]> responseEntity = restTemplate.exchange(apiBaseUrl + resourcePath +
                "screen_name=" + builder.toString(),
                GET,
                generalRequestHeaders("", bearerToken), TwitterUser[].class);
        return responseEntity.getBody();
    }

    private void setBearerToken() {
        if (bearerToken == null) {
            AccessToken accessTokenResponse = getBearerToken();
            bearerToken = accessTokenResponse.getAccess_token();
        }
    }

    private StringBuilder getUsernamesAsCommaSeparatedListFromSpringConfig() {
        StringBuilder builder = new StringBuilder("");
        for (String username : userStreams) {
            builder.append(username + ",");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder;
    }

    private AccessToken getBearerToken() {
        ResponseEntity<AccessToken> responseEntity = restTemplate.exchange(apiBaseUrl + oauthEndpoint,
                POST,
                bearerTokenRequestHeaders(),
                AccessToken.class);
        return responseEntity.getBody();
    }

    public HttpEntity<String> bearerTokenRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.add("accept", "application/json");
        headers.add("Authorization", "Basic "    + getBase64EncodedAuthString());
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        return new HttpEntity<>("grant_type=client_credentials", headers);
    }

    public HttpEntity<String> generalRequestHeaders(String jsonBody, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("accept", "application/json");
        headers.add("Authorization", "Bearer " + bearerToken);

        return new HttpEntity<>(jsonBody, headers);
    }

    private String getBase64EncodedAuthString() {
        String authString = null;
        try {
            authString = URLEncoder.encode(consumerKey, UTF_8.name()) + ":" + URLEncoder.encode(consumerSecret, UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.error("Could not find or url encode your access token or access token secret. Ensure these are supplied in the application.yml configuration file.");
            e.printStackTrace();
            System.exit(1);
        }
        return Base64.getEncoder().encodeToString(authString.getBytes());
    }
}
