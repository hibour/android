package com.dsquare.hibour.fragments;

import android.support.v4.app.Fragment;

import com.dsquare.hibour.adapters.NearByUserChatListAdapter;
import com.dsquare.hibour.database.DatabaseHandler;

public abstract class BaseChatFragment extends Fragment {

  public NearByUserChatListAdapter adapterUserSearch;
  public DatabaseHandler dbHandler;

  public void initializeBaseChatFragment() {
    adapterUserSearch = new NearByUserChatListAdapter(getActivity());
    dbHandler = new DatabaseHandler(getContext());
  }

  public abstract void loadUserSearchResult(String key);

  public abstract void removeUserSearchResult();
}
