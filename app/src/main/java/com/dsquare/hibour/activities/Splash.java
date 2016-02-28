package com.dsquare.hibour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.utils.Hibour;
import com.onesignal.OneSignal;

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
        //Fabric.with(this, new Crashlytics());
        OneSignal.startInit(this).init();


        setContentView(R.layout.activity_splash);

        application = Hibour.getInstance(this);
        img_view = (ImageView) findViewById(R.id.splash_logo);
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        //Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(3000); //Put desired duration per anim cycle here, in milliseconds

        //Start animation
        img_view.startAnimation(anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(application.getUserId().equals("")){
                        Intent i = new Intent(Splash.this, LocationSearch.class);
                        startActivity(i);
                    }else{
                        Intent homeIntent = new Intent(Splash.this, HomeActivity.class);
                        startActivity(homeIntent);
                    }
                    finish();
            }
        }, SPLASH_TIME_OUT);
    }

}