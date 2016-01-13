package com.dsquare.hibour.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Android Dsquare on 12/29/2015.
 */
public class Constants {
    /*urls*/
    public static final String URL_DOMAIN="http://api.hibour.com/v1/";
    public static final String URL_SIGN_IN=URL_DOMAIN+"";
    public static final String URL_SIGN_UP=URL_DOMAIN+"Userreg/create?";
    public static final String URL_TERMS= URL_DOMAIN+"";
    public static final String URL_GET_ALL_PROOFS= URL_DOMAIN+"Proof";
    public static final String URL_INSERT_PROOFS=URL_DOMAIN+"Userreg/";
    public static final String URL_PREFS_ALL=URL_DOMAIN+"Preferences?";
    public static final String URL_PREFS_INSERT=URL_DOMAIN+"Userpreferences/create?";
    public static final String URL_LOC_INSERT=URL_DOMAIN+"";
    public static final String URL_GET_ALL_POSTS = URL_DOMAIN+"Userposts/";
    public static final String URL_GET_ABOUT_US=URL_DOMAIN+"";
    public static final String URL_POST_COMMENT = URL_DOMAIN+"Commentpost/create?";

    /*user constants*/
    public static final String KEYWORD_USER_NAME="Username";
    public static final String KEYWORD_EMAIL="Email";
    public static final String KEYWORD_PASSWORD="password";
    public static final String KEYWORD_SIGNIN_TYPE="signinType";
    public static final String KEYWORD_SIGNUP_TYPE = "Regtype";
    public static final String KEYWORD_USR_ID="userid";
    public static final String KEYWORD_PREFS_IDS="preferencesid";
    public static final String KEYWORD_PROOF_IMAGE="";
    public static final String KEYWORD_PROOF_ID="Prooftypeid";
    public static final String KEYWORD_PROOF_NUMBER="Proofnumber";
    public static final String KEYWORD_LAT="latitude";
    public static final String KEYWORD_LON="longitude";
    public static final String KEYWORD_ADDRESS="address";
    public static final String KEYWORD_USER_ID="Userid";
    public static final String KEYWORD_POST_ID="Postid";
    public static final String KEYWORD_POST_COMMENT="Commentmessage";
    public static final String KEYWORD_SIGNATURE="signature";
    public static final String SIGNATURE_VALUE="I0mkNPgu6h0M4YtOmG6Ib5BdFHy1Knrf";

    public static final String PREFERENCE_FILE_NAME="Hibour";
    public static final String PREFERENCE_USER_ID="userId";

    public static final String SF_ID="userID";
    public static final String SF_FIRST="userName";
    public static final String SF_EMAIL="Email";
    public static final String SF_REGTYPE="Regtype";

    // temp data
    public static Map<String,Integer> PrefernceMap = new LinkedHashMap<String,Integer>();
    public static  List<String[]> PrefernceList = new ArrayList<>();
}
