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
    public static final String URL_SIGN_IN=URL_DOMAIN+"Userreg/check?";
    public static final String URL_SIGN_UP=URL_DOMAIN+"Userreg/create?";
    public static final String URL_TERMS= URL_DOMAIN+"";
    public static final String URL_GET_ALL_PROOFS= URL_DOMAIN+"Proof";
    public static final String URL_INSERT_PROOFS=URL_DOMAIN+"Userreg/";
    public static final String URL_PREFS_ALL=URL_DOMAIN+"Preferences?";
    public static final String URL_MEMBERS_COUNT= URL_DOMAIN+"Userreg/count?";
    public static final String URL_PREFS_INSERT=URL_DOMAIN+"Userpreferences/create?";
    public static final String URL_LOC_INSERT=URL_DOMAIN+"";
    public static final String URL_POST_INSERTS=URL_DOMAIN+"Posts/create";
    public static final String URL_GET_ALL_CATEGORIES= URL_DOMAIN+"Posttype";
    public static final String URL_GET_ALL_POSTS = URL_DOMAIN+"Userposts/";
    public static final String URL_GET_ABOUT_US=URL_DOMAIN+"";
    public static final String URL_POST_COMMENT = URL_DOMAIN+"Commentpost/create?";

    /*user constants*/
    public static final String KEYWORD_USER_NAME="Username";
    public static final String KEYWORD_EMAIL="Email";
    public static final String KEYWORD_PASSWORD="Password";
    public static final String KEYWORD_SIGNIN_TYPE="signinType";
    public static final String KEYWORD_SIGNUP_TYPE = "Regtype";
    public static final String KEYWORD_USR_ID="userid";
    public static final String KEYWORD_PREFS_IDS="preferencesid";
    public static final String KEYWORD_PROOF_IMAGE="profileimage";
    public static final String KEYWORD_PROOF_ID="Prooftypeid";
    public static final String KEYWORD_PROOF_NUMBER="Proofnumber";
    public static final String KEYWORD_LAT="latitude";
    public static final String KEYWORD_LON="longitude";
    public static final String KEYWORD_ADDRESS="Address";
    public static final String KEYWORD_GENDER="Gender";
    public static final String KEYWORD_USER_ID="Userid";
    public static final String KEYWORD_POST_ID="Postid";
    public static final String KEYWORD_POST_TYPE="Posttype";
    public static final String KEYWORD_POST_SUBTYPE="Postsubtype";
    public static final String KEYWORD_POST_MESSAGES="Postmessage";
    public static final String KEYWORD_POST_IMAGES="Postimage";
    public static final String KEYWORD_POST_LIKESCOUNT="Likescount";
    public static final String KEYWORD_POST_STATUS="Status";
    public static final String KEYWORD_POST_COMMENT="Commentmessage";
    public static final String KEYWORD_SIGNATURE="signature";
    public static final String SIGNATURE_VALUE="I0mkNPgu6h0M4YtOmG6Ib5BdFHy1Knrf";
    public static final String USER_LOGIN_FACEBOOK = "facebook";

    public static final String PREFERENCE_FILE_NAME="Hibour";
    public static final String PREFERENCE_USER_ID="userId";

    public static  Double Longitude = 78.00;
    public static  Double Latitude = 21.00;
    public static  String LocationAddress;

    public static final String SF_ID="userID";
    public static final String SF_FIRST="userName";
    public static final String SF_EMAIL="Email";
    public static final String SF_REGTYPE="Regtype";

    public static String userAddress="";

    // temp data
    public static Map<String,String> prefernceMap = new LinkedHashMap<String,String>();
    public static  List<String[]> prefernceList = new ArrayList<>();
}
