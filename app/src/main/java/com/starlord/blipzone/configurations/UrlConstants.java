package com.starlord.blipzone.configurations;

public class UrlConstants {

    //API connection urls------------------------------------------------------------------------------------------------------------------
    public static final String BASE_URL = "https://b16588e30a24.ngrok.io";

    public static final String REGISTER_USER = BASE_URL + "/api/auth/register/";
    public static final String VERIFY_USER = BASE_URL + "/api/auth/emailverify/";
    public static final String LOGIN_USER = BASE_URL + "/api/auth/login/";
    public static final String PROFILE = BASE_URL + "/api/auth/profile/";
    public static final String OTHER_PROFILE = BASE_URL + "/api/auth/profile/?id=";
    public static final String BLOG_LIST = BASE_URL + "/api/blog/post/list/";
    public static final String FOLLOW_LIST = BASE_URL + "/api/network/followlist/";
    public static final String UNFOLLOW = BASE_URL + "/api/network/unfollow/";
    public static final String SEARCH = BASE_URL + "/api/search/";
    public static final String GET_COMMENT = BASE_URL + "/api/blog/comment/";
    public static final String GET_LIKES = BASE_URL + "/api/blog/like/";
    public static final String BLOCK_LIST = BASE_URL + "/api/network/blocklist/";
    public static final String UNBLOCK = BASE_URL + "/api/network/unblock/";
    public static final String NOTIFICATIONS = BASE_URL + "/api/notification/";
    public static final String BLOG_POST = BASE_URL + "/api/blog/post/";

    //Web Socket connection urls------------------------------------------------------------------------------------------------------------------
    public static final String BASE_URL_WS = "wss://45ea49e72ef5.ngrok.io";

    public static final String NOTIFICATION_WS = BASE_URL_WS + "/ws/notification/";

    //Shared Preferences constants---------------------------------------------------------------------------------------------------------
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String USER_LOGGED = "userLoggedIn";
    public static final String PROFILE_DATA = "profileData";
    public static final String USER_PROFILE_IMAGE = "userProfileImage";
    public static final String USER_PROFILE_BIO = "userProfileBio";
    public static final String USER_NAME = "userName";
    public static final String FOLLOWERS_COUNT = "followersCount";
    public static final String FOLLOWING_COUNT = "FollowingCount";

    //Other Constants --------------------------------------------------------------------------------------------------------------------
    public static final String BROADCAST_TYPE = "BROADCAST_TYPE";
    public static final String BROADCAST_NEW_BLOG = "BROADCAST_NEW_BLOG";
    public static final String BROADCAST_NEW_BLOG_UPLOAD_SUCCESSFULLY = "BROADCAST_NEW_BLOG_UPLOAD_SUCCESSFULLY";
    public static final String BROADCAST_BLOG_UPLOADING_ERROR = "BROADCAST_BLOG_UPLOADING_ERROR";
}
