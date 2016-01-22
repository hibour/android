package com.dsquare.hibour.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Dsquare Android on 1/22/2016.
 */
public class Socialize extends android.support.v4.app.Fragment implements View.OnClickListener {

    private Button doneButton,previous;
    private RecyclerView prefsRecycler;
    private List<String[]> prefsList = new ArrayList<>();
    private PreferencesAdapter adapter;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog dialog;
    private Gson gson;
    private Hibour application;

    public Socialize() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragments_socailizes, container, false);
        initializeViews(view);
        initializeEventListeners();
        getAllPrefs();
        return view;
    }
    /* initialize views*/
    private void initializeViews(View view){
        accountsClient = new AccountsClient(getActivity());
        networkDetector = new NetworkDetector(getActivity());
        gson = new Gson();
        application = Hibour.getInstance(getActivity());
//        doneButton = (Button)view.findViewById(R.id.socialize_done_button);
//        previous = (Button)view.findViewById(R.id.socialize_prev_button);
        prefsRecycler = (RecyclerView)view.findViewById(R.id.social_prefs_list);
        prefsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        prefsRecycler.addItemDecoration(new GridLayoutSpacing(3, 5, true));
        prefsRecycler.setHasFixedSize(true);
//        adapter = new PreferencesAdapter(this,prefsList);
//        prefsRecycler.setAdapter(adapter);

    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
//        doneButton.setOnClickListener(this);
//        previous.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
//            case R.id.socialize_done_button:
//                //openHomeActivity();
//                sendUserPrefs();
//                break;
//            case R.id.socialize_prev_button:
//                openPreviousActivity();
//                break;
        }
    }
    /* open home activity*/
    private void openHomeActivity(){
        Intent homeIntent = new Intent(getActivity(), com.dsquare.hibour.activities.Home.class);
        startActivity(homeIntent);
    }
    private void openPreviousActivity(){
        Intent locationIntent = new Intent(getActivity(), com.dsquare.hibour.activities.Home.class);
        startActivity(locationIntent);

    }
    /*send user prefs to server*/
    private void sendUserPrefs(){
        String userPrefs = "";
        for(String pref: Constants.prefernceMap.keySet()){
            userPrefs = userPrefs+","+pref;
        }
        userPrefs = userPrefs.substring(1,userPrefs.length());
        Log.d("userprefs", userPrefs);
        if(userPrefs.equals("")){
            openHomeActivity();
        }else{
            insertUserPrefs(application.getUserId(),userPrefs);
        }
    }
    /* get all prefs*/
    private void getAllPrefs(){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(getActivity(),"",getResources()
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
            Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_LONG).show();
        }
    }
    /* insert prefs*/
    private void insertUserPrefs(String userId,String userPrefs){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(getActivity(),"",getResources()
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
            Toast.makeText(getActivity(),"Network connection error",Toast.LENGTH_LONG).show();
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
        prefsRecycler.setAdapter(new PreferencesAdapter(getActivity(),prefsList));
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
