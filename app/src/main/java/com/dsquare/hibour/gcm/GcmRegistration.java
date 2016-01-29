package com.dsquare.hibour.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dsquare.hibour.R;
import com.dsquare.hibour.pojos.gcm.UserDevice;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Helper;
import com.dsquare.hibour.utils.Hibour;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * register the current user with the server to receive GCM notification.
 */
public class GcmRegistration extends IntentService {
  public static final String LOG_TAG = GcmRegistration.class.getSimpleName();
  private static final String[] TOPICS = {"global"};

  public GcmRegistration() {
    super(LOG_TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    try {
      synchronized (LOG_TAG) {
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        Log.i(LOG_TAG, "GCM Registration Token: " + token);

        sendRegistrationToServer(token);

        // Subscribe to topic channels
        subscribeTopics(token);

        sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, true).apply();
      }
    } catch (Exception e) {
      Log.d(LOG_TAG, "Failed to complete token refresh", e);
    }
  }

  /**
   * Persist registration to third-party servers.
   * <p>
   * Modify this method to associate the user's GCM registration token with any server-side account
   * maintained by your application.
   *
   * @param token The new token.
   */
  // TODO: Implement this method to send any registration to your app's servers.
  private void sendRegistrationToServer(String token) {
    // Add custom implementation, as needed.
    Hibour.getInstance(this).setGCMToken(token);
    UserDevice userDevice = new UserDevice();
    userDevice.gcm_client_id = token;
    userDevice.device_id = new Helper(getBaseContext()).getDeviceId();
    Log.e("token", token);
  }

  private void subscribeTopics(String token) throws IOException {
    for (String topic : TOPICS) {
      GcmPubSub pubSub = GcmPubSub.getInstance(this);
      pubSub.subscribe(token, "/topics/" + topic, null);
    }
  }
}
