package com.dsquare.hibour.gcm;

import android.os.Bundle;
import android.util.Log;

import com.dsquare.hibour.database.DatabaseHandler;
import com.google.android.gms.gcm.GcmListenerService;

public class GcmIntentService extends GcmListenerService {

  private static final String LOG_TAG = GcmIntentService.class.getSimpleName();
  private DatabaseHandler dbHandler;

  /**
   * Called when message is received.
   *
   * @param from SenderID of the sender.
   * @param data Data bundle containing message data as key/value pairs.
   *             For Set of keys use data.keySet().
   */
  @Override
  public void onMessageReceived(String from, Bundle data) {
    String message = data.getString("message", "");
    String className = data.getString("className", "");

    Log.d(LOG_TAG, "From: " + from);
    Log.d(LOG_TAG, "Message: " + message);
    Log.d(LOG_TAG, "ClassName: " + className);


  }
}