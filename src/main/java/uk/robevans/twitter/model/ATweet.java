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
    Boolean truncated;
    String in_reply_to_status_id;
    String in_reply_to_status_id_str;
    String in_reply_to_user_id;
    String in_reply_to_user_id_str;
    String in_reply_to_screen_name;
    TwitterUser user;
    String geo;
    String coordinates;
    String place;
    String contributors;
    Boolean is_quote_status;
    Integer retweet_count;
    Integer favorite_count;
    Map<String, List> entities;
    List<Map> urls;
    List<String>  user_mentions;
    List<String> symbols;
    List<Map> media;
    Map extended_entities;
    Boolean favorited;
    Boolean retweeted;
    Boolean possibly_sensitive;
    String filter_level;
    String lang;
    String timestamp_ms;

    public ATweet() { }

    public ATweet(String created_at, long id, String id_str, String text, List<Integer> display_text_range, String source, Boolean truncated, String in_reply_to_status_id, String in_reply_to_status_id_str, String in_reply_to_user_id, String in_reply_to_user_id_str, String in_reply_to_screen_name,
                  TwitterUser user, String geo, String coordinates, String place, String contributors, Boolean is_quote_status, Integer retweet_count, Integer favorite_count, Map<String, List> entities, List<Map> urls, List<String> user_mentions, List<String> symbols, List<Map> media, Map extended_entities, Boolean favorited, Boolean retweeted, Boolean possibly_sensitive, String filter_level, String lang, String timestamp_ms) {
        this.created_at = created_at;
        this.id = id;
        this.id_str = id_str;
        this.text = text;
        this.display_text_range = display_text_range;
        this.source = source;
        this.truncated = truncated;
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
        this.retweet_count = retweet_count;
        this.favorite_count = favorite_count;
        this.entities = entities;
        this.urls = urls;
        this.user_mentions = user_mentions;
        this.symbols = symbols;
        this.media = media;
        this.extended_entities = extended_entities;
        this.favorited = favorited;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<Integer> getDisplay_text_range() {
        return display_text_range;
    }

    public void setDisplay_text_range(List<Integer> display_text_range) {
        this.display_text_range = display_text_range;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
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

    public Map<String, List> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, List> entities) {
        this.entities = entities;
    }

    public List<Map> getUrls() {
        return urls;
    }

    public void setUrls(List<Map> urls) {
        this.urls = urls;
    }

    public List<String> getUser_mentions() {
        return user_mentions;
    }

    public void setUser_mentions(List<String> user_mentions) {
        this.user_mentions = user_mentions;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public List<Map> getMedia() {
        return media;
    }

    public void setMedia(List<Map> media) {
        this.media = media;
    }

    public Map getExtended_entities() {
        return extended_entities;
    }

    public void setExtended_entities(Map extended_entities) {
        this.extended_entities = extended_entities;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
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
}
