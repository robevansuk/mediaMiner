package uk.robevans.twitter.model;

/**
 * Created by ren7881 on 24/07/2017.
 */
public class TwitterUser {
    String id;
    String id_str;
    String name;
    String screen_name;
    String location;
    String url;
    String description;
    Boolean isProtected;
    Boolean verified;
    Integer followers_count;
    Integer friends_count;
    Integer listed_count;
    Integer favourites_count;
    Integer statuses_count;
    String created_at;
    String utc_offset;
    String time_zone;
    Boolean geo_enabled;
    String lang;
    Boolean contributors_enabled;
    Boolean is_translator;
    String profile_background_color;
    String profile_background_image_url;
    String profile_background_image_url_https;
    Boolean profile_background_tile;
    String profile_link_color;
    String profile_sidebar_border_color;
    String profile_sidebar_fill_color;
    String profile_text_color;
    Boolean profile_use_background_image;
    String profile_image_url;
    String profile_image_url_https;
    String profile_banner_url;
    Boolean default_profile;
    Boolean default_profile_image;
    String following;
    String follow_request_sent;
    String notifications;

    public TwitterUser() { }

    public TwitterUser(String id, String id_str, String name, String screen_name, String location, String url, String description, Boolean isProtected, Boolean verified, Integer followers_count, Integer friends_count, Integer listed_count, Integer favourites_count, Integer statuses_count, String created_at, String utc_offset, String time_zone, Boolean geo_enabled, String lang, Boolean contributors_enabled, Boolean is_translator, String profile_background_color, String profile_background_image_url, String profile_background_image_url_https, Boolean profile_background_tile, String profile_link_color, String profile_sidebar_border_color, String profile_sidebar_fill_color, String profile_text_color, Boolean profile_use_background_image, String profile_image_url, String profile_image_url_https, String profile_banner_url, Boolean default_profile, Boolean default_profile_image, String following, String follow_request_sent, String notifications) {
        this.id = id;
        this.id_str = id_str;
        this.name = name;
        this.screen_name = screen_name;
        this.location = location;
        this.url = url;
        this.description = description;
        this.isProtected = isProtected;
        this.verified = verified;
        this.followers_count = followers_count;
        this.friends_count = friends_count;
        this.listed_count = listed_count;
        this.favourites_count = favourites_count;
        this.statuses_count = statuses_count;
        this.created_at = created_at;
        this.utc_offset = utc_offset;
        this.time_zone = time_zone;
        this.geo_enabled = geo_enabled;
        this.lang = lang;
        this.contributors_enabled = contributors_enabled;
        this.is_translator = is_translator;
        this.profile_background_color = profile_background_color;
        this.profile_background_image_url = profile_background_image_url;
        this.profile_background_image_url_https = profile_background_image_url_https;
        this.profile_background_tile = profile_background_tile;
        this.profile_link_color = profile_link_color;
        this.profile_sidebar_border_color = profile_sidebar_border_color;
        this.profile_sidebar_fill_color = profile_sidebar_fill_color;
        this.profile_text_color = profile_text_color;
        this.profile_use_background_image = profile_use_background_image;
        this.profile_image_url = profile_image_url;
        this.profile_image_url_https = profile_image_url_https;
        this.profile_banner_url = profile_banner_url;
        this.default_profile = default_profile;
        this.default_profile_image = default_profile_image;
        this.following = following;
        this.follow_request_sent = follow_request_sent;
        this.notifications = notifications;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getProtected() {
        return isProtected;
    }

    public void setProtected(Boolean aProtected) {
        isProtected = aProtected;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Integer getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(Integer followers_count) {
        this.followers_count = followers_count;
    }

    public Integer getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(Integer friends_count) {
        this.friends_count = friends_count;
    }

    public Integer getListed_count() {
        return listed_count;
    }

    public void setListed_count(Integer listed_count) {
        this.listed_count = listed_count;
    }

    public Integer getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(Integer favourites_count) {
        this.favourites_count = favourites_count;
    }

    public Integer getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(Integer statuses_count) {
        this.statuses_count = statuses_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUtc_offset() {
        return utc_offset;
    }

    public void setUtc_offset(String utc_offset) {
        this.utc_offset = utc_offset;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public Boolean getGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(Boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Boolean getContributors_enabled() {
        return contributors_enabled;
    }

    public void setContributors_enabled(Boolean contributors_enabled) {
        this.contributors_enabled = contributors_enabled;
    }

    public Boolean getIs_translator() {
        return is_translator;
    }

    public void setIs_translator(Boolean is_translator) {
        this.is_translator = is_translator;
    }

    public String getProfile_background_color() {
        return profile_background_color;
    }

    public void setProfile_background_color(String profile_background_color) {
        this.profile_background_color = profile_background_color;
    }

    public String getProfile_background_image_url() {
        return profile_background_image_url;
    }

    public void setProfile_background_image_url(String profile_background_image_url) {
        this.profile_background_image_url = profile_background_image_url;
    }

    public String getProfile_background_image_url_https() {
        return profile_background_image_url_https;
    }

    public void setProfile_background_image_url_https(String profile_background_image_url_https) {
        this.profile_background_image_url_https = profile_background_image_url_https;
    }

    public Boolean getProfile_background_tile() {
        return profile_background_tile;
    }

    public void setProfile_background_tile(Boolean profile_background_tile) {
        this.profile_background_tile = profile_background_tile;
    }

    public String getProfile_link_color() {
        return profile_link_color;
    }

    public void setProfile_link_color(String profile_link_color) {
        this.profile_link_color = profile_link_color;
    }

    public String getProfile_sidebar_border_color() {
        return profile_sidebar_border_color;
    }

    public void setProfile_sidebar_border_color(String profile_sidebar_border_color) {
        this.profile_sidebar_border_color = profile_sidebar_border_color;
    }

    public String getProfile_sidebar_fill_color() {
        return profile_sidebar_fill_color;
    }

    public void setProfile_sidebar_fill_color(String profile_sidebar_fill_color) {
        this.profile_sidebar_fill_color = profile_sidebar_fill_color;
    }

    public String getProfile_text_color() {
        return profile_text_color;
    }

    public void setProfile_text_color(String profile_text_color) {
        this.profile_text_color = profile_text_color;
    }

    public Boolean getProfile_use_background_image() {
        return profile_use_background_image;
    }

    public void setProfile_use_background_image(Boolean profile_use_background_image) {
        this.profile_use_background_image = profile_use_background_image;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public void setProfile_banner_url(String profile_banner_url) {
        this.profile_banner_url = profile_banner_url;
    }

    public Boolean getDefault_profile() {
        return default_profile;
    }

    public void setDefault_profile(Boolean default_profile) {
        this.default_profile = default_profile;
    }

    public Boolean getDefault_profile_image() {
        return default_profile_image;
    }

    public void setDefault_profile_image(Boolean default_profile_image) {
        this.default_profile_image = default_profile_image;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollow_request_sent() {
        return follow_request_sent;
    }

    public void setFollow_request_sent(String follow_request_sent) {
        this.follow_request_sent = follow_request_sent;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }
}
