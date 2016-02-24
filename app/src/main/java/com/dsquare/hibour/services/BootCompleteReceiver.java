package com.dsquare.hibour.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {

  public static final String ACTION_AUTO_START_APPLICATION = "com.dsquare.hibour.services.BootCompleteReceiver";
  private static final String LOG_TAG = BootCompleteReceiver.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
//    Log.e(LOG_TAG, intent.getAction()+"");
    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
        intent.getAction().equals(ACTION_AUTO_START_APPLICATION)) {
      Intent serviceIntent = new Intent(context, AutoStartUp.class);
      context.startService(serviceIntent);

      Intent autoStartIntent = new Intent(context, BootCompleteReceiver.class);
      autoStartIntent.setAction(ACTION_AUTO_START_APPLICATION);
      PendingIntent temp = PendingIntent.getBroadcast(context, 0, autoStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, temp);
    }
  }
}
