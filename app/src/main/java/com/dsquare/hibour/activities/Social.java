package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.gcm.GcmRegistration;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.signup.Data;
import com.dsquare.hibour.pojos.signup.SignupPojo;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya Ravikanti on 2/5/2016.
 */
public class Social extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    ViewPager viewPager;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    static final int NUM_ITEMS = 4;
    public static final String[] IMAGE_NAME = {"presignup_img1", "presignup_img1", "presignup_img1", "presignup_img1",};
    private static final int RC_SIGN_IN = 9001;
    private static final String intentText = "pintent";
    protected GoogleApiClient mGoogleApiClient;
    private String TAG = "signin";
    private GoogleSignInOptions googleSignInOptions;
    private SignInButton googleSignInButton;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private Button submitButton;
    private Typeface tf;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog signUpDialog;
    private Gson gson;
    private Hibour application;
    private String Useremail="",Userfname="",Userlname="",Usergender="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_social);
        networkDetector = new NetworkDetector(this);
        gson = new Gson();
        application = Hibour.getInstance(this);
        application.initializeSharedPrefs();
        accountsClient = new AccountsClient(this);
        tf = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
        submitButton = (Button)findViewById(R.id.social_signup);
        submitButton.setTypeface(tf);
        submitButton.setOnClickListener(this);

        initializeGplus();
        initializeFb();
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);
    }

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
        public static class SwipeFragment extends Fragment {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View swipeView = inflater.inflate(R.layout.adapter_swipe, container, false);
                ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
                Bundle bundle = getArguments();
                int position = bundle.getInt("position");
                String imageFileName = IMAGE_NAME[position];
                int imgResId = getResources().getIdentifier(imageFileName, "drawable", "com.dsquare.hibour");
                imageView.setImageResource(imgResId);
                return swipeView;
            }

            static SwipeFragment newInstance(int position) {
                SwipeFragment swipeFragment = new SwipeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                swipeFragment.setArguments(bundle);
                return swipeFragment;
            }
        }
    }
    public void initializeGplus(){
        googleSignInButton = (SignInButton) findViewById(R.id.google_plus_signin);
        Button custom_googleplus = (Button)findViewById(R.id.custom_googleplus);
        Log.d("social", "initgplus");
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
                .build();
        googleSignInButton = (SignInButton) findViewById(R.id.google_plus_signin);
        googleSignInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // signInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setScopes(googleSignInOptions.getScopeArray());
        custom_googleplus.setOnClickListener(googleConnectListener);

        for (int i = 0; i < googleSignInButton.getChildCount(); i++) {
            View v = googleSignInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView mTextView = (TextView) v;
                mTextView.setText("Google");
                mTextView.setPadding(45, 0, 0, 0);
                mTextView.setTypeface(tf);
                return;
            }
        }
        Log.d("social", "gplus initialized");
    }

    /* initialize facebook*/
    public void initializeFb(){
        Log.d("social","initfb");
        facebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        Button facebookCustomButton = (Button)findViewById(R.id.custom_facebook);
        facebookLoginButton.setTypeface(tf);
        float fbIconScale = 1.45F;
        Drawable drawable = this.getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale),
                (int) (drawable.getIntrinsicHeight() * fbIconScale));
        facebookLoginButton.setCompoundDrawables(drawable, null, null, null);
        facebookLoginButton.setCompoundDrawablePadding(this.getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));

        facebookLoginButton.setPadding(
                this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0,
                this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));

        callbackManager = CallbackManager.Factory.create();

        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        facebookLoginButton.setReadPermissions(permissions);
        facebookCustomButton.setOnClickListener(facebookConnectListener);
        Log.d("social", "initialize fb");
    }

    private View.OnClickListener facebookConnectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            facebookLoginButton.performClick();
        }
    };

    private View.OnClickListener googleConnectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            googleSignInButton.performClick();
        }
    };
    /* gplus signin*/
    private void gplusSignIn() {
        if (networkDetector.isConnected()){ // check for network connectivity
            Log.d("social", "gplussignin");
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else{
//            closeRegisterDialog();
//            internetDialog = new NoInternetDialog();
//            internetDialog.show(getFragmentManager(), getString(R.string.dialog_identifier));

        }

    }

    /* fb signin*/
    private void fbSignIn(){
        if (networkDetector.isConnected()){ // check for network connectivity
            Log.d("social", "fb");
            facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    try {
                                        Log.d("email",object.optString("email"));
                                        Log.d("id",object.optString("id"));
                                        Log.d("name",object.optString("name"));
                                        Log.d("fname",object.optString("first_name"));
                                        Log.d("lname",object.optString("last_name"));
                                        Log.d("gender",object.optString("gender"));
                                       if(object.optString("gender").equals("male")){
                                            Usergender = String.valueOf(0);
                                        }else
                                       if(object.optString("gender").equals("female")){
                                           Usergender= String.valueOf(1);
                                       }
                                        signUpUser(object.optString("first_name"),object.optString("last_name"), object.optString("email")
                                                , "",object.optString("gender"), "fb");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,first_name,last_name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(Social.this, "User cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(Social.this, "Error on Login, check your facebook app_id", Toast.LENGTH_LONG).show();
                }
            });

            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }else{
//            closeRegisterDialog();
//            internetDialog = new NoInternetDialog();
//            internetDialog.show(getFragmentManager(), getString(R.string.dialog_identifier));

        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Log.d("social","gplus");
            // Signed in successfully, show authenticated UI.
            try {
                GoogleSignInAccount acct = result.getSignInAccount();
                Log.d("name",acct.getDisplayName());
                Log.d("name",acct.getEmail());
                try {
                    if(acct.getDisplayName()!=null){
//                        userName = acct.getDisplayName();
                    }
//                    acct.
                    if(acct.getEmail()!=null){
//                        userMail = acct.getEmail();
                    }
//                    Log.d("gplus",userMail+userName);
//                    signUpUser(userName, userMail, "", "gp");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("social", "g");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d("social","gp");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                acct.getPhotoUrl();
                acct.getId();
                if(acct.getEmail()!=null){
                    Useremail = acct.getEmail();
                }
                Log.e(TAG, acct.getDisplayName());
                Log.e(TAG, acct.getEmail());

                Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                if(String.valueOf(person.getGender())!=null){
                    Usergender = String.valueOf(person.getGender());
                }
                if(person.getName().getGivenName()!=null){
                    Userfname = person.getName().getGivenName();
                }
                if(person.getName().getGivenName()!=null){
                   Userlname = person.getName().getFamilyName();
                }
                signUpUser(Userfname,Userlname, Useremail
                        , "",Usergender, "gp");
            Log.i(TAG, "--------------------------------");
            Log.i(TAG, "Display Name: " + person.getDisplayName());
            Log.i(TAG, "Gender: " + person.getGender());
            Log.i(TAG, "fName: " + person.getName().getGivenName());
            Log.i(TAG, "lname: " + person.getName().getFamilyName());
            Log.i(TAG, "image: " + person.getImage());
//            Log.i(TAG, "Current Location: " + person.getCurrentLocation());
            Log.i(TAG, "Language: " + person.getLanguage());
        }}
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.social_signup:
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                finish();
//                validateData();
                break;
            case R.id.google_plus_signin:
                Log.d("social","clicked on gplus");
                gplusSignIn();
                break;
            case R.id.facebook_login_button:
                fbSignIn();
                break;
        }
    }
    /* sign up the user*/
    private void signUpUser(String userFname,String userLname, String email,String password,String gender, String regType){
        if(networkDetector.isConnected()){
            signUpDialog = ProgressDialog.show(this, "", getResources()
                    .getString(R.string.progress_dialog_text));
            if (application.getGCMToken().equalsIgnoreCase("")) {
                Toast.makeText(this, "Check Internet Connectivity.", Toast.LENGTH_SHORT).show();
                if (application.checkPlayServices(this, null)) {
                    // Start IntentService to register this application with GCM.
                    Intent intent = new Intent(this, GcmRegistration.class);
                    startService(intent);
                }
                return;
            }
            accountsClient.signUpUser(userFname,userLname,email,password,gender,regType,Constants.latitude,Constants.longitude,
                    Constants.locationaddress, Constants.locationaddress1,
                    application.getGCMToken(), new WebServiceResponseCallback() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            parseSigUpDetails(jsonObject);
                            closeSignUpDialog();
                        }

                        @Override
                        public void onFailure(VolleyError error) {
                            Log.d("signup", error.toString());
                            closeSignUpDialog();
                        }
                    });
        }else{
            Toast.makeText(this,"Network not connected.",Toast.LENGTH_LONG).show();
        }
    }
    /* parse sign up details*/
    private void parseSigUpDetails(JSONObject jsonObject){
        try {
            closeSignUpDialog();
            Log.d("details", jsonObject.toString());
            SignupPojo registers = gson.fromJson(jsonObject.toString(), SignupPojo.class);
            Data data = registers.getData();
//            Integer integer = data.getId();
            String s = String.valueOf(data.getId());
            Log.d("integer", s);
            String[] regidetails = {String.valueOf(data.getId()), data.getFirstName(),data.getLastName(), data.getEmail(),data.getGender(), data.getRegtype()};
            application.setLoginDetails(regidetails);
//            Log.d("integer", String.valueOf(integer));
            Log.d("regidetails", String.valueOf(regidetails));
            Intent homeIntent = new Intent(this, MobileNumber.class);
            startActivity(homeIntent);
            this.finish();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }catch (final IllegalArgumentException e) {
            // Handle or log or ignore
            e.printStackTrace();
        }
    }
    /* close signup dialog*/
    private void closeSignUpDialog(){
        if(signUpDialog!=null){
            if(signUpDialog.isShowing()){
                signUpDialog.dismiss();
                signUpDialog=null;
            }
        }

    }
}
