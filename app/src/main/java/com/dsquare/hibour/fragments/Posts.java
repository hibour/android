package com.dsquare.hibour.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PostsAdapter;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posts.Datum;
import com.dsquare.hibour.pojos.posts.Postspojo;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Posts extends Fragment {
    private RecyclerView postsRecycler;
    private List<String[]> postsList = new ArrayList<>();
    private PostsAdapter postsAdapter;
    private NetworkDetector networkDetector;
    private Gson gson;
    private Hibour application;
    private PostsClient postsClient;
    private ProgressDialog postsDialog;
    public Posts() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        initializeViews(view);
        return view;
    }
    /* initializeViews*/
    private void initializeViews(View view){
        postsClient = new PostsClient(getActivity());
        gson = new Gson();
        networkDetector = new NetworkDetector(getActivity());
        postsRecycler = (RecyclerView)view.findViewById(R.id.post_posts_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        postsRecycler.setLayoutManager(layoutManager);
        postsRecycler.setHasFixedSize(true);
       /* for(int i=0;i<10;i++){
            String[] data = new String[6];
            data[0] = "Ashok Madduru";
            data[1] = "2 Jan 2015";
            data[2] = "Planning to start a chinese restaraunt near jubilee hills check post, anyone there to partner with me?";
            data[3] = "category name";
            data[4] = "250";
            data[5] = "10";
            postsList.add(data);
        }*/
        postsAdapter = new PostsAdapter(getActivity(),postsList);
        postsRecycler.setAdapter(postsAdapter);
    }
    /* get all posts from server*/
    private void getAllposts(String userId){
        if(networkDetector.isConnected()){
            postsDialog = ProgressDialog.show(getActivity(),"","Please wait...");
            postsClient.getAllPosts(userId,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parsePostsDetails(jsonObject);
                    closePostsDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("posts error",error.toString());
                    closePostsDialog();
                }
            });
        }else{
            Toast.makeText(getActivity(),"Check network connection",Toast.LENGTH_LONG).show();
        }
    }
    /* parse posts details*/
    private void parsePostsDetails(JSONObject jsonObject){
        Postspojo posts = gson.fromJson(jsonObject.toString(),Postspojo.class);
        List<Datum> postsData = posts.getData();
        for(int i=0;i<postsData.size();i++){
            String[] data = new String[6];
            Datum d = postsData.get(i);
            data[1] = "2 Jan 2015";
            data[2] = "Planning to start a chinese restaraunt near jubilee hills check post, anyone there to partner with me?";
            data[3] = "category name";
            data[4] = "250";
            data[5] = "10";
            postsList.add(data);
        }

    }
    /* close posts dialog*/
    private void closePostsDialog(){
        if(postsDialog!=null){
            if(postsDialog.isShowing()){
                postsDialog.dismiss();
                postsDialog=null;
            }
        }
    }
}
