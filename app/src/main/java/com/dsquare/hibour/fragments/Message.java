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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Message extends Fragment implements View.OnClickListener{


    private ImageView menuIcon,notifIcon;
    private RecyclerView neighboursRecycler;
    private NeighboursAdapter adapter;
    private List<String[]> neighboursList = new ArrayList<>();
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
        for(int i=0;i<10;i++){
            String[] data = new String[2];
            data[0] = "Ashok Madduru";
            data[1] = "Hardware Engineer";
            neighboursList.add(data);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }
}
