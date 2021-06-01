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
}
