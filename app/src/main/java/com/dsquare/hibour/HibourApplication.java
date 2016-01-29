package com.dsquare.hibour;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.dsquare.hibour.gcm.GcmRegistration;
import com.dsquare.hibour.utils.Hibour;

public class HibourApplication extends Application {
  private static final String LOG_TAG = HibourApplication.class.getSimpleName();
  private Hibour application;

  @Override
  public void onCreate() {
    super.onCreate();
    ActiveAndroid.initialize(this);
    application = Hibour.getInstance(this);
    application.initializeSharedPrefs();
    if (application.getGCMToken().equalsIgnoreCase("")) {
      makeGcmRegistration();
    }
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  private void makeGcmRegistration() {
    if (application.checkPlayServices(this, null)) {
      // Start IntentService to register this application with GCM.
      Intent intent = new Intent(this, GcmRegistration.class);
      startService(intent);
    }
  }

}
