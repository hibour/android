package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.home.NavActionType;
import com.dsquare.hibour.adapters.NavigationDrawerAdapter;
import com.dsquare.hibour.fragments.AboutUs;
import com.dsquare.hibour.fragments.Home;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.interfaces.SettingsToHomeCallback;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.HibourConnector;
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


public class HomeActivity extends AppCompatActivity implements NavDrawerCallback
    , AdapterView.OnItemClickListener, /*NewPost.PostsListener,*/SettingsToHomeCallback {

//public class HomeActivity extends AppCompatActivity implements NavDrawerCallback,
//        AdapterView.OnItemClickListener {


  boolean doubleBackToExitPressedOnce = false;
  private FragmentManager manager;
  private FragmentTransaction transaction;
  private DrawerLayout drawer;
  private ListView drawerList;
  private TextView name;
  private ImageView profile;
  private boolean isHome = true;
  private Hibour application;
  private Gson gson;
  private PostsClient postsClient;
  private ProgressDialog dialog;
  private NetworkDetector networkDetector;
  private CoordinatorLayout coordinatorLayout;
  private SharedPreferences sharedPreferences;
  private ImageLoader imageLoader;
  private Fragment activeFragment = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    initializeViews();
    initializeDrawerAdapter();
    initializeEventListeners();
    handleAction(NavActionType.HOME);
    getAllCategoriesTypes();
  }

  /*initialize views*/
  private void initializeViews() {
    networkDetector = new NetworkDetector(this);
    application = Hibour.getInstance(this);
    gson = new Gson();
    postsClient = new PostsClient(this);
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id
        .coordinatorLayout);
    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    manager = getSupportFragmentManager();
    drawerList = (ListView) findViewById(R.id.left_drawer);
    application = Hibour.getInstance(this);
    networkDetector = new NetworkDetector(this);
    gson = new Gson();
    postsClient = new PostsClient(this);
    name = (TextView) findViewById(R.id.sidemenu_name);
    profile = (ImageView) findViewById(R.id.home_user_profile_pic);
    postsClient = new PostsClient(this);
    networkDetector = new NetworkDetector(this);
    gson = new Gson();
    imageLoader = HibourConnector.getInstance(this).getImageLoader();
    Map<String, String> userDetails = application.getUserDetails();

    name.setText(userDetails.get(Constants.SF_FIRST) + " " + userDetails.get(Constants.SF_LAST));
    imageLoader.get(userDetails.get(Constants.SF_IMAGE), ImageLoader.getImageListener(profile
        , R.drawable.avatar1, R.drawable.avatar1));

  }

  private void initializeDrawerAdapter() {
    drawerList.setAdapter(new NavigationDrawerAdapter(this));
  }

  private void initializeEventListeners() {
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
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    handleAction(NavActionType.values()[position]);
  }

  private void handleAction(NavActionType type) {
    switch (type) {
      case HOME:
        isHome = true;
        break;
      case ABOUT_US:
        isHome = false;
        break;
      case INVITE:
        inviteFriends("Hey let's use Hi'bour application");
        break;
      case SETTINGS:
        isHome = false;
        break;
      case LOCATION_SEARCH:
        isHome = false;
        application.removeUserDetails();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
        Intent signInIntent = new Intent(this, LocationSearch.class);
        startActivity(signInIntent);
        this.finish();
        break;
      case SIGNIN:
        isHome = false;
        application.removeUserDetails();
        Intent signInIntent1 = new Intent(this, SignIn.class);
        startActivity(signInIntent1);
        this.finish();
    }

    boolean isNewFragment = false;
    Fragment fragment = manager.findFragmentByTag(type.toString());
    if (fragment == null) {
      isNewFragment = true;
      fragment = getFragment(type);
      if (fragment == null) {
        return;
      }
    } else if (activeFragment == fragment) {
      return;
    }

    FragmentTransaction fragmentTransaction = manager.beginTransaction();
    if (isNewFragment) {
      fragmentTransaction.add(R.id.content_frame, fragment, type.toString());
    } else {
      fragmentTransaction.show(fragment);
    }
    if (activeFragment != null && activeFragment != fragment) {
      fragmentTransaction.hide(activeFragment);
    }
    fragmentTransaction.commit();
    activeFragment = fragment;
  }

  @Nullable
  private Fragment getFragment(NavActionType type) {
    switch (type) {
      case HOME:
        return new Home();
      case ABOUT_US:
        return new AboutUs();
      case SETTINGS:
        //  return new Settings();
    }
    return null;
  }

  @Override
  public void onBackPressed() {
    //super.onBackPressed();
    if (isHome) {
      if (doubleBackToExitPressedOnce) {
        super.onBackPressed();

        return;
      }
      if (isHome) {
        if (doubleBackToExitPressedOnce) {
          super.onBackPressed();
          return;
        }
        this.doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar
            .make(coordinatorLayout, "Please Tap BACK again to exit!", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

          @Override
          public void run() {
            doubleBackToExitPressedOnce = false;
          }
        }, 2000);
        return;
      }
      handleAction(NavActionType.HOME);
    }
  }
    /*invite friends*/

  private void inviteFriends(String invitationMessage) {
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
  private void parseAllCategoriesTypes(JSONObject jsonObject) {
    try {
      PostTypeCatg proofsData = gson.fromJson(jsonObject.toString(), PostTypeCatg.class);
      List<Datum> data = proofsData.getData();
      if (data.size() > 0) {
        if (Constants.postTypesMap.size() > 0) {
          Constants.postTypesMap.clear();
        }
        for (Datum d : data) {
          Map<String, String> postsTypesMap = new LinkedHashMap<>();
          postsTypesMap.put("id", (d.getId() + "").replace(" ", ""));
          postsTypesMap.put("name", d.getPosttypename());
          Log.d("categoryId", d.getId() + "");
          Log.d("category name", d.getPosttypename());
          postsTypesMap.put("placeholder", d.getPlaceholder());
          Constants.postTypesMap.put(d.getPosttypename(), postsTypesMap);
        }
      }
      handleAction(NavActionType.HOME);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
    }
  }

  /* get all categories types*/
  private void getAllCategoriesTypes() {
    if (networkDetector.isConnected()) {
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
    } else {
      Snackbar snackbar = Snackbar
          .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
      // Changing action button text color
      View sbView = snackbar.getView();
      TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
      textView.setTextColor(Color.RED);
      snackbar.show();
    }
  }

  /* close post types dialog*/
  private void closeCategoriesDialog() {
    try {
      if (dialog != null) {
        if (dialog.isShowing()) {
          dialog.dismiss();
          dialog = null;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
 /* @Override
  public void onCancelClicked() {
    handleAction(NavActionType.HOME);
  }

  @Override
  public void onDoneClicked() {
    handleAction(NavActionType.HOME);
  }*/

  @Override
  public void onTabsChoosed(int pos) {
    handleAction(NavActionType.HOME);
  }

}


