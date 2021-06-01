package com.starlord.blipzone.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.starlord.blipzone.R;
import com.starlord.blipzone.api.CommonClassForAPI;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.starlord.blipzone.api.CommonClassForAPI.*;
import static com.starlord.blipzone.configurations.UrlConstants.*;

public class ProfileFragment extends Fragment {
    CircleImageView circleImageView;
    TextView followers, following, bio, usernameTxt;
    Button followUnFollowProfileEdit;
    RecyclerView profileBlogRecyclerView;
    LinearLayoutManager linearLayoutManager;
    String TAG = "ProfileFragmentLog";
    List<BlogModel> blogModelList;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(profileView);
        return  profileView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProfileDetails();
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

    private void processProfileRequest(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject user = data.getJSONObject("user");
                usernameTxt.setText(user.getString("username"));

                //
                Glide.with(getActivity())
                        .load(user.getString("profile_image"))
                        .placeholder(R.drawable.profile_avatar)
                        .into(circleImageView);
                if(user.getString("about") != null) {
                    bio.setVisibility(View.VISIBLE);
                    bio.setText(user.getString("about"));
                }
                JSONObject count = data.getJSONObject("count");
                followers.setText(count.getString("follower"));
                following.setText(count.getString("following"));

                JSONArray blog = data.getJSONArray("blogs");
                for (int i=0; i<blog.length(); i++){
                    BlogModel blogModel = new BlogModel();
                }


            } else {
                Toast.makeText(getActivity(),
                        "Either username or email is already registered",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews(View view){
        circleImageView = view.findViewById(R.id.circleImageView);
        followers = view.findViewById(R.id.follower_txt);
        following = view.findViewById(R.id.following_txt);
        usernameTxt = view.findViewById(R.id.username_profile);
        bio = view.findViewById(R.id.bio_txt);
        followUnFollowProfileEdit = view.findViewById(R.id.follow_unfollow_editprofile_btn);
        profileBlogRecyclerView = view.findViewById(R.id.profile_blog_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        blogModelList = new ArrayList<>();
    }
}