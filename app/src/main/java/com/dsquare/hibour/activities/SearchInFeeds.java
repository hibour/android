package com.dsquare.hibour.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dsquare.hibour.R;

public class SearchInFeeds extends AppCompatActivity implements View.OnClickListener{

    private ImageView backIcon,searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_feeds);
        initializeViews();
        intializeEventListeners();
    }
    /* initialize views*/
    private void initializeViews(){
        searchIcon  = (ImageView)findViewById(R.id.feeds_search_back);
        backIcon = (ImageView)findViewById(R.id.feeds_search_icon);
    }
    /* initializeEventListeners*/
    private void intializeEventListeners(){
        searchIcon.setOnClickListener(this);
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feeds_search_back:
                this.finish();
                break;
            case R.id.feeds_search_icon:
                break;
        }
    }
}
