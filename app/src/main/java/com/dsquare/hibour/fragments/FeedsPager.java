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

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.PostsAdapter;
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
    private List<String[]> postsList = new ArrayList<>();
    private PostsAdapter postsAdapter;
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
        setAdapter();
        new setFeedsTask().execute(categoryName);
    }
    /*set adapter*/
    private void setAdapter(){
        Log.d("name",categoryName);
        if(categoryName.equals("All")){
            for(String s:Constants.postsMap.keySet()){
                List<Postpojos> posts = Constants.postsMap.get(s);
                try {
                    for(int i=0;i<posts.size();i++) {
                        String[] data = new String[8];
                        data[0] = posts.get(i).getUser().getName();
                        data[1] = posts.get(i).getPostDate();
                        data[2] = posts.get(i).getPostMessage();
                        data[3] = posts.get(i).getPostType();
                        data[4] = String.valueOf(posts.get(i).getPostLikesCount());
                        data[5] = Arrays.toString(new int[]{posts.get(i).getPostComments().size()}).replaceAll("\\[|\\]", "");
                        data[6] = posts.get(i).getPostId();
                        data[7] = String.valueOf(posts.get(i).getPostUserLiked());
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
                    String[] data = new String[8];
                    data[0] = posts.get(i).getUser().getName();
                    data[1] = posts.get(i).getPostDate();
                    data[2] = posts.get(i).getPostMessage();
                    data[3] = posts.get(i).getPostType();
                    data[4] = String.valueOf(posts.get(i).getPostLikesCount());
                    data[5] = Arrays.toString(new int[]{posts.get(i).getPostComments().size()}).replaceAll("\\[|\\]", "");
                    data[6] = posts.get(i).getPostId();
                    data[7] = String.valueOf(posts.get(i).getPostUserLiked());
                    postsList.add(data);
                    Log.d("datas",""+data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        postsAdapter = new PostsAdapter(getActivity(),postsList);
        postsRecycler.setAdapter(postsAdapter);
    }

    /* asynchronous task to set data to adapter*/
    class setFeedsTask extends AsyncTask<String,String,Void>{
        private PostsAdapter adapter;
        @Override
        protected void onPreExecute() {
            adapter = (PostsAdapter)postsRecycler.getAdapter();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... params) {
            if(params.equals("All")){
                for(String s:Constants.postsMap.keySet()){
                    List<Postpojos> posts = Constants.postsMap.get(s);
                    try {
                        for(int i=0;i<posts.size();i++) {
                            String[] data = new String[8];
                            data[0] = posts.get(i).getUser().getName();
                            data[1] = posts.get(i).getPostDate();
                            data[2] = posts.get(i).getPostMessage();
                            data[3] = posts.get(i).getPostType();
                            data[4] = String.valueOf(posts.get(i).getPostLikesCount());
                            data[5] = Arrays.toString(new int[]{posts.get(i).getPostComments().size()}).replaceAll("\\[|\\]", "");
                            data[6] = posts.get(i).getPostId();
                            data[7] = String.valueOf(posts.get(i).getPostUserLiked());
                            //postsList.add(data);
                            publishProgress(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else if(!params.equals("")){

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
//            adapter.
        }
    }
}
