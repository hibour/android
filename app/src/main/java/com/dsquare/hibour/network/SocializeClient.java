package com.dsquare.hibour.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.listener.MessageStateResultCallBack;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by ASHOK on 1/26/2016.
 */
public class SocializeClient {
  private static final String LOG_TAG = SocializeClient.class.getSimpleName();
  Hibour hibour;
  private Context context;
  private int MY_SOCKET_TIMEOUT_MS = 30000;

  public SocializeClient(Context context) {
    this.context = context;
  }

  /* To get all categories*/
  public void getNeighbours(String userId, final WebServiceResponseCallback callback) {
    try {

      String urlStr = Constants.URL_GET_SOCIAL_NEIGHBOURS + "id=" + userId + "&"
          + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
      Log.d("signin url", urlStr);
      URL url = new URL(urlStr);
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
          , url.getPath(), url.getQuery(), url.getRef());
      url = uri.toURL();
      JsonObjectRequest neighboursRequest = new JsonObjectRequest(Request.Method.GET
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
      neighboursRequest.setRetryPolicy(new DefaultRetryPolicy(
          MY_SOCKET_TIMEOUT_MS,
          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      HibourConnector.getInstance(context).addToRequestQueue(neighboursRequest);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  /* To get all categories*/
  public void sendMessage(final UserMessage message, final MessageStateResultCallBack<UserMessage> callback) {
    try {

      String urlStr = Constants.URL_SEND_MESSAGE + "Senderuserid=" + message.fromUserID + "&"
          + "Receiveruserid=" + message.toUserID + "&" + "Messages=" + message.message + "&"
          + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE
          + "&Sendergcmid=abc&Receivergcmid=abc";
      Log.d("send message url", urlStr);
      URL url = new URL(urlStr);
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
          , url.getPath(), url.getQuery(), url.getRef());
      url = uri.toURL();
      JsonObjectRequest sendMessageRequest = new JsonObjectRequest(Request.Method.GET
          , url.toString(), (String) null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          callback.onResultCallBack(message, Constants.MESSAGE_SENT, null);
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          callback.onResultCallBack(message, Constants.MESSAGE_FAILED, null);
        }
      });
      sendMessageRequest.setRetryPolicy(new DefaultRetryPolicy(
          MY_SOCKET_TIMEOUT_MS,
          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      HibourConnector.getInstance(context).addToRequestQueue(sendMessageRequest);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public void getNearByUser(final WebServiceResponseCallback callback) {
    try {

      String urlStr = String.format(Constants.URL_GET_NEARBY_USER, Constants.SIGNATURE_VALUE);
      Log.d(LOG_TAG, urlStr);
      URL url = new URL(urlStr);
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
          , url.getPath(), url.getQuery(), url.getRef());
      url = uri.toURL();
      JsonObjectRequest neighboursRequest = new JsonObjectRequest(Request.Method.GET
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
      neighboursRequest.setRetryPolicy(new DefaultRetryPolicy(
          MY_SOCKET_TIMEOUT_MS,
          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      HibourConnector.getInstance(context).addToRequestQueue(neighboursRequest);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

}
