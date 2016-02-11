package com.dsquare.hibour.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.network.NetworkDetector;
import com.google.gson.Gson;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    private TextView back,settings,interests,profileName;
    private ImageView profileImage;
    private Button sendMessageButton;
    private Gson gson;
    private NetworkDetector networkDetector;
    private String userId = "";
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
        if(!id.equals(null)&& !id.equals("null")){
            userId = id;
        }
        back = (TextView)findViewById(R.id.profile_back_text);
        settings = (TextView)findViewById(R.id.profile_settings_text);
        interests = (TextView)findViewById(R.id.profile_interests);
        profileName = (TextView)findViewById(R.id.profile_name);
        profileImage = (ImageView)findViewById(R.id.profile_image);
        sendMessageButton = (Button)findViewById(R.id.profile_send_msg_button);
        gson = new Gson();
        networkDetector = new NetworkDetector(this);
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
                break;
        }
    }
}
