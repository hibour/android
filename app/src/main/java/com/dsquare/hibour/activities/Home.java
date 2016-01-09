package com.dsquare.hibour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.NavigationDrawerAdapter;
import com.dsquare.hibour.fragments.AboutUs;
import com.dsquare.hibour.fragments.Groups;
import com.dsquare.hibour.fragments.Message;
import com.dsquare.hibour.fragments.Settings;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.utils.Hibour;

public class Home extends AppCompatActivity implements NavDrawerCallback, AdapterView.OnItemClickListener{

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private ListView drawerList;
    private boolean isHome = true;
    boolean doubleBackToExitPressedOnce = false;
    private Hibour application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeViews();
        initializeDrawerAdapter();
        initializeEventListeners();
        loadDefaultFragment();
    }

    /*initialize views*/
    private void initializeViews(){
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        manager = getSupportFragmentManager();
        drawerList = (ListView)findViewById(R.id.left_drawer);
        application = Hibour.getInstance(this);
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
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void hideDrawer() {
        drawer.closeDrawer(GravityCompat.START);
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
                transaction.replace(R.id.content_frame,new Message());
                break;
            case 3:
                isHome = false;
                transaction.replace(R.id.content_frame,new Groups());
                break;
            case 4:
                isHome = false;
                transaction.replace(R.id.content_frame,new AboutUs());
                break;
            case 6:
                isHome = false;
                transaction.replace(R.id.content_frame,new Settings());
                break;
            case 7:
                isHome= false;
                application.removeUserDetails();
                Intent signInIntent = new Intent(this,SignIn.class);
                startActivity(signInIntent);
                this.finish();
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
}
