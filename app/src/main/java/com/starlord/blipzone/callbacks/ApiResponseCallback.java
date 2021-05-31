package com.starlord.blipzone.callbacks;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ApiResponseCallback {
    void onApiSuccessResult(JSONObject jsonObject);
    void onApiFailureResult(Exception e);
    void onApiErrorResult(VolleyError volleyError);
}