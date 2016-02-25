package com.dsquare.hibour.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.SocializeAdapter;
import com.dsquare.hibour.adapters.UserChatListAdapter;
import com.dsquare.hibour.pojos.posts.Feeds;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.GridLayoutSpacing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 2/10/2016.
 */
public class SocializePager extends Fragment {

  private RecyclerView postsRecycler;
  private List<Feeds> postsList = new ArrayList<>();
  private List<String[]> prefsList = new ArrayList<>();
  private List<UserDetail> neighboursList = new ArrayList<>();
  private UserChatListAdapter postsAdapter1;
  private SocializeAdapter postsAdapter;
  private String categoryName = "";

  public SocializePager() {
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

  private void initializeViews(View view) {
    categoryName = getArguments().getString("categoryName", "");
    Log.d("catg", categoryName);
    postsRecycler = (RecyclerView) view.findViewById(R.id.post_posts_list);
    if (categoryName.equals("PREFERENCES")) {
      Log.d("catg1", "PREFERENCES");
      postsRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
      postsRecycler.addItemDecoration(new GridLayoutSpacing(3, 5, true));
      postsRecycler.setHasFixedSize(true);
      setAdapter();
    } else {
      Log.d("catg2", "All");
      LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      postsRecycler.setLayoutManager(layoutManager);
      postsRecycler.setHasFixedSize(true);
      setNeighbours();
    }
    // new setFeedsTask().execute(categoryName);
  }

  private void setAdapter() {
    for (String s : Constants.socialPrefsMap.keySet()) {
      String[] details = {Constants.socialPrefsMap.get(s).get(0)
          , Constants.socialPrefsMap.get(s).get(1)
          , Constants.socialPrefsMap.get(s).get(2)
          , Constants.socialPrefsMap.get(s).get(3)
          , Constants.socialPrefsMap.get(s).get(4)
          , Constants.socialPrefsMap.get(s).get(5)};
      prefsList.add(details);
    }
    postsRecycler.setAdapter(new SocializeAdapter(getActivity(), prefsList));
  }

  /* prepare neighbours list*/
  private void setNeighbours() {
    UserDetail userDetail;
    int i;
    //for (i = 0; i < 10; i++) {
    for (i = 0; i < Constants.membersList.size(); i++) {
      Log.d("size", "" + Constants.membersList.size());
      if (Constants.membersList.get(i).getUserName() != null &&
          !Constants.membersList.get(i).getUserName().equals("null")) {
        userDetail = new UserDetail();
        userDetail.Username = Constants.membersList.get(i).getUserName();
        userDetail.id = Constants.membersList.get(i).getUserId();
        userDetail.Image = Constants.membersList.get(i).getUserImage();
        neighboursList.add(userDetail);
      }
    }
    postsRecycler.setAdapter(new UserChatListAdapter(getActivity()));
  }
}
