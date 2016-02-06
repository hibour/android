package com.dsquare.hibour.gcm;

import android.os.Bundle;
import android.util.Log;

import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

import java.util.Date;

import de.greenrobot.event.EventBus;
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
        String messageText = data.getString("message", "");

        Log.d(LOG_TAG, "From: " + from);
        Log.d(LOG_TAG, "Message: " + messageText);

        try {
            JSONObject jsonMessage = new JSONObject(messageText);
            if (jsonMessage.getString(Constants.GCM_FIELDS_MESSAGE_TYPE).equalsIgnoreCase("message")) {
                String receiverId = jsonMessage.getString(Constants.GCM_FIELDS_RECEIVER_ID);
                String senderId = jsonMessage.getString(Constants.GCM_FIELDS_SENDER_ID);
                String userMessage = jsonMessage.getString(Constants.GCM_FIELDS_MESSAGE);

                if (Hibour.getInstance(getApplicationContext()).getUserId().equalsIgnoreCase(receiverId)) {
                    DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
                    UserMessage message = new UserMessage(new Date(), senderId, receiverId, userMessage,
                            Constants.MESSAGE_RECEIVED);
                    dbHandler.insertUserMessage(message);
                    EventBus.getDefault().post(message);
                }

            }
        } catch (Exception e) {
        }

    }
}