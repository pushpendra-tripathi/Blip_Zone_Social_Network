package com.starlord.blipzone.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.starlord.blipzone.R;

public class FollowersListActivity extends AppCompatActivity {
    ImageView backBtn, ellipseMenu;
    RecyclerView followersRecyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follwers_list);

        initializeViews();
        loadFollowersRequest();
    }

    private void loadFollowersRequest() {
    }

    private void initializeViews() {
        backBtn = findViewById(R.id.backBtn_followers);
        ellipseMenu = findViewById(R.id.iv_menu_followers);
        followersRecyclerView = findViewById(R.id.followers_rv);
        linearLayoutManager = new LinearLayoutManager(FollowersListActivity.this);

    }
}