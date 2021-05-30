package com.starlord.blipzone.api;


import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.starlord.blipzone.callbacks.ApiResultCallback;
import com.starlord.blipzone.configurations.UrlConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommonClassForAPI {
    private static final String TAG = "CommonClassForAPI";

    public static void callGetRequest(Activity context, String url, ApiResultCallback apiResultCallback) {
        Log.d(TAG, "callAuthAPI: url  " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Log.d(TAG, "onResponse: callAuthAPI JSONObject " + response);
                    apiResultCallback.onAPIResultSuccess(response);
                }, error -> {
                    Log.d(TAG, "onErrorResponse: callAuthAPI JSONObject " + error);
                    apiResultCallback.onAPIResultError(error);

                });

        // Access the RequestQueue through your singleton class.
        VolleyClient.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public static void callPostRequest(Activity context, String url, ApiResultCallback apiResultCallback) {
        Log.d(TAG, "callAuthAPI: url  " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, response -> {
                    Log.d(TAG, "onResponse: callAuthAPI JSONObject " + response);
                    apiResultCallback.onAPIResultSuccess(response);
                }, error -> {
                    Log.d(TAG, "onErrorResponse: callAuthAPI JSONObject " + error);
                    apiResultCallback.onAPIResultError(error);

                }) {

            //Post method parameters
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("paramName", "value");

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        VolleyClient.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public static void callRegisterRequest(Activity context, String username, String email,
                                           String password, ApiResultCallback apiResultCallback) {

        JSONObject params = new JSONObject();
        try {
            params.put("username", username);
            params.put("email", email);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "callAuthAPI: url  " + UrlConstants.REGISTER_USER);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, UrlConstants.REGISTER_USER, params, response -> {
                    Log.d(TAG, "onResponse: callAuthAPI JSONObject " + response);
                    apiResultCallback.onAPIResultSuccess(response);
                }, error -> {
                    Log.d(TAG, "onErrorResponse: callAuthAPI JSONObject " + error);
                    apiResultCallback.onAPIResultError(error);

                });

        // Access the RequestQueue through your singleton class.
        VolleyClient.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }
}
