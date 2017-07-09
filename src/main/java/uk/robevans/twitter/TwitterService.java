package uk.robevans.twitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by robevans.uk on 22/06/2017.
 */
@Controller
@RequestMapping("/")
class TwitterService {

    List<String> tweets;

    @Value("${spring.social.twitter.appId}")
    String appId;

    @Value("${spring.social.twitter.appSecret}")
    String appSecret;

    private String appToken;

    private String apiKey; //appId
    private String apiSecret;

    private String appAuthUrl;
    private String requestTokenUrl;
    private String authorizeSecureUrl;
    private String accessTokenUrl;

    public TwitterService() { }

    public TwitterService(@Value("${twitter.api_key}") String apiKey,
                          @Value("${twitter.api_secret}") String apiSecret,
                          @Value("${twitter.authorize_secure_url}") String appAuthUrl,
                          @Value("${twitter.request_token_url}")  String requestTokenUrl,
                          @Value("${twitter.authorize_secure_url}") String authorizeSecureUrl,
                          @Value("${twitter.access_token_url}") String accessTokenUrl) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.appAuthUrl = appAuthUrl;
        this.requestTokenUrl = requestTokenUrl;
        this.authorizeSecureUrl = authorizeSecureUrl;
        this.accessTokenUrl = accessTokenUrl;
    }

    @RequestMapping(value="litecoin", method=RequestMethod.GET)
    public String litecoin(Model model) {
        if(appToken == null) {
            this.appToken = fetchApplicationAccessToken();
        }

        this.tweets = searchTwitter("#litecoin :) -RT&result_type=recent&count=100", appToken);

        if (tweets == null) {
            return "redirect:/connect/twitter";
        }
        model.addAttribute("tweets", tweets);
        return "litecoin";
    }

    /**
     * visit localhost:port/hackDay to view this page and its output
     * @param model
     * @return
     */
    @RequestMapping(value="hackDay", method=RequestMethod.GET)
    public String hackDay(Model model) {
        if(appToken == null) {
            this.appToken = fetchApplicationAccessToken();
        }

        List<String> positiveTweets = searchTwitter("hackDay :) -RT&filter=retweets&result_type=recent&count=100", appToken);
        List<String> negativeTweets = searchTwitter("hackDay :( -RT&filter=retweets&result_type=recent&count=100", appToken);

        if (positiveTweets == null && negativeTweets == null) {
            return "redirect:/connect/twitter";
        }
        model.addAttribute("positiveTweets", positiveTweets);
        model.addAttribute("negativeTweets", negativeTweets);

        return "HackDay";
    }

    private String fetchApplicationAccessToken() {
        System.out.println("AppId: " + appId + ", AppSecret: " + appSecret);
        // Twitter supports OAuth2 *only* for obtaining an application token, not for user tokens.
        OAuth2Operations oauth = new OAuth2Template(appId, appSecret, "", "https://api.twitter.com/oauth2/token");
        return oauth.authenticateClient().getAccessToken();
    }

    private static List<String> searchTwitter(String query, String appToken) {
        // Twitter supports OAuth2 *only* for obtaining an application token, not for user tokens.
        // Using application token for search so that we don't have to go through hassle of getting a user token.
        // This is not (yet) supported by Spring Social, so we must construct the request ourselves.
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + appToken);
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        Map<String, ?> result = rest.exchange("https://api.twitter.com/1.1/search/tweets.json?q={query}", HttpMethod.GET, requestEntity, Map.class, query).getBody();
        List<Map<String, ?>> statuses = (List<Map<String, ?>>) result.get("statuses");
        List<String> tweets = new ArrayList<>();
        int i = 1;
        for (Map<String, ?> status : statuses) {
            tweets.add(status.get("text").toString());
            System.out.println(i + " / " + (tweets.size()+1) + " => " + status.get("text").toString());
            i++;
        }
        System.out.println();
        return tweets;
    }

    @ModelAttribute("tweets")
    public List<String> getTweets() {
        return tweets;
    }

    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
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

    public String getAppAuthUrl() {
        return appAuthUrl;
    }

    public void setAppAuthUrl(String appAuthUrl) {
        this.appAuthUrl = appAuthUrl;
    }

    public String getRequestTokenUrl() {
        return requestTokenUrl;
    }

    public void setRequestTokenUrl(String requestTokenUrl) {
        this.requestTokenUrl = requestTokenUrl;
    }

    public String getAuthorizeSecureUrl() {
        return authorizeSecureUrl;
    }

    public void setAuthorizeSecureUrl(String authorizeSecureUrl) {
        this.authorizeSecureUrl = authorizeSecureUrl;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }
}
