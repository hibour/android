package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PreferencesAdapter;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.utils.GridLayoutSpacing;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SocialCategories extends AppCompatActivity implements View.OnClickListener{

    private Button doneButton,previous;
    private RecyclerView prefsRecycler;
    private List<String[]> prefsList = new ArrayList<>();
    private PreferencesAdapter adapter;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_categories);
        preparePrefs();
        initializeViews();
        initializeEventListeners();
    }

    /* initialize views*/
    private void initializeViews(){
        doneButton = (Button)findViewById(R.id.socialize_done_button);
        previous = (Button)findViewById(R.id.socialize_prev_button);
        prefsRecycler = (RecyclerView)findViewById(R.id.social_prefs_list);
        prefsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        prefsRecycler.addItemDecoration(new GridLayoutSpacing(2,5, true));
        prefsRecycler.setHasFixedSize(true);
        adapter = new PreferencesAdapter(this,prefsList);
        prefsRecycler.setAdapter(adapter);
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        doneButton.setOnClickListener(this);
        previous.setOnClickListener(this);
    }
    /* prepare prefs*/
    private void preparePrefs(){
        for(int i=0;i<10;i++){
            String[] data = new String[4];
            data[0] = "Social";
            data[1] = "";
            data[2] = "";
            data[3] = "false";
            prefsList.add(data);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.socialize_done_button:
                openHomeActivity();
                break;
            case R.id.socialize_prev_button:
                openPreviousActivity();
                break;
        }
    }
    /* open home activity*/
    private void openHomeActivity(){
        Intent homeIntent = new Intent(this,Home.class);
        startActivity(homeIntent);
    }
    private void openPreviousActivity(){
        Intent locationIntent = new Intent(this,ChooseLocation.class);
        startActivity(locationIntent);
        finish();
    }
    /* get all prefs*/
    private void getAllPrefs(){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.getAllSocialPrefs(new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseAllPrefs(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    closeDialog();
                }
            });
        }else{
            Toast.makeText(this,"Network connection error",Toast.LENGTH_LONG).show();
        }
    }
    /* insert prefs*/
    private void insertUserPrefs(String userId,String userPrefs){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.insertUserPrefs(userId,userPrefs,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseUserPrefs(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    closeDialog();
                }
            });
        }else{
            Toast.makeText(this,"Network connection error",Toast.LENGTH_LONG).show();
        }
    }
    /* parse all prefs*/
    private void parseAllPrefs(JSONObject jsonObject){

    }
    /* parse user prefs*/
    private void parseUserPrefs(JSONObject jsonObject){

    }
    /* close dialog*/
    private void closeDialog(){
        if(dialog!=null){
            if(dialog.isShowing()){
                dialog.dismiss();
                dialog=null;
            }
        }
    }
}
