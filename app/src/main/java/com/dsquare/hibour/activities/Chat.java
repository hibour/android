package com.dsquare.hibour.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.ChatingAdapter;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.listener.MessageStateResultCallBack;
import com.dsquare.hibour.listener.ResultCallBack;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.pojos.message.UserStatus;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.UIHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;


public class Chat extends BaseActivity implements View.OnClickListener {
  private static final String LOG_TAG = Chat.class.getSimpleName();
  private ImageView backIcon;
  private RecyclerView chatRecycler;
  private List<UserMessage> chatList = new ArrayList<>();
  private ChatingAdapter chatAdapter;
  private Button sendMessage;
  private EditText userMessage;
  private TextView userName;
  private DatabaseHandler dbHandler;
  private TextView userStatus;
  private UIHelper uiHelper;
  private AccountsClient accountsClient;
  private UserDetail user;
  private Handler showStatusHandler;
  private WebServiceResponseCallback userDetailsResultCallback = new WebServiceResponseCallback() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
      try {
        Log.e(LOG_TAG, jsonObject.toString());
        user = new Gson().fromJson(jsonObject.getString("data"), UserDetail.class);
        dbHandler.insertUserDetails(user);
        updateUserUI();
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onFailure(VolleyError error) {

    }
  };
  private String secondUserId;

  private MessageStateResultCallBack<UserMessage> messageSendResultCallback = new MessageStateResultCallBack<UserMessage>() {
    @Override
    public void onResultCallBack(UserMessage message, int state, Exception e) {
      if (e == null) {
        message.messageState = state;
        dbHandler.insertUserMessage(message);
        for (UserMessage m : chatList) {
          if (m.local_id.getTime() == message.local_id.getTime()) {
            m.messageState = state;
            break;
          }
        }
        chatAdapter.notifyDataSetChanged();
      }
    }
  };
  private View.OnClickListener sendMessageListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (userMessage.getText().length() > 0) {
        UserMessage message = new UserMessage(new Date(), application.getUserId(), secondUserId + "", userMessage.getText().toString(), Constants.MESSAGE_SENDING);
        socializeClient.sendMessage(message, messageSendResultCallback);
        chatList.add(0, message);
        chatAdapter.notifyDataSetChanged();
        userMessage.setText("");
        chatRecycler.scrollToPosition(0);
        dbHandler.insertUserMessage(message);
      }
    }
  };
  private ResultCallBack<UserMessage> resendMessageResultCallback = new ResultCallBack<UserMessage>() {
    @Override
    public void onResultCallBack(UserMessage message, Exception e) {
      message.messageState = Constants.MESSAGE_SENDING;
      dbHandler.insertUserMessage(message);
      socializeClient.sendMessage(message, messageSendResultCallback);
      chatAdapter.notifyDataSetChanged();
    }
  };
  private Runnable showStatusRunnable;
  private boolean isShowingStatus = false;

  @Override
  protected void onResume() {
    super.onResume();
    EventBus.getDefault().registerSticky(this);
    refreshUserMessages();
  }

  private void refreshUserMessages() {
    if (chatList.size() == 0) {
      for (UserMessage message : dbHandler.getUserMessage(secondUserId + "", application.getUserId())) {
        chatList.add(message);
      }
      return;
    }
    List<UserMessage> temp = new ArrayList<>();
    for (UserMessage message : dbHandler.getUserMessage(secondUserId + "", application.getUserId())) {
      if (message.local_id.getTime() != chatList.get(0).local_id.getTime())
        temp.add(0, message);
      else
        break;
    }
    for (UserMessage message : temp)
      chatList.add(0, message);
    chatAdapter.notifyDataSetChanged();
  }

  @Override
  protected void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chating);
    dbHandler = new DatabaseHandler(this);
    uiHelper = new UIHelper(this);
    accountsClient = new AccountsClient(this);

    showStatusHandler = new Handler();

    secondUserId = getIntent().getExtras().getString(Constants.KEYWORD_USER_ID, "");

    initializeViews();
    initializeEventListeners();
  }

  public void showUserStatus(String status) {
    cancelShowStatusHandler();
    if (!isShowingStatus && !status.equalsIgnoreCase(userStatus.getText().toString())) {
      userStatus.setText(status);
      uiHelper.expand(userStatus);
      isShowingStatus = true;
    }
    showStatusRunnable = new Runnable() {
      @Override
      public void run() {
        showStatusRunnable = null;
        isShowingStatus = false;
        Log.e(LOG_TAG, "collapse");
        uiHelper.collapse(userStatus);
        userStatus.setText("");
      }
    };
    showStatusHandler.postDelayed(showStatusRunnable, Constants.CHECK_STATUS_INTERVAL);
  }

  private void cancelShowStatusHandler() {
    if (showStatusRunnable != null) {
      showStatusHandler.removeCallbacks(showStatusRunnable);
      showStatusRunnable = null;
    }
  }

  private void initializeViews() {
    backIcon = (ImageView) findViewById(R.id.chat_back_icon);
    chatRecycler = (RecyclerView) findViewById(R.id.chating_list);
    sendMessage = (Button) findViewById(R.id.chating_msg_send);
    userMessage = (EditText) findViewById(R.id.chating_message_edittest);
    userStatus = (TextView) findViewById(R.id.user_status);
    userName = (TextView) findViewById(R.id.chating_text_name);

    userMessage.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        markStatusTyping(secondUserId);
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setReverseLayout(true);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    chatRecycler.setLayoutManager(layoutManager);
    chatRecycler.setHasFixedSize(true);

    refreshUserMessages();
    user = dbHandler.getUserDetail(secondUserId);
    if (user == null) {
      Log.d("user null", "yes");
      accountsClient.getUserDetails(secondUserId, userDetailsResultCallback);
    } else
      updateUserUI();
    chatAdapter = new ChatingAdapter(this, chatList, resendMessageResultCallback);
    chatRecycler.setAdapter(chatAdapter);
  }

  private void updateUserUI() {
    userName.setText(user.Username);
  }

  private void initializeEventListeners() {
    backIcon.setOnClickListener(this);
    sendMessage.setOnClickListener(sendMessageListener);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.chat_back_icon:
        onBackPressed();
        break;
    }
  }

  public void onEvent(UserMessage message) {
    if (message != null) {
      if (message.fromUserID.equalsIgnoreCase(secondUserId + "")) {
        chatList.add(0, message);
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            chatAdapter.notifyDataSetChanged();
          }
        });
      }
    }
  }

  public void onEventMainThread(UserStatus status) {
    if (status != null) {
      if (status.fromUserId.equalsIgnoreCase(secondUserId)) {
        if (status.toUserId.equalsIgnoreCase(application.getUserId())) {
          showUserStatus("Typing...");
        } else {
          showUserStatus("Online");
        }
      }
      EventBus.getDefault().registerSticky(status);
    }
  }
}
