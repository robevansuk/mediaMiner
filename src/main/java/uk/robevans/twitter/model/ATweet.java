package uk.robevans.twitter.model;

import java.util.List;
import java.util.Map;

/**
 * Created by robevans.uk on 24/07/2017.
 */
public class ATweet {
    String created_at;
    Long id;
    String id_str;
    String text;
    List<Integer> display_text_range;
    String source;
    String rel;
    Boolean truncated;
    Long in_reply_to_status_id;
    String in_reply_to_status_id_str;
    Long in_reply_to_user_id;
    String in_reply_to_user_id_str;
    String in_reply_to_screen_name;
    TwitterUser user;
    String geo;
    Object coordinates; // todo coordinates { coordinates : [], type: string }
    Object place;
    String contributors;
    Boolean is_quote_status;
    Integer quote_count;
    Integer reply_count;
    Integer retweet_count;
    Integer favorite_count;
    Object entities;
    Boolean favorited;
    String current_user_retweet;
    Boolean withheld_copyright;
    String withheld_in_countries;
    Boolean retweeted;
    Boolean possibly_sensitive;
    String filter_level;
    String lang;
    String timestamp_ms;

    public ATweet() {
    }

    public ATweet(String created_at, Long id, String id_str, String text, String source, Boolean truncated, List<Integer> display_text_range, String source1, String rel, Boolean truncated1, Long in_reply_to_status_id, String in_reply_to_status_id_str, Long in_reply_to_user_id, String in_reply_to_user_id_str, String in_reply_to_screen_name, TwitterUser user, String geo, Object coordinates, Object place, String contributors, Boolean is_quote_status, Integer quote_count, Integer reply_count, Integer retweet_count, Integer favorite_count, Object entities, Boolean favorited, String current_user_retweet, Boolean withheld_copyright, String withheld_in_countries, Boolean retweeted, Boolean possibly_sensitive, String filter_level, String lang, String timestamp_ms) {
        this.created_at = created_at;
        this.id = id;
        this.id_str = id_str;
        this.text = text;
        this.source = source;
        this.truncated = truncated;
        this.display_text_range = display_text_range;
        this.source = source1;
        this.rel = rel;
        this.truncated = truncated1;
        this.in_reply_to_status_id = in_reply_to_status_id;
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
        this.in_reply_to_user_id = in_reply_to_user_id;
        this.in_reply_to_user_id_str = in_reply_to_user_id_str;
        this.in_reply_to_screen_name = in_reply_to_screen_name;
        this.user = user;
        this.geo = geo;
        this.coordinates = coordinates;
        this.place = place;
        this.contributors = contributors;
        this.is_quote_status = is_quote_status;
        this.quote_count = quote_count;
        this.reply_count = reply_count;
        this.retweet_count = retweet_count;
        this.favorite_count = favorite_count;
        this.entities = entities;
        this.favorited = favorited;
        this.current_user_retweet = current_user_retweet;
        this.withheld_copyright = withheld_copyright;
        this.withheld_in_countries = withheld_in_countries;
        this.retweeted = retweeted;
        this.possibly_sensitive = possibly_sensitive;
        this.filter_level = filter_level;
        this.lang = lang;
        this.timestamp_ms = timestamp_ms;
    }

    public String getCreated_at() {

        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public Boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }

    public Long getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(Long in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }

    public Long getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(Long in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_user_id_str() {
        return in_reply_to_user_id_str;
    }

    public void setIn_reply_to_user_id_str(String in_reply_to_user_id_str) {
        this.in_reply_to_user_id_str = in_reply_to_user_id_str;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public TwitterUser getUser() {
        return user;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public Object getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Object coordinates) {
        this.coordinates = coordinates;
    }

    public Object getPlace() {
        return place;
    }

    public void setPlace(Object place) {
        this.place = place;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public Boolean getIs_quote_status() {
        return is_quote_status;
    }

    public void setIs_quote_status(Boolean is_quote_status) {
        this.is_quote_status = is_quote_status;
    }

    public Integer getQuote_count() {
        return quote_count;
    }

    public void setQuote_count(Integer quote_count) {
        this.quote_count = quote_count;
    }

    public Integer getReply_count() {
        return reply_count;
    }

    public void setReply_count(Integer reply_count) {
        this.reply_count = reply_count;
    }

    public Integer getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(Integer retweet_count) {
        this.retweet_count = retweet_count;
    }

    public Integer getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(Integer favorite_count) {
        this.favorite_count = favorite_count;
    }

    public Object getEntities() {
        return entities;
    }

    public void setEntities(Object entities) {
        this.entities = entities;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public String getCurrent_user_retweet() {
        return current_user_retweet;
    }

    public void setCurrent_user_retweet(String current_user_retweet) {
        this.current_user_retweet = current_user_retweet;
    }

    public Boolean getWithheld_copyright() {
        return withheld_copyright;
    }

    public void setWithheld_copyright(Boolean withheld_copyright) {
        this.withheld_copyright = withheld_copyright;
    }

    public String getWithheld_in_countries() {
        return withheld_in_countries;
    }

    public void setWithheld_in_countries(String withheld_in_countries) {
        this.withheld_in_countries = withheld_in_countries;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Boolean getPossibly_sensitive() {
        return possibly_sensitive;
    }

    public void setPossibly_sensitive(Boolean possibly_sensitive) {
        this.possibly_sensitive = possibly_sensitive;
    }

    public String getFilter_level() {
        return filter_level;
    }

    public void setFilter_level(String filter_level) {
        this.filter_level = filter_level;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTimestamp_ms() {
        return timestamp_ms;
    }

    public void setTimestamp_ms(String timestamp_ms) {
        this.timestamp_ms = timestamp_ms;
    }

    public List<Integer> getDisplay_text_range() {
        return display_text_range;
    }

    public void setDisplay_text_range(List<Integer> display_text_range) {
        this.display_text_range = display_text_range;
    }
}
