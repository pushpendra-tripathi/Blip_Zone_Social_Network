package com.starlord.blipzone.callbacks;

import org.jetbrains.annotations.NotNull;

import okhttp3.WebSocket;

public interface IActivityWebSocket {


    void SetOnActivityWebSocketListener(OnActivityWebSocketListener onActivityWebsocketListener);

    interface OnActivityWebSocketListener {
        void onMessageReceived(@NotNull WebSocket webSocket, @NotNull String text);
    }
}
