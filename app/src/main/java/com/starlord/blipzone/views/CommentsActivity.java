package com.starlord.blipzone.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.CommentsAdapter;
import com.starlord.blipzone.api.CommonClassForAPI;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.BlogModel;
import com.starlord.blipzone.models.CommentModel;
import com.starlord.blipzone.models.LikeModel;
import com.starlord.blipzone.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.starlord.blipzone.api.CommonClassForAPI.*;
import static com.starlord.blipzone.configurations.UrlConstants.*;

public class CommentsActivity extends AppCompatActivity {
    String blogId;
    ImageView backBtn, sendBtn;
    TextView title;
    RecyclerView commentsRecyclerView;
    LinearLayoutManager linearLayoutManager;
    EditText commentBox;
    ArrayList<CommentModel> commentModelArrayList;
    CommentsAdapter commentsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        blogId = getIntent().getStringExtra("blogId");

        initializeViews();

        loadCommentsRequest();

        backBtn.setOnClickListener(v -> {
            onBackPressed();
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
}