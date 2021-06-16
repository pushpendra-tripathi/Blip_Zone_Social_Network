package com.starlord.blipzone.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.starlord.blipzone.callbacks.ServiceCallback;

import java.util.Objects;

import static com.starlord.blipzone.configurations.UrlConstants.BROADCAST_BLOG_UPLOADING_ERROR;
import static com.starlord.blipzone.configurations.UrlConstants.BROADCAST_NEW_BLOG;
import static com.starlord.blipzone.configurations.UrlConstants.BROADCAST_NEW_BLOG_UPLOAD_SUCCESSFULLY;
import static com.starlord.blipzone.configurations.UrlConstants.BROADCAST_TYPE;

public class UploadService extends Service {

    private Activity context;
    private static final String TAG = "UploadService";
    private final IBinder mBinder = new LocalBinder();
    boolean mAllowRebind;
    ServiceCallback Callback;
    String caption;
    Bitmap uploadImage;
    boolean hasUploadedStarted = false;

    public UploadService() {
    }

    public UploadService(Activity context, ServiceCallback serviceCallback) {
        this.context = context;
        Callback = serviceCallback;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    public void setCallbacks(ServiceCallback serviceCallback) {
        Callback = serviceCallback;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            if (Objects.equals(intent.getAction(), "START_SERVICE")) {

                caption = intent.getStringExtra("caption");
                uploadImage = (Bitmap) intent.getParcelableExtra("uploadImage");

                Log.d(TAG, "onStartCommand: uploadImage-> " + uploadImage);
                Log.d(TAG, "onStartCommand: caption-> " + caption);

                if (hasUploadedStarted)
                    return START_NOT_STICKY;
                else hasUploadedStarted = true;

//                new Thread(() ->
//                        ).start();

            } else if (Objects.equals(intent.getAction(), "STOP_SERVICE")) {
                stopForeground(true);
                stopSelf();
            }
        }
        return Service.START_STICKY;
    }


    //send uploading error broadcast
    private void sendUploadingErrorBroadcast() {
        Intent intent1 = new Intent("newVideo");
        intent1.putExtra(BROADCAST_TYPE, BROADCAST_BLOG_UPLOADING_ERROR);
        UploadService.this.sendBroadcast(intent1);
    }

    //send uploading success broadcast
    private void sendUploadingSuccessBroadcast() {
        Log.d(TAG, "sendUploadingSuccessBroadcast: ");

        Intent intent1 = new Intent("newVideo");
        intent1.putExtra(BROADCAST_TYPE, BROADCAST_NEW_BLOG_UPLOAD_SUCCESSFULLY);
        UploadService.this.sendBroadcast(intent1);

        new Handler(getMainLooper()).postDelayed(() -> {
            Intent intent2 = new Intent("newVideoData");
            intent2.putExtra(BROADCAST_TYPE, BROADCAST_NEW_BLOG);
            UploadService.this.sendBroadcast(intent2);
        }, 0);

    }


    public class LocalBinder extends Binder {
        public UploadService getService() {
            return UploadService.this;
        }
    }

}