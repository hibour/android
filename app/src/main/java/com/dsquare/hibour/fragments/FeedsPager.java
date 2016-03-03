package com.dsquare.hibour.fragments;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.FeedsAdapter;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.database.table.FeedsTable;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posts.Feeds;
import com.dsquare.hibour.pojos.posts.PostData;
import com.dsquare.hibour.pojos.posts.PostLikedUser;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsPager extends Fragment {

    public static final String CATEGORY_BUNDLE_ARGUMENT = "categoryName";

    private RecyclerView postsRecycler;
    private List<Feeds> postsList = new ArrayList<>();
    private FeedsAdapter postsAdapter;
    private String categoryName = "";
    SwipeRefreshLayout swipeRefreshLayout;
    private NetworkDetector networkDetector;
    private Gson gson;
    private PostsClient postsClient;
    private DatabaseHandler dbHandler;
    private Hibour application;

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
        application = Hibour.getInstance(getActivity());
        dbHandler = new DatabaseHandler(getActivity().getApplicationContext());
        gson = new Gson();
        networkDetector = new NetworkDetector(getActivity());
        postsClient = new PostsClient(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.feeds_swipeRefresh);
        categoryName = getArguments().getString(CATEGORY_BUNDLE_ARGUMENT, "");
        postsRecycler = (RecyclerView)view.findViewById(R.id.post_posts_list);
       final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        postsRecycler.setLayoutManager(layoutManager);
        postsRecycler.setHasFixedSize(true);
        postsAdapter = new FeedsAdapter(getActivity(), postsList);
        postsRecycler.setAdapter(postsAdapter);

        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.brand), ContextCompat.getColor(getContext(), R.color.green),
                ContextCompat.getColor(getContext(), R.color.brand_status_bar), ContextCompat.getColor(getContext(), R.color.outline_yellow));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("refres", "yes");
                swipeRefreshLayout.setEnabled(false);
                refreshPosts();
            }
        });
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        postsRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                swipeRefreshLayout.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        refresh();

    }
    private SwipeRefreshLayout.OnRefreshListener swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(getActivity(),"refresh",Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setEnabled(false);
           // socializeClient.getNearByUser(application.getUserId(), nearbyUserResultCallBack);

        }
    };

    public void refresh() {
        new FeedsTask().execute();
    }

    /* asynchronous task to set data to adapter*/
    class FeedsTask extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPostExecute(Void result) {
            postsAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        private void addPostPojosList(List<FeedsTable> postList) {
            for (FeedsTable post : postList) {
                postsList.add(new Feeds(post));
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            postsList.clear();
            try {
                if (categoryName.equals("All")) {
                    for (Map.Entry<String, List<FeedsTable>> entry : Constants.postpojosMap.entrySet()) {
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
    /* get all posts from server*/
    private void refreshPosts() {
        if(networkDetector.isConnected()){
            postsClient.getAllPosts(application.getUserId(), new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parsePostsDetails(jsonObject);
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("posts error", error.toString());
                }
            });
        }else{
           Toast.makeText(getActivity(),"Network not connected",Toast.LENGTH_LONG).show();
        }
    }
    /* parse post details*/
    private void parsePostsDetails(JSONObject jsonObject){
        PostData posts = gson.fromJson(jsonObject.toString(), PostData.class);
        dbHandler.insertFeeds(posts);
        List<FeedsTable> feedsData = dbHandler.getFeeds();
        Log.d("feeds size in pager", dbHandler.getFeeds().size() + "");
        List<Postpojos> postpojos = posts.getData();

        Map<String, List<FeedsTable>> postsMap = new LinkedHashMap<>();
        Map<String, List<FeedsTable>> postpojosMap = new LinkedHashMap<>();
        Map<String, List<PostLikedUser>> postlikesMap = new LinkedHashMap<>();
        Map<String, String> categoriesMap = new HashMap<>();
        Map<String, String> searchMap = new LinkedHashMap<String, String>();
        Map<String, Map<String, String>> postTypesMap = new LinkedHashMap<>();
        Set<String> postTypesSet = new HashSet<>();

        if (feedsData.size() > 0) {
            postTypesSet.add("All");
            for (FeedsTable p : feedsData) {
                List<FeedsTable> data1 = new ArrayList<>();
                data1.add(p);
                postpojosMap.put(p.postid, data1);
                searchMap.put(p.description, p.postid);
                String postType = p.posttype ;
                List<FeedsTable> postslist = postsMap.get(postType);
                if (postslist == null) {
                    postslist = new ArrayList<>();
                    postsMap.put(Constants.postTypesMap.get(postType).get("name"), postslist);
                }
                postslist.add(p);

                if (!TextUtils.isEmpty(postType)) {
                    postTypesSet.add(Constants.postTypesMap.get(postType).get("name"));
                }
            }

            List<String> postTypeList = new ArrayList<>();
            postTypeList.addAll(postTypesSet);
            Collections.sort(postTypeList);

            Constants.postsMap = postsMap;
            Constants.postpojosMap = postpojosMap;
            Constants.postlikesMap = postlikesMap;
            Constants.searchMap = searchMap;
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(true);
            refresh();
        } else {
        }
    }
}
