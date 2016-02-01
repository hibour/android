package com.dsquare.hibour.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.GroupPostsAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupPosts extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView groupPostsRecycler;
    private ImageView backIcon;
    private List<String[]> groupPostsList = new ArrayList<>();
    private GroupPostsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_group_chating);
        initializeViews();
        initializeEventListeners();
    }
    private void initializeViews(){
        backIcon = (ImageView)findViewById(R.id.group_posts_back_icon);
        groupPostsRecycler = (RecyclerView)findViewById(R.id.group_posts_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        groupPostsRecycler.setLayoutManager(layoutManager);
        groupPostsRecycler.setHasFixedSize(true);
        for(int i=0;i<10;i++){
            String[] data = new String[6];
            data[0] = "Ashok Madduru";
            data[1] = "10m ago";
            data[2] = "Hi Frnds";
            data[3] = "Bhanu P";
            data[4] = "9m ago";
            data[5] = "how r u???";
            groupPostsList.add(data);
        }
        adapter = new GroupPostsAdapter(this,groupPostsList);
        groupPostsRecycler.setAdapter(adapter);
    }
    private void initializeEventListeners(){
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.group_posts_back_icon:
                this.finish();
                break;
        }
    }
}
