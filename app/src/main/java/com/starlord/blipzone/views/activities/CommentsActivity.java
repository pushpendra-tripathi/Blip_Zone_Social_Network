package com.starlord.blipzone.views.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.CommentsAdapter;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.GlobalVariables;
import com.starlord.blipzone.models.CommentModel;
import com.starlord.blipzone.models.UserModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;
import static com.starlord.blipzone.configurations.UrlConstants.COMMENT_ACTION_WS;
import static com.starlord.blipzone.configurations.UrlConstants.COMMENT_CONTENT_WS;
import static com.starlord.blipzone.configurations.UrlConstants.GET_COMMENT;
import static com.starlord.blipzone.configurations.UrlConstants.NOTIFICATION_WS;
import static com.starlord.blipzone.configurations.UrlConstants.POST_ID_WS;
import static com.starlord.blipzone.configurations.UrlConstants.TYPE_WS;

public class CommentsActivity extends AppCompatActivity {
    String blogId;
    ImageView backBtn, sendBtn;
    TextView title;
    RecyclerView commentsRecyclerView;
    LinearLayoutManager linearLayoutManager;
    EditText commentBox;
    ArrayList<CommentModel> commentModelArrayList;
    CommentsAdapter commentsAdapter;
    private String userId;
    private WebSocket webSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Getting the userId and blogId of the requested post
        blogId = getIntent().getStringExtra("blogId");
        userId = String.valueOf(getIntent().getIntExtra("userId", 0));

        initiateSocketConnection(userId);

        initializeViews();

        loadCommentsRequest();

        backBtn.setOnClickListener(v -> onBackPressed());

        sendBtn.setOnClickListener(v -> {

            String comment = commentBox.getText().toString().trim();
            if (TextUtils.isEmpty(comment)) {
                commentBox.setError("Please enter a comment.");
                return;
            }
            //make a web socket connection and send the comment.
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put(TYPE_WS, COMMENT_ACTION_WS);
                jsonObject.put(POST_ID_WS, blogId);
                jsonObject.put(COMMENT_CONTENT_WS, comment);

                webSocket.send(jsonObject.toString());

                resetCommentBox();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Resetting comment box and loading new comment.
            resetCommentBox();
            loadCommentsRequest();
        });
    }

    private void loadCommentsRequest() {
        callAuthGetRequest(CommentsActivity.this,
                GET_COMMENT + blogId,
                new ApiResultCallback() {
                    @Override
                    public void onAPIResultSuccess(JSONObject jsonObject) {
                        processCommentsResponse(jsonObject);
                    }

                    @Override
                    public void onAPIResultError(VolleyError volleyError) {

                    }
                });
    }

    private void processCommentsResponse(JSONObject jsonObject) {
        if (commentModelArrayList.size() != 0)
            commentModelArrayList.clear();
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject comment = data.getJSONObject(i);
                    CommentModel commentModel = new CommentModel();

                    commentModel.setId(comment.getInt("id"));
                    JSONObject user = comment.getJSONObject("user");
                    UserModel userModel = new UserModel();
                    userModel.setId(user.getInt("id"));
                    userModel.setUserName(user.getString("username"));
                    userModel.setFirstName(user.getString("first_name"));
                    userModel.setLastName(user.getString("last_name"));
                    userModel.setProfileImage(user.getString("profile_image"));
                    commentModel.setUserModel(userModel);

                    commentModel.setCreatedAt(comment.getString("created_at"));
                    commentModel.setLastUpdatedAt(comment.getString("last_updated_at"));
                    commentModel.setContent(comment.getString("content"));
                    commentModel.setPostId(comment.getInt("post"));
                    commentModelArrayList.add(commentModel);
                    commentsAdapter.notifyDataSetChanged();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        title = findViewById(R.id.comment_title);
        title.setText(R.string.comments);
        backBtn = findViewById(R.id.backBtn_comment);
        commentsRecyclerView = findViewById(R.id.comments_rv);
        commentBox = findViewById(R.id.comment);
        sendBtn = findViewById(R.id.ivPostComment);
        commentModelArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(CommentsActivity.this);
        commentsAdapter = new CommentsAdapter(CommentsActivity.this, commentModelArrayList);
        commentsRecyclerView.setLayoutManager(linearLayoutManager);
        commentsRecyclerView.setAdapter(commentsAdapter);
    }

    private void resetCommentBox() {
        commentBox.setText("");
    }

    private void initiateSocketConnection(String userID) {
        String SERVER_PATH = NOTIFICATION_WS + userId + "/?user_token="
                + GlobalVariables.getInstance(CommentsActivity.this).getUserToken();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new CommentWebSocketListener());
    }

    private class CommentWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

//            runOnUiThread(() -> Toast.makeText(CommentsActivity.this,
//                    "Socket Connection Successful!",
//                    Toast.LENGTH_SHORT).show());

        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String textResponse) {
            super.onMessage(webSocket, textResponse);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(textResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @org.jetbrains.annotations.Nullable Response response) {
            super.onFailure(webSocket, t, response);
            runOnUiThread(() -> Toast.makeText(CommentsActivity.this,
                    "Connection Failed, retrying...",
                    Toast.LENGTH_SHORT).show());

            initiateSocketConnection(userId);
        }
    }
}