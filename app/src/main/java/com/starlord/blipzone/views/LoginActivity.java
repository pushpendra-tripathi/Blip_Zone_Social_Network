package com.starlord.blipzone.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.starlord.blipzone.R;
import com.starlord.blipzone.api.CommonClassForAPI;
import com.starlord.blipzone.callbacks.ApiResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText loginData, password;
    TextView forgotPassword, signUp;
    Button login;
    private ProgressDialog progressDialog;
    String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initializeViews();

        login.setOnClickListener(v ->{
            String loginDataValue = loginData.getText().toString().trim();
            String passwordValue = password.getText().toString().trim();

            if (TextUtils.isEmpty(loginDataValue)) {
                loginData.setError("Required field");
                return;
            }
            if (TextUtils.isEmpty(passwordValue)) {
                password.setError("Required field");
                return;
            }

            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            CommonClassForAPI.callLoginRequest(LoginActivity.this,
                    loginDataValue,
                    passwordValue,
                    new ApiResponseCallback() {
                        @Override
                        public void onApiSuccessResult(JSONObject jsonObject) {
                            Log.d(TAG, "onResponse: Success");
                            progressDialog.dismiss();
                            processLoginResponse(jsonObject);
                        }

                        @Override
                        public void onApiFailureResult(Exception e) {
                            Log.d(TAG, "onAPIResultError: " + e.toString());
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onApiErrorResult(VolleyError volleyError) {
                            Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                            Toast.makeText(LoginActivity.this,
                                    "Either username or email is already registered",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        });

        forgotPassword.setOnClickListener(v ->{

        });

        signUp.setOnClickListener(v ->{
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    private void processLoginResponse(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");
            String details = jsonObject.getString("details");

            if (status) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this,
                        "Either username or email is already registered",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        loginData = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        forgotPassword = findViewById(R.id.forgetPassword);
        signUp = findViewById(R.id.signup_txt);
        login = findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
    }
}