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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
    private ImageView feedIcon, socializeIcon, newPostIcon, channelsIcon, moreIcon;
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
        feedIcon = (ImageView)view.findViewById(R.id.home_feed);
        socializeIcon = (ImageView)view.findViewById(R.id.home_socialize_icon);
        newPostIcon = (ImageView)view.findViewById(R.id.home_new_post);
        channelsIcon = (ImageView)view.findViewById(R.id.home_channels);
        moreIcon = (ImageView)view.findViewById(R.id.home_more_icon);
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        navIcon.setOnClickListener(this);
        feedIcon.setOnClickListener(this);
        socializeIcon.setOnClickListener(this);
        newPostIcon.setOnClickListener(this);
        channelsIcon.setOnClickListener(this);
        moreIcon.setOnClickListener(this);
    }
    private void loadDefaultFragment(){
        replaceContainer(3);
//        Fragment fragment = new NewPosts();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.home_menu_icon:
                callback.drawerOpen();
                break;
            case R.id.home_feed:
                applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
                break;
            case R.id.home_socialize_icon:
                applyCurrentStateToAppBarIcons(R.drawable.socialize_filled, socializeIcon);
                replaceContainer(4);
                break;
            case R.id.home_new_post:
                LinearLayout bottomBar = (LinearLayout)this.getActivity().findViewById(R.id.home_bottom_menu);
                if(bottomBar.getVisibility() == View.VISIBLE) {
                    applyCurrentStateToAppBarIcons(R.drawable.cancel_filled, newPostIcon);

                    //mask the rest of the screen
                    FrameLayout screenrest = (FrameLayout) this.getActivity().findViewById(R.id.home_fragment_container);
                    screenrest.setBackgroundColor(getResources().getColor(R.color.black_transparent));

                    bottomBar.setVisibility(View.GONE);

                    replaceContainer(3);
                } else {
                    //TODO: Need to clean this up
                    applyCurrentStateToAppBarIcons(R.drawable.post, newPostIcon);

                    ImageView socializeIcon = (ImageView) this.getActivity().findViewById(R.id.home_socialize_icon);
                    socializeIcon.setImageResource(R.drawable.socialize_filled);

                    replaceContainer(4);

                    bottomBar.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.home_channels:
                applyCurrentStateToAppBarIcons(R.drawable.channels_filled, channelsIcon);
                break;
            case R.id.home_more_icon:
                applyCurrentStateToAppBarIcons(R.drawable.more_filled, moreIcon);
                break;

        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }

    public void applyCurrentStateToAppBarIcons(int res, ImageView icon) {
        feedIcon.setImageResource(R.drawable.feed);
        socializeIcon.setImageResource(R.drawable.socialize);
        newPostIcon.setImageResource(R.drawable.post);
        channelsIcon.setImageResource(R.drawable.channels);
        moreIcon.setImageResource(R.drawable.more);

        icon.setImageResource(res);
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
                fragment = new CreatePost();
                break;
            case 4:
                isHome = true;
                fragment = new Socialize();
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
