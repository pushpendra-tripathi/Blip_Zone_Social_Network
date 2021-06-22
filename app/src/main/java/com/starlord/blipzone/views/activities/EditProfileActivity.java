package com.starlord.blipzone.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private EditText firstName, username, lastName, about, email, phoneNumber;
    private TextView changeProfilePhoto;
    private CircleImageView profilePhoto;
    ImageView backArrowBtn, submitBtn;
    private final String mAppend = "file:/";
    String TAG = "EditProfileLog";
    private String imgUrl;
    private Bitmap uploadBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();

        getIncomingIntent();

        backArrowBtn.setOnClickListener(v-> {
            onBackPressed();
        });

        submitBtn.setOnClickListener(v-> {
            Log.d(TAG, "onClick: attempting to save changes.");

        });

        changeProfilePhoto.setOnClickListener(v-> {
            Log.d(TAG, "onClick: changing profile photo");
            Intent intent = new Intent(EditProfileActivity.this, CreatePostActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //268435456
            startActivity(intent);
            finish();
        });
    }

    private void initializeViews() {
        profilePhoto = findViewById(R.id.profile_photo_edit_profile);
        firstName = findViewById(R.id.first_name);
        username = findViewById(R.id.username);
        lastName = findViewById(R.id.last_name);
        about = findViewById(R.id.description);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        changeProfilePhoto = findViewById(R.id.changeProfilePhoto);
        backArrowBtn = findViewById(R.id.backArrow);
        submitBtn = findViewById(R.id.saveChanges);
    }

    private void getIncomingIntent() {
        Intent intent = getIntent();

        // Setting the profile image on Image View
        if (intent.hasExtra("selected_image")) {
            imgUrl = intent.getStringExtra("selected_image");
            Log.d(TAG, "setImage: got new image url: " + imgUrl);
            File imageFile = new File(imgUrl);
            Picasso.get().load(imageFile).into(profilePhoto);
            //UniversalImageLoader.setImage(imgUrl, profilePhoto, null, mAppend);
        } else if (intent.hasExtra("selected_bitmap")) {
            uploadBitmap = (Bitmap) intent.getParcelableExtra("selected_bitmap");
            Log.d(TAG, "setImage: got new bitmap");
            profilePhoto.setImageBitmap(uploadBitmap);
        }

    }
}