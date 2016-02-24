package com.dsquare.hibour.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AutoStartUp extends Service {
  private static final String LOG_TAG = AutoStartUp.class.getSimpleName();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

}