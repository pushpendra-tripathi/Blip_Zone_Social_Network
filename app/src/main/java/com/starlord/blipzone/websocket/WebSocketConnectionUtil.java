package com.starlord.blipzone.websocket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.starlord.blipzone.configurations.GlobalVariables;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketConnectionUtil {
    private static final String TAG = "WebSocketConnectionUtil";
    public static OkHttpClient okHttpClient;
    public static WebSocket activityWebSocket;
    public static CustomWebSocketListener customWebSocketListener;
    public static Request request;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static WebSocketConnectionUtil webSocketConnectionUtil;


    private final Handler mHandler;


    public WebSocketConnectionUtil(Context context) {
        customWebSocketListener = new CustomWebSocketListener(mContext);
        HandlerThread mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public static WebSocketConnectionUtil getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (webSocketConnectionUtil == null) {
            synchronized (WebSocketConnectionUtil.class) {
                if (webSocketConnectionUtil == null) {
                    webSocketConnectionUtil = new WebSocketConnectionUtil(mContext);
                }
            }
        }
        return webSocketConnectionUtil;
    }

    public void startWebSocket(String url) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: token " + GlobalVariables.getInstance(mContext).getUserToken());
                okHttpClient = new OkHttpClient();
                request = new Request.Builder()
                        //.url(BASE_URL_WS + "/ws/notification/" + GlobalVariables.getInstance(mContext).getWebSocketUserId() + "/?token=" )
                        .url(url)
                        .build();
                activityWebSocket = okHttpClient.newWebSocket(request, customWebSocketListener);
            }
        }, 1000);
    }

    public void closeWebSocket(int code, String reason) {
        if (activityWebSocket != null) {
            activityWebSocket.close(code, reason);
        }
    }

    public boolean isWebSocketConnected() {
        return customWebSocketListener.isWebSocketConnected();
    }


    public void onDestroy() {
        if (mContext != null) {
            mContext = null;
        }
    }

    public void onDestroyCustomWebSocketListener() {
        webSocketConnectionUtil = null;

        if (mContext != null) {
            mContext = null;
        }

        if (customWebSocketListener != null) {
            customWebSocketListener.onDestroy();
            customWebSocketListener = null;
        }

    }
}
