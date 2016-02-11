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
import com.dsquare.hibour.adapters.ServicesAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.network.NetworkDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ariochdivij666 on 2/8/16.
 */
public class Services extends Fragment implements View.OnClickListener, ServicesAdapter.ServicesListInterface {

    private ImageView notifIcon;
    private RecyclerView channelsRecycler;
    private ServicesAdapter adapter;
    private List<String[]> channelList = new ArrayList<>();
    private NetworkDetector networkDetector;
    private NavDrawerCallback callback;


    public Services() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        prepareChannelsList();
        initializeViews(view);
        initializeEventListeners();
        return view;
    }

    /* initialize views*/
    private void initializeViews(View view) {
        notifIcon = (ImageView) view.findViewById(R.id.channel_search_icon);
        channelsRecycler = (RecyclerView) view.findViewById(R.id.channels_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        channelsRecycler.setLayoutManager(layoutManager);
        channelsRecycler.setHasFixedSize(true);
        adapter = new ServicesAdapter(getActivity(), channelList);
        channelsRecycler.setAdapter(adapter);

    }

    /* initialize eventlisteners*/
    private void initializeEventListeners() {
        notifIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.services_menu_icon:
                callback.drawerOpen();
                break;
            case R.id.messages_search_icon:
                break;
        }
    }

    /* prepare neighbours list*/
    private void prepareChannelsList() {
        for (int i = 0; i < 10; i++) {
            String[] data = new String[3];
            data[0] = "Div Test";
            data[1] = "line 1";
            data[2] = "line 2";
            channelList.add(data);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }

    @Override
    public void itemClicked(int id) {
        //Perform click here.
    }
}
