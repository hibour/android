package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.dsquare.hibour.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
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

//    private static final LatLngBounds BOUNDS_INDIA =new LatLngBounds(new LatLng(Constants.Latitude,Constants.Longitude),new LatLng(Constants.Latitude,Constants.Longitude)) ;
    private Button next,previous;
    protected GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mapFragment;
    private TextView locationDisplayTextView;
    private AutoCompleteTextView autoCompleteTextView;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private List<Integer> filterTypes = new ArrayList<Integer>();
    private Place place;
    private ProgressDialog pDialog;
    private Marker marker;
    private PolygonOptions polygonOptions;
    private Polygon polygon;
    private GoogleMap googleMap;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        locationDisplayTextView = (TextView)findViewById(R.id.loc_curr_loc_textview);
        locMap = (WebView)findViewById(R.id.map);

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
        }
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
            Toast.makeText(this, "location not found", Toast.LENGTH_LONG).show();
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
    private void sendLocData(String userId,String lat,String lon,String address){
        if(networkDetector.isConnected()){
            locInsertDialog = ProgressDialog.show(this,""
                    ,getResources().getString(R.string.progress_dialog_text));
            accountsClient.inserUserLocation(userId,lat,lon,address,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseLocDetails(jsonObject);
                    closeLocDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("loc",error.toString());
                    closeLocDialog();
                }
            });
        }else{
            Toast.makeText(this,"Network error",Toast.LENGTH_LONG).show();
        }
    }
    /* parse loc data*/
    private void parseLocDetails(JSONObject jsonObject){

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
}
