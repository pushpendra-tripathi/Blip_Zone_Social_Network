package com.starlord.blipzone.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.ProfileAdapter;
import com.starlord.blipzone.callbacks.ApiResponseCallback;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.GlobalVariables;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.BlogModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;
import static com.starlord.blipzone.api.CommonClassForAPI.callFollowUnfollowRequest;
import static com.starlord.blipzone.configurations.UrlConstants.FOLLOW_LIST;
import static com.starlord.blipzone.configurations.UrlConstants.OTHER_PROFILE;
import static com.starlord.blipzone.configurations.UrlConstants.UNFOLLOW;

public class OtherProfileActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView followers, following, bio, usernameTxt;
    Button followUnFollowBtn, initiateChatBtn, editProfile;
    ImageView backBtn;
    RecyclerView profileBlogRecyclerView;
    GridLayoutManager gridLayoutManager;
    ProfileAdapter profileAdapter;
    ConstraintLayout profileLayout;
    LinearLayout followerLayout, followingLayout;
    String TAG = "OtherProfileActivityLog";
    ArrayList<BlogModel> blogModelList;
    String about = "", firstName = "", lastName = "";
    boolean isFollowing;
    private String userId;
    private String userName;
    private String profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("username");

        initializeViews();
        loadProfileDetails();

        if (userName.equals(GlobalVariables.getInstance(OtherProfileActivity.this).getUserName())){
            initiateChatBtn.setVisibility(View.GONE);
            followUnFollowBtn.setVisibility(View.GONE);
            editProfile.setVisibility(View.VISIBLE);
        }

        followerLayout.setOnClickListener(v ->{
            Intent intent = new Intent(OtherProfileActivity.this, FollowersListActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        followingLayout.setOnClickListener(v ->{
            Intent intent = new Intent(OtherProfileActivity.this, FollowingListActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> onBackPressed());

        editProfile.setOnClickListener(v-> {
            Intent intent = new Intent(OtherProfileActivity.this, EditProfileActivity.class);
            if (!firstName.equals(""))
                intent.putExtra("firstName", firstName);
            if (!lastName.equals(""))
                intent.putExtra("lastName", lastName);
            if (!about.equals(""))
                intent.putExtra("about", about);
            if (!profileImage.equals(""))
                intent.putExtra("profileImage", profileImage);
            startActivity(intent);
        });

        followUnFollowBtn.setOnClickListener(v ->{
            if (userName.equals(GlobalVariables.getInstance(OtherProfileActivity.this).getUserName())){
                //open edit profile activity
            } else {
                //handle follow/unfollow here
                if (followUnFollowBtn.getText().equals("Follow")) {
                    callFollowUnfollowRequest(OtherProfileActivity.this,
                            FOLLOW_LIST,
                            userId,
                            new ApiResponseCallback() {
                                @Override
                                public void onApiSuccessResult(JSONObject jsonObject) {
                                    processFollowResponse(jsonObject);
                                }

                                @Override
                                public void onApiFailureResult(Exception e) {

                                }

                                @Override
                                public void onApiErrorResult(VolleyError volleyError) {

                                }
                            });
                } else if (followUnFollowBtn.getText().equals("Unfollow")) {
                    callFollowUnfollowRequest(OtherProfileActivity.this,
                            UNFOLLOW,
                            userId,
                            new ApiResponseCallback() {
                                @Override
                                public void onApiSuccessResult(JSONObject jsonObject) {
                                    Log.d(TAG, "onResponse: Success");
                                    processUnFollowResponse(jsonObject);
                                }

                                @Override
                                public void onApiFailureResult(Exception e) {
                                    Log.d(TAG, "onAPIResultError: " + e.toString());
                                }

                                @Override
                                public void onApiErrorResult(VolleyError volleyError) {
                                    Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                                    Toast.makeText(OtherProfileActivity.this,
                                            "Something went wrong",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        initiateChatBtn.setOnClickListener(v-> {
            //opening the chat between users.
            Intent chatIntent = new Intent(OtherProfileActivity.this, ChatActivity.class);
            chatIntent.putExtra("username", userName);
            chatIntent.putExtra("profileImage", profileImage);
            startActivity(chatIntent);
        });
    }

    private void processUnFollowResponse(JSONObject jsonObject) {
        GlobalVariables.getInstance(OtherProfileActivity.this).unFollowed(userName);
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                followUnFollowBtn.setText(R.string.follow);
                int count = Integer.parseInt(followers.getText().toString()) - 1;
                followers.setText(String.valueOf(count));
            } else {
                Toast.makeText(OtherProfileActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void processFollowResponse(JSONObject jsonObject) {
        GlobalVariables.getInstance(OtherProfileActivity.this).followed(userName);
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                followUnFollowBtn.setText(R.string.unfollow);
                int count = Integer.parseInt(followers.getText().toString()) + 1;
                followers.setText(String.valueOf(count));
            } else {
                Toast.makeText(OtherProfileActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadProfileDetails() {
        callAuthGetRequest(OtherProfileActivity.this, OTHER_PROFILE + userId, new ApiResultCallback() {
            @Override
            public void onAPIResultSuccess(JSONObject jsonObject) {
                Log.d(TAG, "onResponse: Success-> " + jsonObject);
                processProfileRequest(jsonObject);
            }

            @Override
            public void onAPIResultError(VolleyError volleyError) {
                Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                Toast.makeText(OtherProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processProfileRequest(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONObject data = jsonObject.getJSONObject("data");

                if (!userName.equals(GlobalVariables.getInstance(OtherProfileActivity.this).getUserName())) {
                    isFollowing = data.getBoolean("is_following");
                    if (isFollowing || GlobalVariables.getInstance(OtherProfileActivity.this).checkFollower(userName)) {
                        followUnFollowBtn.setText(R.string.unfollow);
                    } else {
                        followUnFollowBtn.setText(R.string.follow);
                    }
                } else {
                    followUnFollowBtn.setText(R.string.edit_profile);
                }

                JSONObject user = data.getJSONObject("user");

                usernameTxt.setText(user.getString("username"));

                Picasso.get().load(UrlConstants.BASE_URL + user.getString("profile_image"))
                        .placeholder(R.drawable.profile_avatar)
                        .into(circleImageView);
                profileImage = UrlConstants.BASE_URL + user.getString("profile_image");

                if (!user.getString("about").equals("") && user.getString("about").length() > 4) {
                    bio.setVisibility(View.VISIBLE);
                    bio.setText(user.getString("about"));
                    about = user.getString("about");
                }

                if (!user.getString("first_name").equals(""))
                    firstName = user.getString("first_name");

                if (!user.getString("last_name").equals(""))
                    lastName = user.getString("last_name");

                JSONObject count = data.getJSONObject("count");

                followers.setText(count.getString("follower"));

                following.setText(count.getString("following"));

                profileAdapter = new ProfileAdapter(OtherProfileActivity.this, blogModelList,
                        user.getString("username"), user.getString("profile_image"));
                profileBlogRecyclerView.setLayoutManager(gridLayoutManager);
                profileBlogRecyclerView.setAdapter(profileAdapter);

                JSONArray blog = data.getJSONArray("blogs");
                for (int i = 0; i < blog.length(); i++) {
                    JSONObject blogObject = blog.getJSONObject(i);
                    BlogModel blogModel = new BlogModel();
                    blogModel.setImageUrl(blogObject.getString("image"));
                    blogModel.setId(blogObject.getInt("id"));
                    blogModelList.add(blogModel);
                    profileAdapter.notifyDataSetChanged();
                }

            } else {
                Toast.makeText(OtherProfileActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        followerLayout = findViewById(R.id.followersLayout_other);
        followingLayout = findViewById(R.id.followingLayout_other);
        profileLayout = findViewById(R.id.profileLayout);
        circleImageView = findViewById(R.id.circleImageView);
        backBtn = findViewById(R.id.backBtn_profile);
        followers = findViewById(R.id.follower_txt);
        following = findViewById(R.id.following_txt);
        usernameTxt = findViewById(R.id.username_profile);
        bio = findViewById(R.id.bio_txt);
        blogModelList = new ArrayList<>();
        followUnFollowBtn = findViewById(R.id.follow_unfollow_editprofile_btn);
        initiateChatBtn = findViewById(R.id.initiate_chat_btn);
        editProfile = findViewById(R.id.editprofile_btn);
        profileBlogRecyclerView = findViewById(R.id.profile_blog_rv);
        profileBlogRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(OtherProfileActivity.this, 2);

    }
}