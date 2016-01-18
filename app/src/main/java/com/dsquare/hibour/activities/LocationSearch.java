package com.dsquare.hibour.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.dsquare.hibour.R;

/**
 * Created by Dsquare Android on 1/14/2016.
 */
public class LocationSearch extends AppCompatActivity implements View.OnClickListener {
    public Button search,signin;
    private Typeface avenir;
    private AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);
        initializeViews();
        initializeEventListeners();
    }

    private void initializeViews() {
        search = (Button) findViewById(R.id.places_search);
        signin = (Button) findViewById(R.id.places_signup);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.loc_search_autocomplete);
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
        switch(view.getId()) {
            case R.id.places_search:
                Intent intent = new Intent(getApplicationContext(), ChooseLocation.class);
                startActivity(intent);
                break;
            case R.id.places_signup:
                Intent intent1 = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent1);
                break;

        }
    }
}
