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
import com.starlord.blipzone.api.CommonClassForAPI;
import com.starlord.blipzone.callbacks.ApiResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import static com.starlord.blipzone.api.CommonClassForAPI.*;

public class VerificationActivity extends AppCompatActivity {
    EditText verificationOTP;
    Button verify;
    TextView resendOTP, resendCounter;
    String email;
    private ProgressDialog progressDialog;
    String TAG = "VerificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        getSupportActionBar().hide();

        verificationOTP = findViewById(R.id.verification_otp);
        verify = findViewById(R.id.verify_btn);
        resendCounter = findViewById(R.id.resent_otp_counter);
        resendOTP = findViewById(R.id.resend_otp);
        progressDialog = new ProgressDialog(this);

        email = getIntent().getStringExtra("email");

        verify.setOnClickListener(v -> {
            String otpValue = verificationOTP.getText().toString().trim();
            if (TextUtils.isEmpty(otpValue)) {
                verificationOTP.setError("OTP Required!");
                return;
            } else if (otpValue.length() != 6) {
                verificationOTP.setError("OTP length should be 6!");
                return;
            }

            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            callVerificationRequest(VerificationActivity.this,
                    email,
                    otpValue,
                    new ApiResponseCallback() {
                        @Override
                        public void onApiSuccessResult(JSONObject jsonObject) {
                            Log.d(TAG, "onResponse: Success");
                            progressDialog.dismiss();
                            processVerificationResponse(jsonObject);
                        }

                        @Override
                        public void onApiFailureResult(Exception e) {
                            Log.d(TAG, "onAPIResultError: " + e.toString());
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onApiErrorResult(VolleyError volleyError) {
                            Log.d(TAG, "onAPIResultErrorCode: " + volleyError.networkResponse.statusCode);
                            progressDialog.dismiss();
                            Toast.makeText(VerificationActivity.this,
                                    "OTP entered is not correct",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        resendOTP.setOnClickListener(v -> {

        });
    }

    private void processVerificationResponse(JSONObject jsonObject) {
        try {
            boolean status = jsonObject.getBoolean("status");
            String details = jsonObject.getString("details");

            if (status) {
                Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(VerificationActivity.this,
                        "OTP entered is not correct",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}