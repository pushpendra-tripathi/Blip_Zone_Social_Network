package com.starlord.blipzone.views.activities;

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

public class FollowingListActivity extends AppCompatActivity {

    ImageView backBtn, ellipseMenu;
    RecyclerView followersRecyclerView;
    TextView title;
    LinearLayoutManager linearLayoutManager;
    ArrayList<UserModel> followingList;
    String TAG = "FollowingListActivityLog";
    PersonsAdapter personsAdapter;
    String url;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
            url = UrlConstants.OTHERS_FOLLOW_LIST + userId;
        }
        else
            url = UrlConstants.FOLLOW_LIST;

        initializeViews();
        loadFollowingRequest(url);

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void loadFollowingRequest(String url) {
        CommonClassForAPI.callAuthGetRequest(FollowingListActivity.this,
                url,
                new ApiResultCallback() {
                    @Override
                    public void onAPIResultSuccess(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse: Success");
                        processFollowingResponse(jsonObject);
                    }

                    @Override
                    public void onAPIResultError(VolleyError volleyError) {
                        Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                        Toast.makeText(FollowingListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processFollowingResponse(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray followers = data.getJSONArray("following");
                for (int i = 0; i < followers.length(); i++) {

                    try {
                        JSONObject user = followers.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setId(user.getInt("id"));
                        userModel.setUserName(user.getString("username"));
                        userModel.setFirstName(user.getString("first_name"));
                        userModel.setLastName(user.getString("last_name"));
                        userModel.setProfileImage(user.getString("profile_image"));
                        followingList.add(userModel);
                        personsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                Toast.makeText(FollowingListActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        title = findViewById(R.id.title_following);
        title.setText(R.string.following);
        backBtn = findViewById(R.id.backBtn_following);
        ellipseMenu = findViewById(R.id.iv_menu_following);
        followingList = new ArrayList<>();
        followersRecyclerView = findViewById(R.id.following_rv);
        linearLayoutManager = new LinearLayoutManager(FollowingListActivity.this);
        personsAdapter = new PersonsAdapter(FollowingListActivity.this, followingList);
        followersRecyclerView.setLayoutManager(linearLayoutManager);
        followersRecyclerView.setAdapter(personsAdapter);
    }
}