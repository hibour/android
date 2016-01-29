package com.dsquare.hibour.fragments;


import android.app.Activity;
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
import com.dsquare.hibour.adapters.NeighboursAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.user.UserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Message extends Fragment implements View.OnClickListener{


    private ImageView menuIcon,notifIcon;
    private RecyclerView neighboursRecycler;
    private NeighboursAdapter adapter;
    private List<UserDetail> neighboursList = new ArrayList<>();
    private NetworkDetector networkDetector;
    private ProgressDialog dialog;
    private NavDrawerCallback callback;
    public Message() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        prepareNeighboursList();
        initializeViews(view);
        initializeEventListeners();
        return view;
    }
    /* initialize views*/
    private void initializeViews(View view){
        menuIcon = (ImageView)view.findViewById(R.id.messages_menu_icon);
        notifIcon = (ImageView)view.findViewById(R.id.messages_search_icon);
        neighboursRecycler = (RecyclerView)view.findViewById(R.id.messages_neighbours_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        neighboursRecycler.setLayoutManager(layoutManager);
        neighboursRecycler.setHasFixedSize(true);
        adapter = new NeighboursAdapter(getActivity(),neighboursList);
        neighboursRecycler.setAdapter(adapter);
    }
    /* initialize eventlisteners*/
    private void initializeEventListeners(){
        menuIcon.setOnClickListener(this);
        notifIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.messages_menu_icon:
                callback.drawerOpen();
                break;
            case R.id.messages_search_icon:
                break;
        }
    }
    /* prepare neighbours list*/
    private void prepareNeighboursList(){
        UserDetail user;
        user = new UserDetail();
        user.id = 98;
        user.Username = "Ashok";
        user.Address = "Temp1";
        neighboursList.add(user);
        user = new UserDetail();
        user.id = 29;
        user.Username = "Divy";
        user.Address = "Temp2";
        neighboursList.add(user);
        for (int i = 3; i < 10; i++) {
            user = new UserDetail();
            user.id = i;
            user.Username = "Ashok Madduru";
            user.Address = "Hardware Engineer";
            neighboursList.add(user);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }
}
