package com.dsquare.hibour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dsquare.hibour.R;

public class SocialCategories extends AppCompatActivity implements View.OnClickListener{

    private Button doneButton,previous;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_categories);
        initializeViews();
        initializeEventListeners();
    }

    /* initialize views*/
    private void initializeViews(){
        doneButton = (Button)findViewById(R.id.socialize_done_button);
        previous = (Button)findViewById(R.id.socialize_prev_button);
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        doneButton.setOnClickListener(this);
        previous.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.socialize_done_button:
                openHomeActivity();
                break;
            case R.id.socialize_prev_button:
                openPreviousActivity();
                break;
        }
    }
    /* open home activity*/
    private void openHomeActivity(){
        Intent homeIntent = new Intent(this,Home.class);
        startActivity(homeIntent);
    }
    private void openPreviousActivity(){
        Intent locationIntent = new Intent(this,ChooseLocation.class);
        startActivity(locationIntent);
        finish();
    }
}
