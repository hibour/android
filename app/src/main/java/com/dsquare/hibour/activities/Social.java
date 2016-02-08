package com.dsquare.hibour.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.dsquare.hibour.R;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.utils.Fonts;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

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
    public static final String[] IMAGE_NAME = {"a", "bg_googleplaces", "a", "bg_googleplaces",};
    private static final int RC_SIGN_IN = 9001;
    private static final String intentText = "pintent";
    protected GoogleApiClient mGoogleApiClient;
    private String TAG = "signin";
    private GoogleSignInOptions googleSignInOptions;
    private SignInButton signInButton;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private Button submitButton;
    private Typeface tf;
    private NetworkDetector networkDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_social);
        networkDetector = new NetworkDetector(this);
        tf = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
        submitButton = (Button)findViewById(R.id.social_signup);
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
                                        Log.d("fname",object.optString("first_name"));
                                        Log.d("lname",object.optString("last_name"));
                                        Log.d("gender",object.optString("gender"));
//                                        signUpUser(object.optString("name"), object.optString("email")
//                                                , "", "fb");
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
                Person.Gender gender;
                GoogleSignInAccount acct = result.getSignInAccount();
//                application.setSocialPreferences(Constants.USER_LOGIN_GPLUS);
//                Person person = (Person) result.getSignInAccount();
//                person.getGender();
//                Log.d("gender", String.valueOf(person.getGender()));

                Plus.PeopleApi.load(mGoogleApiClient, acct.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                    @Override
                    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                        Person person = loadPeopleResult.getPersonBuffer().get(0);
//                        Log.d("Person loaded");
                        Log.d("fname",person.getName().getGivenName());
                        Log.d("lname",person.getName().getFamilyName());
                        Log.d("name",person.getDisplayName());
                        Log.d("gender",person.getGender() + "");
                    }
                });
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
//            Plus.PeopleApi.load(mGoogleApiClient, "signed_in_user_account_id").setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
//                @Override
//                public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
//                    Person person = loadPeopleResult.getPersonBuffer().get(0);
//                    Timber.d("Person loaded");
//                    Timber.d(person.getName().getGivenName());
//                    Timber.d(person.getName().getFamilyName());
//                    Timber.d(person.getDisplayName());
//                    Timber.d(person.getGender() + "");
//                }
//            });
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
            case R.id.social_signup:
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                finish();
//                validateData();
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

}
