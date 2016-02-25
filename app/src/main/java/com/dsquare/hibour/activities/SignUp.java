package com.dsquare.hibour.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.gcm.GcmRegistration;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.signup.Data;
import com.dsquare.hibour.pojos.signup.SignupPojo;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Helper;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final String intentText = "pintent";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 33;
    protected GoogleApiClient mGoogleApiClient;
    private String TAG = "signin";
    private GoogleSignInOptions googleSignInOptions;
    private SignInButton signInButton;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private Button submitButton;
    private EditText name, email, password, fname, lname;
    private TextInputLayout inputLayoutName, inputLayoutemail, inputLayoutpassword, inputLayoutfName, inputLayoutlName;
    private TextView signInText, termsText, back;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog signUpDialog;
    private Hibour application;
    private Typeface tf;
    private Gson gson;
    private Intent data;
    private String string;
    private String userName="",userNumber="",userMail="",socialType="",userpassword="",userfirst="",userlast="";
    private ImageView male,female;
    private LinearLayout linearmale,linearfemale;
    private String Gender="";
    private DatabaseHandler handler;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.signup);
        initializeViews();
        initializeEventlisteners();
//        initializeGplus();
//        initializeFb();
    }
    /* initialize views*/
    private void initializeViews(){
        handler = new DatabaseHandler(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        submitButton = (Button)findViewById(R.id.signup_next);
        fname = (EditText)findViewById(R.id.signup_firstname);
        lname = (EditText)findViewById(R.id.signup_lastname);

        email= (EditText)findViewById(R.id.signup_email);
        password = (EditText) findViewById(R.id.signup_pwd);
        male = (ImageView) findViewById(R.id.image_male);
        female = (ImageView) findViewById(R.id.image_female);
        linearmale = (LinearLayout) findViewById(R.id.linear_male);
        linearfemale = (LinearLayout) findViewById(R.id.linear_female);
        back = (TextView) findViewById(R.id.signup_back);
        linearmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setImageResource(R.mipmap.ic_men_filed_icon);
                female.setImageResource(R.mipmap.ic_female_icon);
                Gender = "0";
            }
        });
        linearfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setImageResource(R.mipmap.ic_female_filed_icon);
                male.setImageResource(R.mipmap.ic_men_icon);
                Gender = "1";
            }
        });
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                hideKeyboard();
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    validateData();
                    handled = true;
                }
                return handled;
            }
        });
        inputLayoutfName = (TextInputLayout) findViewById(R.id.signup_firstname_inputlayout);
        inputLayoutlName = (TextInputLayout) findViewById(R.id.signup_lastname_inputlayout);
        inputLayoutemail = (TextInputLayout) findViewById(R.id.signup_email_inputlayout);
        inputLayoutpassword = (TextInputLayout) findViewById(R.id.signup_pwd_inputlayout);
        accountsClient = new AccountsClient(this);
        networkDetector = new NetworkDetector(this);
        gson = new Gson();
        application = Hibour.getInstance(this);
        application.initializeSharedPrefs();
        inputLayoutfName.setTypeface(tf);
        inputLayoutlName.setTypeface(tf);
        inputLayoutemail.setTypeface(tf);
        inputLayoutpassword.setTypeface(tf);

        //Trying to prepopulate the form
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);

        } else {
            this.prepopulateForm();

        }



    }

    private void prepopulateForm() {
        Helper helper = new Helper(this);

        String emailString = helper.getUserEmail();
        if(emailString != null){
            email.setText(emailString);

        }

        String[] name = helper.getName();
        if(name!= null) {
            if(name[1] != null && name[2] != null) {
                fname.setText(name[1]);
                lname.setText(name[2]);
            } else if(name[0] != null) {
                fname.setText(name[0]);

            }

        }

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //  imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    /*initialize event listeners*/
    private void initializeEventlisteners() {
        submitButton.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    public void initializeGplus(){
        signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
        Log.d("social", "initgplus");
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
        signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(googleSignInOptions.getScopeArray());
        signInButton.setOnClickListener(this);
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView mTextView = (TextView) v;
                mTextView.setText(this.getResources().getString(R.string.gplus_text));
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
        facebookLoginButton.setOnClickListener(this);
        Log.d("social", "initialize fb");
    }
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
                                        Log.d("gender", object.optString("gender"));
//                                        signUpUser(object.optString("name"), object.optString("email")
//                                                , "", "fb");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(SignUp.this, "User cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(SignUp.this, "Error on Login, check your facebook app_id", Toast.LENGTH_LONG).show();
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
//                application.setSocialPreferences(Constants.USER_LOGIN_GPLUS);

                try {
                    if(acct.getDisplayName()!=null){
                        userName = acct.getDisplayName();
                    }
                    if(acct.getEmail()!=null){
                        userMail = acct.getEmail();
                    }
                    Log.d("gplus",userMail+userName);
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
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup_next:
                validateData();
                break;
            case R.id.signup_back:
                openSignInActivity();
                break;
            case R.id.signup_terms:
                openTermsDialog();
                break;
            case R.id.btn_sign_in:
                Log.d("social","clicked on gplus");
                gplusSignIn();
                break;
            case R.id.facebook_login_button:
                fbSignIn();
                break;
        }
    }
    /*open govt proof activity*/
    private void openProofActivity(){
        Intent proofIntent = new Intent(this,GovtProof.class);
        startActivity(proofIntent);
    }
    /* open sign in activity*/
    private void openSignInActivity(){
        Intent signInIntent = new Intent(this, Social.class);
        startActivity(signInIntent);
        this.finish();
    }
    /* open terms dialog*/
    private void openTermsDialog(){
        Intent termsDialog = new Intent(this,TermsAndConditions.class);
        startActivity(termsDialog);
    }
    /*validate data*/
    private void validateData(){
        String userFname = fname.getText().toString();
        String userLname = lname.getText().toString();
        String userMail = email.getText().toString();
        String userPass = password.getText().toString();
        if (!userFname.equals(null) && !userFname.equals("null") && !userFname.equals("") &&
            !userLname.equals(null) && !userLname.equals("null") && !userLname.equals("") &&
                !userMail.equals(null)&&!userMail.equals("null")&& ! userMail.equals("")&&
            !userPass.equals(null) && !userPass.equals("null") && !userPass.equals("") && !Gender.equals("")) {
            if(application.validateEmail(userMail)){
                signUpUser(userFname, userLname, userMail, userPass, "normal");
            }else{
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Enter valid email!", Snackbar.LENGTH_LONG);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();
            }
        }else{
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Enter valid credentials!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
    }
    /* sign up the user*/
    private void signUpUser(String userFname, String userLname, String email, String password, String regType) {
        if(networkDetector.isConnected()){
            signUpDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            if (application.getGCMToken().equalsIgnoreCase("")) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Check Internet Connectivity.", Snackbar.LENGTH_LONG);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();
                if (application.checkPlayServices(this, null)) {
                    // Start IntentService to register this application with GCM.
                    Intent intent = new Intent(this, GcmRegistration.class);
                    startService(intent);
                }
                return;
            }
            accountsClient.signUpUser(userFname, userLname, email, password, Gender, regType, Constants.latitude, Constants.longitude,
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
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
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
            String[] regidetails = {String.valueOf(data.getId()), data.getFirstName(), data.getLastName(), data.getEmail(), data.getGender(), data.getRegtype(), Constants.locationaddress};
            application.setLoginDetails(regidetails);
            application.setIsFirst(true);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch(requestCode) {
            case SignUp.PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    this.prepopulateForm();

                }
                break;

        }

    }

}
