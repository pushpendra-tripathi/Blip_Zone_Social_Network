package com.starlord.blipzone.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import static com.starlord.blipzone.configurations.UrlConstants.OTHER_PROFILE;

public class OtherProfileActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView followers, following, bio, usernameTxt, blockedText;
    Button followUnFollowBtn;
    ImageView backBtn;
    RecyclerView profileBlogRecyclerView;
    GridLayoutManager gridLayoutManager;
    ProfileAdapter profileAdapter;
    ConstraintLayout profileLayout;
    String TAG = "OtherProfileActivityLog";
    ArrayList<BlogModel> blogModelList;
    boolean isFollowing, isBlocked;
    private String userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("username");

        initializeViews();
        loadProfileDetails();

        backBtn.setOnClickListener(v ->{
            onBackPressed();
        });

        followUnFollowBtn.setOnClickListener(v ->{
            if (userName.equals(GlobalVariables.getInstance(OtherProfileActivity.this).getUserName())){
                //open edit profile activity
            } else {
                //handle follow/unfollow here
            }
        });
    }

    private void loadProfileDetails() {
        callAuthGetRequest(OtherProfileActivity.this, OTHER_PROFILE + userId, new ApiResultCallback() {
            @Override
            public void onAPIResultSuccess(JSONObject jsonObject) {
                Log.d(TAG, "onResponse: Success");
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

                isBlocked = data.getBoolean("is_blocked");
                if (!isBlocked){
                    blockedText.setVisibility(View.GONE);
                    profileLayout.setVisibility(View.VISIBLE);
                }
                if (!userName.equals(GlobalVariables.getInstance(OtherProfileActivity.this).getUserName())) {
                    isFollowing = data.getBoolean("is_following");
                    if (isFollowing) {
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

                if (!user.getString("about").equals("") && user.getString("about").length() > 4) {
                    bio.setVisibility(View.VISIBLE);
                    bio.setText(user.getString("about"));

                }
                JSONObject count = data.getJSONObject("count");

                followers.setText(count.getString("follower"));

                following.setText(count.getString("following"));

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
        profileLayout = findViewById(R.id.profileLayout);
        circleImageView = findViewById(R.id.circleImageView);
        backBtn = findViewById(R.id.backBtn_profile);
        followers = findViewById(R.id.follower_txt);
        blockedText = findViewById(R.id.blocked_text);
        following = findViewById(R.id.following_txt);
        usernameTxt = findViewById(R.id.username_profile);
        bio = findViewById(R.id.bio_txt);
        blogModelList = new ArrayList<>();
        followUnFollowBtn = findViewById(R.id.follow_unfollow_editprofile_btn);
        profileBlogRecyclerView = findViewById(R.id.profile_blog_rv);
        profileBlogRecyclerView.setHasFixedSize(false);
        gridLayoutManager = new GridLayoutManager(OtherProfileActivity.this, 3);
        profileAdapter = new ProfileAdapter(OtherProfileActivity.this, blogModelList);
        profileBlogRecyclerView.setLayoutManager(gridLayoutManager);
        profileBlogRecyclerView.setAdapter(profileAdapter);
    }
}