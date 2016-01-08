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
    public void signUpUser(String userName,String email,String regType,String password
            ,final WebServiceResponseCallback callback){
        try {
            String urlStr = getSignUpUrl(userName,email,password,regType);
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.GET
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
            signUpRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(signUpRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /* get signup url string*/
    private String getSignUpUrl(String userName,String email,String regType,String password){
        String url = Constants.URL_SIGN_UP+Constants.KEYWORD_USER_NAME+"="+userName+"&"
                +Constants.KEYWORD_EMAIL+"="+email+"&"+Constants.KEYWORD_PASSWORD+"="+password+"&"
                +Constants.KEYWORD_SIGNUP_TYPE+"="+regType;
        return url;
    }

    /*get terms and conditions*/
    public void getTermsAndConditions(final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_TERMS;
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest termsRequest = new JsonObjectRequest(Request.Method.GET
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
            termsRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(termsRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /* get all proofs types*/
    public void getAllProofTypes(final WebServiceResponseCallback callback){
        try {
            String urlStr = Constants.URL_GET_ALL_PROOFS;
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
    /* insert user proof details*/
    public void insertProofDetails(String userId,String cardType,String cardNumber,String proofImage
            ,final WebServiceResponseCallback callback){
        try {
            String urlStr = getProofString(userId,cardType,cardNumber,proofImage);
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
    /* get insert proof url*/
    private String getProofString(String userId,String cardType,String cardNumber,String proofImage){
        String url = Constants.URL_INSERT_PROOFS+userId+"/edit?"
                +Constants.KEYWORD_PROOF_ID+"="+cardType+"&"
                +Constants.KEYWORD_PROOF_NUMBER+"="+cardNumber+"&"
                +Constants.KEYWORD_PROOF_IMAGE+"="+proofImage;
        return "";
    }
    /* insert user location*/
    public void inserUserLocation(String userId,String lat,String longi,String address
            ,final  WebServiceResponseCallback callback){
        try {
            String urlStr = getInsertLocString(userId,lat,longi,address);
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                    , url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            JsonObjectRequest locInsertRequest = new JsonObjectRequest(Request.Method.GET
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
            locInsertRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HibourConnector.getInstance(context).addToRequestQueue(locInsertRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /* get insert location String*/
    private String getInsertLocString(String userId,String lat,String longi,String address){
        String url = Constants.URL_LOC_INSERT+Constants.KEYWORD_USR_ID+"="+userId+"&"
                +Constants.KEYWORD_LAT+"="+lat+"&"+Constants.KEYWORD_LON+"="+longi+"&"
                +Constants.KEYWORD_ADDRESS+"="+address;
        return address;
    }

    /* get all social prefs*/
}
