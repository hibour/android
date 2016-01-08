package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button submitButton;
    private EditText name,email,password;
    private TextInputLayout inputLayoutName, inputLayoutemail,inputLayoutpassword;
    private TextView signInText,termsText;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog signUpDialog;
    private Hibour application;
    private Typeface avenir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeViews();
        initializeEventlisteners();
    }
    /* initialize views*/
    private void initializeViews(){
        submitButton = (Button)findViewById(R.id.signup_signup_button);
        name = (EditText)findViewById(R.id.signup_name);
        email= (EditText)findViewById(R.id.signup_email);
        password= (EditText)findViewById(R.id.signup_password);
        inputLayoutName=(TextInputLayout)findViewById(R.id.signup_name_inputlayout);
        inputLayoutemail=(TextInputLayout)findViewById(R.id.signup_mail_inputlayout);
        inputLayoutpassword=(TextInputLayout)findViewById(R.id.signup_password_inputlayout);
        signInText = (TextView)findViewById(R.id.signup_signin_text);
        termsText = (TextView)findViewById(R.id.signup_terms);
        accountsClient = new AccountsClient(this);
        networkDetector = new NetworkDetector(this);
        application = Hibour.getInstance(this);
        avenir = Typeface.createFromAsset(getAssets(),"fonts/AvenirLTStd-Book.otf");
        submitButton.setTypeface(avenir);
        name.setTypeface(avenir);
        email.setTypeface(avenir);
        password.setTypeface(avenir);
        inputLayoutName.setTypeface(avenir);
        inputLayoutemail.setTypeface(avenir);
        inputLayoutpassword.setTypeface(avenir);
    }

    /*initialize event listeners*/
    private void initializeEventlisteners(){
        submitButton.setOnClickListener(this);
        signInText.setOnClickListener(this);
        termsText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup_signup_button:
                validateData();
                break;
            case R.id.signup_signin_text:
                openSignInActivity();
                break;
            case R.id.signup_terms:
                openTermsDialog();
                break;
        }
    }
    /*open govt proof activity*/
    private void openProofActivity(){
        Intent proofIntent = new Intent(this,GovtProof.class);
        startActivity(proofIntent);
    }
    /* open sign in activity*/
    private void openSignInActivity(){
        Intent signInIntent = new Intent(this,SignIn.class);
        startActivity(signInIntent);
        this.finish();
    }
    /* open terms dialog*/
    private void openTermsDialog(){
        Intent termsDialog = new Intent(this,TermsAndConditions.class);
        startActivity(termsDialog);
    }
    /*validate data*/
    private void validateData(){
        String userName = name.getText().toString();
        String userMail = email.getText().toString();
        String userPass = password.getText().toString();
        if(!userName.equals(null)&&!userName.equals("null")&&!userName.equals("")&&
                !userMail.equals(null)&&!userMail.equals("null")&& ! userMail.equals("")&&
                !userPass.equals(null)&&!userPass.equals("null") && ! userPass.equals("")){
            if(application.validateEmail(userMail)){
                openProofActivity();
                //signUpUser(userName,userMail,userPass,"normal");
            }else{
                Toast.makeText(this,"Enter valid email",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Enter valid credentials",Toast.LENGTH_LONG).show();
        }
    }
    /* sign up the user*/
    private void signUpUser(String userName,String email,String password,String regType){
        if(networkDetector.isConnected()){
            signUpDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.signUpUser(userName,email,password,regType,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseSigUpDetails(jsonObject);
                    closeSignUpDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("signup", error.toString());
                    closeSignUpDialog();
                }
            });
        }else{
            Toast.makeText(this,"Network not connected.",Toast.LENGTH_LONG).show();
        }
    }
    /* parse sign up details*/
    private void parseSigUpDetails(JSONObject jsonObject){

    }
    /* close signup dialog*/
    private void closeSignUpDialog(){
        if(signUpDialog!=null){
            if(signUpDialog.isShowing()){
                signUpDialog.dismiss();
                signUpDialog=null;
            }
        }

    }
}
