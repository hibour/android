package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.NavigationDrawerAdapter;
import com.dsquare.hibour.fragments.AboutUs;
import com.dsquare.hibour.fragments.NewPost;
import com.dsquare.hibour.fragments.Settings;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posttype.Datum;
import com.dsquare.hibour.pojos.posttype.PostTypeCatg;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity implements NavDrawerCallback
        , AdapterView.OnItemClickListener,NewPost.PostsListener{

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private ListView drawerList;
    private TextView name;
    private boolean isHome = true;
    boolean doubleBackToExitPressedOnce = false;
    private Hibour application;
    private Gson gson;
    private PostsClient postsClient;
    private ProgressDialog dialog;
    private NetworkDetector networkDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeViews();
        initializeDrawerAdapter();
        initializeEventListeners();
        getAllCategoriesTypes();
    }

    /*initialize views*/
    private void initializeViews(){
        gson = new Gson();
        networkDetector = new NetworkDetector(this);
        postsClient = new PostsClient(this);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        manager = getSupportFragmentManager();
        drawerList = (ListView)findViewById(R.id.left_drawer);
        application = Hibour.getInstance(this);
        name = (TextView)findViewById(R.id.sidemenu_name);
    }

    private void loadDefaultFragment(){
        transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, new com.dsquare.hibour.fragments.Home());
        transaction.commit();
    }
    private void initializeDrawerAdapter(){
        drawerList.setAdapter(new NavigationDrawerAdapter(this));
    }
    private void initializeEventListeners(){
        drawerList.setOnItemClickListener(this);
    }


    @Override
    public void drawerOpen() {
        drawer.openDrawer(GravityCompat.END);
    }

    @Override
    public void hideDrawer() {
        drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public void replaceFragment(int position) {
        replaceWithNewFragment(position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        replaceWithNewFragment(position);
    }

    private void replaceWithNewFragment(int position){
        transaction = manager.beginTransaction();
        switch (position){
            case 0:
                isHome = true;
                transaction.replace(R.id.content_frame, new com.dsquare.hibour.fragments.Home());
                break;
            case 1:
                isHome = false;
                transaction.replace(R.id.content_frame,new AboutUs());
                break;
            case 2:
                isHome = false;
                inviteFriends("");
                break;
            case 3:
                isHome = false;
                transaction.replace(R.id.content_frame,new Settings());
                break;
            case 4:
                isHome = false;
                application.removeUserDetails();
                Intent signInIntent = new Intent(this,SignIn.class);
                startActivity(signInIntent);
                this.finish();
           /* case 4:
                isHome = false;
                transaction.replace(R.id.content_frame,new AboutUs());
                break;
            case 5:
                isHome = false;
                inviteFriends("Hey let's use Hi'bour application");
                break;
            case 7:
                isHome = false;
                transaction.replace(R.id.content_frame,new Settings());
                break;
            case 8:
                isHome= false;
                application.removeUserDetails();
                Intent signInIntent = new Intent(this,SignIn.class);
                startActivity(signInIntent);
                this.finish();*/
        }
        transaction.commit();
        hideDrawer();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (isHome) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please Tap BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
            return;
        }
        replaceWithNewFragment(0);
    }
    /*invite friends*/
    private void inviteFriends(String invitationMessage){
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
        sendIntent.setType("text/plain");

        PackageManager pm = getPackageManager();
        Resources resources = getResources();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            Log.d("Package Name", packageName);
            if (packageName.contains("android.talk")
                    || packageName.contains("facebook")
                    || packageName.contains("whatsapp")
                    || packageName.contains("mms")
                    || packageName.contains("android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
                intent.setPackage(packageName);
                targetShareIntents.add(intent);
            }
        }
        if (!targetShareIntents.isEmpty()) {
            System.out.println("Have Intent");
            Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to invite friends");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));

            startActivity(chooserIntent);
        } else {

        }
    }
    /* parse categories types*/
    private void parseAllCategoriesTypes(JSONObject jsonObject){
        try {
            PostTypeCatg proofsData = gson.fromJson(jsonObject.toString(),PostTypeCatg.class);
            List<Datum> data = proofsData.getData();
            if(data.size()>0) {
                if(Constants.postTypesMap.size()>0){
                    Constants.postTypesMap.clear();
                }
                for (Datum d : data) {
                    Map<String,String> postsTypesMap = new LinkedHashMap<>();
                    postsTypesMap.put("id",d.getId()+"");
                    postsTypesMap.put("name",d.getPosttypename());
                    postsTypesMap.put("placeholder",d.getPlaceholder());
                    Constants.postTypesMap.put(d.getPosttypename(),postsTypesMap);
                }
            }
            loadDefaultFragment();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    /* get all categories types*/
    private void getAllCategoriesTypes(){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(this, "", getResources()
                    .getString(R.string.progress_dialog_text));
            postsClient.getAllcategoriesTypes(new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseAllCategoriesTypes(jsonObject);
                    closeCategoriesDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("govt", error.toString());
                    closeCategoriesDialog();
                }
            });
        }else{
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }
    /* close post types dialog*/
    private void closeCategoriesDialog(){
        try {
            if(dialog!=null){
                if(dialog.isShowing()){
                    dialog.dismiss();
                    dialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelClicked() {
        replaceWithNewFragment(0);
    }

    @Override
    public void onDoneClicked() {
        replaceWithNewFragment(0);
    }
}

