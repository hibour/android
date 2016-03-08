package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PreferencesAdapter;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.SocializeClient;
import com.dsquare.hibour.pojos.Socialize.ChoosedUser;
import com.dsquare.hibour.pojos.Socialize.Data;
import com.dsquare.hibour.pojos.preference.Datum;
import com.dsquare.hibour.pojos.preference.Preference;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.GridLayoutSpacing;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dsquare Android on 1/25/2016.
 */
public class SocialPrefernce extends AppCompatActivity implements View.OnClickListener{

    public static Map<String, String> searchMap = new LinkedHashMap<>();
    private Button doneButton,previous;
    private AutoCompleteTextView searchText;
    private Typeface proxima;
    private RecyclerView prefsRecycler,prefsRecycler1;
    private ImageView back,back1,search;
    private List<String[]> prefsList = new ArrayList<>();
    private List<String[]> prefsList1 = new ArrayList<>();
    private List<String> searchList=new ArrayList<>();
    private List<String> suggestionList = new ArrayList<>();
    private PreferencesAdapter adapter;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog dialog;
    private Gson gson;
    private Hibour application;
    private View searchBar;
    private UIHelper uiHelper;
    private LinearLayout update;
    private GridLayoutManager layoutManager,layoutManager1;
    private SocializeClient socialClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefernces);
        initializeViews();
        initializeEventListeners();
        getMembers(application.getUserId());
    }

    /* initialize views*/
    private void initializeViews(){
        proxima = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
        accountsClient = new AccountsClient(this);
        socialClient = new SocializeClient(this);
        networkDetector = new NetworkDetector(this);
        gson = new Gson();
        application = Hibour.getInstance(this);
        back = (ImageView)findViewById(R.id.socillize_menu_icon);
        searchText = (AutoCompleteTextView)findViewById(R.id.home_search_autocomplete);
        prefsRecycler = (RecyclerView)findViewById(R.id.social_prefs_list);
        layoutManager = new GridLayoutManager(this, 3);
        prefsRecycler.setLayoutManager(layoutManager);
        prefsRecycler.addItemDecoration(new GridLayoutSpacing(3, 5, true));
        prefsRecycler.setHasFixedSize(true);
        prefsRecycler1 = (RecyclerView)findViewById(R.id.social_prefs_list1);
        searchBar = findViewById(R.id.messages_search_bar);
        search = (ImageView)findViewById(R.id.home_search_icon);
        back1 = (ImageView) findViewById(R.id.navigation_back);
        update = (LinearLayout) findViewById(R.id.update_prefernces);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this, android.R.layout.simple_dropdown_item_1line,
//                        searchList);
//        searchText.setAdapter(adapter);
//        searchText.setThreshold(1);
//        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String itemName = searchText.getText().toString();
//                if (parent != null && parent.getChildAt(0) != null) {
//                    String cardType = searchList.get(position);
//                    Log.d("cardtype", cardType);
////                        if(!cardType.equals("Select Card")){
////                            cardTypeId = searchMap.get(cardType);
////                            Log.d("cardtype",cardType);
////                        }
//                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
//                            .getColor(R.color.black_1));
//                    ((TextView) parent.getChildAt(0)).setTypeface(proxima);
//                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
//                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                    Log.d("itemname", itemName);
////                setRecyclerList(itemName);
//                }}
//        });
    }

    public void setSecondRecycler(int count,List<String[]> list){
        layoutManager1 = new GridLayoutManager(this, count);
        prefsRecycler1.setLayoutManager(layoutManager1);
        prefsRecycler1.setHasFixedSize(true);
        prefsRecycler1.addItemDecoration(new GridLayoutSpacing(count, 5, true));
        prefsRecycler1.setAdapter(new PreferencesAdapter(this,list));
    }

    private void showSearchView() {
        back.setVisibility(View.GONE);
        searchBar.setVisibility(View.VISIBLE);
        searchText.setText("");
        searchText.requestFocus();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line,
                        searchList);
        searchText.setAdapter(adapter);
        searchText.setThreshold(1);
        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemName = searchText.getText().toString();
                if (parent != null && parent.getChildAt(0) != null) {
                    String cardType = searchList.get(position);
                    Log.d("cardtype", cardType);
//                        if(!cardType.equals("Select Card")){
//                            cardTypeId = searchMap.get(cardType);
//                            Log.d("cardtype",cardType);
//                        }
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.black_1));
                    ((TextView) parent.getChildAt(0)).setTypeface(proxima);
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    Log.d("itemname", itemName);
//                setRecyclerList(itemName);
                }
            }
        });
    }

    private boolean hideSearchView() {
        if (searchBar.getVisibility() == View.VISIBLE) {
            searchBar.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }


    /* initialize event listeners*/
    private void initializeEventListeners(){
      back.setOnClickListener(this);
        back1.setOnClickListener(this);
        search.setOnClickListener(this);
        update.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.socillize_menu_icon:
                this.finish();
                break;
            case R.id.navigation_back:
                hideSearchView();
                break;
            case R.id.home_search_icon:
                showSearchView();
                break;
            case R.id.update_prefernces:
                sendUserPrefs();
                break;
        }
    }
    /* open home activity*/
    private void openHomeActivity(){
        Intent homeIntent = new Intent(this, Settings.class);
        startActivity(homeIntent);
        finish();
    }
    private void openPreviousActivity(){
        Intent locationIntent = new Intent(this, HomeActivity.class);
        startActivity(locationIntent);
        finish();
    }
    /*send user prefs to server*/
    private void sendUserPrefs(){
        String userPrefs = "";
        for(String pref: Constants.prefernceMap.keySet()){
            userPrefs = userPrefs+","+pref;
        }
        Log.d("userprefs",userPrefs);
        if(userPrefs.equals("")){
//            openHomeActivity();
            Toast.makeText(this,"Please select minimum one Interest",Toast.LENGTH_SHORT).show();
        }else{
            userPrefs = userPrefs.substring(1,userPrefs.length());
            Log.d("userprefs",userPrefs);

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
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
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
                searchMap.put(data.get(i).getPreferencesname(),data.get(i).getId()+"");
                searchList.add(data.get(i).getPreferencesname());
            }
//            setAdapters(prefsList);
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
        prefsRecycler.setAdapter(new PreferencesAdapter(this,prefsList));
        if(!(prefsList.size()%3==0)){
            int count = prefsList.size()%3;
            Log.d("count",count+"");
            List<String[]> list = new ArrayList<>();
            for(int i=(prefsList.size()-count);i<(prefsList.size());i++){
                list.add(prefsList.get(i));
            }
            Log.d("prefslength",prefsList.size()+"");
            for(int i=(prefsList.size()-count-1);i<(prefsList.size());i++){
                Log.d("i",i+"");
                prefsList.remove(i);
            }
            setSecondRecycler(count,list);
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
                searchMap.put(d.getPreferenceName(),d.getPreferenceId()+"");
                searchList.add(d.getPreferenceName());
                // Constants.nearByUsersMap.put(d.getPreferenceId(),d.getChoosedUsers());
                for(ChoosedUser ch:d.getChoosedUsers()){
                    Constants.membersList.add(ch);
                }
            }
            setAdapters();
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
}
