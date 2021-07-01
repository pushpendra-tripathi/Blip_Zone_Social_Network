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

public class NotificationsWebSocketConnectionUtil {
    private final String TAG = "Notifications_WS_ConLog";
    private OkHttpClient okHttpClient;
    private WebSocket activityWebSocket;
    private NotificationsWebSocketListener notificationsWebSocketListener;
    private Request request;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static NotificationsWebSocketConnectionUtil notificationsWebSocketConnectionUtil;


    private final Handler mHandler;


    public NotificationsWebSocketConnectionUtil(Context context) {
        notificationsWebSocketListener = new NotificationsWebSocketListener(mContext);
        HandlerThread mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public static NotificationsWebSocketConnectionUtil getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (notificationsWebSocketConnectionUtil == null) {
            synchronized (NotificationsWebSocketConnectionUtil.class) {
                if (notificationsWebSocketConnectionUtil == null) {
                    notificationsWebSocketConnectionUtil = new NotificationsWebSocketConnectionUtil(mContext);
                }
            }
        }
        return notificationsWebSocketConnectionUtil;
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
                activityWebSocket = okHttpClient.newWebSocket(request, notificationsWebSocketListener);
            }
        }, 1000);
    }

    public void closeWebSocket(int code, String reason) {
        if (activityWebSocket != null) {
            activityWebSocket.close(code, reason);
        }
    }

    public boolean isWebSocketConnected() {
        return notificationsWebSocketListener.isWebSocketConnected();
    }


    public void onDestroy() {
        if (mContext != null) {
            mContext = null;
        }
    }

    public void onDestroyCustomWebSocketListener() {
        notificationsWebSocketConnectionUtil = null;

        if (mContext != null) {
            mContext = null;
        }

        if (notificationsWebSocketListener != null) {
            notificationsWebSocketListener.onDestroy();
            notificationsWebSocketListener = null;
        }

    }
}
