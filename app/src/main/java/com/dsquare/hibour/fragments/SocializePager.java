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
import com.dsquare.hibour.adapters.NearByUserChatListAdapter;
import com.dsquare.hibour.adapters.SocializeAdapter;
import com.dsquare.hibour.pojos.posts.Feeds;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.GridLayoutSpacing;
import com.dsquare.hibour.utils.MySpanSizeLookup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 2/10/2016.
 */
public class SocializePager extends Fragment {

  public static final String CATEGORY_BUNDLE_ARGUMENT = "categoryName";

  private RecyclerView postsRecycler;
  private List<Feeds> postsList = new ArrayList<>();
  private List<String[]> prefsList = new ArrayList<>();
  private List<UserDetail> neighboursList = new ArrayList<>();
  private NearByUserChatListAdapter postsAdapter1;
  private SocializeAdapter postsAdapter;
  private String categoryName = "";
  private GridLayoutManager layoutManager;
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
    categoryName = getArguments().getString(CATEGORY_BUNDLE_ARGUMENT, "");
    Log.d("catg", categoryName);
    postsRecycler = (RecyclerView) view.findViewById(R.id.post_posts_list);
    if (categoryName.equals("PREFERENCES")) {
      Log.d("catg1", "PREFERENCES");
//      layoutManager = new GridLayoutManager(getActivity(), 3);
//      layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//        @Override
//        public int getSpanSize(int position) {
//          int mod = position % 3;
//          Log.d("size",prefsList.size()+"");
////          if(prefsList.size()%3==0) {
////            return 3;
////          }
////          else if(prefsList.size()%3==2) {
////            return 2;
////          }
////          else {
////            return 1;
////          }
//
//          if (position % 3 == 0) {
//            return 3;
//          }
//          else if (position % 3 == 2)
//            return 2;
////          else if (position % 3 == 1)
////            return 1;
//          else
//            return 1;
//        }
//      });
//      postsRecycler.setLayoutManager(layoutManager);
//      postsRecycler.addItemDecoration(new GridLayoutSpacing(3, 5, true));
//      postsRecycler.setHasFixedSize(true);
      setAdapter();
    } else {
      Log.d("catg2", "All");
      LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      postsRecycler.setLayoutManager(layoutManager);
      postsRecycler.setHasFixedSize(true);
      setNeighbours();
    }
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
    layoutManager = new GridLayoutManager(getActivity(), 3);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        int mod = position % 3;
//        Log.d("size",prefsList.size()+"");
//          if(position == 0 || position == 1 || position == 2)
//            return 3;
//          else if(mod == 0 || mod == 1 || mod == 2)
//            return 3;
//          else if(mod == 0 || mod == 1)
//            return 2;
//          else
//            return 3;
        return postsAdapter.getItemViewType(position);
      }
    });
//    layoutManager.setSpanSizeLookup(new MySpanSizeLookup(9, 1, 3));
//    layoutManager.setSpanSizeLookup(new MySpanSizeLookup(10, 1, 3));
    postsRecycler.setLayoutManager(layoutManager);
//    postsRecycler.addItemDecoration(new GridLayoutSpacing(3, 5, true));
//    postsRecycler.setHasFixedSize(true);
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
    postsRecycler.setAdapter(new NearByUserChatListAdapter(getActivity()));
  }
}
