package com.starlord.blipzone.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.callbacks.ApiResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.starlord.blipzone.api.CommonClassForAPI.callRegisterRequest;

public class SignUpActivity extends AppCompatActivity {
    EditText username, email, password;
    Button signUp;
    TextView login;
    private ProgressDialog progressDialog;
    String TAG = "SignUpActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeViews();

        signUp.setOnClickListener(v -> {
            String userNameValue = username.getText().toString().trim();
            String emailValue = email.getText().toString().trim();
            String passwordValue = password.getText().toString().trim();

            if (TextUtils.isEmpty(userNameValue)) {
                username.setError("Required field");
                return;
            }

            if (TextUtils.isEmpty(emailValue)) {
                email.setError("Required field");
                return;
            }

            if (TextUtils.isEmpty(passwordValue)) {
                password.setError("Required field");
                return;
            }

            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            //calling registration api.
            callRegisterRequest(SignUpActivity.this,
                    userNameValue,
                    emailValue,
                    passwordValue,
                    new ApiResponseCallback() {
                        @Override
                        public void onApiSuccessResult(JSONObject jsonObject) {
                            Log.d(TAG, "onResponse: Success");
                            progressDialog.dismiss();
                            processRegisterResponse(jsonObject);
                        }

                        @Override
                        public void onApiFailureResult(Exception e) {
                            Log.d(TAG, "onAPIResultError: " + e.toString());
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onApiErrorResult(VolleyError volleyError) {
                            Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                            Toast.makeText(SignUpActivity.this,
                                    "Either username or email is already registered",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

        });

        login.setOnClickListener(v ->{
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

    }

    private void initializeViews() {
        username = findViewById(R.id.username_sign_up);
        email = findViewById(R.id.email_sign_up);
        password = findViewById(R.id.password_sign_up);
        signUp = findViewById(R.id.sign_up_btn);
        login = findViewById(R.id.login_txt);
        progressDialog = new ProgressDialog(this);
    }

    private void processRegisterResponse(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");
            String details = jsonObject.getString("details");

            if (status) {
                Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                intent.putExtra("email", email.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(SignUpActivity.this,
                        "Either username or email is already registered",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}