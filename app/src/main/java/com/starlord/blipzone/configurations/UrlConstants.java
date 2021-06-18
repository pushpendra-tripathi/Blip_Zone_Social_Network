package com.starlord.blipzone.configurations;

public class UrlConstants {

    //API connection urls------------------------------------------------------------------------------------------------------------------
    public static final String BASE_URL = "http://13.126.42.158";

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
    public static final String UNLIKE = BASE_URL + "/api/blog/unlike/?id=";
    public static final String CHAT_MESSAGES = BASE_URL + "/api/chat/";
    public static final String GET_CHAT_LIST = BASE_URL + "/chat/get/chatlist/";

    //Web Socket connection urls------------------------------------------------------------------------------------------------------------------
    public static final String BASE_URL_WS = "ws://13.126.42.158";

    public static final String NOTIFICATION_WS = BASE_URL_WS + "/ws/notification/";
    public static final String INITIATE_CHAT_WS = BASE_URL_WS + "/ws/chat/";

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

    //WEB_SOCKET_CONSTANTS ---------------------------------------------------------------------------------------------------------------
    public static final String COMMENT_ACTION_WS = "COMMENT";
    public static final String LIKE_ACTION_WS = "LIKE";
    public static final String POST_ID_WS = "id";
    public static final String COMMENT_CONTENT_WS = "content";
    public static final String TYPE_WS = "type";
}
