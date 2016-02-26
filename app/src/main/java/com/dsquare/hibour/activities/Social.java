package com.dsquare.hibour.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.gcm.GcmRegistration;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.signup.Data;
import com.dsquare.hibour.pojos.signup.SignupPojo;
import com.dsquare.hibour.pojos.user.UserDetail;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aditya Ravikanti on 2/5/2016.
 */
public class Social extends FragmentActivity implements View.OnClickListener
    , GoogleApiClient.OnConnectionFailedListener, ViewPager.OnPageChangeListener {
  public static final String[] IMAGE_NAME = {"presignup_img1", "presignup_img1", "presignup_img1", "presignup_img1",};
  static final int NUM_ITEMS = 4;
  private static final int RC_SIGN_IN = 9001;
  private static final String intentText = "pintent";
  protected GoogleApiClient mGoogleApiClient;
  ViewPager viewPager;
  ImageFragmentPagerAdapter imageFragmentPagerAdapter;
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
  private int pos = 0;
  private Hibour application;
  private String Useremail = "", Userfname = "", Userlname = "", Usergender = "";
  private View dot1, dot2, dot3, dot4;
  private LinearLayout socialSignIn;
    private CoordinatorLayout coordinatorLayout;
    private Bitmap bitmap;
    private String imageString="a";
  private View.OnClickListener facebookConnectListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      facebookLoginButton.performClick();
        fbSignIn();
    }
  };
  private View.OnClickListener googleConnectListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      googleSignInButton.performClick();
        gplusSignIn();
    }
  };

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
    setContentView(R.layout.activity_social);
    networkDetector = new NetworkDetector(this);
    gson = new Gson();
    application = Hibour.getInstance(this);
    application.initializeSharedPrefs();
    accountsClient = new AccountsClient(this);
      coordinatorLayout = (CoordinatorLayout) findViewById(R.id
              .coordinatorLayout);
    tf = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
    submitButton = (Button) findViewById(R.id.social_signup);
    submitButton.setTypeface(tf);
    submitButton.setOnClickListener(this);
    dot1 = (View) findViewById(R.id.tour_one);
    dot2 = (View) findViewById(R.id.tour_two);
    dot3 = (View) findViewById(R.id.tour_three);
    dot4 = (View) findViewById(R.id.tour_four);
    socialSignIn = (LinearLayout) findViewById(R.id.social_signin);
    socialSignIn.setOnClickListener(this);
    initializeGplus();
    initializeFb();
    imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
    viewPager = (ViewPager) findViewById(R.id.pager);
    viewPager.setAdapter(imageFragmentPagerAdapter);
    viewPager.setOnPageChangeListener(this);
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    Log.d("lposition", position + "");
    switch (position) {
      case 0:
        dot1.setBackgroundResource(R.drawable.tour_circle_filled);
        dot2.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot3.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot4.setBackgroundResource(R.drawable.tour_circle_not_filled);
        pos = position;
        break;
      case 1:
        dot1.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot2.setBackgroundResource(R.drawable.tour_circle_filled);
        dot3.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot4.setBackgroundResource(R.drawable.tour_circle_not_filled);
        pos = position;
        break;
      case 2:
        dot1.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot2.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot3.setBackgroundResource(R.drawable.tour_circle_filled);
        dot4.setBackgroundResource(R.drawable.tour_circle_not_filled);
        pos = position;
        break;
      case 3:
        dot1.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot2.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot3.setBackgroundResource(R.drawable.tour_circle_not_filled);
        dot4.setBackgroundResource(R.drawable.tour_circle_filled);
        pos = position;
        break;
    }

  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }

  public void initializeGplus() {
    googleSignInButton = (SignInButton) findViewById(R.id.google_plus_signin);
    Button custom_googleplus = (Button) findViewById(R.id.custom_googleplus);
    Log.d("social", "initgplus");
    googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestId()
        .requestScopes(new Scope(Scopes.PLUS_ME))
        .requestScopes(new Scope(Scopes.PLUS_LOGIN))
        .build();

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this, this)
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
  public void initializeFb() {
    Log.d("social", "initfb");
    facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
    Button facebookCustomButton = (Button) findViewById(R.id.custom_facebook);
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

  /* gplus signin*/
  @SuppressLint("ResourceAsColor")
  private void gplusSignIn() {
    if (networkDetector.isConnected()) { // check for network connectivity
      Log.d("social", "gplussignin");
      Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
      startActivityForResult(signInIntent, RC_SIGN_IN);
    } else {
//            closeRegisterDialog();
//            internetDialog = new NoInternetDialog();
//            internetDialog.show(getFragmentManager(), getString(R.string.dialog_identifier));
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(R.color.newbrand);
        snackbar.show();

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
                    Log.d("fname", object.optString("first_name"));
                    Log.d("lname", object.optString("last_name"));
                    Log.d("gender", object.optString("gender"));

                    if (object.optString("gender").equals("male")) {
                      Usergender = String.valueOf(0);
                    } else if (object.optString("gender").equals("female")) {
                      Usergender = String.valueOf(1);
                    }
                     URL url = new URL("https://graph.facebook.com/"+object.optString("id")+"/picture");
                      Log.d("profilePicUrl",url+"");
                      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                      HttpURLConnection.setFollowRedirects(HttpURLConnection.getFollowRedirects());
                      connection.setDoInput(true);
                      connection.connect();
                      InputStream input = connection.getInputStream();
                      bitmap = BitmapFactory.decodeStream(input);
//                      bitmap= BitmapFactory.decodeStream(url);
//                      bitmap = getProfilePicture(profilePicUrl);
                      imageString = getStringImage(bitmap);
                      Log.d("imageString",imageString);
//                      String profilePicUrl = object.optString("picture").getJSONObject("data").getString("url");
//                      Bitmap profilePic = getFacebookProfilePicture(profilePicUrl);

                    signUpUser(object.optString("first_name"), object.optString("last_name"), object.optString("email")
                        , "", object.optString("gender"), "fb");
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

        @SuppressLint("ResourceAsColor")
        @Override
        public void onCancel() {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "User cancelled", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(R.color.newbrand);
            snackbar.show();
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onError(FacebookException exception) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Error on Login, check your facebook app_id", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(R.color.newbrand);
            snackbar.show();
        }
      });

      this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    } else {
//            closeRegisterDialog();
//            internetDialog = new NoInternetDialog();
//            internetDialog.show(getFragmentManager(), getString(R.string.dialog_identifier));

    }

  }
//    public static Bitmap getProfilePicture(String url) throws IOException {
//        URL facebookProfileURL= new URL(url);
//        Bitmap bitmap = BitmapFactory.decodeStream(new URL(facebookProfileURL).openConnection().getInputStream());
//        return bitmap;
//    }

  private void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      Log.d("social", "gplus");
      // Signed in successfully, show authenticated UI.
      try {
        GoogleSignInAccount acct = result.getSignInAccount();
        Log.d("name", acct.getDisplayName());
        Log.d("name", acct.getEmail());
        try {
          if (acct.getDisplayName() != null) {
//                        userName = acct.getDisplayName();
          }
//                    acct.
          if (acct.getEmail() != null) {
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
      Log.d("social", "gp");
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
      if (result.isSuccess()) {
        GoogleSignInAccount acct = result.getSignInAccount();
        acct.getPhotoUrl();
        acct.getId();
        if (acct.getEmail() != null) {
          Useremail = acct.getEmail();
        }
        Log.e(TAG, acct.getDisplayName());
        Log.e(TAG, acct.getEmail());

        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (String.valueOf(person.getGender()) != null) {
          Usergender = String.valueOf(person.getGender());
        }
        if (person.getName().getGivenName() != null) {
          Userfname = person.getName().getGivenName();
        }
        if (person.getName().getGivenName() != null) {
          Userlname = person.getName().getFamilyName();
        }
          String profilePicUrl = String.valueOf(person.getImage());
          Log.d("profilePicUrl",profilePicUrl);
//          try {
//              bitmap = getProfilePicture(profilePicUrl);
//          } catch (IOException e) {
//              e.printStackTrace();
//          }
          Log.d("bitmap",bitmap+"");
//          imageString = getStringImage(bitmap);
          Log.d("imageString",imageString);
        signUpUser(Userfname, Userlname, Useremail
            , "", Usergender, "gp");
        Log.i(TAG, "--------------------------------");
        Log.i(TAG, "Display Name: " + person.getDisplayName());
        Log.i(TAG, "Gender: " + person.getGender());
        Log.i(TAG, "fName: " + person.getName().getGivenName());
        Log.i(TAG, "lname: " + person.getName().getFamilyName());
        Log.i(TAG, "image: " + person.getImage());
//            Log.i(TAG, "Current Location: " + person.getCurrentLocation());
        Log.i(TAG, "Language: " + person.getLanguage());

      }
    }
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.social_signup:
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
        finish();
//                validateData();
        break;
      case R.id.google_plus_signin:
        Log.d("social", "clicked on gplus");
        gplusSignIn();
        break;
      case R.id.facebook_login_button:
        fbSignIn();
        break;
      case R.id.social_signin:
        Intent signIntent = new Intent(this, SignIn.class);
        startActivity(signIntent);
        this.finish();
        break;
    }
  }

  /* sign up the user*/
  @SuppressLint("ResourceAsColor")
  private void signUpUser(String userFname, String userLname, String email, String password, String gender, String regType) {
    if (networkDetector.isConnected()) {
      signUpDialog = ProgressDialog.show(this, "", getResources()
          .getString(R.string.progress_dialog_text));
      if (application.getGCMToken().equalsIgnoreCase("")) {
          Snackbar snackbar = Snackbar
                  .make(coordinatorLayout, "Check Internet Connectivity.", Snackbar.LENGTH_LONG);
          // Changing action button text color
          View sbView = snackbar.getView();
          TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
          textView.setTextColor(R.color.newbrand);
          snackbar.show();
        if (application.checkPlayServices(this, null)) {
          // Start IntentService to register this application with GCM.
          Intent intent = new Intent(this, GcmRegistration.class);
          startService(intent);
        }
        return;
      }
        Map<String,String> userDetails = application.getUserDetails();
      accountsClient.signUpUser(userFname, userLname, email, password, gender, regType,imageString,userDetails.get(Constants.SF_LAT)
              , userDetails.get(Constants.SF_LNG),userDetails.get(Constants.SF_LOCADD)
              , userDetails.get(Constants.SF_SUB_LOC),
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
    } else {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(R.color.newbrand);
        snackbar.show();
    }
  }

  /* parse sign up details*/
  private void parseSigUpDetails(JSONObject jsonObject) {
    try {
      closeSignUpDialog();
      Log.d("details", jsonObject.toString());
      SignupPojo registers = gson.fromJson(jsonObject.toString(), SignupPojo.class);
      Data data = registers.getData();
//            Integer integer = data.getId();
      String s = String.valueOf(data.getId());
      Log.d("integer", s);
        Map<String,String> userDetails = new HashMap<>();
        userDetails.put(Constants.PREFERENCE_USER_ID,data.getId()+"");
        userDetails.put(Constants.SF_FIRST,data.getFirstName());
        userDetails.put(Constants.SF_LAST,data.getLastName());
        userDetails.put(Constants.SF_EMAIL,data.getEmail());
        userDetails.put(Constants.SF_LOCADD,data.getAddress());
        userDetails.put(Constants.SF_SUB_LOC,data.getAddress1());
        userDetails.put(Constants.SF_LAT,data.getLattiude());
        userDetails.put(Constants.SF_LNG,data.getLongittude());
        userDetails.put(Constants.SF_PASS,data.getPassword());
        userDetails.put(Constants.SF_DOB,data.getDob());
        userDetails.put(Constants.SF_IMAGE,data.getImage());
        userDetails.put(Constants.SF_GENDER,data.getGender());
        userDetails.put(Constants.SF_REGTYPE,data.getRegtype());
        userDetails.put(Constants.SF_MOBILE,data.getMobile());
        application.setUserDetails(userDetails);
        accountsClient.getUserDetails(data.getId() + "", userDetailCallbackListener);
//      String[] regidetails = {String.valueOf(data.getId()), data.getFirstName(), data.getLastName(), data.getEmail(), data.getGender(), data.getRegtype(), Constants.locationaddress};
//      application.setLoginDetails(regidetails);
      Intent homeIntent = new Intent(this, MobileNumber.class);
      startActivity(homeIntent);
      this.finish();
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
    } catch (final IllegalArgumentException e) {
      // Handle or log or ignore
      e.printStackTrace();
    }
  }

  /* close signup dialog*/
  private void closeSignUpDialog() {
    if (signUpDialog != null) {
      if (signUpDialog.isShowing()) {
        signUpDialog.dismiss();
        signUpDialog = null;
      }
    }

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
      static SwipeFragment newInstance(int position) {
        SwipeFragment swipeFragment = new SwipeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        swipeFragment.setArguments(bundle);
        return swipeFragment;
      }

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
    }
  }

    public String getStringImage(Bitmap bmp) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
//        bitmap = BitmapFactory.decodeFile(imgPath,
//                options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
