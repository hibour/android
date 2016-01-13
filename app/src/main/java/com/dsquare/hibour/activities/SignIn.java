package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONObject;

public class SignIn extends AppCompatActivity implements View.OnClickListener{

    private Button signInButton;
    private TextView signUpText,termsText;
    private EditText mailText,passText;
    private TextInputLayout mailLayout,passLayout;
    private ProgressDialog signInDialog;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private Typeface avenir;
    private Hibour application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initializeViews();
        initializeEventListeners();
    }
    /* initialize views*/
    private void initializeViews(){
        signInButton = (Button)findViewById(R.id.signin_button);
        signUpText = (TextView)findViewById(R.id.signin_signup_text);
        termsText = (TextView)findViewById(R.id.signin_terms);
        mailText = (EditText)findViewById(R.id.signin_email);
        passText = (EditText)findViewById(R.id.signin_password);
        mailLayout = (TextInputLayout)findViewById(R.id.signin_mail_inputlayout);
        passLayout = (TextInputLayout)findViewById(R.id.signin_password_inputlayout);
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);
        avenir = Typeface.createFromAsset(getAssets(),"fonts/AvenirLTStd-Book.otf");
        signInButton.setTypeface(avenir);
        mailLayout.setTypeface(avenir);
        passLayout.setTypeface(avenir);
        mailText.setTypeface(avenir);
        passText.setTypeface(avenir);
        application = Hibour.getInstance(this);
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        signInButton.setOnClickListener(this);
        termsText.setOnClickListener(this);
        signUpText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin_button:
                validateSignInData();
                break;
            case R.id.signin_signup_text:
                openSignUpActivity();
                break;
            case R.id.signin_terms:
                openTermsDialog();
                break;
        }
    }
    /* open signup activity*/
    private void openSignUpActivity(){
        Intent signUpIntent = new Intent(this,SignUp.class);
        startActivity(signUpIntent);
        this.finish();
    }
    /* open terms and conditions dialog*/
    private void openTermsDialog(){
        Intent termsIntent = new Intent(this,TermsAndConditions.class);
        startActivity(termsIntent);
    }
    /* open Home Activity*/
    private void openHomeActivity(){
        Intent homeIntent = new Intent(this,GovtProof.class);
        startActivity(homeIntent);
        this.finish();
    }
    /*validate signin data*/
    private void validateSignInData(){
        String mail="",pass="";
        mail = mailText.getText().toString();
        pass = passText.getText().toString();
        if(!mail.equals(null)&&!mail.equals("null")&&!mail.equals("")&&
                !pass.equals(null)&&!pass.equals("null")&&!pass.equals("")){
            if(application.validateEmail(mail)){
                openHomeActivity();
                //sendDataToServer(mail,pass,"normal");
            }
        }else{
            Toast.makeText(this,"Enter valid credentials",Toast.LENGTH_LONG).show();
        }
    }
    /* send sign in data to server*/
    private void sendDataToServer(String userName,String password,String signInType){
        if(networkDetector.isConnected()){
            signInDialog = ProgressDialog.show(this,""
                    ,getResources().getString(R.string.progress_dialog_text));
            accountsClient.signIn(userName,password,signInType,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseSignInDetails(jsonObject);
                    closeSignInDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("signin",error.toString());
                    closeSignInDialog();
                }
            });
        }else{

        }
    }
    /*parse signin details*/
    private void parseSignInDetails(JSONObject jsonObject){

    }
    /*close signin dialog*/
    private void closeSignInDialog(){
        if(signInDialog!=null){
            if(signInDialog.isShowing()){
                signInDialog.dismiss();
                signInDialog=null;
            }
        }
    }
}
