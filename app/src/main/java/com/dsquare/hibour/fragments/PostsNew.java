package com.dsquare.hibour.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.dsquare.hibour.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsNew extends Fragment {


    private RecyclerView recyclerView;
    private ImageView downArrow;
    private HorizontalScrollView scrollView;
    public PostsNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_posts_new, container, false) ;
        initializeViews(view);
        return view;
    }
    private void initializeViews(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.post_post);
        downArrow = (ImageView)view.findViewById(R.id.feeds_categories_drowdown);

        scrollView = (HorizontalScrollView)view.findViewById(R.id.feeds_category_scroll);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
       // recyclerView.setAdapter(new FeedsAdapter(10));
        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrollView.getVisibility() == View.GONE){
                    scrollView.setVisibility(View.VISIBLE);
                    downArrow.setImageDrawable(getActivity().getDrawable(R.drawable.ic_arrow_up));
                }else{
                    scrollView.setVisibility(View.GONE);
                }
            }
        });
    }


}
