package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PlaceAutoCompleteAdapter;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChooseLocation extends AppCompatActivity implements View.OnClickListener
        ,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    private static final LatLngBounds BOUNDS_INDIA =new LatLngBounds(
            new LatLng(Constants.Latitude,Constants.Longitude)
            ,new LatLng(Constants.Latitude,Constants.Longitude)) ;

    private Button next,previous;
    protected GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private String TAG = "signin";
    private GoogleSignInOptions googleSignInOptions;
    private SignInButton signInButton;
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private SupportMapFragment mapFragment;
    private TextView locationDisplayTextView,countText;
    private AutoCompleteTextView autoCompleteTextView;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private List<Integer> filterTypes = new ArrayList<Integer>();
    private Place place;
    private ProgressDialog pDialog;
    private Marker marker;
    private PolygonOptions polygonOptions;
    private Polygon polygon;
    private GoogleMap googleMap;
    private ProgressDialog signUpDialog;
    private Hibour application;
    private Gson gson;
    private String userName,userMail;
    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    private double latitude,longitude;
    private String locAddress;
    private LatLng latLng;
    private boolean markerDrag = false;
    private Typeface avenir;
    private ProgressDialog locInsertDialog;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private WebView locMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        locationDisplayTextView = (TextView)findViewById(R.id.loc_curr_loc_textview);
        locMap = (WebView)findViewById(R.id.map);
        countText = (TextView)findViewById(R.id.loc_members_count);

        accountsClient = new AccountsClient(this);
        application = Hibour.getInstance(this);
        gson = new Gson();
        next = (Button)findViewById(R.id.location_next_button);
//        initializeViews();
       initializeEventListeners();

        // Receiving latitude from MainActivity screen
//        double latitude = getIntent().getDoubleExtra("latitude", 0);
//
//        // Receiving longitude from MainActivity screen
//        double longitude = getIntent().getDoubleExtra("longitude", 0);
//
        String address = getIntent().getStringExtra("address");
//
        locationDisplayTextView.setText(address);
        Constants.userAddress = address;
        networkDetector = new NetworkDetector(this);
        getMembersCount(address);
        if(networkDetector.isConnected()){
            try {
                URL url = new URL("http://hibour.com/test.php?area="+address);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort()
                        , url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
                locMap.loadUrl(url.toString());
                locMap.getSettings().setJavaScriptEnabled(true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,"Can't connect to network.",Toast.LENGTH_LONG).show();
        }
        initializeGplus();
        initializeFb();


//
//        LatLng position = new LatLng(latitude, longitude);
//
//        // Instantiating MarkerOptions class
//        MarkerOptions options = new MarkerOptions();
//
//        // Setting title for the MarkerOptions
//        options.title(address);
//
//        // Setting snippet for the MarkerOptions
//        options.snippet("Latitude:"+latitude+",Longitude:"+longitude);
//
//        // Getting Reference to SupportMapFragment of activity_map.xml
//        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
//
//        // Getting reference to google map
//        GoogleMap googleMap = fm.getMap();
//
//        // Adding Marker on the Google Map
//        googleMap.addMarker(new MarkerOptions().position(position).title(address).draggable(true));
//
//        // Creating CameraUpdate object for position
//        CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(position);
//
////        CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(15);
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position,15));
    }

    /*initialize views*/
    /*private void initializeViews(){
        next = (Button)findViewById(R.id.location_next_button);
//        previous = (Button)findViewById(R.id.location_prev_button);
        locationDisplayTextView = (TextView)findViewById(R.id.loc_curr_loc_textview);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.loc_search_autocomplete);
        avenir = Typeface.createFromAsset(getAssets(),"fonts/AvenirLTStd-Book.otf");
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);
        Constants.Longitude=longitude;
        Constants.Latitude=latitude;
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        filterTypes.add(Place.TYPE_GEOCODE);
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .addApi(LocationServices.API)
//                .build();
//        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this,android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_INDIA, AutocompleteFilter.create(filterTypes));
        autoCompleteTextView.setAdapter(placeAutoCompleteAdapter);
        next.setTypeface(avenir);
//        previous.setTypeface(avenir);
        autoCompleteTextView.setTypeface(avenir);
    }*/
    /* initialize event listeners*/
    private void initializeEventListeners(){
        next.setOnClickListener(this);
//        previous.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.location_next_button:
                openSocializeActivity();
                break;
//            case R.id.location_prev_button:
//                openPreviousActivity();
//                break;
            case R.id.btn_sign_in:
                Log.d("social","clicked on gplus");
                gplusSignIn();
                break;
            case R.id.facebook_login_button:
                fbSignIn();
                break;
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
                                    try {
                                        Log.d("email",object.optString("email"));
                                        Log.d("id",object.optString("id"));
                                        Log.d("name",object.optString("name"));
                                        signUpUser(object.optString("name"), object.optString("email")
                                                , "", "fb");
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
                    Toast.makeText(ChooseLocation.this, "User cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(ChooseLocation.this, "Error on Login, check your facebook app_id", Toast.LENGTH_LONG).show();
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
                    signUpUser(userName, userMail, "", "gp");
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

    /* open socialize activity*/
    private void openSocializeActivity(){
        Intent socialIntent = new Intent(this,SignUp.class);
        startActivity(socialIntent);

    }
    /* open govtproof activity*/
    private void openPreviousActivity(){
        Intent proofIntent = new Intent(this,GovtProof.class);
        startActivity(proofIntent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng coords = new LatLng(latitude, longitude);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords,15));
        if(marker==null){
            marker = googleMap.addMarker(new MarkerOptions().position(coords).title(locAddress).draggable(true));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords,15));
        }else{
            marker.remove();
            marker = googleMap.addMarker(new MarkerOptions().position(coords).title(locAddress).draggable(true));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords,15));
        }
        if(pDialog!=null){
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                markerDrag = true;
                LatLng latLng=marker.getPosition();
                Double[] params=new Double[2];
                params[0]= latLng.latitude;
                params[1]= latLng.longitude;
//                GetCurrentAddress currentadd=new GetCurrentAddress();
                try {
//                    currentadd.execute(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public void countPolygonPoints(){
        if(polygonOptions.getPoints().size()>3){
            polygonOptions.strokeColor(Color.RED);
            polygonOptions.strokeWidth((float) 0.30);
            polygonOptions.fillColor(Color.RED);
            polygon = googleMap.addPolygon(polygonOptions);

        }
    }
    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Double[] params=new Double[2];
            params[0]= mLastLocation.getLatitude();
            params[1]= mLastLocation.getLongitude();
//            GetCurrentAddress currentadd=new GetCurrentAddress();
            try {
//                currentadd.execute(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("lat+lon",mLastLocation.getLatitude()+" "+mLastLocation.getLongitude());
        } else {
           // Toast.makeText(this, "location not found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("loc", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("loc", "Connection suspended");
        mGoogleApiClient.connect();
    }

    /* async task for getting address*/
    private class GetCurrentAddress extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
            try {
                Geocoder geocoder = new Geocoder(ChooseLocation.this, Locale.getDefault());
                List<Address> addresses=null;
                addresses = geocoder.getFromLocation(params[0], params[1], 5);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String ad = address.getAddressLine(0)+" "+address.getAddressLine(1)
                            +" "+address.getAddressLine(2);
                    Log.d("address",address.toString());
                    Log.d("address",address.getLocality()+" "+address.getSubLocality());
                    // locAddress = ad;
                    locAddress = address.getAddressLine(1);
                    latitude=params[0];
                    longitude = params[1];
                    //autoLoc = address.getLocality();
                    return address.getLocality();
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return "";
        }
        @Override
        protected void onPostExecute(String resultString) {
            startMap();
        }
    }
    private void startMap(){
        if(!markerDrag){
            mapFragment.getMapAsync(this);
        }else{
            markerDrag = false;
        }
        locationDisplayTextView.setText(locAddress);
    }

    /* validate data*/
    /*send loc data to server*/
    private void getMembersCount(String loc){
        if(networkDetector.isConnected()){
            locInsertDialog = ProgressDialog.show(this,""
                    ,getResources().getString(R.string.progress_dialog_text));
            accountsClient.getMembersCount(loc, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseLocDetails(jsonObject);
                    closeLocDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("loc", error.toString());
                    closeLocDialog();
                }
            });
        }else{
            Toast.makeText(this,"Network error",Toast.LENGTH_LONG).show();
        }
    }
    /* parse loc data*/
    private void parseLocDetails(JSONObject jsonObject){
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            Log.d("data",data.toString());
            int count = data.getInt("Count");
            countText.setText("There are about "+count+" members registered from your area.");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /* close loc dialog*/
    private void closeLocDialog(){
        if(locInsertDialog!=null){
            if(locInsertDialog.isShowing()){
                locInsertDialog.dismiss();
                locInsertDialog=null;
            }
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
