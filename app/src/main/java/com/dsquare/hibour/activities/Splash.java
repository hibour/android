package com.dsquare.hibour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.dsquare.hibour.R;
import com.dsquare.hibour.utils.Hibour;

/**
 * Created by Dsquare Android on 1/14/2016.
 */
public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private Hibour application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        application = Hibour.getInstance(this);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                    if(application.getUserId().equals("")){
                        Intent i = new Intent(Splash.this, LocationSearch.class);
                        startActivity(i);
                    }else{
                        Intent homeIntent = new Intent(Splash.this,Home.class);
                        startActivity(homeIntent);
                    }

                    finish();
            }
        }, SPLASH_TIME_OUT);
    }

}

