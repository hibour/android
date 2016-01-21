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
 * Created by Dsquare Android on 1/8/2016.
 */
public class PostsClient {
    private Context context;
    private int MY_SOCKET_TIMEOUT_MS= 30000;
    public PostsClient(Context context){
        this.context=context;
    }

    /* To get all posts*/
    public void getAllPosts(String userId,final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_GET_ALL_POSTS+userId+"?"+Constants.KEYWORD_SIGNATURE
                    +"="+Constants.SIGNATURE_VALUE;
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest postsRequest = new JsonObjectRequest(Request.Method.GET
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
            postsRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(postsRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /* comment on a post*/
    public void commentOnPost(String userId,String postId,String message
            ,final WebServiceResponseCallback callback){
        try {
            String urlStr = getCommentOnPostUrl(userId,postId,message);
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest postsRequest = new JsonObjectRequest(Request.Method.GET
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
            postsRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(postsRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /* get comment on post url*/
    private String getCommentOnPostUrl(String userId,String postId,String message){
        String url = Constants.URL_POST_COMMENT +Constants.KEYWORD_USER_ID+"="+userId+"&"
                +Constants.KEYWORD_POST_ID+"="+postId+"&"
                +Constants.KEYWORD_POST_COMMENT+"="+message;
        return url;
    }

    /* insert on a post*/
    public void insertonPost(String userId,String postType,String postsubType,String postMessages,String postImages,
                             String status
            ,final WebServiceResponseCallback callback){
        try {
            String urlStr = getInsertOnPostUrl(userId, postType, postsubType,postMessages,postImages,status);
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest postsRequest = new JsonObjectRequest(Request.Method.GET
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
            postsRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(postsRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /* get comment on post url*/
    private String getInsertOnPostUrl(String userId,String postType,String postsubType,String postMessages,String postImages,String status){
        String url = Constants.URL_POST_INSERTS +Constants.KEYWORD_USER_ID+"="+userId+"&"
                +Constants.KEYWORD_POST_TYPE+"="+postType+"&"
                +Constants.KEYWORD_POST_SUBTYPE+"="+postsubType+"&"
                +Constants.KEYWORD_POST_MESSAGES+"="+postMessages+"&"
                +Constants.KEYWORD_POST_IMAGES+"="+postImages+"&"
                +Constants.KEYWORD_POST_STATUS+"="+status+"&"+Constants.KEYWORD_SIGNATURE+"="+Constants.SIGNATURE_VALUE;
        Log.d("post", url);
        return url;
    }

    /* get all categories types*/
    public void getAllcategoriesTypes(final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_GET_ALL_CATEGORIES+"?"+Constants.KEYWORD_SIGNATURE+"="+Constants.SIGNATURE_VALUE;
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest proofsRequest = new JsonObjectRequest(Request.Method.GET
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
            proofsRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(proofsRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
