package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.AdapterPostLikes;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posts.PostLikedUser;
import com.dsquare.hibour.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/28/2016.
 */
public class PostLikes extends AppCompatActivity implements View.OnClickListener {
    private ImageView backIcon;
    private RecyclerView likesList;
    private List<String[]> likeList = new ArrayList<>();
    private AdapterPostLikes adapter;
    private String postId = "";
    private NetworkDetector networkDetector;
    private PostsClient postsClient;
    private Gson gson;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_likes);
        initializeViews();
        initializeEventListeners();
        // prepareLikesList();
    }

    private void initializeViews() {
        postsClient = new PostsClient(this);
        gson = new Gson();
        networkDetector = new NetworkDetector(this);
        postId = getIntent().getStringExtra("postId");
        getLikes(postId);
        backIcon = (ImageView) findViewById(R.id.likes_back_icon);
        likesList = (RecyclerView) findViewById(R.id.likes_post_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        likesList.setLayoutManager(layoutManager);
        likesList.setHasFixedSize(true);
    }

    private void initializeEventListeners() {
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            for (int i = 0; i < posts.size(); i++) {
                String[] data = new String[1];
                data[0] = posts.get(i).getName();
                likeList.add(data);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* get likes*/
    private void getLikes(String postId) {
        if (networkDetector.isConnected()) {
            dialog = ProgressDialog.show(this, "", "Please Wait...");
            postsClient.getLikesonPosts(postId, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseLikes(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("error in getting likes", error.toString());
                }
            });
        }
    }

    private void parseLikes(JSONObject jsonObject) {
        likeList.clear();
        try {
            JSONArray users = jsonObject.getJSONArray("users");
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                String[] data = new String[3];
                data[0] = user.getString("id");
                data[1] = user.getString("name");
                data[2] = user.getString("image");
                likeList.add(data);
            }
            adapter = new AdapterPostLikes(this, likeList);
            likesList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void closeDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }
}
