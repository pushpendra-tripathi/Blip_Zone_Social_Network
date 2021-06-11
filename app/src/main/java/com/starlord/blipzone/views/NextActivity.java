package com.starlord.blipzone.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.starlord.blipzone.R;
import com.starlord.blipzone.callbacks.ServiceCallback;
import com.starlord.blipzone.configurations.GlobalVariables;
import com.starlord.blipzone.services.UploadService;
import com.starlord.blipzone.utils.UniversalImageLoader;

import java.io.File;
import java.io.IOException;

public class NextActivity extends AppCompatActivity implements ServiceCallback {
    private static final String TAG = "NextActivityLog";

    private EditText mCaption;
    private final String mAppend = "file:/";
    private int imageCount = 0;
    private String imgUrl, caption = "";
    private Bitmap uploadBitmap;
    private Intent intent;
    private ImageView backArrow;
    private TextView share;
    ServiceCallback serviceCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        setImage();

        mCaption = (EditText) findViewById(R.id.caption);

        backArrow = (ImageView) findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });

        share = (TextView) findViewById(R.id.tvShare);

        share.setOnClickListener(v -> {
            Log.d(TAG, "onClick: navigating to the final share screen.");
            //upload the image to firebase
            Toast.makeText(NextActivity.this, "Attempting to upload new photo", Toast.LENGTH_SHORT).show();
            caption = mCaption.getText().toString();

            if (TextUtils.isEmpty(caption)) {
                mCaption.setError("Required field");
                return;
            }

            if (intent.hasExtra("selected_image")) {
                imgUrl = intent.getStringExtra("selected_image");
                Uri imgUri = Uri.fromFile(new File(imgUrl));
                try {
                    uploadBitmap = MediaStore.Images.Media.getBitmap(NextActivity.this.getContentResolver(), imgUri);
                    callBlogPostService(uploadBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (intent.hasExtra("selected_bitmap")) {
                uploadBitmap = (Bitmap) intent.getParcelableExtra("selected_bitmap");
                callBlogPostService(uploadBitmap);
            }

        });

    }

    public void callBlogPostService(Bitmap uploadBitmap) {
        serviceCallback = (ServiceCallback) NextActivity.this;
        UploadService mService = new UploadService(NextActivity.this, serviceCallback);

        if (!GlobalVariables.getInstance(NextActivity.this).isMyServiceRunning(mService.getClass())) {
            Intent mServiceIntent = new Intent(this.getApplicationContext(), mService.getClass());

            mServiceIntent.setAction("START_SERVICE");
            mServiceIntent.putExtra("caption", caption);
            mServiceIntent.putExtra("uploadImage", uploadBitmap);

            startService(mServiceIntent);

            startActivity(new Intent(NextActivity.this, MainActivity.class));

        } else {
            Toast.makeText(this, getString(R.string.upload_progress), Toast.LENGTH_SHORT).show();
        }

    }

    private void setImage() {
        intent = getIntent();
        ImageView image = (ImageView) findViewById(R.id.imageShare);

        if (intent.hasExtra("selected_image")) {
            imgUrl = intent.getStringExtra("selected_image");
            Log.d(TAG, "setImage: got new image url: " + imgUrl);
            UniversalImageLoader.setImage(imgUrl, image, null, mAppend);
        } else if (intent.hasExtra("selected_bitmap")) {
            uploadBitmap = (Bitmap) intent.getParcelableExtra("selected_bitmap");
            Log.d(TAG, "setImage: got new bitmap");
            image.setImageBitmap(uploadBitmap);
        }
    }

    @Override
    public void showResponse(String response) {

    }
}