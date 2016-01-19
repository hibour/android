package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.register.Data;
import com.dsquare.hibour.pojos.register.Registers;
import com.dsquare.hibour.utils.Constants;
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

import java.util.Arrays;

public class SignUp extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private String TAG = "signin";
    private GoogleSignInOptions googleSignInOptions;
    private SignInButton signInButton;
    private static final String intentText = "pintent";

    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private Button submitButton;
    private EditText name,email,password;
    private TextInputLayout inputLayoutName, inputLayoutemail,inputLayoutpassword;
    private TextView signInText,termsText;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog signUpDialog;
    private Hibour application;
    private Typeface avenir;
    private Gson gson;

    private String userName="",userNumber="",userMail="",socialType="",userpassword="",userfirst="",userlast="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);
        initializeViews();
        initializeEventlisteners();
        initializeGplus();
        initializeFb();
    }
    /* initialize views*/
    private void initializeViews(){
        submitButton = (Button)findViewById(R.id.signup_signup_button);
        name = (EditText)findViewById(R.id.signup_name);
        email= (EditText)findViewById(R.id.signup_email);
        password= (EditText)findViewById(R.id.signup_password);
        inputLayoutName=(TextInputLayout)findViewById(R.id.signup_name_inputlayout);
        inputLayoutemail=(TextInputLayout)findViewById(R.id.signup_mail_inputlayout);
        inputLayoutpassword=(TextInputLayout)findViewById(R.id.signup_password_inputlayout);
        signInText = (TextView)findViewById(R.id.signup_signin_text);
        termsText = (TextView)findViewById(R.id.signup_terms);
        accountsClient = new AccountsClient(this);
        networkDetector = new NetworkDetector(this);
        gson = new Gson();
        application = Hibour.getInstance(this);
        avenir = Typeface.createFromAsset(getAssets(),"fonts/AvenirLTStd-Book.otf");
        submitButton.setTypeface(avenir);
        name.setTypeface(avenir);
        email.setTypeface(avenir);
        password.setTypeface(avenir);
        inputLayoutName.setTypeface(avenir);
        inputLayoutemail.setTypeface(avenir);
        inputLayoutpassword.setTypeface(avenir);
    }

    /*initialize event listeners*/
    private void initializeEventlisteners() {
        submitButton.setOnClickListener(this);
        signInText.setOnClickListener(this);
        termsText.setOnClickListener(this);
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
                mTextView.setTypeface(avenir);
                return;
            }
        }
        Log.d("social", "gplus initialized");
    }

    /* initialize facebook*/
    public void initializeFb(){
        Log.d("social","initfb");
        facebookLoginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        facebookLoginButton.setTypeface(avenir);
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

        facebookLoginButton.setReadPermissions(Arrays.asList("email", "user_photos", "public_profile"));
        facebookLoginButton.setOnClickListener(this);
        Log.d("social", "initialize fb");
    }



    /* gplus signin*/
    private void gplusSignIn() {
        if (networkDetector.isConnected()){ // check for network connectivity
            Log.d("social","gplussignin");
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
                userName = acct.getDisplayName();
                userNumber = "";
                userMail = acct.getEmail();
                socialType="google";
                userpassword= "";
                userfirst=acct.getDisplayName();
                userlast="";
                Log.d("gplus",userMail+userfirst);
                signUpUser(userName, userMail, userpassword, "gp");

                Log.d("Name",userName);
//                sendSocialData(userName,userNumber,userMail,socialType,application.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if(prevActType.equals("login")){
                openLanguageSelectorActivity();
            }else{
                SocialNetworks.this.finish();
            }*/
            // mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
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
            case R.id.signup_signup_button:
                validateData();
                break;
            case R.id.signup_signin_text:
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
        Intent signInIntent = new Intent(this,SignIn.class);
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
        String userName = name.getText().toString();
        String userMail = email.getText().toString();
        String userPass = password.getText().toString();
        if(!userName.equals(null)&&!userName.equals("null")&&!userName.equals("")&&
                !userMail.equals(null)&&!userMail.equals("null")&& ! userMail.equals("")&&
                !userPass.equals(null)&&!userPass.equals("null") && ! userPass.equals("")){
            if(application.validateEmail(userMail)){
                signUpUser(userName, userMail, userPass, "normal");
            }else{
                Toast.makeText(this,"Enter valid email",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Enter valid credentials",Toast.LENGTH_LONG).show();
        }
    }
    /* sign up the user*/
    private void signUpUser(String userName,String email,String password,String regType){
        if(networkDetector.isConnected()){
            signUpDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.signUpUser(userName,email,password,regType, Constants.userAddress
                    ,new WebServiceResponseCallback() {
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
            Registers registers = gson.fromJson(jsonObject.toString(), Registers.class);
            Data data = registers.getData();
//            Integer integer = data.getId();
            String s = String.valueOf(data.getId());
            Log.d("integer", s);
            String[] regidetails = {String.valueOf(data.getId()), data.getUsername(), data.getEmail(), data.getRegtype()};
            application.setLoginDetails(regidetails);
//            Log.d("integer", String.valueOf(integer));
            Log.d("regidetails", String.valueOf(regidetails));
            Intent homeIntent = new Intent(this, GovtProof.class);
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
