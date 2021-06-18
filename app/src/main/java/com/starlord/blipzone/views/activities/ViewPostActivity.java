package com.starlord.blipzone.views.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.HomeAdapter;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.GlobalVariables;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.models.UserModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthPostRequest;
import static com.starlord.blipzone.configurations.UrlConstants.LIKE_ACTION_WS;
import static com.starlord.blipzone.configurations.UrlConstants.NOTIFICATION_WS;
import static com.starlord.blipzone.configurations.UrlConstants.POST_ID_WS;
import static com.starlord.blipzone.configurations.UrlConstants.TYPE_WS;
import static com.starlord.blipzone.configurations.UrlConstants.UNLIKE;

public class ViewPostActivity extends AppCompatActivity {
    ArrayList<BlogModel> blogModelArrayList;
    ArrayList<BlogModel> profileBlogList;
    TextView title;
    ImageView backBtn;
    String userName, profileImageUrl;
    RecyclerView homeRecyclerView;
    LinearLayoutManager linearLayoutManager;
    HomeAdapter homeAdapter;
    private WebSocket webSocket;

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
        homeAdapter = new HomeAdapter(ViewPostActivity.this, profileBlogList, (userId, blogId, liked) -> {
            if (!liked){
                initiateSocketConnection(userId);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(TYPE_WS, LIKE_ACTION_WS);
                    jsonObject.put(POST_ID_WS, blogId);

                    webSocket.send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //implement api call for unlike the post
                callUnlikeRequest(blogId);
            }
        });
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        homeRecyclerView.setAdapter(homeAdapter);
        title = findViewById(R.id.username_view_post);
        title.setText(R.string.user_post);
        backBtn = findViewById(R.id.backBtn_view_post);
    }

    private void callUnlikeRequest(String blogId) {
        callAuthPostRequest(ViewPostActivity.this, UNLIKE + blogId, new ApiResultCallback() {
            @Override
            public void onAPIResultSuccess(JSONObject jsonObject) {

            }

            @Override
            public void onAPIResultError(VolleyError volleyError) {
                Toast.makeText(ViewPostActivity.this,
                        "Something went wrong, please check your internet connectivity",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initiateSocketConnection(String userID) {
        String SERVER_PATH = NOTIFICATION_WS + userID + "/?user_token="
                + GlobalVariables.getInstance(ViewPostActivity.this).getUserToken();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new CommentWebSocketListener());
    }

    private static class CommentWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String textResponse) {
            super.onMessage(webSocket, textResponse);

        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @org.jetbrains.annotations.Nullable Response response) {
            super.onFailure(webSocket, t, response);

        }
    }
}