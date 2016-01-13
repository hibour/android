package com.dsquare.hibour.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Android Dsquare on 12/29/2015.
 */
public class Hibour {
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

    public void setuserId(Integer integer){

    }
    /*get user id from preferences*/
    public String getUserId(){
        initializeSharedPrefs();
        return sharedPreferences.getString(Constants.PREFERENCE_USER_ID, "");
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
}
