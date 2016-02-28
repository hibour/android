package com.dsquare.hibour.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dsquare.hibour.listener.ResultCallBack;
import com.dsquare.hibour.network.SocializeClient;
import com.dsquare.hibour.pojos.message.UserStatus;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;

public class BaseActivity extends AppCompatActivity {

  private static final String LOG_TAG = BaseActivity.class.getSimpleName();
  public Hibour application;
  public SocializeClient socializeClient;
  private Handler typingHandler;
  private Runnable typingRunnable;
  private boolean isTyping = false;
  private Runnable statusUpdateRunnable;
  private Handler sendStatusHandler;
  private String toUser;
  private ResultCallBack statusResultCallback = new ResultCallBack() {
    @Override
    public void onResultCallBack(Object object, Exception e) {
      if (statusUpdateRunnable != null) {
        startSendUserStatus();
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    typingHandler = new Handler();
    sendStatusHandler = new Handler();
    application = Hibour.getInstance(this);
    socializeClient = new SocializeClient(this);
  }

  public void startSendUserStatus() {
    statusUpdateRunnable = new Runnable() {
      @Override
      public void run() {
        UserStatus userStatus = new UserStatus();
        userStatus.fromUserId = application.getUserId();
        if (isTyping) {
          userStatus.toUserId = toUser;
          userStatus.status = Constants.USER_STATUS_TYPING;
        } else {
          userStatus.status = Constants.USER_STATUS_ONLINE;
        }
        Log.e(LOG_TAG, "Sending:" + userStatus.status);
        socializeClient.sendUserStatus(userStatus, statusResultCallback);
//        startSendUserStatus();
      }
    };
    sendStatusHandler.postDelayed(statusUpdateRunnable, Constants.SEND_STATUS_INTERVAL);
  }

  public void stopSendUserStatus() {
    if (statusUpdateRunnable != null) {
      sendStatusHandler.removeCallbacks(statusUpdateRunnable);
      statusUpdateRunnable = null;
    }

  }

  public void markStatusTyping(final String toUser) {
    isTyping = true;
    this.toUser = toUser;
    cancelStatusHandler();
    typingRunnable = new Runnable() {
      @Override
      public void run() {
        typingRunnable = null;
        isTyping = false;
        BaseActivity.this.toUser = null;
      }
    };
    typingHandler.postDelayed(typingRunnable, Constants.MARK_TYPING_INTERVAL);
  }

  private void cancelStatusHandler() {
    if (typingRunnable != null) {
      typingHandler.removeCallbacks(typingRunnable);
      typingRunnable = null;
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    stopSendUserStatus();
  }

  @Override
  protected void onResume() {
    super.onResume();
    startSendUserStatus();
  }
}
