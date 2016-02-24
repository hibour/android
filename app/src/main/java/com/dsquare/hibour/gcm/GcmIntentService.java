package com.dsquare.hibour.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.dsquare.hibour.activities.Chat;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.helper.NotificationHelper;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import de.greenrobot.event.EventBus;

public class GcmIntentService extends GcmListenerService {

  private static final String LOG_TAG = GcmIntentService.class.getSimpleName();
  private DatabaseHandler dbHandler;
  private NotificationHelper notificationHelper;
  private String userMessage;
  private WebServiceResponseCallback userDetailsResultCallback = new WebServiceResponseCallback() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
      try {
        UserDetail user = new Gson().fromJson(jsonObject.getString("data"), UserDetail.class);
        dbHandler.insertUserDetails(user);
        if (!notificationHelper.isAppOnForeground()) {
          createUserMessageNotification(user);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onFailure(VolleyError error) {
    }
  };

  /**
   * Called when message is received.
   *
   * @param from SenderID of the sender.
   * @param data Data bundle containing message data as key/value pairs.
   *             For Set of keys use data.keySet().
   */
  @Override
  public void onMessageReceived(String from, Bundle data) {
    notificationHelper = new NotificationHelper(getApplicationContext());
    dbHandler = new DatabaseHandler(getApplicationContext());
    String messageText = data.getString("message", "");

    Log.e(LOG_TAG, "From: " + from);
    Log.e(LOG_TAG, "Message: " + messageText);

    try {
      JSONObject jsonMessage = new JSONObject(messageText);
      if (jsonMessage.getString(Constants.GCM_FIELDS_MESSAGE_TYPE).equalsIgnoreCase("message")) {
        String receiverId = jsonMessage.getString(Constants.GCM_FIELDS_RECEIVER_ID);
        String senderId = jsonMessage.getString(Constants.GCM_FIELDS_SENDER_ID);
        userMessage = jsonMessage.getString(Constants.GCM_FIELDS_MESSAGE);

        if (Hibour.getInstance(getApplicationContext()).getUserId().equalsIgnoreCase(receiverId)) {
          UserMessage message = new UserMessage(new Date(), senderId, receiverId, userMessage,
              Constants.MESSAGE_RECEIVED);
          dbHandler.insertUserMessage(message);
          EventBus.getDefault().post(message);
          Log.e(LOG_TAG, "AppStatus:" + notificationHelper.isAppOnForeground());
          UserDetail user = dbHandler.getUserDetail(senderId);
          if (user != null) {
            if (!notificationHelper.isAppOnForeground()) {
              createUserMessageNotification(user);
            }
          } else {
            new AccountsClient(this).getUserDetails(senderId, userDetailsResultCallback);
          }
        }
      }
    } catch (Exception e) {
      Log.e(LOG_TAG, Log.getStackTraceString(e));
    }

  }

  private void createUserMessageNotification(UserDetail user) {
    Log.e(LOG_TAG, "Creating Notification");
    Intent intent = new Intent(getApplicationContext(), Chat.class);
    Bundle data = new Bundle();
    data.putString(Constants.KEYWORD_USER_ID, user.id);
    intent.putExtras(data);
    notificationHelper.createNotification(user, userMessage, Constants.NOTIFICATION_ID_MESSAGE, intent);
  }
}