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
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;
import static com.starlord.blipzone.configurations.UrlConstants.GET_LIKES;

public class LikesActivity extends AppCompatActivity {
    ImageView backBtn, ellipseMenu;
    RecyclerView likesRecyclerView;
    TextView title;
    LinearLayoutManager linearLayoutManager;
    ArrayList<UserModel> likesList;
    String TAG = "LikesActivityLog";
    PersonsAdapter personsAdapter;
    String blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        blogId = getIntent().getStringExtra("blogId");

        initializeViews();
        loadLikesRequest();

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void loadLikesRequest() {
        callAuthGetRequest(LikesActivity.this,
                GET_LIKES + blogId,
                new ApiResultCallback() {
                    @Override
                    public void onAPIResultSuccess(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse: Success");
                        processLikesResponse(jsonObject);
                    }

                    @Override
                    public void onAPIResultError(VolleyError volleyError) {
                        Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                        Toast.makeText(LikesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processLikesResponse(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {

                    try {
                        JSONObject likes = data.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        JSONObject user = likes.getJSONObject("user");
                        userModel.setId(user.getInt("id"));
                        userModel.setUserName(user.getString("username"));
                        userModel.setFirstName(user.getString("first_name"));
                        userModel.setLastName(user.getString("last_name"));
                        userModel.setProfileImage(user.getString("profile_image"));
                        likesList.add(userModel);
                        personsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                Toast.makeText(LikesActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        title = findViewById(R.id.title_likes);
        title.setText(R.string.likes);
        backBtn = findViewById(R.id.backBtn_likes);
        ellipseMenu = findViewById(R.id.iv_menu_likes);
        likesList = new ArrayList<>();
        likesRecyclerView = findViewById(R.id.likes_rv);
        linearLayoutManager = new LinearLayoutManager(LikesActivity.this);
        personsAdapter = new PersonsAdapter(LikesActivity.this, likesList);
        likesRecyclerView.setLayoutManager(linearLayoutManager);
        likesRecyclerView.setAdapter(personsAdapter);
    }
}