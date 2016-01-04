package com.dsquare.hibour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dsquare.hibour.R;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button submitButton;
    private EditText name,email,password;
    private TextInputLayout inputLayoutName, inputLayoutemail,inputLayoutpassword;
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

        name.addTextChangedListener(new MyTextWatcher(name));
        email.addTextChangedListener(new MyTextWatcher(email));
        password.addTextChangedListener(new MyTextWatcher(password));
    }

    /*initialize event listeners*/
    private void initializeEventlisteners(){
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup_signup_button:
                openProofActivity();
                break;
        }
    }
    /*open govt proof activity*/
    private void openProofActivity(){
        Intent proofIntent = new Intent(this,GovtProof.class);
        startActivity(proofIntent);
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

        }
    }
}
