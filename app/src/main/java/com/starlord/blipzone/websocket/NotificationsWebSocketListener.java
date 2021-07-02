package com.starlord.blipzone.websocket;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.starlord.blipzone.callbacks.OnActivityWebSocketListener;
import com.starlord.blipzone.configurations.GlobalVariables;

import org.jetbrains.annotations.NotNull;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class NotificationsWebSocketListener extends WebSocketListener {
    private final String TAG = "Notifications_WS_Log";
    private Context mContext;
    private boolean isWebSocketConnected;

    private OnActivityWebSocketListener onActivityWebsocketListener;

    public NotificationsWebSocketListener(Context context, OnActivityWebSocketListener onActivityWebSocketListener) {
        mContext = context;
        this.onActivityWebsocketListener = onActivityWebSocketListener;
        Log.d(TAG, "NotificationsWebSocketListener: ");
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        Log.d(TAG, "onClosed: "+reason);
        isWebSocketConnected = false;
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        Log.d(TAG, "onClosing: "+reason);
        isWebSocketConnected = false;
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        Log.d(TAG, "onFailure: t "+ t );
        Log.d(TAG, "onFailure: response "+response);

        isWebSocketConnected = false;
        NotificationsWebSocketConnectionUtil.getInstance(mContext, onActivityWebsocketListener)
                .startWebSocket(GlobalVariables.getInstance(mContext).getWebSocketUrl());
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        Log.d(TAG, "onMessage: 1 " + text);
        if(onActivityWebsocketListener != null)
            onActivityWebsocketListener.onMessageReceived(webSocket, text);

    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
        Log.d(TAG, "onMessage: 2 " + bytes);

    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        Log.d(TAG, "onOpen: response " + response);
        Log.d(TAG, "onOpen: webSocket " + webSocket);

        isWebSocketConnected = true;
    }

    public boolean isWebSocketConnected(){
        return isWebSocketConnected;
    }


    public void onDestroy(){
        if(onActivityWebsocketListener != null){
            onActivityWebsocketListener = null;
        }

        if(mContext != null){
            mContext = null;
        }
    }
}