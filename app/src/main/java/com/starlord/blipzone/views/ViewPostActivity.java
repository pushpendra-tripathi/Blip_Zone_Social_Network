package com.starlord.blipzone.views;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.HomeAdapter;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.models.UserModel;

import java.util.ArrayList;

public class ViewPostActivity extends AppCompatActivity {
    ArrayList<BlogModel> blogModelArrayList;
    ArrayList<BlogModel> profileBlogList;
    TextView title;
    ImageView backBtn;
    String userName, profileImageUrl;
    RecyclerView homeRecyclerView;
    LinearLayoutManager linearLayoutManager;
    HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        blogModelArrayList = (ArrayList<BlogModel>) getIntent().getSerializableExtra("blogModelArray");
        userName = getIntent().getStringExtra("userName");
        profileImageUrl = getIntent().getStringExtra("profileImage");

        initializeViews();

        for (int i = 0; i < blogModelArrayList.size(); i++) {
            BlogModel blogModel = blogModelArrayList.get(i);
            UserModel userModel = new UserModel();
            userModel.setUserName(userName);
            userModel.setProfileImage(profileImageUrl);
            blogModel.setUserModel(userModel);
            profileBlogList.add(blogModel);
            homeAdapter.notifyDataSetChanged();
        }

        backBtn.setOnClickListener(v -> onBackPressed());

    }

    private void initializeViews() {
        profileBlogList = new ArrayList<>();
        homeRecyclerView = findViewById(R.id.home_view_post_rv);
        linearLayoutManager = new LinearLayoutManager(ViewPostActivity.this);
        homeAdapter = new HomeAdapter(ViewPostActivity.this, profileBlogList);
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        homeRecyclerView.setAdapter(homeAdapter);
        title = findViewById(R.id.username_view_post);
        title.setText(R.string.user_post);
        backBtn = findViewById(R.id.backBtn_view_post);
    }
}