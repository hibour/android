package com.dsquare.hibour.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PostsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Posts extends Fragment {


    private RecyclerView postsRecycler;
    private List<String[]> postsList = new ArrayList<>();
    private PostsAdapter postsAdapter;
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
        postsRecycler = (RecyclerView)view.findViewById(R.id.post_posts_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        postsRecycler.setLayoutManager(layoutManager);
        postsRecycler.setHasFixedSize(true);
        for(int i=0;i<10;i++){
            String[] data = new String[6];
            data[0] = "Ashok Madduru";
            data[1] = "2 Jan 2015";
            data[2] = "Planning to start a chinese restaraunt near jubilee hills check post, anyone there to partner with me?";
            data[3] = "category name";
            data[4] = "250";
            data[5] = "10";
            postsList.add(data);
        }
        postsAdapter = new PostsAdapter(getActivity(),postsList);
        postsRecycler.setAdapter(postsAdapter);
    }
}
