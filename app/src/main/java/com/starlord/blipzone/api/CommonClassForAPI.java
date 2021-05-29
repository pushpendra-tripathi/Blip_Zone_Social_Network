package com.starlord.blipzone.api;


import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.starlord.blipzone.base.VolleyClient;
import com.starlord.blipzone.callbacks.ApiResultCallback;

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
}
