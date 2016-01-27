package com.dsquare.hibour.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by Dsquare Android on 1/5/2016.
 */
public interface WebServiceResponse {

    void onSuccess(JSONArray json);
    void onFailure(VolleyError error);
}
