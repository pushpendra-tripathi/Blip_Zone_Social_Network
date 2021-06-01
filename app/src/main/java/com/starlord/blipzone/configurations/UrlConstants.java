package com.starlord.blipzone.configurations;

public class UrlConstants {

    //API connection urls
    public static final String BASE_URL = "https://603a2f0f58cb.ngrok.io/";
    public static final String REGISTER_USER = BASE_URL + "api/auth/register/";
    public static final String VERIFY_USER = BASE_URL + "api/auth/emailverify/";
    public static final String LOGIN_USER = BASE_URL + "api/auth/login/";
    public static final String PROFILE = BASE_URL + "api/auth/profile/";

    //Shared Preferences constants
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String USER_LOGGED = "userLoggedIn";
}
