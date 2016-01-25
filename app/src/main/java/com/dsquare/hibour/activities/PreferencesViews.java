package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.NeighboursAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.network.NetworkDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/25/2016.
 */
public class PreferencesViews extends AppCompatActivity implements View.OnClickListener {
    private ImageView menuIcon,notifIcon;
    private RecyclerView neighboursRecycler;
    private NeighboursAdapter adapter;
    private List<String[]> neighboursList = new ArrayList<>();
    private NetworkDetector networkDetector;
    private ProgressDialog dialog;
    private NavDrawerCallback callback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefernceview);
        prepareNeighboursList();
        initializeViews();
        initializeEventListeners();
    }
    /* initialize views*/
    private void initializeViews(){
        menuIcon = (ImageView)findViewById(R.id.messages_menu_icon);
        notifIcon = (ImageView)findViewById(R.id.messages_search_icon);
        neighboursRecycler = (RecyclerView)findViewById(R.id.prefernce_neighbours_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        neighboursRecycler.setLayoutManager(layoutManager);
        neighboursRecycler.setHasFixedSize(true);
        adapter = new NeighboursAdapter(this,neighboursList);
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
                this.finish();
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
}
