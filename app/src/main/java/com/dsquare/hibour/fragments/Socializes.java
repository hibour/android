package com.dsquare.hibour.fragments;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.PreferencesViews;
import com.dsquare.hibour.adapters.PreferencesAdapter;
import com.dsquare.hibour.adapters.SocializeAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.SocializeClient;
import com.dsquare.hibour.pojos.Socialize.ChoosedUser;
import com.dsquare.hibour.pojos.Socialize.Data;
import com.dsquare.hibour.pojos.preference.Datum;
import com.dsquare.hibour.pojos.preference.Preference;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.GridLayoutSpacing;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dsquare Android on 1/22/2016.
 */
public class Socializes extends android.support.v4.app.Fragment implements View.OnClickListener {
    private ImageView menuIcon,notifIcon;
    private Button doneButton,previous;
    private RecyclerView prefsRecycler;
    private List<String[]> prefsList = new ArrayList<>();
    private PreferencesAdapter adapter;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog dialog;
    private Gson gson;
    private Hibour application;
    private NavDrawerCallback callback;
    private SocializeClient socialClient;
    public Socializes() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_socilize, container, false);
        initializeViews(view);
        initializeEventListeners();
        getMembers(application.getUserId());
        //getAllPrefs();
        return view;
    }
    /* initialize views*/
    private void initializeViews(View view){
        menuIcon = (ImageView)view.findViewById(R.id.socailize_menu_icon);
        notifIcon = (ImageView)view.findViewById(R.id.socailize_search_icon);
        doneButton = (Button)view.findViewById(R.id.socialize_done_button);
        previous = (Button)view.findViewById(R.id.socialize_prev_button);
        accountsClient = new AccountsClient(getActivity());
        networkDetector = new NetworkDetector(getActivity());
        socialClient = new SocializeClient(getActivity());
        gson = new Gson();
        application = Hibour.getInstance(getActivity());
        prefsRecycler = (RecyclerView)view.findViewById(R.id.social_prefs_list);
        prefsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        prefsRecycler.addItemDecoration(new GridLayoutSpacing(3, 5, true));
        prefsRecycler.setHasFixedSize(true);
//        adapter = new PreferencesAdapter(this,prefsList);
//        prefsRecycler.setAdapter(adapter);

    }
    /* initialize event listeners*/
    private void initializeEventListeners(){

        menuIcon.setOnClickListener(this);
        notifIcon.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        previous.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.socailize_menu_icon:
                callback.drawerOpen();
               break;
            case R.id.socailize_search_icon:
                break;
            case R.id.socialize_done_button:
                //openHomeActivity();
                openPreviousActivity();
                break;
            case R.id.socialize_prev_button:
                openPreviousActivity();
                break;
        }
    }
    /* open home activity*/
    private void openHomeActivity(){
        Intent homeIntent = new Intent(getActivity(), PreferencesViews.class);
        startActivity(homeIntent);
    }
    private void openPreviousActivity(){
        Intent locationIntent = new Intent(getActivity(), PreferencesViews.class);
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
           // setAdapters(prefsList);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    /* parse user prefs*/
    private void parseUserPrefs(JSONObject jsonObject){

    }

    private void setAdapters(){
        for(String s:Constants.socialPrefsMap.keySet()){
            String[] details = {Constants.socialPrefsMap.get(s).get(0)
                    , Constants.socialPrefsMap.get(s).get(1)
                    , Constants.socialPrefsMap.get(s).get(2)
                    , Constants.socialPrefsMap.get(s).get(3)
                    ,Constants.socialPrefsMap.get(s).get(4)
                    ,Constants.socialPrefsMap.get(s).get(5)};
            prefsList.add(details);
        }
        prefsRecycler.setAdapter(new SocializeAdapter(getActivity(),prefsList));
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
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }
    /* get socialize pref members*/
    private void getMembers(String userId){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(getActivity(),"","Please Wait...");
            socialClient.getNeighbours(userId,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseSocialData(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    closeDialog();
                    Log.d("error",error.toString());
                }
            });
        }else{

        }
    }
    /* parse social data*/
    private void parseSocialData(JSONObject jsonObject){
        try {
            Data socialize = gson.fromJson(jsonObject.toString(),Data.class);
            List<com.dsquare.hibour.pojos.Socialize.Datum> data = socialize.getData();
            Constants.socialPrefsList.clear();
            for(com.dsquare.hibour.pojos.Socialize.Datum d:data){
                Constants.socialPrefsList.add(d);
                List<String> dd = new ArrayList<>();
                dd.add(d.getPreferenceId());
                dd.add(d.getPreferenceName());
                dd.add(d.getPreferenceImage1());
                dd.add(d.getPreferenceImage2());
                dd.add(d.getIsUserSelected());
                dd.add(d.getChoosedUsers().size()+"");
                Constants.socialPrefsMap.put(d.getPreferenceId(),dd);
                Constants.prefsMap.put(d.getPreferenceId(),d);
                for(ChoosedUser ch:d.getChoosedUsers()){
                    Constants.membersList.add(ch);
                }
            }
            setAdapters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
