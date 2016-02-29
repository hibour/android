package com.dsquare.hibour.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.NearByUserChatListAdapter;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;

import java.util.List;

/**
 * Created by Dsquare Android on 2/24/2016.
 */
public class SearchInChats extends AppCompatActivity implements View.OnClickListener {

  private ImageView backIcon, searchIcon;
  private RecyclerView searchChats;
  private NearByUserChatListAdapter chatsAdapter;
  private Intent data;
  private String name = "", id = "";
  private AutoCompleteTextView autoCompleteTextView;
  private Typeface proxima;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_in_chats);
    data = getIntent();
    name = data.getExtras().getString("value1");
    id = data.getExtras().getString("value");
    Constants.CHATID = "";
    Constants.CHATID = id;
    Log.d("string", name);
    Log.d("string", id);
    initializeViews();
    intializeEventListeners();
  }

  /* initialize views*/
  private void initializeViews() {
    searchIcon = (ImageView) findViewById(R.id.feeds_search_back);
    backIcon = (ImageView) findViewById(R.id.feeds_search_icon);
    searchChats = (RecyclerView) findViewById(R.id.feeds_search_list);
    autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.home_search_autocomplete);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    searchChats.setLayoutManager(layoutManager);
    searchChats.setHasFixedSize(true);
    chatsAdapter = new NearByUserChatListAdapter(this);
    searchChats.setAdapter(chatsAdapter);
    parseUserDetails();
    autoCompleteTextView.setText(name);

    if (Constants.searchChat.size() > 0) {
      Constants.chatList.clear();
      for (String key : Constants.searchChat.keySet()) {
        Constants.chatList.add(key);
      }
    }

    proxima = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
    ArrayAdapter<String> adapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_dropdown_item_1line,
            Constants.chatList);
    autoCompleteTextView.setAdapter(adapter);
    autoCompleteTextView.setThreshold(3);
    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemName = autoCompleteTextView.getText().toString();
        if (parent != null && parent.getChildAt(0) != null) {
          String neighbourName = Constants.chatList.get(position);
          String neighbourid = Constants.searchChat.get(itemName);
          Log.d("catid", neighbourid);
          Log.d("neighbourName", neighbourName);
          Constants.chatList.clear();
          Constants.CHATID = "";
          Constants.CHATID = neighbourid;
          parseUserDetails();
          ((TextView) parent.getChildAt(0)).setTextColor(getResources()
              .getColor(R.color.black_1));
          ((TextView) parent.getChildAt(0)).setTypeface(proxima);
          ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
          ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
          Log.d("itemname", itemName);
        }
      }
    });
  }

  /* initializeEventListeners*/
  private void intializeEventListeners() {
    searchIcon.setOnClickListener(this);
    backIcon.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.feeds_search_back:
        this.finish();
        break;
      case R.id.feeds_search_icon:
        break;
    }
  }

  private void parseUserDetails() {
    List<UserDetail> list = Constants.chatsMap.get(Constants.CHATID);
    chatsAdapter.getUserList().clear();
    chatsAdapter.getUserList().addAll(list);
    chatsAdapter.notifyDataSetChanged();
  }

}
