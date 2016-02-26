package com.dsquare.hibour.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.utils.Constants;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASHOK on 1/7/2016.
 */
public class AccountsClient {
    private static final String LOG_TAG = AccountsClient.class.getSimpleName();
    private HibourConnector mConnector;
    private NetworkUtils mNetworkUtils;
    private final int MY_SOCKET_TIMEOUT_MS = 30000;

    public AccountsClient(Context context) {
        mConnector = HibourConnector.getInstance(context);
        mNetworkUtils = new NetworkUtils();
    }

    /* To get all categories*/
    public void signIn(String userName, String password, String signInType, String gcmToken
            , final WebServiceResponseCallback callback) {
        try {
            String urlStr = getSignInString(userName, password, signInType, gcmToken);
            Log.d("signin url", urlStr);
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /* get user signin url String*/
    private String getSignInString(String userName, String password, String signInType, String gcmToken) {
        return Constants.URL_SIGN_IN + "email=" + userName + "&"
                + Constants.KEYWORD_PASSWORD + "=" + password + "&" + Constants.KEYWORD_GCM + "=" + gcmToken + "&"
                + Constants.KEYWORD_SIGNIN_TYPE + "=" + signInType + "&" + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
    }

    /* get user sign up url String*/
    public void signUpUser(String userFname, String userLname, String email, String password, String gender, String regType,String image,
                           String userlat, String userlog, String address,
                           String address1, String gcmToken, final WebServiceResponseCallback callback) {

        try {
            //String urlStr = getSignUpUrl(userFname, userLname, email, password, gender, regType, userlat, userlog, address, address1, gcmToken);
            //Log.d("signup url", urlStr);
            //mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
            String urlStr = Constants.URL_SIGN_UP;
            Map<String, String> params = new HashMap<>();
//            Log.d("post",userId+postType+postsubType+postMessages+postImages+status);
            params.put(Constants.KEYWORD_USER_NAME, userFname);
            params.put(Constants.KEYWORD_USER_FIRSTNAME, userFname);
            params.put(Constants.KEYWORD_USER_LASTNAME, userLname);
            params.put(Constants.KEYWORD_EMAIL, email);
            params.put(Constants.KEYWORD_PASSWORD, password);
            params.put(Constants.KEYWORD_GENDER1, gender);
            params.put(Constants.KEYWORD_SIGNUP_TYPE, regType);
            params.put(Constants.KEYWORD_PROFILE_IMAGE, image);
            params.put(Constants.KEYWORD_USER_LATITUDE, userlat);
            params.put(Constants.KEYWORD_USER_LONGITUDE, userlog);
            params.put(Constants.KEYWORD_ADDRESS, address);
            params.put(Constants.KEYWORD_ADDRESS1, address1);
            params.put(Constants.KEYWORD_GCM, gcmToken);
            params.put(Constants.KEYWORD_SIGNATURE, Constants.SIGNATURE_VALUE);
            CustomRequest updateRequest = new CustomRequest(Request.Method.POST, urlStr, params
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        Log.d("TAG", Log.getStackTraceString(error));
                    }
                    callback.onFailure(error);
                }
            });
            updateRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
           // HibourConnector.getInstance(context).addToRequestQueue(updateRequest);
            mConnector.addToRequestQueue(updateRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//          String urlStr = getSignUpUrl(userFname, userLname, email, password, gender, regType, userlat, userlog, address, address1, gcmToken);
//            URL url = new URL(urlStr);
//            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
//                    , url.getPath(), url.getQuery(), url.getRef());
//            url = uri.toURL();
//          JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST
//                    , url.toString(), (String) null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    callback.onSuccess(response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    callback.onFailure(error);
//                }
//            });
//            signUpRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    MY_SOCKET_TIMEOUT_MS,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            HibourConnector.getInstance(context).addToRequestQueue(signUpRequest);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

    }

    /* get signup url string*/
    private String getSignUpUrl(String userFname, String userLname, String email, String password, String gender, String regType,
                                String userlat, String userlog, String address,
                                String address1, String gcmToken) {
      return Constants.URL_SIGN_UP + Constants.KEYWORD_USER_NAME + "=" + userFname + "&"
          + Constants.KEYWORD_USER_FIRSTNAME + "=" + userFname + "&" +
          Constants.KEYWORD_USER_LASTNAME + "=" + userLname + "&"
                + Constants.KEYWORD_EMAIL + "=" + email + "&" + Constants.KEYWORD_PASSWORD + "=" + password + "&"
          + Constants.KEYWORD_GENDER1 + "=" + gender + "&" + Constants.KEYWORD_SIGNUP_TYPE + "=" + regType + "&" +
          Constants.KEYWORD_USER_LATITUDE + "=" + userlat + "&" +
          Constants.KEYWORD_USER_LONGITUDE + "=" + userlog + "&"
                + Constants.KEYWORD_GCM + "=" + gcmToken + "&" +
                Constants.KEYWORD_ADDRESS + "=" + address + "&" +
          Constants.KEYWORD_ADDRESS1 + "=" + address1 + "&" +
          Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
    }

    /*get terms and conditions*/
    public void getTermsAndConditions(final WebServiceResponseCallback callback) {
        try {
            String urlStr = Constants.URL_TERMS;
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }

    }
  /* get all proofs types*/

    public void getAllProofTypes(final WebServiceResponseCallback callback) {
        try {
            String urlStr = Constants.URL_GET_ALL_PROOFS + "?" + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /* insert user proof details*/
    public void insertProofDetails(String userId, String cardType, String cardNumber, String proofImage
            , String gender, final WebServiceResponseCallback callback) {
        try {
            String urlStr = getProofString(userId, cardType, cardNumber, proofImage, gender);
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
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
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
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
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /* insert user social prefs*/
    public void insertUserPrefs(String userId, String prefs, final WebServiceResponseCallback callback) {
        try {
            String urlStr = getUserPrefsString(userId, prefs);
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /* get insert userprefs string*/
    private String getUserPrefsString(String userId, String prefs) {
        return Constants.URL_PREFS_INSERT + Constants.KEYWORD_USR_ID + "=" + userId + "&"
                + Constants.KEYWORD_PREFS_IDS + "=" + prefs + "&" + Constants.KEYWORD_SIGNATURE + "="
                + Constants.SIGNATURE_VALUE;
    }

    /* get count of the people registered in a particular location*/
    public void getMembersCount(String loc, final WebServiceResponseCallback callback) {
        try {
            String urlStr = Constants.URL_MEMBERS_COUNT + "address=" + loc + "&" + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /* get all categories types*/
    public void getAllSettings(String userId, final WebServiceResponseCallback callback) {
        try {
            String urlStr = Constants.URL_SETTINGS + "userid=" + userId + "&"
                    + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

  /* get all categories types*/
  public void getAllUpdateSettings(String userId, String userName, String userLastName, String email, String password, String gender, String userNum,String dob, String image, final WebServiceResponseCallback callback) {

    try {
      String urlStr = Constants.URL_PROFILE_UPDATE + userId + "/edit?";
      Map<String, String> params = new HashMap<>();
//            Log.d("post",userId+postType+postsubType+postMessages+postImages+status);
      params.put(Constants.KEYWORD_USER_FIRSTNAME, userName);
      params.put(Constants.KEYWORD_USER_LASTNAME, userLastName);
      params.put(Constants.KEYWORD_EMAIL, email);
      params.put(Constants.KEYWORD_PASSWORD, password);
      params.put(Constants.KEYWORD_GENDER, gender);
      params.put(Constants.KEYWORD_MOBILE_NUMBER1, userNum);
      params.put("Age", dob);
      params.put(Constants.KEYWORD_SIGNUP_TYPE, "update");
      params.put(Constants.KEYWORD_PROFILE_IMAGE, image);
      params.put(Constants.KEYWORD_SIGNATURE, Constants.SIGNATURE_VALUE);
      CustomRequest updateRequest = new CustomRequest(Request.Method.POST, urlStr, params
          , new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          callback.onSuccess(response);
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          if (error != null) {
            Log.d("TAG", Log.getStackTraceString(error));
          }
          callback.onFailure(error);
        }
      });
      mConnector.addToRequestQueue(updateRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
    /* get user details*/
    public void getUserDetails(String userId, final WebServiceResponseCallback callback) {
        try {
            String urlStr = String.format(Constants.URL_USER_DETAIL, userId, Constants.SIGNATURE_VALUE);
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

  /* get user phoneno url String*/
  public void mobilenumUser(String userid, String userNum, final WebServiceResponseCallback callback) {
    try {
        String urlStr = getPhoneUserUrl(userid, userNum);
        mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }

  }

    /* get mobilenumUser url string*/
    private String getPhoneUserUrl(String userid, String userNum) {
      return Constants.URL_MOBILE_NUMBER + "id" + "=" + userid + "&" + "mobile_number" + "=" + userNum + "&"
          + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
    }

    public void getAllBusinessServiceTypes(final WebServiceResponseCallback callback) {
        try {
            String urlStr = Constants.URL_GET_ALL_BUSINEES_TYPES + "?" + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void getAllBusinessServiceSubTypes(String userId,String busiId, final WebServiceResponseCallback callback) {
        try {
            String urlStr = Constants.URL_GET_ALL_BUSINEES_SUB_TYPES + "?id="+userId+"bid="+busiId+ Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
            mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

  /* get other user details*/
  public void getOtherUserDetails(String userId, final WebServiceResponseCallback callback) {
    try {
      String urlStr = Constants.URL_GET_OTHER_USR_DETAILS + Constants.KEYWORD_USR_ID +
          "=" + userId + "&" + Constants.KEYWORD_SIGNATURE + "=" + Constants.SIGNATURE_VALUE;
        mConnector.addToRequestQueue(mNetworkUtils.getJsonObjectRequest(urlStr, callback));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  /* get all categories types*/
  public void getUpdateLocation(String userId, String address, String address1, String latitude, String longitude, final WebServiceResponseCallback callback) {

    try {
      String urlStr = Constants.URL_UPDATE_USER_LOCATION;
      Map<String, String> params = new HashMap<>();
      Log.d("data", userId + address + address1 + latitude + longitude);
      params.put("Address", address);
      params.put("Address1", address1);
      params.put("user_id", userId);
      params.put("geo_lat", latitude);
      params.put("geo_lon", longitude);
      params.put(Constants.KEYWORD_SIGNATURE, Constants.SIGNATURE_VALUE);
      CustomRequest updateLocationRequest = new CustomRequest(Request.Method.POST, urlStr, params
          , new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          callback.onSuccess(response);
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          if (error != null) {
            Log.d("TAG", Log.getStackTraceString(error));
          }
          callback.onFailure(error);
        }
      });
      mConnector.addToRequestQueue(updateLocationRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
