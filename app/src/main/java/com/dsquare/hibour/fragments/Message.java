package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.ChatTypeViewPagerAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.adapters.NeighboursAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.user.UserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Message extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = Message.class.getSimpleName();
    ViewPager pager;
    TabLayout tabs;
    private ImageView menuIcon, notifIcon;
    private NavDrawerCallback callback;

    public Message() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initializeViews(view);
        initializeEventListeners();
        return view;
    }

    /* initialize views*/
    private void initializeViews(View view) {
        menuIcon = (ImageView) view.findViewById(R.id.messages_menu_icon);
        notifIcon = (ImageView) view.findViewById(R.id.messages_search_icon);
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (TabLayout) view.findViewById(R.id.tabs);

        ChatTypeViewPagerAdapter adapter = new ChatTypeViewPagerAdapter(getChildFragmentManager(), getResources().getStringArray(R.array.chat_types));
        tabs.setTabTextColors(ContextCompat.getColorStateList(getContext(), R.color.selector));
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    /* initialize eventlisteners*/
    private void initializeEventListeners() {
        menuIcon.setOnClickListener(this);
        notifIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messages_menu_icon:
                callback.drawerOpen();
                break;
            case R.id.messages_search_icon:
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }
}
