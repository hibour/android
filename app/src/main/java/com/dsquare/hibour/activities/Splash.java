package com.dsquare.hibour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.dsquare.hibour.R;
import com.dsquare.hibour.utils.Hibour;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Dsquare Android on 1/14/2016.
 */
public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private Hibour application;
    private ImageView img_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_splash);

        application = Hibour.getInstance(this);
        /*img_view = (ImageView) findViewById(R.id.splash_logo);
        RotateAnimation rotate = new RotateAnimation(0 ,720);
        rotate.setDuration(1000);
        img_view.startAnimation(rotate);*/
        new Handler().postDelayed(new Runnable() {




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

