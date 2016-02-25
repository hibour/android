package com.dsquare.hibour.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PlaceAutoCompleteAdapter;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.LocationClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.ProximaLight;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
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

/**
 * Created by Dsquare Android on 1/14/2016.
 */
public class LocationSearch extends AppCompatActivity implements View.OnClickListener
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback {

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(8.4, 37.6), new LatLng(68.7, 97.25));
    public Button search, signin;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private List<Integer> filterTypes = new ArrayList<Integer>();
    private Place place;
    private ProgressDialog pDialog;
    private AutoCompleteTextView autoCompleteTextView, autoCompleteTextView1;
    private double latitude, longitude;
    private String locAddress,subLocality;
    private LatLng latLng;
    private boolean markerDrag = false;
    private Typeface tf;
    private ProgressDialog locInsertDialog;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private Hibour application;
    private Gson gson;
    private boolean isAutoComplete = false;
    private WebView locMap;
    private TextView locationDisplayTextView, countText;
    private Button next;
    private RelativeLayout map,locLayout;
    private RelativeLayout searchLayout,mapLayout;
    private LinearLayout auto,signInLayout;
    private LocationClient locationClient;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private CoordinatorLayout coordinatorLayout;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("Result Callback", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
//            pDialog = new ProgressDialog(LocationSearch.this);
//            pDialog.setMessage("Loading Location ....");
//            pDialog.show();
            // Get the Place object from the buffer.
            place = places.get(0);
            latLng = place.getLatLng();Log.d("address,type",place.getAddress()+" "+place.getPlaceTypes().get(0));
            Log.d("places latlng", latLng.toString());
//            mapFragment.getMapAsync(ChooseLocation.this);
            Log.i("RESULT CALLBACK 2", "Place details received: " + place.getName());

            hideSoftKeyboard();
            places.release();
            if (place == null)
                Toast.makeText(LocationSearch.this, "Location Not Changed", Toast.LENGTH_SHORT).show();
            else {
                getAddress(latLng.latitude+"",latLng.longitude+"",locAddress,place.getId());
                Log.d("lat and long", latLng.latitude + " " + latLng.longitude);
                Double[] params = new Double[2];
                params[0] = latLng.latitude;
                params[1] = latLng.longitude;
                GetCurrentAddress currentadd = new GetCurrentAddress();
                try {
                    isAutoComplete = true;
                   // currentadd.execute(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutoCompleteAdapter.PlaceAutocomplete item = placeAutoCompleteAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.d("placeId", placeId);
            Log.i("On set ClickListener", "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//            polygonOptions.add();
//            countPolygonPoints();
            /*Toast.makeText(getApplicationContext(), "Clicked: " + item.description,
                    Toast.LENGTH_SHORT).show();*/
            Log.i("SEE", "Called getPlaceById to get Place details for " + item.placeId);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if( mapFragment.getView().getVisibility() == View.GONE){
            Log.d("visibility","yes");
            mapFragment.getView().setVisibility(View.VISIBLE);
        }
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_places);
        initializeViews();
        initializeEventListeners();
    }

    private void initializeViews() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.loc_map);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        mapFragment.getMapAsync(this);
        locationClient = new LocationClient(this);
        auto = (LinearLayout) findViewById(R.id.loc_search_layout);
        map = (RelativeLayout) findViewById(R.id.relative_map);
        locationDisplayTextView = (TextView) findViewById(R.id.loc_curr_loc_textview);
        locMap = (WebView) findViewById(R.id.map);
        countText = (TextView) findViewById(R.id.loc_members_count);
        accountsClient = new AccountsClient(this);
        application = Hibour.getInstance(this);
        application.initializeSharedPrefs();
        gson = new Gson();
        searchLayout = (RelativeLayout) findViewById(R.id.relative_auto);
        next = (Button) findViewById(R.id.serach_sumbit);
        signInLayout = (LinearLayout) findViewById(R.id.sign_in_text);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.loc_search_autocomplete);
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.loc_search_autocomplete1);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    autoCompleteTextView.setHint("");
                    ImageView imageViewlogo = (ImageView) LocationSearch.this.findViewById(R.id.hibour_logo_landing_page);
                    ProximaLight proximaLighttemp2 = (ProximaLight) LocationSearch.this.findViewById(R.id.loc_search_text_temp2);
                    ProximaLight proximaLighttemp1 = (ProximaLight) LocationSearch.this.findViewById(R.id.loc_search_text_temp1);

                    LinearLayout linearLayoutLocSearch = (LinearLayout) LocationSearch.this.findViewById(R.id.loc_search_layout);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayoutLocSearch.getLayoutParams();

                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                    alphaAnimation.setDuration(700);
                    alphaAnimation.setInterpolator(new LinearInterpolator());
                    alphaAnimation.setFillAfter(true);

                    imageViewlogo.setAnimation(alphaAnimation);
                    proximaLighttemp1.setAnimation(alphaAnimation);
                    proximaLighttemp2.setAnimation(alphaAnimation);

                    proximaLighttemp2.animate();
                    proximaLighttemp1.animate();
                    imageViewlogo.animate();


                    TranslateAnimation translateAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_PARENT, -0.4f);
                    translateAnimation.setDuration(1000);
                    translateAnimation.setFillAfter(true);
                    translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

                    linearLayoutLocSearch.setAnimation(translateAnimation);
                    linearLayoutLocSearch.animate();

                } else {
                    autoCompleteTextView.setHint(R.string.loc_locality);
                    LocationSearch.this.findViewById(R.id.hibour_logo_landing_page).setVisibility(View.VISIBLE);
                    LocationSearch.this.findViewById(R.id.loc_search_text_temp2).setVisibility(View.VISIBLE);
                    LocationSearch.this.findViewById(R.id.loc_search_text_temp1).setVisibility(View.VISIBLE);
                }

            }
        });
        autoCompleteTextView1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    autoCompleteTextView1.setHint("");
                } else {
                    autoCompleteTextView1.setHint(R.string.loc_locality);
                }

            }
        });
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);

        filterTypes.add(Place.TYPE_GEOCODE);

        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        autoCompleteTextView1.setOnItemClickListener(mAutocompleteClickListener);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_INDIA, AutocompleteFilter.create(filterTypes));
        autoCompleteTextView.setAdapter(placeAutoCompleteAdapter);
        autoCompleteTextView1.setAdapter(placeAutoCompleteAdapter);
        autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoCompleteTextView.equals(null) || autoCompleteTextView.getText().toString().equals("")) {
                    locAddress = autoCompleteTextView.getText().toString();
                } else {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        autoCompleteTextView1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (autoCompleteTextView.equals(null) || autoCompleteTextView.getText().toString().equals("")) {
                    locAddress = autoCompleteTextView1.getText().toString();
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tf = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
        autoCompleteTextView.setTypeface(tf);
        autoCompleteTextView1.setTypeface(tf);
    }

    private void initializeEventListeners() {
        next.setOnClickListener(this);
        signInLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.places_search:
                break;
            case R.id.places_signup:
                Intent intent1 = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent1);
                this.finish();
                break;
            case R.id.serach_sumbit:
                Intent intent2 = new Intent(getApplicationContext(), Social.class);
                startActivity(intent2);
                this.finish();
                break;
            case R.id.sign_in_text:
                Intent signInIntent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(signInIntent);
                this.finish();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Double[] params=new Double[2];
            params[0]= mLastLocation.getLatitude();
            params[1]= mLastLocation.getLongitude();
            GetCurrentAddress currentadd=new GetCurrentAddress();
            try {
               currentadd.execute(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("lat+lon",mLastLocation.getLatitude()+" "+mLastLocation.getLongitude());
        } else {
           // Toast.makeText(this, "location not found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("loc", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {
        Log.i("loc", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }



    /* async task for getting address*/
    private class GetCurrentAddress extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
            try {
                Geocoder geocoder = new Geocoder(LocationSearch.this, Locale.getDefault());
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
                    Constants.latitude = "";
                    Constants.longitude = "";
                    Constants.locationaddress = "";
                    Constants.locationaddress1 = "";
                    Constants.latitude = String.valueOf(latitude);
                    Constants.longitude = String.valueOf(longitude);
                    Constants.locationaddress = autoCompleteTextView.getText().toString();
                    Constants.locationaddress1 = locAddress;
                    Constants.Latitude=latitude;
                    Constants.Longitude=longitude;
                    Constants.LocationAddress=locAddress;
                   Log.d("latitude", String.valueOf(latitude));
                    Log.d("longitude", String.valueOf(longitude));
                    //autoLoc = address.getLocality();
                    return address.getLocality();
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return "";
        }
        @SuppressLint("ResourceAsColor")
        @Override

        protected void onPostExecute(String resultString) {

            if(isAutoComplete){
                map.setVisibility(View.VISIBLE);
//                signin.setVisibility(View.VISIBLE);
                autoCompleteTextView1.setText(autoCompleteTextView.getText().toString());
                Constants.userAddress = locAddress;
                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -200);
                anim.setDuration(1000);
                auto.setAnimation(anim);
                if (networkDetector.isConnected()) {
                    try {
                        URL url = new URL("http://hibour.com/test.php?area=" + autoCompleteTextView.getText().toString());
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
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(R.color.newbrand);
                    snackbar.show();
                }
                isAutoComplete = false;

                anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        searchLayout.setVisibility(View.GONE);
                    }
                });


                getMembersCount(autoCompleteTextView1.getText().toString());
            }
        }
    }
    @SuppressLint("ResourceAsColor")
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
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(R.color.newbrand);
            snackbar.show();
        }
    }

    /* parse loc data*/
    private void parseLocDetails(JSONObject jsonObject){
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            Log.d("data",data.toString());
            int count = data.getInt("Count");
            countText.setText("There are about "+count+" members registered from your area.");
            closeLocDialog();
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

    /**
            * Hides the soft keyboard
    */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
    /* get address from google javascript api*/
    public void getAddress(String lat,String lng, final String locAddress, final String placeId){
        if(networkDetector.isConnected()){
            locationClient.getAddress(lat, lng, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseLocationDetails(jsonObject,locAddress,placeId);
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("error",error.toString());
                }
            });
        }else{

        }
    }
    /* parse location details*/
    public void parseLocationDetails(JSONObject jsonObject,String locAddress,String placeId){
        try {
            Log.d("loc",jsonObject.toString());
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject addressData = results.getJSONObject(0);
            JSONArray addressComponents = addressData.getJSONArray("address_components");
            if(addressComponents.toString().contains("sublocality_level_1")){
                for(int i=0;i<addressComponents.length();i++) {
                    JSONObject addressObject = addressComponents.getJSONObject(i);
                    JSONArray types = addressObject.getJSONArray("types");
                    if(types.toString().contains("sublocality_level_1")){
                        String sub_locality = addressObject.getString("long_name");
                        Log.d("sublocalityIf",sub_locality);
                        subLocality = sub_locality;
                        setOnMap(locAddress);
                    }
                }
            }else if(addressComponents.toString().contains("sublocality_level_2")){
                for(int i=0;i<addressComponents.length();i++) {
                    JSONObject addressObject = addressComponents.getJSONObject(i);
                    JSONArray types = addressObject.getJSONArray("types");
                    if(types.toString().contains("sublocality_level_2")){
                        String sub_locality = addressObject.getString("long_name");
                        Log.d("sublocalityElse", sub_locality);
                        subLocality = sub_locality;
                        setOnMap(locAddress);
                    }
                }
            }else{
                for(int i=0;i<addressComponents.length();i++) {
                    JSONObject addressObject = addressComponents.getJSONObject(i);
                    JSONArray types = addressObject.getJSONArray("types");
                    if(types.toString().contains("locality")){
                        String sub_locality = addressObject.getString("long_name");
                        Log.d("localityElse",sub_locality);
                        subLocality = sub_locality;
                        setOnMap(locAddress);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* set map*/
    @SuppressLint("ResourceAsColor")
    public void setOnMap(String address){
        map.setVisibility(View.VISIBLE);
        if(searchLayout.getVisibility()==View.VISIBLE){
            autoCompleteTextView1.setText(autoCompleteTextView.getText().toString());
        }
        Constants.userAddress = locAddress;
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -200);
        anim.setDuration(1000);
        auto.setAnimation(anim);
        if(networkDetector.isConnected()){
            try {
                URL url = new URL("http://hibour.com/test.php?area="+autoCompleteTextView1.getText().toString());
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
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchLayout.setVisibility(View.GONE);
            }
        });


        getMembersCount(autoCompleteTextView1.getText().toString());
    }
}
