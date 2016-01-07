package com.dsquare.hibour.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by ASHOK on 1/7/2016.
 */
public interface WebServiceResponseCallback {
    void onSuccess(JSONObject jsonObject);
    void onFailure(VolleyError error);
}
