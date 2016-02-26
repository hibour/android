package com.dsquare.hibour.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.utils.Constants;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Aditya Ravikanti on 2/23/2016.
 */
public class LocationClient {

    private HibourConnector mConnector;

    public LocationClient(Context context) {
        mConnector = HibourConnector.getInstance(context);
    }

    /* To get all posts*/
    public void getAddress(String lat,String lon,final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_GOOGLE_LOC_ADDRESS+"latlng="+lat+","+lon+"&key="
                    +Constants.GOOGLE_ADDRESS_API_KEY;
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            Log.d("url", "" + url);
            JsonObjectRequest addressRequest = new JsonObjectRequest(Request.Method.GET
                    , url.toString(), (String) null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onFailure(error);
                }
            });
            mConnector.addToRequestQueue(addressRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
