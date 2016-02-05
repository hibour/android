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
import java.util.HashMap;
import java.util.Map;

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
            Log.d("url",""+url);
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
            Log.d("url ",""+url);
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
                +Constants.KEYWORD_POST_COMMENT+"="+message+"&"+Constants.KEYWORD_SIGNATURE+"="+Constants.SIGNATURE_VALUE;
        return url;
    }


    /* comment on a post*/
    public void getcommentOnPost(String postId
            ,final WebServiceResponseCallback callback){
        try {
            String urlStr = getAllCommentOnPostUrl(postId);
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            Log.d("url ",""+url);
            JsonObjectRequest postsAllRequest = new JsonObjectRequest(Request.Method.GET
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
            postsAllRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(postsAllRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /* get comment on post url*/
    private String getAllCommentOnPostUrl(String postId){
        String url = Constants.URL_POST_GET_COMMENT+postId+"?"
                +Constants.KEYWORD_SIGNATURE+"="+Constants.SIGNATURE_VALUE;
        return url;
    }

    /* insert on a post*/
    public void insertonPost(final String userId, final String postType,final String postsubType
            ,final String postMessages,final String postImages,final String status
            ,final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_POST_INSERTS;
            Map<String, String> params = new HashMap<>();
            Log.d("post",userId+postType+postsubType+postMessages+postImages+status);
            params.put(Constants.KEYWORD_USER_ID, userId);
            params.put(Constants.KEYWORD_POST_TYPE, postType);
            params.put(Constants.KEYWORD_POST_SUBTYPE, postsubType);
            params.put(Constants.KEYWORD_POST_MESSAGES, postMessages);
            params.put(Constants.KEYWORD_POST_IMAGES,postImages);
            params.put(Constants.KEYWORD_POST_STATUS,status);
            params.put(Constants.KEYWORD_SIGNATURE, Constants.SIGNATURE_VALUE);
            params.put(Constants.KEYWORD_POST_LIKESCOUNT,"0");
            CustomRequest postsRequest = new CustomRequest(Request.Method.POST,urlStr,params
                    ,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccess(response);
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        Log.d("TAG", Log.getStackTraceString(error));
                    }
                    callback.onFailure(error);
                }
            });
            postsRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(postsRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    /* get neighbourhoods*/
    public void getAllNeighbourhoods(String userId,final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_GET_NEIGHBOURHOODS+Constants.KEYWORD_USR_ID+"="+userId
                    +"&"+Constants.KEYWORD_SIGNATURE+"="
                    +Constants.SIGNATURE_VALUE;
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest neighbourhoodsRequest = new JsonObjectRequest(Request.Method.GET
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
            neighbourhoodsRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(neighbourhoodsRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /* get Likesonposts*/
    public void getLikesonPosts(String userId,String Postid,final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_POST_LIKE+"Userid"+"="+userId+"&"
                    +Constants.KEYWORD_POST_ID+"="+Postid+"&"+Constants.KEYWORD_SIGNATURE+"="
                    +Constants.SIGNATURE_VALUE;
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            Log.d("url",""+url);
            JsonObjectRequest neighbourhoodsRequest = new JsonObjectRequest(Request.Method.GET
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
            neighbourhoodsRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(neighbourhoodsRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void likePost(String userId,String postId,final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_POST_LIKE+Constants.KEYWORD_USER_ID+"="+userId+"&"
                    +Constants.KEYWORD_POST_ID+"="+postId+"&"+Constants.KEYWORD_SIGNATURE+"="
                    +Constants.SIGNATURE_VALUE;
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            Log.d("url",""+url);
            JsonObjectRequest insertLikeRequest = new JsonObjectRequest(Request.Method.GET
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
            insertLikeRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(insertLikeRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
