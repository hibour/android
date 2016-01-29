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
import com.dsquare.hibour.utils.Hibour;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by ASHOK on 1/7/2016.
 */
public class AccountsClient {
  Hibour hibour;
  private Context context;
  private int MY_SOCKET_TIMEOUT_MS = 30000;

  public AccountsClient(Context context) {
    this.context = context;
  }

  /* To get all categories*/
  public void signIn(String userName, String password, String signInType, String gcmToken
      , final WebServiceResponseCallback callback) {
    try {

      String urlStr = getSignInString(userName, password, signInType, gcmToken);
      Log.d("signin url", urlStr);
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
  private String getSignInString(String userName, String password, String signInType, String gcmToken) {
    String signInUrl = Constants.URL_SIGN_IN + "email=" + userName + "&"
        + Constants.KEYWORD_PASSWORD + "=" + password + "&" + Constants.KEYWORD_GCM + "=" + gcmToken + "&"
        + Constants.KEYWORD_SIGNIN_TYPE + "=" + signInType + "&" + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
    return signInUrl;
  }

  /* get user sign up url String*/
  public void signUpUser(String userName, String email, String password, String regType,
                         String address, String gcmToken, final WebServiceResponseCallback callback) {
    try {
      String urlStr = getSignUpUrl(userName, email, password, regType, address, gcmToken);
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
  private String getSignUpUrl(String userName, String email, String password, String regType
      , String address, String gcmToken) {
    String url = Constants.URL_SIGN_UP + Constants.KEYWORD_USER_NAME + "=" + userName + "&"
        + Constants.KEYWORD_EMAIL + "=" + email + "&" + Constants.KEYWORD_PASSWORD + "=" + password + "&"
        + Constants.KEYWORD_SIGNUP_TYPE + "=" + regType + "&"
        + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE + "&"
        + Constants.KEYWORD_GCM + "=" + gcmToken + "&" +
        Constants.KEYWORD_ADDRESS + "=" + address;
    Log.d("url", url);
    return url;
  }

  /*get terms and conditions*/
  public void getTermsAndConditions(final WebServiceResponseCallback callback) {
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
  public void getAllProofTypes(final WebServiceResponseCallback callback) {
    try {
      String urlStr = Constants.URL_GET_ALL_PROOFS + "?" + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
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
  public void insertProofDetails(String userId, String cardType, String cardNumber, String proofImage
      , String gender, final WebServiceResponseCallback callback) {
    try {
      String urlStr = getProofString(userId, cardType, cardNumber, proofImage, gender);
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
  private String getProofString(String userId, String cardType, String cardNumber
      , String proofImage, String gender) {
    String url = Constants.URL_INSERT_PROOFS + userId + "/edit?"
        + Constants.KEYWORD_PROOF_ID + "=" + cardType + "&"
        + Constants.KEYWORD_PROOF_NUMBER + "=" + cardNumber + "&"
        + Constants.KEYWORD_GENDER + "=" + gender + "&"
        + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE + "&"
        + Constants.KEYWORD_PROOF_IMAGE + "=" + proofImage;
    ;
    return url;
  }

  /* insert user location*/
  public void inserUserLocation(String userId, String lat, String longi, String address
      , final WebServiceResponseCallback callback) {
    try {
      String urlStr = getInsertLocString(userId, lat, longi, address);
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
  private String getInsertLocString(String userId, String lat, String longi, String address) {
    String url = Constants.URL_LOC_INSERT + Constants.KEYWORD_USR_ID + "=" + userId + "&"
        + Constants.KEYWORD_LAT + "=" + lat + "&" + Constants.KEYWORD_LON + "=" + longi + "&"
        + Constants.KEYWORD_ADDRESS + "=" + address;
    return address;
  }

  /* get all social prefs*/
  public void getAllSocialPrefs(final WebServiceResponseCallback callback) {
    try {
      String urlStr = Constants.URL_PREFS_ALL + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
      ;
      URL url = new URL(urlStr);
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
          , url.getPath(), url.getQuery(), url.getRef());
      url = uri.toURL();
      JsonObjectRequest prefsRequest = new JsonObjectRequest(Request.Method.GET
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
      prefsRequest.setRetryPolicy(new DefaultRetryPolicy(
          MY_SOCKET_TIMEOUT_MS,
          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      HibourConnector.getInstance(context).addToRequestQueue(prefsRequest);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  /* insert user social prefs*/
  public void insertUserPrefs(String userId, String prefs, final WebServiceResponseCallback callback) {
    try {
      String urlStr = getUserPrefsString(userId, prefs);
      URL url = new URL(urlStr);
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
          , url.getPath(), url.getQuery(), url.getRef());
      url = uri.toURL();
      JsonObjectRequest prefsRequest = new JsonObjectRequest(Request.Method.GET
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
      prefsRequest.setRetryPolicy(new DefaultRetryPolicy(
          MY_SOCKET_TIMEOUT_MS,
          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      HibourConnector.getInstance(context).addToRequestQueue(prefsRequest);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  /* get insert userprefs string*/
  private String getUserPrefsString(String userId, String prefs) {
    String url = Constants.URL_PREFS_INSERT + Constants.KEYWORD_USR_ID + "=" + userId + "&"
        + Constants.KEYWORD_PREFS_IDS + "=" + prefs + "&" + Constants.KEYWORD_SIGNATURE + "="
        + Constants.SIGNATURE_VALUE;
    return url;
  }

  /* get count of the people registered in a particular location*/
  public void getMembersCount(String loc, final WebServiceResponseCallback callback) {
    try {
      String urlStr = Constants.URL_MEMBERS_COUNT + "address=" + loc + "&" + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
      URL url = new URL(urlStr);
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
          , url.getPath(), url.getQuery(), url.getRef());
      url = uri.toURL();
      JsonObjectRequest prefsRequest = new JsonObjectRequest(Request.Method.GET
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
      prefsRequest.setRetryPolicy(new DefaultRetryPolicy(
          MY_SOCKET_TIMEOUT_MS,
          DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
      HibourConnector.getInstance(context).addToRequestQueue(prefsRequest);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  /* get all categories types*/
  public void getAllSettings(String userId, final WebServiceResponseCallback callback) {
    try {
      String urlStr = Constants.URL_SETTINGS + "userid=" + userId + "&"
          + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
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

  /* get user details*/
  public void getUserDetails(String userId, final WebServiceResponseCallback callback) {
    try {
      String urlStr = String.format(Constants.URL_USER_DETAIL, userId);
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
