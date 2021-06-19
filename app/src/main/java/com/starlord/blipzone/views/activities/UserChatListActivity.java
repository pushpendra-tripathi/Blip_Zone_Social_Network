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
import com.starlord.blipzone.adapters.ChatListAdapter;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.GlobalVariables;
import com.starlord.blipzone.configurations.UrlConstants;
import com.starlord.blipzone.models.ChatListModel;
import com.starlord.blipzone.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;

public class UserChatListActivity extends AppCompatActivity {
    RecyclerView chatListRecyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView backBtn;
    TextView title;
    ArrayList<ChatListModel> chatListModelArrayList;
    ChatListAdapter chatListAdapter;
    String TAG = "UserChatListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat_list);

        initializeViews();

        loadChatListRequest();

        backBtn.setOnClickListener(v-> {
            onBackPressed();
        });
    }

    private void loadChatListRequest() {
        callAuthGetRequest(UserChatListActivity.this,
                UrlConstants.GET_CHAT_LIST,
                new ApiResultCallback() {
                    @Override
                    public void onAPIResultSuccess(JSONObject jsonObject) {
                        Log.d(TAG, "onResponseSuccess: " + jsonObject);
                        processChatListResponse(jsonObject);
                    }

                    @Override
                    public void onAPIResultError(VolleyError volleyError) {
                        Log.d(TAG, "OnError Response: " + volleyError.networkResponse.statusCode);
                        Toast.makeText(UserChatListActivity.this, "No chat data is available...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processChatListResponse(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i=0; i<data.length(); i++){
                    JSONObject chat = data.getJSONObject(i);
                    ChatListModel chatListModel = new ChatListModel();

                    chatListModel.setLastMessageTimeStamp(chat.getString("last_message"));
                    UserModel userModel = new UserModel();
                    JSONArray userArray = chat.getJSONArray("user");
                    if (userArray.length() == 2) {
                        JSONObject user1 = userArray.getJSONObject(0);
                        JSONObject user2 = userArray.getJSONObject(1);
                        try {
                            if (!user1.getString("username").equals(GlobalVariables.getInstance(UserChatListActivity.this).getUserName())) {
                                userModel.setId(user1.getInt("id"));
                                userModel.setUserName(user1.getString("username"));
                                userModel.setFirstName(user1.getString("first_name"));
                                userModel.setLastName(user1.getString("last_name"));
                                userModel.setActive(user1.getBoolean("is_active"));
                                userModel.setProfileImage(user1.getString("profile_image"));
                            } else {
                                userModel.setId(user2.getInt("id"));
                                userModel.setUserName(user2.getString("username"));
                                userModel.setFirstName(user2.getString("first_name"));
                                userModel.setLastName(user2.getString("last_name"));
                                userModel.setActive(user2.getBoolean("is_active"));
                                userModel.setProfileImage(user2.getString("profile_image"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        chatListModel.setUserModel(userModel);
                    } else {
                        continue;
                    }

                    JSONObject messageDetail = chat.getJSONObject("last_message_data");
                    chatListModel.setSender(messageDetail.getString("sender"));
                    chatListModel.setText(messageDetail.getString("text"));

                    chatListModelArrayList.add(chatListModel);
                    chatListAdapter.notifyDataSetChanged();

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        backBtn = findViewById(R.id.backBtn_chatList);
        title = findViewById(R.id.title_chatList);
        title.setText(R.string.chats);
        chatListRecyclerView = findViewById(R.id.chatList_rv);
        linearLayoutManager = new LinearLayoutManager(UserChatListActivity.this);
        chatListModelArrayList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(UserChatListActivity.this, chatListModelArrayList);
        chatListRecyclerView.setLayoutManager(linearLayoutManager);
        chatListRecyclerView.setAdapter(chatListAdapter);
    }
}