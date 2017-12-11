package uk.robevans.twitter

import com.google.gson.Gson
import org.junit.Test
import spock.lang.Specification
import uk.robevans.twitter.model.ATweet

class TweetParserTest extends Specification {

    private static final String jsonTweet1 = """{"created_at":"Thu Nov 30 15:56:49 +0000 2017","id":936262749331398656,"id_str":"936262749331398656","text":"Day 2 of Sneakers and Sci-Fi brings this gem. My favorite in the trilogy. The Red Wedding\\u2026 https:\\/\\/t.co\\/MjnTILjOYM","source":"\\u003ca href=\\"http:\\/\\/instagram.com\\" rel=\\"nofollow\\"\\u003eInstagram\\u003c\\/a\\u003e","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":389507678,"id_str":"389507678","name":"Adam.J.Ellsworth","screen_name":"AdamJEllsworth","location":"Everywhere","url":null,"description":"Imma truck driver\\n\\nInsragram big.rig_big.shoes","translator_type":"none","protected":false,"verified":false,"followers_count":193,"friends_count":117,"listed_count":1,"favourites_count":27161,"statuses_count":6465,"created_at":"Wed Oct 12 14:58:43 +0000 2011","utc_offset":null,"time_zone":null,"geo_enabled":true,"lang":"en","contributors_enabled":false,"is_translator":false,"profile_background_color":"C0DEED","profile_background_image_url":"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png","profile_background_image_url_https":"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png","profile_background_tile":false,"profile_link_color":"1DA1F2","profile_sidebar_border_color":"C0DEED","profile_sidebar_fill_color":"DDEEF6","profile_text_color":"333333","profile_use_background_image":true,"profile_image_url":"http:\\/\\/pbs.twimg.com\\/profile_images\\/854684519949062144\\/wVY1SsyM_normal.jpg","profile_image_url_https":"https:\\/\\/pbs.twimg.com\\/profile_images\\/854684519949062144\\/wVY1SsyM_normal.jpg","profile_banner_url":"https:\\/\\/pbs.twimg.com\\/profile_banners\\/389507678\\/1436201973","default_profile":true,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null},"geo":{"type":"Point","coordinates":[38.0272,-97.9398]},"coordinates":{"type":"Point","coordinates":[-97.9398,38.0272]},"place":{"id":"0e9606ca4b6d8988","url":"https:\\/\\/api.twitter.com\\/1.1\\/geo\\/id\\/0e9606ca4b6d8988.json","place_type":"city","name":"South Hutchinson","full_name":"South Hutchinson, KS","country_code":"US","country":"United States","bounding_box":{"type":"Polygon","coordinates":[[[-97.967822,38.012968],[-97.967822,38.042468],[-97.925800,38.042468],[-97.925800,38.012968]]]},"attributes":{}},"contributors":null,"is_quote_status":false,"quote_count":0,"reply_count":0,"retweet_count":0,"favorite_count":0,"entities":{"hashtags":[],"urls":[{"url":"https:\\/\\/t.co\\/MjnTILjOYM","expanded_url":"https:\\/\\/www.instagram.com\\/p\\/BcC-MGMl5gn\\/","display_url":"instagram.com\\/p\\/BcC-MGMl5gn\\/","indices":[91,114]}],"user_mentions":[],"symbols":[]},"favorited":false,"retweeted":false,"possibly_sensitive":false,"filter_level":"low","lang":"en","timestamp_ms":"1512057409018"}"""
    private static final String jsonTweet2 = """{"created_at":"Thu Nov 30 15:58:56 +0000 2017","id":936263282096209920,"id_str":"936263282096209920","text":"#Ego #clean \\u267b\\ufe0f that's my #ecoclean BREATH \\ud83c\\udf2c\\ud83d\\udca8\\ud83c\\udf2a\\ud83d\\udeb6\\ud83c\\udffd HE TURNED IT #iAm #black but my blood #gogreen\\u2026 https:\\/\\/t.co\\/R9xtSYmwEB","source":"\\u003ca href=\\"http:\\/\\/instagram.com\\" rel=\\"nofollow\\"\\u003eInstagram\\u003c\\/a\\u003e","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":834477962443575297,"id_str":"834477962443575297","name":"MASTER OF ADLIBS \\ud83d\\udc9c\\ud83d\\udcab\\u2650\\ufe0f","screen_name":"ForcedIntoGlory","location":"Austin, TX","url":"http:\\/\\/Snapchat.com\\/add\\/KendrickRambo","description":"\\ud83d\\ude48\\ud83d\\ude49\\ud83d\\ude4a https:\\/\\/t.co\\/sWhg7QaLKY \\ud83c\\udf81 https:\\/\\/t.co\\/ROQrezZ9Vo \\ud83d\\udcf2\\ud83d\\udcab\\ud83d\\udcf8 #GuestList BOOKING https:\\/\\/t.co\\/87lLly4gZw \\ud83d\\udd25 https:\\/\\/t.co\\/ANOXkef3oH","translator_type":"none","protected":false,"verified":false,"followers_count":666,"friends_count":1693,"listed_count":17,"favourites_count":1616,"statuses_count":21553,"created_at":"Wed Feb 22 19:00:24 +0000 2017","utc_offset":-28800,"time_zone":"Pacific Time (US & Canada)","geo_enabled":true,"lang":"en","contributors_enabled":false,"is_translator":false,"profile_background_color":"F5F8FA","profile_background_image_url":"","profile_background_image_url_https":"","profile_background_tile":false,"profile_link_color":"1DA1F2","profile_sidebar_border_color":"C0DEED","profile_sidebar_fill_color":"DDEEF6","profile_text_color":"333333","profile_use_background_image":true,"profile_image_url":"http:\\/\\/pbs.twimg.com\\/profile_images\\/934973053766963201\\/19txr1wI_normal.jpg","profile_image_url_https":"https:\\/\\/pbs.twimg.com\\/profile_images\\/934973053766963201\\/19txr1wI_normal.jpg","profile_banner_url":"https:\\/\\/pbs.twimg.com\\/profile_banners\\/834477962443575297\\/1507321032","default_profile":true,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null},"geo":{"type":"Point","coordinates":[30.30893,-97.74109]},"coordinates":{"type":"Point","coordinates":[-97.74109,30.30893]},"place":{"id":"c3f37afa9efcf94b","url":"https:\\/\\/api.twitter.com\\/1.1\\/geo\\/id\\/c3f37afa9efcf94b.json","place_type":"city","name":"Austin","full_name":"Austin, TX","country_code":"US","country":"United States","bounding_box":{"type":"Polygon","coordinates":[[[-97.928935,30.127892],[-97.928935,30.518799],[-97.580513,30.518799],[-97.580513,30.127892]]]},"attributes":{}},"contributors":null,"is_quote_status":false,"quote_count":0,"reply_count":0,"retweet_count":0,"favorite_count":0,"entities":{"hashtags":[{"text":"Ego","indices":[0,4]},{"text":"clean","indices":[5,11]},{"text":"ecoclean","indices":[25,34]},{"text":"iAm","indices":[61,65]},{"text":"black","indices":[66,72]},{"text":"gogreen","indices":[86,94]}],"urls":[{"url":"https:\\/\\/t.co\\/R9xtSYmwEB","expanded_url":"https:\\/\\/www.instagram.com\\/p\\/BcH-8bnBbmE\\/","display_url":"instagram.com\\/p\\/BcH-8bnBbmE\\/","indices":[96,119]}],"user_mentions":[],"symbols":[]},"favorited":false,"retweeted":false,"possibly_sensitive":true,"filter_level":"low","lang":"en","timestamp_ms":"1512057536039"}"""
    private static final String jsonTweet3 = """{"created_at":"Mon Dec 11 17:35:29 +0000 2017","id":940273847571542028,"id_str":"940273847571542028","text":"There is absolutely no reason 4the price of BTC2hovering around\$16.5k.It seems as though the BTCprice is tracking h\u2026 https://t.co/2q7bFycu6J","source":"","truncated":true,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":922013804074094592,"id_str":"922013804074094592","name":"Micro2sipps","screen_name":"MillenniaIMatt_","location":"Utah, USA","url":"https://www.youtube.com/channel/UCAGev9kR2t3POMymQfERKTA","description":"Knight of the Crystal Blade     Follow Me on Gab https://gab.ai/Microchip  \ud83d\ude4f\u271d\ufe0f\u2721\ufe0f","translator_type":"none","protected":false,"verified":false,"followers_count":1907,"friends_count":67,"listed_count":10,"favourites_count":2440,"statuses_count":4464,"created_at":"Sun Oct 22 08:16:35 +0000 2017","utc_offset":-18000,"time_zone":"Eastern Time (US & Canada)","geo_enabled":true,"lang":"en","contributors_enabled":false,"is_translator":false,"profile_background_color":"F5F8FA","profile_background_image_url":"","profile_background_image_url_https":"","profile_background_tile":false,"profile_link_color":"1DA1F2","profile_sidebar_border_color":"C0DEED","profile_sidebar_fill_color":"DDEEF6","profile_text_color":"333333","profile_use_background_image":true,"profile_image_url":"http://pbs.twimg.com/profile_images/934011909858357248/sh1l87cu_normal.jpg","profile_image_url_https":"https://pbs.twimg.com/profile_images/934011909858357248/sh1l87cu_normal.jpg","profile_banner_url":"https://pbs.twimg.com/profile_banners/922013804074094592/1511521021","default_profile":true,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null},"geo":null,"coordinates":null,"place":null,"contributors":null,"withheld_in_countries":["DE"],"is_quote_status":false,"extended_tweet":{"full_text":"There is absolutely no reason 4the price of BTC2hovering around\$16.5k.It seems as though the BTCprice is tracking he future market not the futures market bring a derivative of BTC.Pretty retarded right now,but my only guess is big dogs are keeping it pinned and stable on purpose","display_text_range":[0,279],"entities":{"hashtags":[],"urls":[],"user_mentions":[],"symbols":[]}},"quote_count":0,"reply_count":0,"retweet_count":0,"favorite_count":0,"entities":{"hashtags":[],"urls":[{"url":"https://t.co/2q7bFycu6J","expanded_url":"https://twitter.com/i/web/status/940273847571542028","display_url":"twitter.com/i/web/status/9\u2026","indices":[117,140]}],"user_mentions":[],"symbols":[]},"favorited":false,"retweeted":false,"filter_level":"low","lang":"en","timestamp_ms":"1513013729361"}"""
    private static final String jsonTweet4 = """{"created_at":"Mon Dec 11 21:01:58 +0000 2017","id":940325812963872774,"id_str":"940325812963872774","text":"Yes, #Bitcoin and some other cryptocurrencies have a bright future. But never forget that #Bitcoins and the other\u2026 https:\\/\\/t.co\\/2X9oSrGGyS","display_text_range":[0,140],"source":"<a href=\\"http:\\/\\/twitter.com\\" rel=\\"nofollow\\"\\u003eTwitter Web Client\\u003c/a\\u003e","truncated":true,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":2927236667,"id_str":"2927236667","name":"Terco Recalcitrante","screen_name":"TercoRec","location":null,"url":null,"description":"No conozco a la mitad de ustedes, ni la mitad de lo que quisiera y lo que yo quisiera es menos de la mitad de lo que la mitad de ustedes merece. (Bilbo Bolson)","translator_type":"none","protected":false,"verified":false,"followers_count":794,"friends_count":573,"listed_count":151,"favourites_count":96916,"statuses_count":94433,"created_at":"Wed Dec 17 16:24:51 +0000 2014","utc_offset":3600,"time_zone":"Madrid","geo_enabled":false,"lang":"es","contributors_enabled":false,"is_translator":false,"profile_background_color":"000000","profile_background_image_url":"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png","profile_background_image_url_https":"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png","profile_background_tile":false,"profile_link_color":"0084B4","profile_sidebar_border_color":"000000","profile_sidebar_fill_color":"000000","profile_text_color":"000000","profile_use_background_image":false,"profile_image_url":"http:\\/\\/pbs.twimg.com\\/profile_images\\/545344545265696768\\/IYOllP79_normal.png","profile_image_url_https":"https:\\/\\/pbs.twimg.com\\/profile_images\\/545344545265696768\\/IYOllP79_normal.png","profile_banner_url":"https:\\/\\/pbs.twimg.com\\/profile_banners\\/2927236667\\/1472351995","default_profile":false,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null},"geo":null,"coordinates":null,"place":null,"contributors":null,"withheld_in_countries":["TR"],"quoted_status_id":940169530436915200,"quoted_status_id_str":"940169530436915200","quoted_status":{"created_at":"Mon Dec 11 10:40:58 +0000 2017","id":940169530436915200,"id_str":"940169530436915200","text":"Bitcoin Price Surges 20% Overnight as Volume of CBOE Futures Skyrockets https:\\/\\/t.co\\/mIpbQ7hIxy https:\\/\\/t.co\\/ZQapGefcpU","display_text_range":[0,95],"source":"\u003ca href=\\"https:\\/\\/www.cryptocoinsnews.com\\" rel=\\"nofollow\\"\u003eCCNtw\u003c\\/a\u003e","truncated":false,"in_reply_to_status_id":null,"in_reply_to_status_id_str":null,"in_reply_to_user_id":null,"in_reply_to_user_id_str":null,"in_reply_to_screen_name":null,"user":{"id":1856523530,"id_str":"1856523530","name":"CryptoCoinsNews","screen_name":"CryptoCoinsNews","location":null,"url":"http:\\/\\/www.cryptocoinsnews.com","description":"Get the latest Bitcoin News from the World's Leading & Independent #Bitcoin News Source","translator_type":"none","protected":false,"verified":false,"followers_count":102870,"friends_count":21200,"listed_count":1898,"favourites_count":36,"statuses_count":21066,"created_at":"Thu Sep 12 06:45:14 +0000 2013","utc_offset":7200,"time_zone":"Athens","geo_enabled":false,"lang":"en","contributors_enabled":false,"is_translator":false,"profile_background_color":"C0DEED","profile_background_image_url":"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png","profile_background_image_url_https":"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png","profile_background_tile":false,"profile_link_color":"1DA1F2","profile_sidebar_border_color":"C0DEED","profile_sidebar_fill_color":"DDEEF6","profile_text_color":"333333","profile_use_background_image":true,"profile_image_url":"http:\\/\\/pbs.twimg.com\\/profile_images\\/505651786208645121\\/nmm3-XPn_normal.png","profile_image_url_https":"https:\\/\\/pbs.twimg.com\\/profile_images\\/505651786208645121\\/nmm3-XPn_normal.png","profile_banner_url":"https:\\/\\/pbs.twimg.com\\/profile_banners\\/1856523530\\/1409391728","default_profile":true,"default_profile_image":false,"following":null,"follow_request_sent":null,"notifications":null},"geo":null,"coordinates":null,"place":null,"contributors":null,"is_quote_status":false,"quote_count":3,"reply_count":1,"retweet_count":30,"favorite_count":34,"entities":{"hashtags":[],"urls":[{"url":"https:\\/\\/t.co\\/mIpbQ7hIxy","expanded_url":"https:\\/\\/www.cryptocoinsnews.com\\/bitcoin-price-surges-by-20-overnight-as-volume-of-cboe-futures-skyrockets\\/","display_url":"cryptocoinsnews.com\\/bitcoin-price-\u2026","indices":[72,95]}],"user_mentions":[],"symbols":[],"media":[{"id":940169528205393921,"id_str":"940169528205393921","indices":[96,119],"media_url":"http:\\/\\/pbs.twimg.com\\/media\\/DQwnNGVUQAE_AQ-.jpg","media_url_https":"https:\\/\\/pbs.twimg.com\\/media\\/DQwnNGVUQAE_AQ-.jpg","url":"https:\\/\\/t.co\\/ZQapGefcpU","display_url":"pic.twitter.com\\/ZQapGefcpU","expanded_url":"https:\\/\\/twitter.com\\/CryptoCoinsNews\\/status\\/940169530436915200\\/photo\\/1","type":"photo","sizes":{"thumb":{"w":150,"h":150,"resize":"crop"},"large":{"w":1000,"h":668,"resize":"fit"},"small":{"w":680,"h":454,"resize":"fit"},"medium":{"w":1000,"h":668,"resize":"fit"}}}]},"extended_entities":{"media":[{"id":940169528205393921,"id_str":"940169528205393921","indices":[96,119],"media_url":"http:\\/\\/pbs.twimg.com\\/media\\/DQwnNGVUQAE_AQ-.jpg","media_url_https":"https:\\/\\/pbs.twimg.com\\/media\\/DQwnNGVUQAE_AQ-.jpg","url":"https:\\/\\/t.co\\/ZQapGefcpU","display_url":"pic.twitter.com\\/ZQapGefcpU","expanded_url":"https:\\/\\/twitter.com\\/CryptoCoinsNews\\/status\\/940169530436915200\\/photo\\/1","type":"photo","sizes":{"thumb":{"w":150,"h":150,"resize":"crop"},"large":{"w":1000,"h":668,"resize":"fit"},"small":{"w":680,"h":454,"resize":"fit"},"medium":{"w":1000,"h":668,"resize":"fit"}}}]},"favorited":false,"retweeted":false,"possibly_sensitive":false,"filter_level":"low","lang":"en"},"is_quote_status":true,"extended_tweet":{"full_text":"Yes, #Bitcoin and some other cryptocurrencies have a bright future. But never forget that #Bitcoins and the other #cryptocurrencies are RISK investments. NEVER INVEST ON THEM MORE THAN YOU CAN AFFORD TO LOOSE. https:\\/\\/t.co\\/vGd1cdUNPA","display_text_range":[0,209],"entities":{"hashtags":[{"text":"Bitcoin","indices":[5,13]},{"text":"Bitcoins","indices":[90,99]},{"text":"cryptocurrencies","indices":[114,131]}],"urls":[{"url":"https:\\/\\/t.co\\/vGd1cdUNPA","expanded_url":"https:\\/\\/twitter.com\\/CryptoCoinsNews\\/status\\/940169530436915200","display_url":"twitter.com\\/CryptoCoinsNew\u2026","indices":[210,233]}],"user_mentions":[],"symbols":[]}},"quote_count":0,"reply_count":0,"retweet_count":0,"favorite_count":0,"entities":{"hashtags":[{"text":"Bitcoin","indices":[5,13]},{"text":"Bitcoins","indices":[90,99]}],"urls":[{"url":"https:\\/\\/t.co\\/2X9oSrGGyS","expanded_url":"https:\\/\\/twitter.com\\/i\\/web\\/status\\/940325812963872774","display_url":"twitter.com\\/i\\/web\\/status\\/9\u2026","indices":[115,138]}],"user_mentions":[],"symbols":[]},"favorited":false,"retweeted":false,"possibly_sensitive":false,"filter_level":"low","lang":"en","timestamp_ms":"1513026118876"}"""

    private final Gson gson = new Gson()

    @Test
    def "should parse jsonTweet1 with geo coordinates"() {
        when:
        ATweet tweet = gson.fromJson(jsonTweet1, ATweet.class)

        then:
        assert tweet != null
    }

    @Test
    def "should parse jsonTweet2"() {
        when:
        ATweet tweet = gson.fromJson(jsonTweet2, ATweet.class)

        then:
        assert tweet != null
    }

    @Test
    def "should parse jsonTweet3"() {
        when:
        ATweet tweet = gson.fromJson(jsonTweet3, ATweet.class)

        then:
        assert tweet != null
    }

    @Test
    def "should parse jsonTweet4"() {
        when:
        ATweet tweet = gson.fromJson(jsonTweet4, ATweet.class)

        then:
        assert tweet != null
    }
}