package com.dsquare.hibour.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by nageswara on 2/25/16.
 */
public class NetworkUtils {

    public JsonObjectRequest getJsonObjectRequest(final String urlStr, final WebServiceResponseCallback callback)
            throws MalformedURLException, URISyntaxException {
        URL url = new URL(urlStr);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                , url.getPath(), url.getQuery(), url.getRef());
        url = uri.toURL();
        return new JsonObjectRequest(Request.Method.GET, url.toString(), (String) null,
                new Response.Listener<JSONObject>() {
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
    }
}
