package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.GroupsAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Groups extends Fragment implements View.OnClickListener{


    private RecyclerView groupsRecycler;
    private ImageView menuIcon,notifIcon;
    private NavDrawerCallback callback;
    private List<String[]> groupsList=new ArrayList<>();
    private GroupsAdapter groupsAdapter;
    public Groups() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        initializeViews(view);
        initializeEventListeners();
        return view;
    }


    private void initializeViews(View view){
        menuIcon = (ImageView)view.findViewById(R.id.groups_menu_icon);
        notifIcon = (ImageView)view.findViewById(R.id.groups_notif_icon);
        groupsRecycler = (RecyclerView)view.findViewById(R.id.groups_list);LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        groupsRecycler.setLayoutManager(layoutManager);
        groupsRecycler.setHasFixedSize(true);
        for(int i=0;i<10;i++){
            String[] data = new String[6];
            data[0] = "Bhanu Elite";
            data[1] = "120 Members";
            data[2] = "Planning to start a chinese restaraunt near jubilee hills check post, anyone there to partner with me?";
            groupsList.add(data);
        }
        groupsAdapter = new GroupsAdapter(getActivity(),groupsList);
        groupsRecycler.setAdapter(groupsAdapter);
    }
    private void initializeEventListeners(){
        menuIcon.setOnClickListener(this);
        notifIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.groups_menu_icon:
                callback.drawerOpen();
                break;
            case R.id.groups_notif_icon:
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }
}
