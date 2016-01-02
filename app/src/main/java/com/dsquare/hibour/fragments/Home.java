package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.HomeTabsPager;
import com.dsquare.hibour.interfaces.NavDrawerCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener,TabHost.OnTabChangeListener{


    private ImageView navIcon;
    private NavDrawerCallback callback;
    private TabHost tabHost;
    private static int id = 10000;
    private TabLayout tabLayout;
    private String[] tabTitles = {"POST","SUGGESTIONS","CLASSIFIEDS"};
    private HomeTabsPager pagerAdapter;
    private ViewPager pager;
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
        tabHost = (TabHost)view.findViewById(R.id.home_tabhost);
        pager = (ViewPager)view.findViewById(R.id.home_pager);
        initializeTabhost();
    }

    /* initialize event listeners*/
    private void initializeEventListeners(){
        navIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.home_menu_icon:
                callback.drawerOpen();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }

    private void initializeTabhost(){
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);
        final LinearLayout inputFormsView = new LinearLayout(getActivity());
        View indicatorView = LayoutInflater.from(getActivity())
                .inflate(R.layout.home_tabs, null);
        ((TextView)indicatorView.findViewById(R.id.tabsText)).setText("POSTS");
        tabHost.addTab(
                tabHost.newTabSpec("POSTS")
                        .setIndicator(indicatorView)
                        .setContent(new TabHost.TabContentFactory() {
                                        public View createTabContent(String tag) {
                                            inputFormsView.setLayoutParams(new LinearLayout
                                                    .LayoutParams(AbsListView.LayoutParams
                                                    .WRAP_CONTENT,
                                                    AbsListView.LayoutParams.WRAP_CONTENT));
                                            inputFormsView.setId(R.id.homeTabId);
                                            inputFormsView.setTag("POSTS");
                                            return inputFormsView;
                                        }
                                    }
                        )
        );
        final LinearLayout inputFormsViewTwo = new LinearLayout(getActivity());
        View indicatorViewTwo = LayoutInflater.from(getActivity())
                .inflate(R.layout.home_tabs, null);
        ((TextView)indicatorViewTwo.findViewById(R.id.tabsText)).setText("SUGGESTIONS");
        tabHost.addTab(
                tabHost.newTabSpec("SUGGESTIONS")
                        .setIndicator(indicatorViewTwo)
                        .setContent(new TabHost.TabContentFactory() {
                                        public View createTabContent(String tag) {
                                            inputFormsViewTwo.setLayoutParams(new LinearLayout
                                                    .LayoutParams(AbsListView.LayoutParams
                                                    .WRAP_CONTENT, AbsListView.LayoutParams
                                                    .WRAP_CONTENT));
                                            inputFormsViewTwo.setId(R.id.homeTabId);
                                            inputFormsViewTwo.setTag("SUGGESTIONS");
                                            return inputFormsViewTwo;
                                        }
                                    }
                        )
        );

        final LinearLayout inputFormsViewThree = new LinearLayout(getActivity());
        View indicatorViewThree = LayoutInflater.from(getActivity())
                .inflate(R.layout.home_tabs, null);
        ((TextView)indicatorViewThree.findViewById(R.id.tabsText)).setText("CLASSIFIEDS");
        tabHost.addTab(
                tabHost.newTabSpec("CLASSIFIEDS")
                        .setIndicator(indicatorViewThree)
                        .setContent(new TabHost.TabContentFactory() {
                                        public View createTabContent(String tag) {
                                            inputFormsViewThree.setLayoutParams(new LinearLayout
                                                    .LayoutParams(AbsListView.LayoutParams
                                                    .WRAP_CONTENT, AbsListView.LayoutParams
                                                    .WRAP_CONTENT));
                                            inputFormsViewThree.setId(R.id.homeTabId);
                                            inputFormsViewThree.setTag("CLASSIFIEDS");
                                            return inputFormsViewThree;
                                        }
                                    }
                        )
        );
        tabHost.setCurrentTab(0);
    }

    private int getTabid(){
        return id + 1;
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d("tabId", tabId);
        switch (tabId){
            case "POSTS":
                tabHost.setCurrentTab(0);
                replaceContainer(0);
                break;
            case "SUGGESTIONS":
                tabHost.setCurrentTab(1);
                replaceContainer(1);
                break;
            case "CLASSIFIEDS":
                tabHost.setCurrentTab(2);
                replaceContainer(2);
                break;
        }
    }
    /* replace tab's fragment*/
    public void replaceContainer(int id){
        pagerAdapter  = new HomeTabsPager(getFragmentManager(),3,tabTitles);
        pager.setAdapter(pagerAdapter);
        Fragment fragment = null;
      /*  switch(id){
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
            fragmentTransaction.replace(R.id.home_frame_container, fragment);
            fragmentTransaction.commit();
        }*/
    }
}
