package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.NavDrawerCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener{


    private ImageView navIcon;
    private NavDrawerCallback callback;
    private ImageView newPostIcon,postsIcon,suggestionsIcon,classifiedsIcon,socializeIcon;
    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(view);
        initializeEventListeners();
        return view;
    }

    /*initialize views*/
    private void initializeViews(View view){
        navIcon = (ImageView)view.findViewById(R.id.home_menu_icon);
        newPostIcon = (ImageView)view.findViewById(R.id.home_new_post);
        postsIcon = (ImageView)view.findViewById(R.id.home_posts_icon);
        suggestionsIcon = (ImageView)view.findViewById(R.id.home_suggestions_icon);
        classifiedsIcon = (ImageView)view.findViewById(R.id.home_classifieds_icon);
        socializeIcon = (ImageView)view.findViewById(R.id.home_socialize_icon);
    }

    /* initialize event listeners*/
    private void initializeEventListeners(){
        navIcon.setOnClickListener(this);
        newPostIcon.setOnClickListener(this);
        postsIcon.setOnClickListener(this);
        suggestionsIcon.setOnClickListener(this);
        classifiedsIcon.setOnClickListener(this);
        socializeIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.home_menu_icon:
                callback.drawerOpen();
                break;
            case R.id.home_new_post:
                replaceContainer(4);
                break;
            case R.id.home_posts_icon:
                replaceContainer(0);
                break;
            case R.id.home_suggestions_icon:
                replaceContainer(2);
                break;
            case R.id.home_classifieds_icon:
                replaceContainer(3);
                break;
            case R.id.home_socialize_icon:
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }


    /* replace tab's fragment*/
    public void replaceContainer(int id){
        Fragment fragment = null;
        switch(id){
            case 0:
                fragment = new Posts();
                break;
            case 1:
                fragment = new Suggestions();
                break;
            case 2:
                fragment = new Classifieds();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }
}
