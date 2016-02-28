package com.dsquare.hibour.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.UserChatListAdapter;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.network.SocializeClient;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousChat extends BaseChatFragment {


  private static final String LOG_TAG = PreviousChat.class.getSimpleName();
  private RecyclerView recyclerView;
  private UserChatListAdapter adapter;
  private SocializeClient socializeClient;
  private DatabaseHandler dbHandler;

  public PreviousChat() {
    // Required empty public constructor
  }

  @Override
  public void onResume() {
    super.onResume();
    findPreviousChatUser();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_previous_chat, container, false);
    socializeClient = new SocializeClient(getContext());
    dbHandler = new DatabaseHandler(getContext());
    initializeViews(view);
    return view;
  }

  private void findPreviousChatUser() {
    adapter.getUserList().clear();
    adapter.getUserList().addAll(dbHandler.getChartUserList());
    adapter.notifyDataSetChanged();
      for (UserDetail userDetail:dbHandler.getChartUserList()) {
          List<UserDetail> data1 = new ArrayList<>();
          data1.add(userDetail);
          Constants.chatsMap.put(userDetail.id, data1);
          Constants.searchChat.put(userDetail.Username, userDetail.id);
          if (Constants.searchChat.size() > 0) {
              Constants.chatList.clear();
              for (String key1 : Constants.searchChat.keySet()) {
                  Constants.chatList.add(key1);
                  Log.d("size1", "" + Constants.chatList.size());
              }
          }
      }
  }

  /* initialize views*/
  private void initializeViews(View view) {
    recyclerView = (RecyclerView) view.findViewById(R.id.messages_neighbours_list);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
    adapter = new UserChatListAdapter(getActivity());
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void loadUserSearchResult(String key) {
    adapterUserSearch.getUserList().clear();
    adapterUserSearch.getUserList().addAll(dbHandler.getUserListContainKey(key));
    recyclerView.setAdapter(adapterUserSearch);
    adapterUserSearch.notifyDataSetChanged();

  }

  @Override
  public void removeUserSearchResult() {
    recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();
  }
}
