package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.SearchInChats;
import com.dsquare.hibour.adapters.ChatTypeViewPagerAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.UIHelper;
import com.dsquare.hibour.views.CustomViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Message extends HibourBaseTabFragment implements View.OnClickListener {


  private static final String LOG_TAG = Message.class.getSimpleName();
//  private EditText searchView;
  private CustomViewPager pager;
  private TabLayout tabs;
  private UIHelper uiHelper;
  private ImageView menuIcon, searchIcon, navigationBack;
  private NavDrawerCallback callback;
  private View searchBar;
  private Typeface proxima;
  private ChatTypeViewPagerAdapter adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private RelativeLayout searchLayout;
    private TextView name;
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
    proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
    menuIcon = (ImageView) view.findViewById(R.id.messages_menu_icon);
    searchIcon = (ImageView) view.findViewById(R.id.messages_search_icon);
    pager = (CustomViewPager) view.findViewById(R.id.pager);
    tabs = (TabLayout) view.findViewById(R.id.tabs);
    navigationBack = (ImageView) view.findViewById(R.id.navigation_back);
    searchBar = view.findViewById(R.id.messages_search_bar);
//    searchView = (EditText) view.findViewById(R.id.search_key);
    autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.home_search_autocomplete);
    searchLayout = (RelativeLayout)view.findViewById(R.id.home_search_layout);
    name = (TextView)view.findViewById(R.id.neighbours_name);
    adapter = new ChatTypeViewPagerAdapter(getChildFragmentManager(), getResources().getStringArray(R.array.chat_types));
    tabs.setTabTextColors(ContextCompat.getColorStateList(getContext(), R.color.selector));
    pager.setAdapter(adapter);
    tabs.setupWithViewPager(pager);

//    searchView.addTextChangedListener(textChangeListener);
  }

  public void showSearchView() {
    uiHelper.collapse(tabs);
    searchBar.setVisibility(View.VISIBLE);
    pager.setPagingEnabled(false);
    autoCompleteTextView.setText("");
    autoCompleteTextView.requestFocus();
    uiHelper.showKeyboard();

          ArrayAdapter<String> adapter = new ArrayAdapter<String>
                  (getActivity(), android.R.layout.simple_dropdown_item_1line,
                          Constants.chatList);
          autoCompleteTextView.setAdapter(adapter);
          autoCompleteTextView.setThreshold(1);
          autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  String itemName = autoCompleteTextView.getText().toString();
                  if (parent != null && parent.getChildAt(0) != null) {
                      String neighbourName = Constants.chatList.get(position);
                      String neighbourid = Constants.searchChat.get(itemName);
                      Log.d("catid", neighbourid);
                      Intent intent = new Intent(getActivity(), SearchInChats.class);
                      intent.putExtra("value", neighbourid);
                      intent.putExtra("value1", itemName);
                      startActivity(intent);
                      Log.d("neighbourName", neighbourName);
//                        if(!cardType.equals("Select Card")){
//                            cardTypeId = searchMap.get(cardType);
//                            Log.d("cardtype",cardType);
//                        }
                      ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                              .getColor(R.color.black_1));
                      ((TextView) parent.getChildAt(0)).setTypeface(proxima);
                      ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                      ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                      Log.d("itemname", itemName);
//                setRecyclerList(itemName);
                  }
              }
          });

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
    searchIcon.setOnClickListener(this);
    navigationBack.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
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
