package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class VerifyOtp extends AppCompatActivity implements View.OnClickListener {

    Button sumbit;
    EditText enterOtp;
    TextView welcome1, welcome2, resend, change, back;
    private String mobileNo, otp,servicertype;
    private Intent data;
    private IntentFilter filter;
    private BroadcastReceiver otpReceiver;
    private boolean otpStatus = false;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog phoneDialog;
    private Gson gson;
    private Hibour application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        data = getIntent();
        initializeViews();
        initializeEventListeners();
    }

    private void initializeViews() {
        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        Typeface numbers = Typeface.createFromAsset(getAssets(),
                "fonts/pn_regular.otf");
        sumbit = (Button) findViewById(R.id.otp_next);
        sumbit.setTypeface(numbers);
        enterOtp = (EditText) findViewById(R.id.otp_edittest);
//        otp.setTypeface(numbers);
        resend = (TextView) findViewById(R.id.otp_resend);
        back = (TextView) findViewById(R.id.otp_back);
//        enterOtp.setText(data.getExtras().getString("otp"));
        accountsClient = new AccountsClient(this);
        networkDetector = new NetworkDetector(this);
        gson = new Gson();
        application = Hibour.getInstance(this);
        otpReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context c, Intent intent) {
                Log.d("intent","braodcast receiver");
                // setOtpText(i.getStringExtra("otp"));
                Bundle myBundle = intent.getExtras();
                SmsMessage[] messages = null;
                String strMessage = "";
                String from="";

                if (myBundle != null) {
                    Object [] pdus = (Object[]) myBundle.get("pdus");
                    messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        strMessage += "SMS From: " + messages[i].getOriginatingAddress();
                        strMessage += " : ";
                        strMessage += messages[i].getMessageBody();
                        strMessage += "\n";
                        from = messages[i].getOriginatingAddress();
                    }
//                    Toast.makeText(OtpActivity.this, strMessage, Toast.LENGTH_SHORT).show();
                    Log.d("message", strMessage);
                    Log.d("sb",from);
                    if(strMessage.contains("MD-ONLYHT")){
                        Log.d("otp","in if");
                        String o=getOtpFromString(strMessage);
                        Log.d("otp",o);
                        setOtpText(o);
                    }
                }
            }
        };
        this.registerReceiver(otpReceiver, filter);
    }

    private void initializeEventListeners() {
        back.setOnClickListener(this);
        resend.setOnClickListener(this);
        sumbit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.otp_next:
                openSocilizeactivity();
                break;
            case R.id.otp_back:
                openMobileactivity();
                break;
            case R.id.otp_resend:
                sendtoMobilenumUser();
                break;
        }
    }

    private void openMobileactivity() {
        Intent intent = new Intent(getApplicationContext(), MobileNumber.class);
        startActivity(intent);
        finish();
    }

    private void openSocilizeactivity() {
            Intent intent = new Intent(getApplicationContext(), SocialCategories.class);
            startActivity(intent);
            finish();
    }

    private void verifyReceivedOTP() {
        if (mobileNo != null && otp != null) {
            if (!(enterOtp.getText().toString().equals(null))) {
                if (otp.equals(enterOtp.getText().toString())) {

                    Intent intent = new Intent(this, SocialCategories.class);
                    startActivity(intent);
                    this.finish();
                } else {
                    Toast.makeText(this, "Check otp", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "Please Check  OTP", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("null", "null");
        }
    }
    private void setOtpText(String ot){
        Log.d("otp",ot);
        Log.d("otp",otp);
        if(ot.contains(otp)){
            Log.d("otp","in if of ot");
            if (otpStatus == false) {
                otpStatus = true;
                enterOtp.setText(otp);
                Log.d("otp", "text setted");
                verifyReceivedOTP();
            }
        }
    }

    private String getOtpFromString(String otpString){
        String otp = "";
        if(otpString.contains("?")){
            String first = otpString.substring(otpString.lastIndexOf("?"),otpString.length()-1);
            otp = first.substring(1,first.indexOf(" "));
        }
        return otp;
    }

    /* mobile the user*/
    private void sendtoMobilenumUser() {
        if (networkDetector.isConnected()) {
            phoneDialog = ProgressDialog.show(this, "", getResources()
                .getString(R.string.progress_dialog_text));
            accountsClient.mobilenumUser(application.getUserId(), data.getExtras().getString("number")
                , new WebServiceResponseCallback() {
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
        } else {
            Toast.makeText(this, "Network not connected.", Toast.LENGTH_LONG).show();
        }
    }

    private void parsemobileDetails(JSONObject jsonObject) {
        closeMobileDialog();
        Log.d("json", jsonObject.toString());
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            String number = data.getString("number");
            String otp = data.getString("otp");
            enterOtp.setText(otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* close signup dialog*/
    private void closeMobileDialog() {
        if (phoneDialog != null) {
            if (phoneDialog.isShowing()) {
                phoneDialog.dismiss();
                phoneDialog = null;
            }
        }
    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        this.unregisterReceiver(otpReceiver);
//    }
}
