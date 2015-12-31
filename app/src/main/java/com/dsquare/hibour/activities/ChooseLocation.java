package com.dsquare.hibour.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dsquare.hibour.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseLocation extends AppCompatActivity implements View.OnClickListener
        ,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private Button next;
    protected GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mapFragment;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    private double latitude,longitude;
    private String locAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        initializeViews();
        initializeEventListeners();
        buildGoogleApiClient();
    }

    /*initialize views*/
    private void initializeViews(){
        next = (Button)findViewById(R.id.location_next_button);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.location_next_button:
                openSocializeActivity();
                break;
        }
    }

    /* open socialize activity*/
    private void openSocializeActivity(){
        Intent socialIntent = new Intent(this,SocialCategories.class);
        startActivity(socialIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng coords = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(coords).title(locAddress));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords,15));

    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
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
            GetCurrentAddress currentadd=new GetCurrentAddress();
            try {
                currentadd.execute(params);
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
                //addresses = geocoder.getFromLocation(17.4435285, 78.3870871, 5);
                addresses = geocoder.getFromLocation(params[0], params[1], 5);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String ad = address.getAddressLine(0)+" "+address.getAddressLine(1)
                            +" "+address.getAddressLine(2);
                    Log.d("address",address.toString());
                    Log.d("address",address.getLocality()+" "+address.getSubLocality());
                    locAddress = ad;
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
        mapFragment.getMapAsync(this);
    }

}
