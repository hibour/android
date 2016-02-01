package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;

import org.json.JSONObject;

public class TermsAndConditions extends AppCompatActivity implements View.OnClickListener {

    private TextView termsText;
    private ImageView closeTerms;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog termsDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        initializeViews();
        initializeEventListeners();
    }
    /* initialize Views*/
    private void initializeViews(){
        termsText = (TextView)findViewById(R.id.terms_text);
        closeTerms = (ImageView)findViewById(R.id.terms_close_icon);
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);
    }
    /* initialize Event listeners*/
    private void initializeEventListeners(){
        closeTerms.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        this.finish();
    }
    /* get terms and condition from server*/
    private void getTerms(){
        if(networkDetector.isConnected()){
            termsDialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.getTermsAndConditions(new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseTermsDetails(jsonObject);
                    closeTermsDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("terms", error.toString());
                    closeTermsDialog();
                }
            });
        }else{
            Toast.makeText(this,"Check network connectivity",Toast.LENGTH_LONG).show();
        }
    }
    /* [arse terms and conditions details*/
    private void parseTermsDetails(JSONObject jsonObject){

    }
    /* close term dialog*/
    private void closeTermsDialog(){
        if(termsDialog!=null){
            if(termsDialog.isShowing()){
                termsDialog.dismiss();
                termsDialog=null;
            }
        }
    }
}
