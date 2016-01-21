package com.dsquare.hibour.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PlaceAutoCompleteAdapter;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dsquare Android on 1/14/2016.
 */
public class LocationSearch extends AppCompatActivity implements View.OnClickListener
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(8.4, 37.6), new LatLng(68.7, 97.25));
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private List<Integer> filterTypes = new ArrayList<Integer>();
    private Place place;
    private ProgressDialog pDialog;
    public Button search, signin;
    private AutoCompleteTextView autoCompleteTextView;

    protected Location mLastLocation;
    private double latitude, longitude;
    private String locAddress;
    private LatLng latLng;
    private boolean markerDrag = false;
    private Typeface avenir;
    private ProgressDialog locInsertDialog;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private boolean isAutoComplete = false;
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

            places.release();
            if (place == null)
                Toast.makeText(LocationSearch.this, "Location Not Changed", Toast.LENGTH_SHORT).show();
            else {
                Log.d("lat and long", latLng.latitude + " " + latLng.longitude);
//                locationDisplayTextView.setText("");
                Double[] params = new Double[2];
                params[0] = latLng.latitude;
                params[1] = latLng.longitude;
                GetCurrentAddress currentadd = new GetCurrentAddress();
                try {
                    isAutoComplete = true;
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
//            polygonOptions.add();
//            countPolygonPoints();
            /*Toast.makeText(getApplicationContext(), "Clicked: " + item.description,
                    Toast.LENGTH_SHORT).show();*/
            Log.i("SEE", "Called getPlaceById to get Place details for " + item.placeId);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);
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
        search = (Button) findViewById(R.id.places_search);
        signin = (Button) findViewById(R.id.places_signup);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.loc_search_autocomplete);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    autoCompleteTextView.setHint("");
                } else {
                    autoCompleteTextView.setHint(R.string.loc_locality);
                }

            }
        });
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);

//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
        filterTypes.add(Place.TYPE_GEOCODE);

        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_INDIA, AutocompleteFilter.create(filterTypes));
        autoCompleteTextView.setAdapter(placeAutoCompleteAdapter);

        autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(autoCompleteTextView.equals(null)||autoCompleteTextView.getText().toString().equals("")){

                }else{
                    /*Intent intent = new Intent(getApplicationContext(), ChooseLocation.class);
                    intent.putExtra("latitude",Constants.Latitude);
                    intent.putExtra("longitude",Constants.Longitude);
                    intent.putExtra("address",autoCompleteTextView.getText().toString());
                    startActivity(intent);*/
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        avenir = Typeface.createFromAsset(getAssets(),"fonts/AvenirLTStd-Book.otf");
        search.setTypeface(avenir);
        signin.setTypeface(avenir);
        autoCompleteTextView.setTypeface(avenir);
    }

    private void initializeEventListeners() {
        search.setOnClickListener(this);
        signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.places_search:
                Intent intent = new Intent(getApplicationContext(), ChooseLocation.class);
                if(autoCompleteTextView.getText()!=null && !autoCompleteTextView.getText().toString().equals("")){
                    intent.putExtra("latitude",Constants.Latitude);
                    intent.putExtra("longitude",Constants.Longitude);
                    intent.putExtra("address",autoCompleteTextView.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"Choose neighbourhood",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.places_signup:
                Intent intent1 = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent1);
                break;

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
            /*Intent intent = new Intent(getApplicationContext(), ChooseLocation.class);
            intent.putExtra("latitude",Constants.Latitude);
            intent.putExtra("longitude",Constants.Longitude);
            intent.putExtra("address",autoCompleteTextView.getText().toString());
            startActivity(intent);*/
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
        @Override
        protected void onPostExecute(String resultString) {

            if(isAutoComplete){
                Intent intent = new Intent(getApplicationContext(), ChooseLocation.class);
                intent.putExtra("latitude",Constants.Latitude);
                intent.putExtra("longitude",Constants.Longitude);
                intent.putExtra("address",locAddress);//autoCompleteTextView.getText().toString());
                isAutoComplete = false;
                startActivity(intent);
            }
//            startMap();
        }
    }
}
