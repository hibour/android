package com.dsquare.hibour.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.PreferencesViews;
import com.dsquare.hibour.adapters.PreferencesAdapter;
import com.dsquare.hibour.adapters.SocializeAdapter;
import com.dsquare.hibour.adapters.SocializeTabsPager;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.SocializeClient;
import com.dsquare.hibour.pojos.Socialize.ChoosedUser;
import com.dsquare.hibour.pojos.Socialize.Data;
import com.dsquare.hibour.pojos.preference.Datum;
import com.dsquare.hibour.pojos.preference.Preference;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/22/2016.
 */
public class Socialize extends android.support.v4.app.Fragment implements View.OnClickListener {

    private SocializeTabsPager mPagerAdapter;
    private Button doneButton,previous;
    private RecyclerView prefsRecycler;
    private List<String[]> prefsList = new ArrayList<>();
    private PreferencesAdapter adapter;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog dialog;
    private Gson gson;
    private Hibour application;
    private SocializeClient socialClient;
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private List<String> tabsList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
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
        getMembers(application.getUserId());
        //getAllPrefs();
        return view;
    }
    /* initialize views*/
    private void initializeViews(View view){
        accountsClient = new AccountsClient(getActivity());
        networkDetector = new NetworkDetector(getActivity());
        gson = new Gson();
        socialClient = new SocializeClient(getActivity());
        application = Hibour.getInstance(getActivity());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);
//        doneButton = (Button)view.findViewById(R.id.socialize_done_button);
//        previous = (Button)view.findViewById(R.id.socialize_prev_button);
        pager = (ViewPager) view.findViewById(R.id.socialize_pager);
        tabsList.add("PREFERENCES");
        tabsList.add("ALL");
        tabs = (SlidingTabLayout) view.findViewById(R.id.socialize_tabs);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.newbrand);
            }
        });
        tabs.setTabsBackgroundColor(getResources().getColor(R.color.white));
//        prefsRecycler = (RecyclerView)view.findViewById(R.id.social_prefs_list);
//        prefsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        prefsRecycler.addItemDecoration(new GridLayoutSpacing(3, 5, true));
//        prefsRecycler.setHasFixedSize(true);
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
//        doneButton.setOnClickListener(this);
//        previous.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.socialize_done_button:
                openPreviousActivity();
                break;
            case R.id.socialize_prev_button:
                openPreviousActivity();
                break;
        }
    }
    /* open home activity*/
    private void openHomeActivity(){
        Intent homeIntent = new Intent(getActivity(), com.dsquare.hibour.activities.PreferencesViews.class);
        homeIntent.putExtra("frmAdapter",false);
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
            insertUserPrefs(application.getUserId(), userPrefs);
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
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
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
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
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
//                String[] detail={"Preference","All"};

            }
//           setAdapters(prefsList);
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
        prefsRecycler.setAdapter(new SocializeAdapter(getActivity(), prefsList));
    }

    /*set pager adapter*/
    private void setPager() {
        if (mPagerAdapter == null) {
            mPagerAdapter = new SocializeTabsPager(getFragmentManager());
        }
        mPagerAdapter.updateTabs(tabsList);
        tabs.setDistributeEvenly(true);
        try {
            pager.setAdapter(mPagerAdapter);
            tabs.setViewPager(pager);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /* get socialize pref members*/
    private void getMembers(String userId){
        if(networkDetector.isConnected()){
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
            Constants.socialPrefsMap.clear();
            Constants.prefsMap.clear();
            Constants.membersList.clear();
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
            setPager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
