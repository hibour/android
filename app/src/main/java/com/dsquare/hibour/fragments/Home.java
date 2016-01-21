package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.os.Build;
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

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ImageView navIcon;
    private NavDrawerCallback callback;
    private boolean isHome = true;
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
        loadDefaultFragment();
        return view;
    }

    /*initialize views*/
    private void initializeViews(View view){
        navIcon = (ImageView)view.findViewById(R.id.home_menu_icon);
        newPostIcon = (ImageView)view.findViewById(R.id.home_new_post);
        postsIcon = (ImageView)view.findViewById(R.id.home_posts_icon);
        suggestionsIcon = (ImageView)view.findViewById(R.id.home_suggestion_icon);
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

    private void loadDefaultFragment(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            newPostIcon.setColorFilter(getResources().getColor(R.color.brand,getActivity().getTheme()));
            postsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
            suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
            classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
            socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
        }else {
            newPostIcon.setColorFilter(getResources().getColor(R.color.brand));
            postsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
            suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
            classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
            socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1));
        }
        replaceContainer(3);
//        Fragment fragment = new NewPosts();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.home_menu_icon:
                callback.drawerOpen();
                break;
            case R.id.home_new_post:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    newPostIcon.setColorFilter(getResources().getColor(R.color.brand,getActivity().getTheme()));
                    postsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                }else {
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    postsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                }
                replaceContainer(3);
                break;
            case R.id.home_posts_icon:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    postsIcon.setColorFilter(getResources().getColor(R.color.brand,getActivity().getTheme()));
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                }else {
                    postsIcon.setColorFilter(getResources().getColor(R.color.brand));
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                }

                replaceContainer(0);


                break;
            case R.id.home_suggestion_icon:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.brand,getActivity().getTheme()));
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    postsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                }else {
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.brand));
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    postsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                }

                replaceContainer(1);
                break;
            case R.id.home_classifieds_icon:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.brand,getActivity().getTheme()));
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    postsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                }else {
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.brand));
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    postsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    socializeIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                }
                replaceContainer(4);
                break;
            case R.id.home_socialize_icon:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.brand,getActivity().getTheme()));
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                    postsIcon.setColorFilter(getResources().getColor(R.color.gray_1,getActivity().getTheme()));
                }else {
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.brand));
                    newPostIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    suggestionsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    classifiedsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                    postsIcon.setColorFilter(getResources().getColor(R.color.gray_1));
                }
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
                isHome = false;
                fragment = new Posts();
                break;
            case 1:
                isHome = false;
                fragment = new Suggestions();
                break;
            case 2:
                isHome = false;
                fragment = new Classifieds();
                break;
            case 3:
                isHome = true;
                fragment = new NewPosts();
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
