package com.dsquare.hibour.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

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
        editor.putString(Constants.SF_EMAIL, details[2]);
        editor.putString(Constants.SF_REGTYPE, details[3]);
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
    /* remove user id*/
    public void removeUserDetails(){
        initializeSharedPrefs();
        editor.remove(Constants.PREFERENCE_USER_ID);
    }
    /* validate email*/
    public boolean validateEmail(String mail){
        if(!mail.contains("@")&& !mail.contains(" "))
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
