package com.dsquare.hibour.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.UserChatListAdapter;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.SocializeClient;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByUserChat extends BaseChatFragment {


  private static final String LOG_TAG = NearByUserChat.class.getSimpleName();
  SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerView recyclerView;
  private UserChatListAdapter adapter;
  private SocializeClient socializeClient;
  private Hibour application;
  private WebServiceResponseCallback nearbyUserResultCallBack = new WebServiceResponseCallback() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
//      Log.e(LOG_TAG, jsonObject.toString());
      try {
        List<UserDetail> list = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<ArrayList<UserDetail>>() {
        }.getType());
        adapter.getUserList().clear();
        adapter.getUserList().addAll(list);
        adapter.notifyDataSetChanged();
        for (UserDetail userDetail : list) {
          List<UserDetail> data1 = new ArrayList<>();
          data1.add(userDetail);
          Constants.chatsMap.put(userDetail.id, data1);
          Constants.searchChat.put(userDetail.Username, userDetail.id);
          if (Constants.searchChat.size() > 0) {
            Constants.chatList.clear();
            for (String key : Constants.searchChat.keySet()) {
              Constants.chatList.add(key);
              Log.d("size", "" + Constants.chatList.size());
            }
          }
        }

      } catch (JSONException e) {
        e.printStackTrace();
      }
      swipeRefreshLayout.setRefreshing(false);
      swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void onFailure(VolleyError error) {
      Log.e(LOG_TAG, "error");
    }
  };
  private SwipeRefreshLayout.OnRefreshListener swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
      swipeRefreshLayout.setEnabled(false);
      socializeClient.getNearByUser(application.getUserId(), nearbyUserResultCallBack);
    }
  };

  public NearByUserChat() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    initializeBaseChatFragment();
    View view = inflater.inflate(R.layout.fragment_nearby_user_chat, container, false);
    socializeClient = new SocializeClient(getContext());
    application = Hibour.getInstance(getContext());
    initializeViews(view);
    socializeClient.getNearByUser(application.getUserId(), nearbyUserResultCallBack);
    return view;
  }

  /* initialize views*/
  private void initializeViews(View view) {
    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    recyclerView = (RecyclerView) view.findViewById(R.id.messages_neighbours_list);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
    adapter = new UserChatListAdapter(getActivity());
    recyclerView.setAdapter(adapter);

    swipeRefreshLayout.setColorSchemeColors(
        ContextCompat.getColor(getContext(), R.color.brand), ContextCompat.getColor(getContext(), R.color.green),
        ContextCompat.getColor(getContext(), R.color.brand_status_bar), ContextCompat.getColor(getContext(), R.color.outline_yellow));
    swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener);
    swipeRefreshLayout.setRefreshing(false);
    swipeRefreshLayout.setEnabled(false);

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        //Remove swiped item from list and notify the RecyclerView
        adapter.removeItem(viewHolder.getAdapterPosition());
      }
    };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
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
