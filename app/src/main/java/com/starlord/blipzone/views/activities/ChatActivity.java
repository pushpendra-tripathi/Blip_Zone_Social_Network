package com.starlord.blipzone.views.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.adapters.ChatAdapter;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.GlobalVariables;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static com.starlord.blipzone.api.CommonClassForAPI.callAuthGetRequest;
import static com.starlord.blipzone.configurations.UrlConstants.CHAT_MESSAGES;
import static com.starlord.blipzone.configurations.UrlConstants.CONTENT_WS;
import static com.starlord.blipzone.configurations.UrlConstants.INITIATE_CHAT_WS;
import static com.starlord.blipzone.configurations.UrlConstants.MESSAGE_ACTION_WS;
import static com.starlord.blipzone.configurations.UrlConstants.TYPE_WS;

public class ChatActivity extends AppCompatActivity implements TextWatcher {

    private WebSocket chatWebSocket;
    private EditText messageEdit;
    private ImageView sendBtn, backBtn;
    private RecyclerView recyclerView;
    private ChatAdapter messageAdapter;
    private TextView userNameTxt;
    CircleImageView profileImageView;
    String userName;
    boolean inScreen = true;
    String TAG = "ChatActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userName = getIntent().getStringExtra("username");
        String profileImage = getIntent().getStringExtra("profileImage");

        initializeView();

        initiateChatWebSocketConnection(userName);

        loadChatMessagesRequest(userName);

        userNameTxt.setText(userName);
        Picasso.get().load(profileImage)
                .placeholder(R.drawable.profile_avatar)
                .into(profileImageView);

        backBtn.setOnClickListener(v-> onBackPressed());
    }

    private void loadChatMessagesRequest(String userName) {
        callAuthGetRequest(ChatActivity.this,
                CHAT_MESSAGES + userName +"/",
                new ApiResultCallback() {
                    @Override
                    public void onAPIResultSuccess(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse ChatMessages: " + jsonObject);
                        processChatMessagesRequest(jsonObject);
                    }

                    @Override
                    public void onAPIResultError(VolleyError volleyError) {
                        Log.d(TAG, "onError ChatMessages: " + volleyError.networkResponse.statusCode);
                    }
                });
    }

    private void processChatMessagesRequest(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject user = data.getJSONObject("user_data");
                ArrayList<JSONObject> chatList = new ArrayList<>();

                JSONArray messageArray = data.getJSONArray("message");
                for (int i = 0; i < messageArray.length(); i++) {
                    JSONObject message = messageArray.getJSONObject(i);
                    JSONObject chatMessage = new JSONObject();
                    chatMessage.put("text", message.getString("text"));
                    chatMessage.put("username", message.getString("sender"));

                    chatList.add(chatMessage);
                }

                for (int i = chatList.size() - 1; i >= 0; i--) {
                    messageAdapter.addItem(chatList.get(i));
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                }

            } else {
                Toast.makeText(ChatActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        } else {

            sendBtn.setVisibility(View.VISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);
        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        messageEdit.addTextChangedListener(this);

    }

    private void initiateChatWebSocketConnection(String userName) {
        String SERVER_PATH = INITIATE_CHAT_WS + userName + "/?user_token="
                + GlobalVariables.getInstance(ChatActivity.this).getUserToken();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        chatWebSocket = client.newWebSocket(request, new ChatWebSocketListener());
        Log.d(TAG, "ChatWebSocketAddress: " + SERVER_PATH);

    }


    private void initializeView() {

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        backBtn = findViewById(R.id.backBtn_chat);
        recyclerView = findViewById(R.id.recyclerView);
        userNameTxt = findViewById(R.id.username_chat);
        profileImageView = findViewById(R.id.profile_photo_chat);

        messageAdapter = new ChatAdapter(ChatActivity.this, userName, getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(view -> {
            // Sending messages to respective web socket connections
            try {
                JSONObject message = new JSONObject();
                message.put(TYPE_WS, MESSAGE_ACTION_WS);
                message.put(CONTENT_WS, messageEdit.getText().toString());
                //if (inScreen)
                    chatWebSocket.send(message.toString());
                //else
                    //globalChatWebSocket.send(messageEdit.getText().toString());
                resetMessageEdit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }


    private class ChatWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> Log.d(TAG, "ChatWebSocket onOpen: " + response.message()));

        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String textResponse) {
            super.onMessage(webSocket, textResponse);

            runOnUiThread(() -> {

                try {
                    Log.d(TAG, "ChatWebSocket onMessage: " + textResponse);
                    JSONObject jsonObject = new JSONObject(textResponse);
                    if (jsonObject.has(userName))
                        inScreen = jsonObject.getBoolean(userName);
                    //Log.d(TAG, userName + " " + inScreen);
                    if (jsonObject.has("text")) {
                        messageAdapter.addItem(jsonObject);
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @org.jetbrains.annotations.Nullable Response response) {
            super.onFailure(webSocket, t, response);
            runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show());
            assert response != null;
            Log.d(TAG, "ChatWebSocket onFailure: " + response.message());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatWebSocket.close(1000, "User exited the chat screen");
    }
}