package com.starlord.blipzone.configurations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.starlord.blipzone.configurations.UrlConstants.*;

public class GlobalVariables {

    private static final String SHARED_PREFERENCES_FILE = "com.starlord.blipzone.SHARED_PREFERENCES_FILE";

    private static GlobalVariables instance;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static GlobalVariables getInstance(Context context) {
        if (instance == null) {
            instance = new GlobalVariables(context);
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    private GlobalVariables(Context context) {
        this.context = context.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


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

    public String getUserToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, "");
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(REFRESH_TOKEN, "");
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public boolean isProfileDataSaved() {
        return sharedPreferences.getBoolean(PROFILE_DATA, false);
    }

    public void saveProfileData() {
        editor.putBoolean(PROFILE_DATA, true);
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
}
