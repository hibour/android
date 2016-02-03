package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MobileNumber extends AppCompatActivity implements View.OnClickListener {

    private EditText mobile;
    private Button sumbit;
    RadioGroup gender,services;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog phoneDialog;
    private  Gson gson;
    private Hibour application;
    private String genderstring="",serviceString="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        initializeViews();
        initializeEventListeners();
    }
    private void initializeViews() {
        Typeface numbers = Typeface.createFromAsset(getAssets(),
                "fonts/pn_regular.otf");
        mobile = (EditText) findViewById(R.id.verification_phone_edit);
        mobile.setTypeface(numbers);
        sumbit = (Button) findViewById(R.id.phone_submit);
        sumbit.setTypeface(numbers);
        gender = (RadioGroup) findViewById(R.id.group_gender);
        services = (RadioGroup) findViewById(R.id.group_services);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton)findViewById(checkedId);
                String string= button.getText().toString();
                if(button.getText().toString().equals("Male")){
                    genderstring="0";
                }else {
                    genderstring="1";
                }
            }

        });
        services.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton)findViewById(checkedId);
                String string= button.getText().toString();
                if(button.getText().toString().equals("Personal")){
                    serviceString="0";
                }else {
                    serviceString="1";
                }
            }

        });
        accountsClient = new AccountsClient(this);
        networkDetector = new NetworkDetector(this);
        gson = new Gson();
        application = Hibour.getInstance(this);
    }
    private void initializeEventListeners() {
        sumbit.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.phone_submit:
                openOtpActivity();
                break;
        }
    }
    private void openOtpActivity() {
        if(mobile.getText().toString().length() < 11 && mobile.getText().toString().length() > 9){
            if (gender.getCheckedRadioButtonId() != -1) {
                if (services.getCheckedRadioButtonId() != -1) {
                    sendtoMobilenumUser();

                }else {
                    Toast.makeText(getApplicationContext(), "Please select Services", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
        }
    }
    /* mobile the user*/
    private void sendtoMobilenumUser(){
        if(networkDetector.isConnected()){
            phoneDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.mobilenumUser(application.getUserId(),serviceString,genderstring,mobile.getText().toString()
                    ,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parsemobileDetails(jsonObject);
                    closeMobileDialog();
                }
                @Override
                public void onFailure(VolleyError error) {
                    Log.d("signup", error.toString());
                    closeMobileDialog();
                }
            });
        }else{
            Toast.makeText(this, "Network not connected.", Toast.LENGTH_LONG).show();
        }
    }
    private void parsemobileDetails(JSONObject jsonObject){
        closeMobileDialog();
        int selected = services.getCheckedRadioButtonId();
        String serices_type = ((RadioButton) findViewById(selected)).getText().toString();
        Log.d("servicetype",serices_type);
        Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
        intent.putExtra("number", mobile.getText().toString());
        intent.putExtra("services",serices_type);
        startActivity(intent);
        finish();
    }
    /* close signup dialog*/
    private void closeMobileDialog(){
        if(phoneDialog!=null){
            if(phoneDialog.isShowing()){
                phoneDialog.dismiss();
                phoneDialog=null;
            }
        }
    }
}
