package com.dsquare.hibour.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.AdapterPostLikes;
import com.dsquare.hibour.pojos.posts.PostLikedUser;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/28/2016.
 */
public class PostLikes  extends AppCompatActivity implements View.OnClickListener {
    private ImageView backIcon;
    private RecyclerView likesList;
    private List<String[]> likeList = new ArrayList<>();
    private AdapterPostLikes adapter;
    private String postId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_likes);
        initializeViews();
        initializeEventListeners();
        prepareLikesList();
    }

    private void initializeViews() {
        postId = getIntent().getStringExtra("postId");
        backIcon = (ImageView) findViewById(R.id.likes_back_icon);
        likesList = (RecyclerView) findViewById(R.id.likes_post_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        likesList.setLayoutManager(layoutManager);
        likesList.setHasFixedSize(true);
        adapter = new AdapterPostLikes(this,likeList);
        likesList.setAdapter(adapter);
    }

    private void initializeEventListeners() {
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.likes_back_icon:
                this.finish();
                break;
        }
    }

    private void prepareLikesList(){
        likeList.clear();
        Log.d("id",postId);
        List<PostLikedUser> posts = Constants.postlikesMap.get(postId);
        try {
            for(int i=0;i<posts.size();i++) {
                String[] data = new String[1];
                data[0] =  posts.get(i).getName();
                likeList.add(data);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
