package com.starlord.blipzone.configurations;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import static com.starlord.blipzone.configurations.UrlConstants.ACCESS_TOKEN;
import static com.starlord.blipzone.configurations.UrlConstants.FOLLOWERS_COUNT;
import static com.starlord.blipzone.configurations.UrlConstants.FOLLOWING_COUNT;
import static com.starlord.blipzone.configurations.UrlConstants.PROFILE_DATA;
import static com.starlord.blipzone.configurations.UrlConstants.REFRESH_TOKEN;
import static com.starlord.blipzone.configurations.UrlConstants.USER_LOGGED;
import static com.starlord.blipzone.configurations.UrlConstants.USER_NAME;
import static com.starlord.blipzone.configurations.UrlConstants.USER_PROFILE_BIO;
import static com.starlord.blipzone.configurations.UrlConstants.USER_PROFILE_IMAGE;

public class GlobalVariables {

    private static final String SHARED_PREFERENCES_FILE = "com.starlord.blipzone.SHARED_PREFERENCES_FILE";

    private static GlobalVariables instance;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    HashMap<String, Boolean> followerRecord;
    String webSocketUrl;

    public static GlobalVariables getInstance(Context context) {
        if (instance == null) {
            instance = new GlobalVariables(context);
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    private GlobalVariables(Context context) {
        this.context = context.getApplicationContext();
        followerRecord = new HashMap<>();
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        webSocketUrl = "";
    }

    // Methods --------------------------------------------------------------------------------------------------------------------------
    public void setData(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public boolean hasUserLoggedIN() {
        return sharedPreferences.getBoolean(USER_LOGGED, false);
    }

    public void userLoggedIN() {
        editor.putBoolean(USER_LOGGED, true);
        editor.apply();
    }

    //Access tokens ---------------------------------------------------------------------------------------------------------------------
    public String getUserToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, "");
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(REFRESH_TOKEN, "");
    }

    //-----------------------------------------------------------------------------------------------------------------------------------


    //User profile data shared preferences ---------------------------------------------------------------------------------------------
    public boolean isProfileDataSaved() {
        return sharedPreferences.getBoolean(PROFILE_DATA, false);
    }

    public void saveProfileData() {
        editor.putBoolean(PROFILE_DATA, true);
        editor.apply();
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public void setFollowers(String followersCount) {
        editor.putString(FOLLOWERS_COUNT, followersCount);
        editor.apply();
    }

    public String getFollowers() {
        return sharedPreferences.getString(FOLLOWERS_COUNT, "");
    }

    public void setFollowing(String followingCount) {
        editor.putString(FOLLOWING_COUNT, followingCount);
        editor.apply();
    }

    public String getFollowing() {
        return sharedPreferences.getString(FOLLOWING_COUNT, "");
    }

    public void setUserProfileImage(String userProfileImage) {
        editor.putString(USER_PROFILE_IMAGE, userProfileImage);
        editor.apply();
    }

    public String getUserProfileImage() {
        return sharedPreferences.getString(USER_PROFILE_IMAGE, "");
    }

    public void setUserProfileBio(String userProfileImage) {
        editor.putString(USER_PROFILE_BIO, userProfileImage);
        editor.apply();
    }

    public String getUserProfileBio() {
        return sharedPreferences.getString(USER_PROFILE_BIO, "");
    }
    //-----------------------------------------------------------------------------------------------------------------------------------

    public void followed(String username) {
        followerRecord.put(username, true);
    }

    public void unFollowed(String username) {
        followerRecord.put(username, false);
    }

    public boolean checkFollower(String userName) {
        if (followerRecord.containsKey(userName))
            return followerRecord.get(userName);
        return false;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------
    public void setWebSocketUrl(String url) {
        webSocketUrl = url;
    }

    public String getWebSocketUrl() {
        return webSocketUrl;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        if (context != null) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.i("isMyServiceRunning?", true + "");
                    return true;
                }
            }
            Log.i("isMyServiceRunning?", false + "");
            return false;
        }
        return false;
    }
}
