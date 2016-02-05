package com.dsquare.hibour.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dsquare.hibour.R;

public class VerifyOtp extends AppCompatActivity implements View.OnClickListener {

    Button sumbit;
    EditText enterOtp;
    TextView welcome1, welcome2, resend, change;
    private String mobileNo, otp,servicertype;
    private Intent data;
    private IntentFilter filter;
    private BroadcastReceiver otpReceiver;
    private boolean otpStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        Button submitButton = (Button)findViewById(R.id.otp_next);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SocialCategories.class);
                startActivity(intent);
            }
        });
        TextView back = (TextView)findViewById(R.id.otp_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MobileNumber.class);
                startActivity(intent);
            }
        });
//        data = getIntent();
//        initializeViews();
//        initializeEventListeners();
    }

    private void initializeViews() {
        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        Typeface numbers = Typeface.createFromAsset(getAssets(),
                "fonts/pn_regular.otf");
        sumbit = (Button) findViewById(R.id.otp_sumbit);
        sumbit.setTypeface(numbers);
        enterOtp = (EditText) findViewById(R.id.otp_edittest);
//        otp.setTypeface(numbers);
        resend = (TextView) findViewById(R.id.otp_resend);
        change = (TextView) findViewById(R.id.otp_change_number);
        welcome1 = (TextView) findViewById(R.id.otp_welcome1);
        welcome2 = (TextView) findViewById(R.id.otp_welcome2);
        welcome2.setText(welcome2.getText().toString() + " " + data.getStringExtra("number"));
        String text = "<u>Resend</u>";
        resend.setText(Html.fromHtml(text));
        mobileNo = data.getExtras().getString("number");
        servicertype = data.getExtras().getString("services");
        otp = data.getExtras().getString("otp");
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
                    // Toast.makeText(OtpActivity.this, strMessage, Toast.LENGTH_SHORT).show();
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
        change.setOnClickListener(this);
        resend.setOnClickListener(this);
        sumbit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.otp_sumbit:
                openSocilizeactivity();
                break;
            case R.id.otp_change_number:
                openMobileactivity();
                break;
            case R.id.otp_resend:
                break;
        }
    }

    private void openMobileactivity() {
        Intent intent = new Intent(getApplicationContext(), MobileNumber.class);
        startActivity(intent);
        finish();
    }

    private void openSocilizeactivity() {
        if (servicertype.equals("Business")){
            Intent intent = new Intent(getApplicationContext(), BusinessServices.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(getApplicationContext(), SocialCategories.class);
            startActivity(intent);
            finish();
        }
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
            if(otpStatus==false) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(otpReceiver);
    }
}
