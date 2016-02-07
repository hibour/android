package com.dsquare.hibour.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.FeedsAdapter;
import com.dsquare.hibour.adapters.PostsAdapter;
import com.dsquare.hibour.pojos.posts.Feeds;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsPager extends Fragment {

    private RecyclerView postsRecycler;
    private List<Feeds> postsList = new ArrayList<>();
    private FeedsAdapter postsAdapter;
    private String categoryName = "";
    public FeedsPager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feeds_pager, container, false);
        initializeViews(view);
        return view;
    }

    /* initialize views*/
    private void initializeViews(View view){
        categoryName = getArguments().getString("categoryName", "");
        postsRecycler = (RecyclerView)view.findViewById(R.id.post_posts_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        postsRecycler.setLayoutManager(layoutManager);
        postsRecycler.setHasFixedSize(true);
        postsAdapter = new FeedsAdapter(getActivity(),postsList);
        setAdapter();
        // new setFeedsTask().execute(categoryName);
    }
    /*set adapter*/
    private void setAdapter(){
        Log.d("name",categoryName);
        /*if(categoryName.equals("All")){
            for(String s:Constants.postsMap.keySet()){
                List<Postpojos> posts = Constants.postsMap.get(s);
                try {
                    for(int i=0;i<posts.size();i++) {
                        String[] data = new String[11];
                        data[0] = posts.get(i).getUser().getId();
                        data[1] = posts.get(i).getPostDate();
                        data[2] = posts.get(i).getPostMessage();
                        data[3] = posts.get(i).getPostType();
                        data[4] = String.valueOf(posts.get(i).getPostLikesCount());
                        data[5] = Arrays.toString(new int[]{posts.get(i).getPostComments().size()}).replaceAll("\\[|\\]", "");
                        data[6] = posts.get(i).getPostId();
                        data[7] = String.valueOf(posts.get(i).getPostUserLiked());
                        data[8] = posts.get(i).getPostImage();
                        data[9] = posts.get(i).getPostTime();
                        data[10]=posts.get(i).getUser().getName();
                        postsList.add(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(!categoryName.equals("")){
            List<Postpojos> posts = Constants.postsMap.get(categoryName);

            try {
                for(int i=0;i<posts.size();i++) {
                    String[] data = new String[11];
                    data[0] = posts.get(i).getUser().getId();
                    data[1] = posts.get(i).getPostDate();
                    data[2] = posts.get(i).getPostMessage();
                    data[3] = posts.get(i).getPostType();
                    data[4] = String.valueOf(posts.get(i).getPostLikesCount());
                    data[5] = Arrays.toString(new int[]{posts.get(i).getPostComments().size()}).replaceAll("\\[|\\]", "");
                    data[6] = posts.get(i).getPostId();
                    data[7] = String.valueOf(posts.get(i).getPostUserLiked());
                    data[8] = posts.get(i).getPostImage();
                    data[9] = posts.get(i).getPostTime();
                    data[10]=posts.get(i).getUser().getName();
                    postsList.add(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        Log.d("posts size",postsList.size()+"");
        postsRecycler.setAdapter(postsAdapter);
        new FeedsTask().execute();
    }

    /* asynchronous task to set data to adapter*/

    class FeedsTask extends AsyncTask<Void, String, Void>{

        FeedsAdapter feeds;

        @Override
        protected void onPostExecute(Void result) {
           // Toast.makeText(MainActivity.this, "Loading completed", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            //feeds = (FeedsAdapter) postsRecycler.getAdapter();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            postsList.add(new Feeds(values[0], values[1], values[2], values[3], values[4], values[5], values[6]
                    , values[7], values[8], values[9], values[10], values[11]));
            postsAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(categoryName.equals("All")){
                for(String s:Constants.postsMap.keySet()){
                    List<Postpojos> posts = Constants.postsMap.get(s);
                    try {
                        for(int i=0;i<posts.size();i++) {
                            publishProgress(posts.get(i).getPostId(),posts.get(i).getPostImage()
                                    ,posts.get(i).getPostMessage()
                                    ,posts.get(i).getPostDate(),posts.get(i).getPostTime()
                                    ,posts.get(i).getUser().getName(),posts.get(i).getUser().getImage()
                                    ,posts.get(i).getUser().getId(),String.valueOf(posts.get(i).getPostUserLiked())
                                    ,String.valueOf(posts.get(i).getPostLikesCount())
                                    ,String.valueOf(posts.get(i).getPostComments().size())
                                    ,posts.get(i).getPostType());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if(!categoryName.equals("")){
                List<Postpojos> posts = Constants.postsMap.get(categoryName);
                try {
                    for(int i=0;i<posts.size();i++) {
                        publishProgress(posts.get(i).getPostId(),posts.get(i).getPostImage()
                                ,posts.get(i).getPostMessage()
                                ,posts.get(i).getPostDate(),posts.get(i).getPostTime()
                                ,posts.get(i).getUser().getName(),posts.get(i).getUser().getImage()
                                ,posts.get(i).getUser().getId(),String.valueOf(posts.get(i).getPostUserLiked())
                                ,String.valueOf(posts.get(i).getPostLikesCount())
                                ,String.valueOf(posts.get(i).getPostComments().size())
                                ,posts.get(i).getPostType());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

    }
}
