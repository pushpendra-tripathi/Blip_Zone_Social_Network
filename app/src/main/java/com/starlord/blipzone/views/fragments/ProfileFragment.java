package com.starlord.blipzone.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.ProfileAdapter;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.GlobalVariables;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.views.activities.EditProfileActivity;
import com.starlord.blipzone.views.activities.FollowersListActivity;
import com.starlord.blipzone.views.activities.FollowingListActivity;
import com.starlord.blipzone.views.activities.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;
import static com.starlord.blipzone.configurations.UrlConstants.BASE_URL;
import static com.starlord.blipzone.configurations.UrlConstants.PROFILE;

public class ProfileFragment extends Fragment {
    CircleImageView circleImageView;
    TextView followers, following, bio, usernameTxt;
    String about = "", firstName = "", lastName = "", profileImage = "";
    Button followUnFollowProfileEdit;
    RecyclerView profileBlogRecyclerView;
    GridLayoutManager gridLayoutManager;
    ImageView logOutBtn;
    ProfileAdapter profileAdapter;
    LinearLayout followerLayout, followingLayout;
    String TAG = "ProfileFragmentLog";
    ArrayList<BlogModel> blogModelList;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
      ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(profileView);
        return profileView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (GlobalVariables.getInstance(getActivity()).isProfileDataSaved())
            loadProfileInfo();

        loadProfileDetails();

        followerLayout.setOnClickListener(v ->{
            startActivity(new Intent(getActivity(), FollowersListActivity.class));
        });

        followingLayout.setOnClickListener(v ->{
            startActivity(new Intent(getActivity(), FollowingListActivity.class));
        });

        logOutBtn.setOnClickListener(v-> {
            Log.d(TAG, "Log Out button pressed.");
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Sign Out")
                    .setMessage("Do you want to log out!")
                    .setPositiveButton("Log Out", (dialogInterface, i) -> {
                        Log.d(TAG, "User Logged out successfully");
                        GlobalVariables.getInstance(getActivity()).userLogOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        requireActivity().finish();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });

        followUnFollowProfileEdit.setOnClickListener(v-> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
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
    }

    private void loadProfileDetails() {
        callAuthGetRequest(getActivity(), PROFILE, new ApiResultCallback() {
            @Override
            public void onAPIResultSuccess(JSONObject jsonObject) {
                Log.d(TAG, "onResponse: Success");
                processProfileRequest(jsonObject);
            }

            @Override
            public void onAPIResultError(VolleyError volleyError) {
                Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfileInfo() {
        usernameTxt.setText(GlobalVariables.getInstance(getActivity()).getUserName());

        Picasso.get().load(GlobalVariables.getInstance(getActivity()).getUserProfileImage())
                .placeholder(R.drawable.profile_avatar)
                .into(circleImageView);
        if (!GlobalVariables.getInstance(getActivity()).getUserProfileBio().equals("")) {
            bio.setVisibility(View.VISIBLE);
            bio.setText(GlobalVariables.getInstance(getActivity()).getUserProfileBio());
        }
        followers.setText(GlobalVariables.getInstance(getActivity()).getFollowers());
        following.setText(GlobalVariables.getInstance(getActivity()).getFollowing());
    }

    private void processProfileRequest(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject user = data.getJSONObject("user");

                usernameTxt.setText(user.getString("username"));
                GlobalVariables.getInstance(getActivity()).setUserName(user.getString("username"));

                profileImage = BASE_URL + user.getString("profile_image");
                Picasso.get().load(BASE_URL + user.getString("profile_image"))
                        .placeholder(R.drawable.profile_avatar)
                        .into(circleImageView);
                GlobalVariables.getInstance(getActivity()).setUserProfileImage(user.getString("profile_image"));

                if (!user.getString("about").equals("") && user.getString("about").length() > 4) {
                    bio.setVisibility(View.VISIBLE);
                    bio.setText(user.getString("about"));
                    about = user.getString("about");
                    GlobalVariables.getInstance(getActivity()).setUserProfileBio(user.getString("about"));

                }

                if (!user.getString("first_name").equals(""))
                    firstName = user.getString("first_name");

                if (!user.getString("last_name").equals(""))
                    lastName = user.getString("last_name");

                JSONObject count = data.getJSONObject("count");

                if (count.getString("follower").equals("")) {
                    followers.setText("0");
                    GlobalVariables.getInstance(getActivity()).setFollowers("0");
                } else {
                    followers.setText(count.getString("follower"));
                    GlobalVariables.getInstance(getActivity()).setFollowers(count.getString("follower"));
                }

                if (count.getString("following").equals("")) {
                    following.setText("0");
                    GlobalVariables.getInstance(getActivity()).setFollowing("0");
                } else{
                    following.setText(count.getString("following"));
                    GlobalVariables.getInstance(getActivity()).setFollowing(count.getString("following"));
                }

                profileAdapter = new ProfileAdapter(getActivity(), blogModelList,
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

                GlobalVariables.getInstance(getActivity()).saveProfileData();

            } else {
                Toast.makeText(getActivity(),
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews(View view) {
        circleImageView = view.findViewById(R.id.circleImageView);
        logOutBtn = view.findViewById(R.id.iv_menu_logout);
        followers = view.findViewById(R.id.follower_txt);
        following = view.findViewById(R.id.following_txt);
        usernameTxt = view.findViewById(R.id.username_profile);
        followerLayout = view.findViewById(R.id.followerLayout);
        followingLayout = view.findViewById(R.id.followingLayout);
        bio = view.findViewById(R.id.bio_txt);
        blogModelList = new ArrayList<>();
        followUnFollowProfileEdit = view.findViewById(R.id.follow_unfollow_editprofile_btn);
        profileBlogRecyclerView = view.findViewById(R.id.profile_blog_rv);
        profileBlogRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

    }
}