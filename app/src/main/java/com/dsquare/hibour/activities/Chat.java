package com.dsquare.hibour.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.UIHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Chat extends AppCompatActivity implements View.OnClickListener {
  private static final String LOG_TAG = Chat.class.getSimpleName();
  private ImageView backIcon;
  private RecyclerView chatRecycler;
  private List<UserMessage> chatList = new ArrayList<>();
  private ChatingAdapter chatAdapter;
  private Button sendMessage;
  private EditText userMessage;
  private DatabaseHandler dbHandler;
  private TextView userStatus;
  private UIHelper uiHelper;
  private AccountsClient accountsClient;
  private Hibour application;
  private UserDetail user;
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
  private int secondUserId;
  private View.OnClickListener sendMessageListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (userMessage.getText().length() > 0) {
        UserMessage message = new UserMessage(application.getUserId(), secondUserId + "", userMessage.getText().toString());
        chatList.add(0, message);
        chatAdapter.notifyDataSetChanged();
        userMessage.setText("");
        chatRecycler.scrollToPosition(0);
        dbHandler.insertUserMessage(message);
      }
      if (userStatus.getVisibility() == View.VISIBLE)
        hideUserStatus();
      else
        showUserStatus();
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    showUserStatus();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chating);
    dbHandler = new DatabaseHandler(this);
    uiHelper = new UIHelper(this);
    accountsClient = new AccountsClient(this);
    application = Hibour.getInstance(this);

    secondUserId = getIntent().getExtras().getInt(Constants.KEYWORD_USER_ID, 0);

    initializeViews();
    initializeEventListeners();
  }

  public void showUserStatus() {
    uiHelper.zoomInView(userStatus);
  }

  public void hideUserStatus() {
    uiHelper.zoomOutView(userStatus);
  }

  private void initializeViews() {
    backIcon = (ImageView) findViewById(R.id.chat_back_icon);
    chatRecycler = (RecyclerView) findViewById(R.id.chating_list);
    sendMessage = (Button) findViewById(R.id.chating_msg_send);
    userMessage = (EditText) findViewById(R.id.chating_message_edittest);
    userStatus = (TextView) findViewById(R.id.user_status);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setReverseLayout(true);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    chatRecycler.setLayoutManager(layoutManager);
    chatRecycler.setHasFixedSize(true);

    for (UserMessage message : dbHandler.getUserMessage(secondUserId + "", application.getUserId())) {
      chatList.add(message);
    }
    user = dbHandler.getUserDetail(secondUserId);
    if (user == null)
      accountsClient.getUserDetails(secondUserId + "", userDetailsResultCallback);
    else
      updateUserUI();
    chatAdapter = new ChatingAdapter(this, chatList);
    chatRecycler.setAdapter(chatAdapter);
  }

  private void updateUserUI() {

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
}
