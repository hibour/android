package com.dsquare.hibour.utils;

import android.content.Context;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {

  private Context context;

  public Helper(Context context) {
    this.context = context;
  }

  /* get today date*/
  public static String getTodayDate() {
    Calendar currentDate = Calendar.getInstance(); //Get the current date
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
    return formatter.format(currentDate.getTime());
  }

  public String getDeviceId() {
    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

}
