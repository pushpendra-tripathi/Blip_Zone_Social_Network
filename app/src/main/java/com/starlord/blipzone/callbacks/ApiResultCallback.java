package com.starlord.blipzone.callbacks;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ApiResultCallback {
    void onAPIResultSuccess(JSONObject jsonObject);
    void onAPIResultError(VolleyError volleyError);
}
