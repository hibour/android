package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.dialogs.WelcomeDialog;
import com.dsquare.hibour.gcm.GcmRegistration;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.user.UserDetail;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignIn extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

  private static final String LOG_TAG = SignIn.class.getSimpleName();
  private static final int RC_SIGN_IN = 9001;
  private static final String intentText = "pintent";
  protected GoogleApiClient mGoogleApiClient;
  private String TAG = "signin";
  private GoogleSignInOptions googleSignInOptions;
  private SignInButton signInButton;

  private LoginButton facebookLoginButton;
  private CallbackManager callbackManager;

  private Button signIn;
  private TextView signUpText, termsText;
  private EditText mailText, passText;
  private TextInputLayout mailLayout, passLayout;
  private ProgressDialog signInDialog;
  private NetworkDetector networkDetector;
  private AccountsClient accountsClient;
  private Typeface tf;
  private Hibour application;



  private String userName = "", userNumber = "", userMail = "", socialType = "", userpassword = "", userfirst = "", userlast = "";
  private WebServiceResponseCallback userDetailCallbackListener = new WebServiceResponseCallback() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
      try {
        UserDetail user = new Gson().fromJson(jsonObject.getString("data"), UserDetail.class);
        new DatabaseHandler(getApplicationContext()).insertUserDetails(user);
      } catch (JSONException e) {
      }
    }

    @Override
    public void onFailure(VolleyError error) {
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());
    setContentView(R.layout.activity_signin);
    initializeViews();
    initializeEventListeners();
    initializeGplus();
    initializeFb();
  }

  /* initialize views*/
  private void initializeViews() {
    signIn = (Button) findViewById(R.id.signin_button);
    signUpText = (TextView) findViewById(R.id.signin_signup_text);
    termsText = (TextView) findViewById(R.id.signin_terms);
    mailText = (EditText) findViewById(R.id.signin_email);
    passText = (EditText) findViewById(R.id.signin_password);
    passText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        boolean handled = false;
        if (i == EditorInfo.IME_ACTION_DONE) {
          validateSignInData();
          handled = true;
        }
        return handled;
      }
    });
    mailLayout = (TextInputLayout) findViewById(R.id.signin_mail_inputlayout);
    passLayout = (TextInputLayout) findViewById(R.id.signin_password_inputlayout);
    networkDetector = new NetworkDetector(this);
    accountsClient = new AccountsClient(this);
    tf = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
    signIn.setTypeface(tf);
    mailLayout.setTypeface(tf);
    passLayout.setTypeface(tf);
    mailText.setTypeface(tf);
    passText.setTypeface(tf);
    application = Hibour.getInstance(this);
    application.initializeSharedPrefs();
  }

  /* initialize event listeners*/
  private void initializeEventListeners() {
    signIn.setOnClickListener(this);
    termsText.setOnClickListener(this);
    signUpText.setOnClickListener(this);
  }

  public void initializeGplus() {
    signInButton = (SignInButton) findViewById(R.id.btn_sign_in);
    Log.d("social", "initgplus");
    googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestId()
        .build();

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this, this)
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
  public void initializeFb() {
    Log.d("social", "initfb");
    facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
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
    if (networkDetector.isConnected()) { // check for network connectivity
      Log.d("social", "gplussignin");
      Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
      startActivityForResult(signInIntent, RC_SIGN_IN);
    } else {
//            closeRegisterDialog();
//            internetDialog = new NoInternetDialog();
//            internetDialog.show(getFragmentManager(), getString(R.string.dialog_identifier));

    }

  }

  /* fb signin*/
  private void fbSignIn() {
    if (networkDetector.isConnected()) { // check for network connectivity
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
                        Log.d("email", object.optString("email"));
                        Log.d("id", object.optString("id"));
                        Log.d("name", object.optString("name"));
                        sendDataToServer(object.optString("email"), "", "fb");
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
          Log.d("social", "cancelled");
          Toast.makeText(SignIn.this, "User cancelled", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(FacebookException e) {
          Log.d("social", "hr" + e.toString());
          Toast.makeText(SignIn.this, "Error on Login, check your facebook app_id", Toast.LENGTH_LONG).show();

        }
      });
      this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    } else {
//            closeRegisterDialog();
//            internetDialog = new NoInternetDialog();
//            internetDialog.show(getFragmentManager(), getString(R.string.dialog_identifier));

    }
  }


  private void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      Log.d("social", "gplus");
      // Signed in successfully, show authenticated UI.
      try {
        GoogleSignInAccount acct = result.getSignInAccount();
//                application.setSocialPreferences(Constants.USER_LOGIN_GPLUS);
        try {
          if (acct.getDisplayName() != null) {
            userName = acct.getDisplayName();
          }
          if (acct.getEmail() != null) {
            userMail = acct.getEmail();
          }
          Log.d("gplus", userMail + userName);
          sendDataToServer(userMail, "", "gp");
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
      Log.d("social", "gp");
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
    switch (v.getId()) {
      case R.id.signin_button:
        validateSignInData();
        break;
      case R.id.signin_signup_text:
        openSignUpActivity();
        break;
      case R.id.signin_terms:
        openTermsDialog();
        break;
      case R.id.btn_sign_in:
        Log.d("social", "clicked on gplus");
        gplusSignIn();
        break;
      case R.id.facebook_login_button:
        fbSignIn();
        break;
    }
  }

  /* open signup activity*/
  private void openSignUpActivity() {
    //Intent signUpIntent = new Intent(this, SignUp.class);
    Intent signUpIntent = new Intent(this, MobileNumber.class);
    signUpIntent.putExtra("data","2");
    startActivity(signUpIntent);
    this.finish();
  }

  /* open terms and conditions dialog*/
  private void openTermsDialog() {
    Intent termsIntent = new Intent(this, TermsAndConditions.class);
    startActivity(termsIntent);
  }

  /* open Home Activity*/
  private void openHomeActivity() {
    Intent homeIntent = new Intent(this, Home.class);
    startActivity(homeIntent);
    this.finishAffinity();
  }

  /*validate signin data*/
  private void validateSignInData() {
    String mail, pass;
    mail = mailText.getText().toString();
    pass = passText.getText().toString();
    if (mail != null && !mail.equals("null") && !mail.equals("") &&
        pass != null && !pass.equals("null") && !pass.equals("")) {
      if (application.validateEmail(mail)) {
        //openHomeActivity();
        sendDataToServer(mail, pass, "normal");
      }
    } else {
      Toast.makeText(this, "Enter valid credentials", Toast.LENGTH_LONG).show();
    }
  }

  /* send sign in data to server*/
  private void sendDataToServer(String userName, String password, String signInType) {
    if (networkDetector.isConnected()) {
      signInDialog = ProgressDialog.show(this, ""
          , getResources().getString(R.string.progress_dialog_text));
      if (application.getGCMToken().equalsIgnoreCase("")) {
      //  Toast.makeText(this, "Check Internet Connectivity.", Toast.LENGTH_SHORT).show();
        if (application.checkPlayServices(this, null)) {
          // Start IntentService to register this application with GCM.
          Intent intent = new Intent(this, GcmRegistration.class);
          startService(intent);
        }
        closeSignInDialog();
        return;
      }
      accountsClient.signIn(userName, password, signInType, application.getGCMToken(), new WebServiceResponseCallback() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
          closeSignInDialog();
          parseSignInDetails(jsonObject);
        }

        @Override
        public void onFailure(VolleyError error) {
          Log.d("signin", error.toString());
          closeSignInDialog();
        }
      });
    } else {
      Toast.makeText(this,"check internet connectivity",Toast.LENGTH_SHORT).show();

    }
  }



  /*parse signin details*/
  private void parseSignInDetails(JSONObject jsonObject) {
    Log.d("json", jsonObject.toString());
    try {
      JSONObject data = jsonObject.getJSONObject("data");
      int id = data.getInt("id");
      Log.d("id", id + "");
      if (id == 0) {
        Toast.makeText(this, "Credentials not matched", Toast.LENGTH_LONG).show();
      } else {
        application.setuserId(id + "");
        accountsClient.getUserDetails(id + "", userDetailCallbackListener);
        openHomeActivity();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /*close signin dialog*/
  private void closeSignInDialog() {
    if (signInDialog != null) {
      if (signInDialog.isShowing()) {
        try {
          signInDialog.dismiss();
          signInDialog = null;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
