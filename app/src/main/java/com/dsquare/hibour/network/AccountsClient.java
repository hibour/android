package com.dsquare.hibour.network;

import android.content.Context;

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
 * Created by ASHOK on 1/7/2016.
 */
public class AccountsClient {
    private Context context;
    private int MY_SOCKET_TIMEOUT_MS= 30000;
    public AccountsClient(Context context){
        this.context=context;
    }

    /* To get all categories*/
    public void signIn(String userName,String password,String signInType
            ,final WebServiceResponseCallback callback){
        try {
            String urlStr = getSignInString(userName,password,signInType);
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest signInRequest = new JsonObjectRequest(Request.Method.GET
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
            signInRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(signInRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /* get user signin url String*/
    private String getSignInString(String userName,String password,String signInType){
        String signInUrl = Constants.URL_SIGN_IN+Constants.KEYWORD_USER_NAME+"="+userName+"+&"
                +Constants.KEYWORD_PASSWORD+"="+password+"&"
                +Constants.KEYWORD_SIGNIN_TYPE+"="+signInType;
        return signInUrl;
    }
    /* get user sign up url String*/
}
