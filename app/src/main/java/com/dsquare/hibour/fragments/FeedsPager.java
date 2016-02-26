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
import com.dsquare.hibour.adapters.FeedsAdapter;
import com.dsquare.hibour.pojos.posts.Feeds;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsPager extends Fragment {

    public static final String CATEGORY_BUNDLE_ARGUMENT = "categoryName";

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
        categoryName = getArguments().getString(CATEGORY_BUNDLE_ARGUMENT, "");
        postsRecycler = (RecyclerView)view.findViewById(R.id.post_posts_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        postsRecycler.setLayoutManager(layoutManager);
        postsRecycler.setHasFixedSize(true);
        postsAdapter = new FeedsAdapter(getActivity(), postsList);
        postsRecycler.setAdapter(postsAdapter);
        refresh();
    }

    public void refresh() {
        new FeedsTask().execute();
    }

    /* asynchronous task to set data to adapter*/
    class FeedsTask extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPostExecute(Void result) {
            postsAdapter.notifyDataSetChanged();
            Log.d("name", categoryName);
            Log.d("posts size", postsList.size() + "");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        private void addPostPojosList(List<Postpojos> postList) {
            for (Postpojos post : postList) {
                postsList.add(new Feeds(post));
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            postsList.clear();
            try {
                if (categoryName.equals("All")) {
                    for(Map.Entry<String, List<Postpojos>> entry: Constants.postpojosMap.entrySet()){
                        addPostPojosList(entry.getValue());
                    }
                } else {
                    addPostPojosList(Constants.postsMap.get(categoryName));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
