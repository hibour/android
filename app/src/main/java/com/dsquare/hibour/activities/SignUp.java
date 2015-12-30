package com.dsquare.hibour.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dsquare.hibour.R;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeViews();
        initializeEventlisteners();
    }
    /* initialize views*/
    private void initializeViews(){
        submitButton = (Button)findViewById(R.id.signup_signup_button);
    }

    /*initialize event listeners*/
    private void initializeEventlisteners(){
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup_signup_button:
                openProofActivity();
                break;
        }
    }
    /*open govt proof activity*/
    private void openProofActivity(){
        Intent proofIntent = new Intent(this,GovtProof.class);
        startActivity(proofIntent);
    }
}
