import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import twitter4j.QueryResult
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

import javax.management.Query

/**
 * Created on 11/22/16.
 */
@Configuration
@ComponentScan
class ApplicationConfiguration {


    @Value("${twitter.api_key}")
    private String apiKey
    @Value("${twitter.api_secret}")
    private String apiSecret
    @Value("${twitter.access_token}")
    private String accessToken
    @Value("${twitter.access_token_secret}")
    private String accessTokenSecret
    @Value("${twitter.app_auth_url}")
    private String appAuthUrl
    @Value("${twitter.request_token_url}")
    private String requestTokenUrl
    @Value("${twitter.authorize_secure_url}")
    private String authorizeSecureUrl
    @Value("${twitter.access_token_url}")
    private String accessTokenUrl

    public void someMethod() {
//        Configuration config = new ConfigurationBuilder()
//                .setOAuthConsumerKey(apiKey)
//                .setOAuthConsumerSecret(apiSecret)
//                .setOAuthAccessToken(accessToken)
//                .setOAuthAccessTokenSecret(accessTokenSecret)
//                .build()

        Twitter twitter = TwitterFactory.getSingleton()
        Query query = new Query("source:twitter4j yusukey")
        QueryResult result = twitter.search(query)

        System.out.println(result.getCount())
    }

}