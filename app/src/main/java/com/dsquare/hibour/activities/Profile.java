package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    private TextView back,settings,interests,profileName;
    private ImageView profileImage;
    private Button sendMessageButton;
    private Gson gson;
    private NetworkDetector networkDetector;
    private String userId = "";
    private AccountsClient accountsClient;
    private ProgressDialog dialog;
    private Hibour application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeViews();
        initializeEventListeners();
    }
    /* initialize views*/
    private void initializeViews(){
        String id = getIntent().getStringExtra("userId");
        back = (TextView)findViewById(R.id.profile_back_text);
        application = Hibour.getInstance(this);
        settings = (TextView)findViewById(R.id.profile_settings_text);
        interests = (TextView)findViewById(R.id.profile_interests);
        profileName = (TextView)findViewById(R.id.profile_name);
        profileImage = (ImageView)findViewById(R.id.profile_image);
        sendMessageButton = (Button)findViewById(R.id.profile_send_msg_button);
        gson = new Gson();
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);
        if(!id.equals(null)&& !id.equals("null")){
            userId = id;
            if(userId.equals(application.getUserId()))
                sendMessageButton.setVisibility(View.INVISIBLE);
            getUserDetails(userId);
        }
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        back.setOnClickListener(this);
        settings.setOnClickListener(this);
        sendMessageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_back_text:
                this.finish();
                break;
            case R.id.profile_settings_text:
                break;
            case R.id.profile_send_msg_button:
                openChatScreen();
                break;
        }
    }
    /*open chat screen*/
    private void openChatScreen(){
        Intent chatIntent = new Intent(this,Chat.class);
        chatIntent.putExtra(Constants.KEYWORD_USER_ID,userId);
        chatIntent.putExtra(Constants.KEYWORD_USER_NAME,profileName.getText().toString());
        startActivity(chatIntent);
    }
    /* get user details*/
    private void getUserDetails(String userId){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(this,"","Please wait...");
            accountsClient.getOtherUserDetails(userId, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseUserDetails(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("error",error.toString());
                    closeDialog();
                }
            });
        }else{
            Toast.makeText(this,"Check network connectivity",Toast.LENGTH_LONG).show();
        }
    }
    /* parse user details*/
    private void parseUserDetails(JSONObject jsonObject){
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            profileName.setText(data.getString("name"));
            interests.setText(data.getString("preferences"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*close progress dialog*/
    private void closeDialog(){
        if(dialog!=null){
            if(dialog.isShowing()){
                dialog.dismiss();
                dialog = null;
            }
        }
    }
}
