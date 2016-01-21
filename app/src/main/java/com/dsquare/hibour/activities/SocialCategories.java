package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PreferencesAdapter;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.preference.Datum;
import com.dsquare.hibour.pojos.preference.Preference;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.GridLayoutSpacing;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
    private Gson gson;
    private Hibour application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_categories);
        initializeViews();
        initializeEventListeners();
        getAllPrefs();
    }

    /* initialize views*/
    private void initializeViews(){
        accountsClient = new AccountsClient(this);
        networkDetector = new NetworkDetector(this);
        gson = new Gson();
        application = Hibour.getInstance(this);
        doneButton = (Button)findViewById(R.id.socialize_done_button);
        previous = (Button)findViewById(R.id.socialize_prev_button);
        prefsRecycler = (RecyclerView)findViewById(R.id.social_prefs_list);
        prefsRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        prefsRecycler.addItemDecoration(new GridLayoutSpacing(3, 5, true));
        prefsRecycler.setHasFixedSize(true);
//        adapter = new PreferencesAdapter(this,prefsList);
//        prefsRecycler.setAdapter(adapter);

    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        doneButton.setOnClickListener(this);
        previous.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.socialize_done_button:
                //openHomeActivity();
                sendUserPrefs();
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
        Intent locationIntent = new Intent(this,Home.class);
        startActivity(locationIntent);
        finish();
    }
    /*send user prefs to server*/
    private void sendUserPrefs(){
        String userPrefs = "";
        for(String pref: Constants.prefernceMap.keySet()){
            userPrefs = userPrefs+","+pref;
        }
        userPrefs = userPrefs.substring(1,userPrefs.length());
        Log.d("userprefs",userPrefs);
        if(userPrefs.equals("")){
            openHomeActivity();
        }else{
            insertUserPrefs(application.getUserId(),userPrefs);
        }
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
            accountsClient.insertUserPrefs(userId, userPrefs, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseUserPrefs(jsonObject);
                    closeDialog();
                    openHomeActivity();
                }

                @Override
                public void onFailure(VolleyError error) {
                    closeDialog();
                    Log.d("prefserror",error.toString());
                }
            });
        }else{
            Toast.makeText(this,"Network connection error",Toast.LENGTH_LONG).show();
        }
    }
    /* parse all prefs*/
    private void parseAllPrefs(JSONObject jsonObject){
        Log.d("data",jsonObject.toString());
        try {
            Preference preference = gson.fromJson(jsonObject.toString(), Preference.class);
            List<Datum> data = preference.getData();

            for(int i=0;i<data.size();i++){
                String[] details = {String.valueOf(data.get(i).getId())
                        , data.get(i).getPreferencesname()
                        , data.get(i).getImage1()
                        , data.get(i).getImage2(),"false"};
                prefsList.add(details);
            }
            setAdapters(prefsList);
            } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    /* parse user prefs*/
    private void parseUserPrefs(JSONObject jsonObject){

    }

    private void setAdapters(List<String[]> prefsList){
        prefsRecycler.setAdapter(new PreferencesAdapter(this,prefsList));
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
