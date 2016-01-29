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
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog phoneDialog;
    private  Gson gson;
    private Hibour application;
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
        mobile = (EditText) findViewById(R.id.phone_edit_text);
        mobile.setTypeface(numbers);
        sumbit = (Button) findViewById(R.id.phone_submit);
        sumbit.setTypeface(numbers);
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
            Intent intent = new Intent(getApplicationContext(),VerifyOtp.class);
            intent.putExtra("number",mobile.getText().toString());
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
        }
    }
    /* mobile the user*/
    private void sendtoMobilenumUser(){
        if(networkDetector.isConnected()){
            phoneDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.mobilenumUser(mobile.getText().toString()
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