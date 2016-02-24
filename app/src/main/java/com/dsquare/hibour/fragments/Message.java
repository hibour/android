package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.ChatTypeViewPagerAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.utils.UIHelper;
import com.dsquare.hibour.views.CustomViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Message extends Fragment implements View.OnClickListener {


  private static final String LOG_TAG = Message.class.getSimpleName();
  private EditText searchView;
  private CustomViewPager pager;
  private TabLayout tabs;
  private UIHelper uiHelper;
  private ImageView menuIcon, searchIcon, navigationBack;
  private NavDrawerCallback callback;
  private View searchBar;
  private ChatTypeViewPagerAdapter adapter;
  private TextWatcher textChangeListener = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      BaseChatFragment baseChatFragment = adapter.getFragmentAtPosition(pager.getCurrentItem());

      if (baseChatFragment == null) {
        return;
      }
      if (s.length() > 0) {
        baseChatFragment.loadUserSearchResult(s.toString());
      } else {
        baseChatFragment.removeUserSearchResult();
      }
    }
  };

  public Message() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_message, container, false);
    uiHelper = new UIHelper(getContext());
    initializeViews(view);
    initializeEventListeners();
    return view;
  }

  /* initialize views*/
  private void initializeViews(View view) {
    menuIcon = (ImageView) view.findViewById(R.id.messages_menu_icon);
    searchIcon = (ImageView) view.findViewById(R.id.messages_search_icon);
    pager = (CustomViewPager) view.findViewById(R.id.pager);
    tabs = (TabLayout) view.findViewById(R.id.tabs);
    navigationBack = (ImageView) view.findViewById(R.id.navigation_back);
    searchBar = view.findViewById(R.id.messages_search_bar);
    searchView = (EditText) view.findViewById(R.id.search_key);

    adapter = new ChatTypeViewPagerAdapter(getChildFragmentManager(), getResources().getStringArray(R.array.chat_types));
    tabs.setTabTextColors(ContextCompat.getColorStateList(getContext(), R.color.selector));
    pager.setAdapter(adapter);
    tabs.setupWithViewPager(pager);

    searchView.addTextChangedListener(textChangeListener);
  }

  public void showSearchView() {
    uiHelper.collapse(tabs);
    searchBar.setVisibility(View.VISIBLE);
    pager.setPagingEnabled(false);
    searchView.setText("");
    searchView.requestFocus();
    uiHelper.showKeyboard();
  }

  public boolean hideSearchView() {
    if (tabs.getVisibility() == View.GONE) {
      uiHelper.expand(tabs);
      searchBar.setVisibility(View.GONE);
      pager.setPagingEnabled(true);
      uiHelper.hideKeyboard();
      adapter.getFragmentAtPosition(pager.getCurrentItem()).removeUserSearchResult();
      return true;
    }
    return false;
  }

  /* initialize eventlisteners*/
  private void initializeEventListeners() {
    menuIcon.setOnClickListener(this);
    searchIcon.setOnClickListener(this);
    navigationBack.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.messages_menu_icon:
        callback.drawerOpen();
        break;
      case R.id.messages_search_icon:
        if (tabs.getVisibility() == View.VISIBLE)
          showSearchView();
        else
          hideSearchView();
        break;
      case R.id.navigation_back:
        hideSearchView();
        break;
    }
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    callback = (NavDrawerCallback) activity;
  }

  public boolean onBackPressed() {
    if (hideSearchView())
      return true;
    return false;
  }
}
