package com.starlord.blipzone.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.andrognito.flashbar.Flashbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.starlord.blipzone.R;
import com.starlord.blipzone.configurations.GlobalVariables;
import com.starlord.blipzone.views.fragments.AlertFragment;
import com.starlord.blipzone.views.fragments.HomeFragment;
import com.starlord.blipzone.views.fragments.ProfileFragment;
import com.starlord.blipzone.views.fragments.SearchFragment;
import com.starlord.blipzone.websocket.NotificationsWebSocketConnectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.starlord.blipzone.configurations.UrlConstants.NOTIFICATION_WS;

public class MainActivity extends AppCompatActivity {

    NotificationsWebSocketConnectionUtil notificationsWebSocketConnectionUtil;

    //close two time back press
    private long backPressedTime;
    private final long backPressedOffsetTime = 2000;

    Flashbar flashbar;

    private final String TAG = "MainActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        //Starting the global web socket for notifications.
        initiateGlobalNotificationWebSocket();

    }

    private void initiateGlobalNotificationWebSocket() {
        String SERVER_PATH = NOTIFICATION_WS +
                GlobalVariables.getInstance(MainActivity.this).getUserId()
                + "/?user_token="
                + GlobalVariables.getInstance(MainActivity.this).getUserToken();
        GlobalVariables.getInstance(MainActivity.this).setWebSocketUrl(SERVER_PATH);
        Log.d(TAG, "GlobalNotificationWebSocket address: " + SERVER_PATH);
        notificationsWebSocketConnectionUtil.startWebSocket(SERVER_PATH);
    }

    private void initializeViews() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        notificationsWebSocketConnectionUtil = NotificationsWebSocketConnectionUtil.
                getInstance(MainActivity.this, (webSocket, text) -> runOnUiThread(() ->{
                    try {
                        Log.d(TAG, "onMessageReceived: " + text);
                        JSONObject jsonObject = new JSONObject(text);
                        JSONObject message = jsonObject.getJSONObject("message");
                        String title;
                        if (message.getInt("type") == 2)
                            title = "Like";
                        else
                            title = "Comment";

                        flashbar = new Flashbar.Builder(MainActivity.this)
                                .gravity(Flashbar.Gravity.TOP)
                                .title(title)
                                .titleSizeInSp(24)
                                .message(message.getString("comment"))
                                .backgroundDrawable(R.drawable.bg_gradient)
                                .build();

                        flashbar.show();

                        new Handler().postDelayed(() -> flashbar.dismiss(), 2000);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    @SuppressLint("NonConstantResourceId")
    final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_search:
                fragment = new SearchFragment();
                break;

            case R.id.navigation_camera:
                startActivity(new Intent(MainActivity.this, CreatePostActivity.class));
                break;

            case R.id.navigation_alert:
                fragment = new AlertFragment();
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }

        return loadFragment(fragment);
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        if ((System.currentTimeMillis() - backPressedTime) < backPressedOffsetTime && (System.currentTimeMillis() - backPressedTime) > 0) {
            finish();
        } else {

            Toast.makeText(MainActivity.this,
                    "Tap again to exit."
                    , Toast.LENGTH_SHORT).show();
            backPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationsWebSocketConnectionUtil.closeWebSocket(1000, "User exited the app.");
        notificationsWebSocketConnectionUtil.onDestroyNotificationsWebSocketListener();
    }
}