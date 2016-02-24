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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MobileNumber extends AppCompatActivity implements View.OnClickListener {

    RadioGroup gender, services;
    private EditText mobile;
    private Button sumbit;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog phoneDialog;
    private TextView back;
    private  Gson gson;
    private Hibour application;
    private String genderstring = "", serviceString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilenumber);
        initializeViews();
        initializeEventListeners();
    }
    private void initializeViews() {
        Typeface numbers = Typeface.createFromAsset(getAssets(),
                "fonts/pn_regular.otf");
        mobile = (EditText) findViewById(R.id.mobile_edit);
        mobile.setTypeface(numbers);
        sumbit = (Button) findViewById(R.id.moblie_send);
        sumbit.setTypeface(numbers);
        back = (TextView) findViewById(R.id.mobile_back);
        accountsClient = new AccountsClient(this);
        networkDetector = new NetworkDetector(this);
        gson = new Gson();
        application = Hibour.getInstance(this);
    }
    private void initializeEventListeners() {
        sumbit.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.moblie_send:
                openOtpActivity();
                break;
            case R.id.mobile_back:
                openbackActivity();
                break;
        }
    }

    private void openbackActivity() {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
        finish();
    }

    private void openOtpActivity() {
        if (mobile.getText().toString().length() < 11 && mobile.getText().toString().length() > 9) {
            sendtoMobilenumUser();
        } else {
            Toast.makeText(this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
        }
    }
    /* mobile the user*/
    private void sendtoMobilenumUser(){
        if(networkDetector.isConnected()){
            phoneDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.mobilenumUser(application.getUserId(), mobile.getText().toString()
                    ,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    //      parsemobileDetails(jsonObject);
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
        Log.d("json", jsonObject.toString());
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            String number = data.getString("number");
            String otp = data.getString("otp");
            if (!otp.equals(null) && !otp.equals("null") && !otp.equals("")) {
                //   Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("number", number);
                intent.putExtra("otp", otp);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
