package com.starlord.blipzone.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.PersonsAdapter;
import com.starlord.blipzone.api.CommonClassForAPI;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowersListActivity extends AppCompatActivity {
    ImageView backBtn, ellipseMenu;
    RecyclerView followersRecyclerView;
    TextView title;
    LinearLayoutManager linearLayoutManager;
    ArrayList<UserModel> followersList;
    String TAG = "FollowersListActivityLog";
    PersonsAdapter personsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follwers_list);

        initializeViews();
        loadFollowersRequest();

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void loadFollowersRequest() {
        CommonClassForAPI.callAuthGetRequest(FollowersListActivity.this,
                UrlConstants.FOLLOW_LIST,
                new ApiResultCallback() {
                    @Override
                    public void onAPIResultSuccess(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse: Success");
                        processFollowersResponse(jsonObject);
                    }

                    @Override
                    public void onAPIResultError(VolleyError volleyError) {
                        Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                        Toast.makeText(FollowersListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processFollowersResponse(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray followers = data.getJSONArray("follower");
                for (int i = 0; i < followers.length(); i++) {

                    try {
                        JSONObject user = followers.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setId(user.getInt("id"));
                        userModel.setUserName(user.getString("username"));
                        userModel.setFirstName(user.getString("first_name"));
                        userModel.setLastName(user.getString("last_name"));
                        userModel.setProfileImage(user.getString("profile_image"));
                        followersList.add(userModel);
                        personsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                Toast.makeText(FollowersListActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        title = findViewById(R.id.title_follower);
        title.setText(R.string.followers);
        backBtn = findViewById(R.id.backBtn_followers);
        ellipseMenu = findViewById(R.id.iv_menu_followers);
        followersList = new ArrayList<>();
        followersRecyclerView = findViewById(R.id.followers_rv);
        linearLayoutManager = new LinearLayoutManager(FollowersListActivity.this);
        personsAdapter = new PersonsAdapter(FollowersListActivity.this, followersList);
        followersRecyclerView.setLayoutManager(linearLayoutManager);
        followersRecyclerView.setAdapter(personsAdapter);
    }
}