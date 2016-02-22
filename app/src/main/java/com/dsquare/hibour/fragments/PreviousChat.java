package com.dsquare.hibour.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.NeighboursAdapter;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.network.SocializeClient;
import com.dsquare.hibour.pojos.user.UserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousChat extends Fragment {


  private static final String LOG_TAG = PreviousChat.class.getSimpleName();
  private RecyclerView recyclerView;
  private NeighboursAdapter adapter;
  private List<UserDetail> chatUserList = new ArrayList<>();
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
    chatUserList.clear();
    chatUserList.addAll(dbHandler.getChartUserList());
    adapter.notifyDataSetChanged();
  }

  /* initialize views*/
  private void initializeViews(View view) {
    recyclerView = (RecyclerView) view.findViewById(R.id.messages_neighbours_list);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
    adapter = new NeighboursAdapter(getActivity(), chatUserList, R.layout.adapter_chats);
    recyclerView.setAdapter(adapter);
  }
}
