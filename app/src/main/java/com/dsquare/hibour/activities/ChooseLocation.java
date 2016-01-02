package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PlaceAutoCompleteAdapter;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChooseLocation extends AppCompatActivity implements View.OnClickListener
        ,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final LatLngBounds BOUNDS_INDIA =new LatLngBounds(new LatLng(8.4,37.6),new LatLng(68.7,97.25)) ;
    private Button next;
    protected GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mapFragment;
    private TextView locationDisplayTextView;
    private AutoCompleteTextView autoCompleteTextView;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private List<Integer> filterTypes = new ArrayList<Integer>();
    private Place place;
    private ProgressDialog pDialog;
    private Marker marker;
    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    private double latitude,longitude;
    private String locAddress;
    private LatLng latLng;
    private boolean markerDrag = false;
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
            pDialog = new ProgressDialog(ChooseLocation.this);
            pDialog.setMessage("Loading Location ....");
            pDialog.show();
            // Get the Place object from the buffer.
            place = places.get(0);
            latLng = place.getLatLng();
            Log.d("places latlng", latLng.toString());
            mapFragment.getMapAsync(ChooseLocation.this);
            Log.i("RESULT CALLBACK 2", "Place details received: " + place.getName());

            places.release();
            if (place == null)
                Toast.makeText(ChooseLocation.this, "Location Not Changed", Toast.LENGTH_SHORT).show();
            else {
              //  latitude=latLng.latitude;
               // longitude = latLng.longitude;
                Log.d("lat and long",latLng.latitude+" "+latLng.longitude);
                locationDisplayTextView.setText("");
                Double[] params=new Double[2];
                params[0]= latLng.latitude;
                params[1]= latLng.longitude;
                GetCurrentAddress currentadd=new GetCurrentAddress();
                try {
                    currentadd.execute(params);
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
            Log.i("On set ClickListener", "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            /*Toast.makeText(getApplicationContext(), "Clicked: " + item.description,
                    Toast.LENGTH_SHORT).show();*/
            Log.i("SEE", "Called getPlaceById to get Place details for " + item.placeId);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        initializeViews();
        initializeEventListeners();
        //buildGoogleApiClient();
    }

    /*initialize views*/
    private void initializeViews(){
        next = (Button)findViewById(R.id.location_next_button);
        locationDisplayTextView = (TextView)findViewById(R.id.loc_curr_loc_textview);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.loc_search_autocomplete);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        filterTypes.add(Place.TYPE_GEOCODE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this,android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_INDIA, AutocompleteFilter.create(filterTypes));
        autoCompleteTextView.setAdapter(placeAutoCompleteAdapter);

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
        if(marker==null){
            marker = googleMap.addMarker(new MarkerOptions().position(coords).title(locAddress).draggable(true));
        }else{
            marker.remove();
            marker = googleMap.addMarker(new MarkerOptions().position(coords).title(locAddress).draggable(true));
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 10));
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
                GetCurrentAddress currentadd=new GetCurrentAddress();
                try {
                    currentadd.execute(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
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
        if(!markerDrag){
            mapFragment.getMapAsync(this);
        }else{
            markerDrag = false;
        }
        locationDisplayTextView.setText(locAddress);
    }

}
