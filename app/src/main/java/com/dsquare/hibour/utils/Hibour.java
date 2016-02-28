package com.dsquare.hibour.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android Dsquare on 12/29/2015.
 */
public class Hibour {
    private static final String LOG_TAG = Hibour.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static Hibour mInstance;  // class instance
    private static Context context;
    private SharedPreferences sharedPreferences; // for shared preferences
    private SharedPreferences.Editor editor;  // preferences editor
    /*set context*/
    private Hibour(Context context){
        this.context  = context;
    }
    /* initialize Hibour if not initialized*/
    public static synchronized Hibour getInstance(Context context){
        if(mInstance==null){
            mInstance = new Hibour(context);
        }
        return mInstance;
    }
    /*initialize shared preferences if not already initialized*/
    public void initializeSharedPrefs(){
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_FILE_NAME
                    ,Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }
    public void setLoginDetails(String[] details){
        initializeSharedPrefs();
        editor.putString(Constants.PREFERENCE_USER_ID, details[0]);
        editor.putString(Constants.SF_FIRST, details[1]);
        editor.putString(Constants.SF_LAST, details[2]);
        editor.putString(Constants.SF_EMAIL, details[3]);
        editor.putString(Constants.SF_GENDER, details[4]);
        editor.putString(Constants.SF_REGTYPE, details[5]);
        editor.putString(Constants.SF_LOCADD, details[6]);
        editor.commit();
    }

    /* get user details*/
    public Map<String, String> getUserDetails() {
        Map<String, String> details = new HashMap<>();
        details.put(Constants.PREFERENCE_USER_ID
            , sharedPreferences.getString(Constants.PREFERENCE_USER_ID, ""));
        details.put(Constants.SF_FIRST, sharedPreferences.getString(Constants.SF_FIRST, ""));
        details.put(Constants.SF_LAST, sharedPreferences.getString(Constants.SF_LAST, ""));
        details.put(Constants.SF_EMAIL, sharedPreferences.getString(Constants.SF_EMAIL, ""));
        details.put(Constants.SF_GENDER, sharedPreferences.getString(Constants.SF_GENDER, ""));
        details.put(Constants.SF_REGTYPE, sharedPreferences.getString(Constants.SF_REGTYPE, ""));
        details.put(Constants.SF_LOCADD, sharedPreferences.getString(Constants.SF_LOCADD, ""));
        details.put(Constants.SF_SUB_LOC, sharedPreferences.getString(Constants.SF_SUB_LOC, ""));
        details.put(Constants.SF_LAT, sharedPreferences.getString(Constants.SF_LAT, ""));
        details.put(Constants.SF_LNG, sharedPreferences.getString(Constants.SF_LNG, ""));
        details.put(Constants.SF_PASS, sharedPreferences.getString(Constants.SF_PASS, ""));
        details.put(Constants.SF_DOB, sharedPreferences.getString(Constants.SF_DOB, ""));
        details.put(Constants.SF_NOTIF, sharedPreferences.getString(Constants.SF_NOTIF, ""));
        details.put(Constants.SF_IMAGE, sharedPreferences.getString(Constants.SF_IMAGE, ""));
        details.put(Constants.SF_MOBILE, sharedPreferences.getString(Constants.SF_MOBILE, ""));
        return details;
    }

    /* set user details*/
    public void setUserDetails(Map<String, String> userDetails) {
        initializeSharedPrefs();
        Log.e(LOG_TAG, userDetails.get(Constants.PREFERENCE_USER_ID));
        Log.e(LOG_TAG, userDetails.get(Constants.SF_FIRST));
        Log.e(LOG_TAG, userDetails.get(Constants.SF_LAST));
        Log.e(LOG_TAG, userDetails.get(Constants.SF_EMAIL));
        editor.putString(Constants.PREFERENCE_USER_ID, userDetails.get(Constants.PREFERENCE_USER_ID));
        editor.putString(Constants.SF_FIRST, userDetails.get(Constants.SF_FIRST));
        editor.putString(Constants.SF_LAST, userDetails.get(Constants.SF_LAST));
        editor.putString(Constants.SF_EMAIL, userDetails.get(Constants.SF_EMAIL));
        editor.putString(Constants.SF_GENDER, userDetails.get(Constants.SF_GENDER));
        editor.putString(Constants.SF_REGTYPE, userDetails.get(Constants.SF_REGTYPE));
        editor.putString(Constants.SF_LOCADD, userDetails.get(Constants.SF_LOCADD));
        editor.putString(Constants.SF_SUB_LOC, userDetails.get(Constants.SF_SUB_LOC));
        editor.putString(Constants.SF_LAT, userDetails.get(Constants.SF_LAT));
        editor.putString(Constants.SF_LNG, userDetails.get(Constants.SF_LNG));
        editor.putString(Constants.SF_PASS, userDetails.get(Constants.SF_PASS));
        editor.putString(Constants.SF_DOB, userDetails.get(Constants.SF_DOB));
        editor.putString(Constants.SF_NOTIF, userDetails.get(Constants.SF_NOTIF));
        editor.putString(Constants.SF_IMAGE, userDetails.get(Constants.SF_IMAGE));
        editor.putString(Constants.SF_MOBILE, userDetails.get(Constants.SF_MOBILE));
        editor.commit();
    }

    public void setLocDetails(String subLocality, String loc, String lat, String lng) {
        initializeSharedPrefs();
        editor.putString(Constants.SF_LAT, lat);
        editor.putString(Constants.SF_LNG, lng);
        editor.putString(Constants.SF_SUB_LOC, subLocality);
        editor.putString(Constants.SF_LOCADD, loc);
        editor.commit();
    }
    public void setuserId(String userId){
        initializeSharedPrefs();
        editor.putString(Constants.PREFERENCE_USER_ID,userId);
        editor.commit();
    }
    /*get user id from preferences*/
    public String getUserId(){
        initializeSharedPrefs();
        return sharedPreferences.getString(Constants.PREFERENCE_USER_ID, "");
    }

    public String getUserName(){
        initializeSharedPrefs();
        return sharedPreferences.getString(Constants.SF_FIRST, "");
    }

    public String getUserLocation() {
        initializeSharedPrefs();
        return sharedPreferences.getString(Constants.SF_LOCADD, "");
    }

    /* remove user id*/
    public void removeUserDetails(){
        initializeSharedPrefs();
        editor.clear();
        editor.apply();
        //editor.remove(Constants.PREFERENCE_USER_ID);
        //editor.apply();
        // sharedPreferences = null;
    }

    public boolean getIsFirst() {
        initializeSharedPrefs();
        return sharedPreferences.getBoolean("isFirst", true);
    }

    public void setIsFirst(boolean isFirst) {
        initializeSharedPrefs();
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    /* validate email*/
    public boolean validateEmail(String mail){
        if (!mail.contains("@") && mail.contains(" "))
            return false;
        return true;
    }

    public String getGCMToken() {
        if (!sharedPreferences.getString(Constants.SHARED_PREFERENCES_GCM_TOKEN, "").equals("")) {
            return sharedPreferences.getString(Constants.SHARED_PREFERENCES_GCM_TOKEN, "");
        }
        return "";
    }

    public void setGCMToken(String gcmToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREFERENCES_GCM_TOKEN, gcmToken);
        editor.apply();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices(Context context, AppCompatActivity appCompatActivity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                if (appCompatActivity != null)
                    GooglePlayServicesUtil.getErrorDialog(resultCode, appCompatActivity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e(LOG_TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public boolean isFirstRun() {
        return sharedPreferences.getBoolean(Constants.SHARED_PREFERENCES_FIRST_RUN, true);
    }

    public void setFirstRun(boolean firstRun) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SHARED_PREFERENCES_FIRST_RUN, firstRun);
        editor.apply();
    }
}
