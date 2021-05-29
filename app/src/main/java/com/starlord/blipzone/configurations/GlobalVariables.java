package com.starlord.blipzone.configurations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

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


    public void setData() {
        editor.putString("key", "value");
        editor.apply();
    }

    public boolean hasUserLoggedIN() {
        return sharedPreferences.getBoolean("PREF_USER_LOGGED", false);
    }
}
