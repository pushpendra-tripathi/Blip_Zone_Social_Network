package com.starlord.blipzone.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.starlord.blipzone.R;
import com.starlord.blipzone.callbacks.ApiResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.starlord.blipzone.api.CommonClassForAPI.callSaveProfileDataRequest;

public class EditProfileActivity extends AppCompatActivity {
    private EditText firstName, lastName, about;
    private TextView changeProfilePhoto;
    private CircleImageView profilePhoto;
    ImageView backArrowBtn, submitBtn;
    String TAG = "EditProfileLog";
    private String imgUrl;
    private Bitmap uploadBitmap;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();

        // getting the photo from gallery or camera
        getIncomingIntent();

        backArrowBtn.setOnClickListener(v-> {
            onBackPressed();
        });

        submitBtn.setOnClickListener(v-> {
            Log.d(TAG, "onClick: attempting to save changes.");
            String firstNameValue = firstName.getText().toString().trim();
            if (TextUtils.isEmpty(firstNameValue)){
                firstName.setError("Required field");
                return;
            }
            String lastNameValue = lastName.getText().toString().trim();
            if (TextUtils.isEmpty(lastNameValue)){
                lastName.setError("Required field");
                return;
            }
            String aboutValue = about.getText().toString().trim();
            if (TextUtils.isEmpty(aboutValue)){
                about.setError("Required field");
                return;
            }

            saveProfileData(firstNameValue, lastNameValue, aboutValue, uploadBitmap);
            progressDialog.show();
        });

        changeProfilePhoto.setOnClickListener(v-> {
            Log.d(TAG, "onClick: changing profile photo");
            Intent intent = new Intent(EditProfileActivity.this, CreatePostActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (!firstName.getText().toString().equals(""))
                intent.putExtra("firstName", firstName.getText().toString());
            if (!lastName.getText().toString().equals(""))
                intent.putExtra("lastName", lastName.getText().toString());
            if (!about.getText().toString().equals(""))
                intent.putExtra("about", about.getText().toString());
            startActivity(intent);
            finish();
        });
    }

    private void saveProfileData(String firstNameValue, String lastNameValue, String aboutValue, Bitmap uploadBitmap) {
        callSaveProfileDataRequest(EditProfileActivity.this,
                firstNameValue,
                lastNameValue,
                aboutValue,
                uploadBitmap,
                new ApiResponseCallback() {
                    @Override
                    public void onApiSuccessResult(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse: " + jsonObject);
                        processSaveProfileDataResponse(jsonObject);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onApiFailureResult(Exception e) {
                        Log.d(TAG, "onExceptionOccurred: " + e);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onApiErrorResult(VolleyError volleyError) {
                        Log.d(TAG, "onErrorCode: " + volleyError.networkResponse.statusCode);
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this,
                                "Please check your internet connectivity...",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processSaveProfileDataResponse(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");
            String details = jsonObject.getString("detail");

            if (status) {
                Toast.makeText(this, "Profile details saved successfully!", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            } else {
                Toast.makeText(EditProfileActivity.this,
                        details,
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        profilePhoto = findViewById(R.id.profile_photo_edit_profile);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        about = findViewById(R.id.description);
        changeProfilePhoto = findViewById(R.id.changeProfilePhoto);
        backArrowBtn = findViewById(R.id.backArrow);
        submitBtn = findViewById(R.id.saveChanges);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, while your profile is updating...");
    }

    private void getIncomingIntent() {
        Intent intent = getIntent();

        // Setting the profile image on Image View
        if (intent.hasExtra("selected_image")) {
            imgUrl = intent.getStringExtra("selected_image");
            Log.d(TAG, "setImage: got new image url: " + imgUrl);
            File imageFile = new File(imgUrl);
            Uri imgUri = Uri.fromFile(imageFile);
            try {
                uploadBitmap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Picasso.get().load(imageFile).into(profilePhoto);
        } else if (intent.hasExtra("selected_bitmap")) {
            uploadBitmap = (Bitmap) intent.getParcelableExtra("selected_bitmap");
            Log.d(TAG, "setImage: got new bitmap");
            profilePhoto.setImageBitmap(uploadBitmap);
        }

        // Loading the existing details of the user
        if (intent.hasExtra("firstName"))
            firstName.setText(intent.getStringExtra("firstName"));

        if (intent.hasExtra("lastName"))
            lastName.setText(intent.getStringExtra("lastName"));

        if (intent.hasExtra("about"))
            about.setText(intent.getStringExtra("about"));

        if (intent.hasExtra("profileImage"))
            Picasso.get().load(intent.getStringExtra("profileImage"))
            .placeholder(R.drawable.profile_avatar)
            .into(profilePhoto);
    }
}