package com.dsquare.hibour.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import com.dsquare.hibour.adapters.FeedsAdapter;
import com.dsquare.hibour.pojos.posts.Feeds;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;

import java.util.ArrayList;
import java.util.List;

public class SearchInFeeds extends AppCompatActivity implements View.OnClickListener {

  private ImageView backIcon, searchIcon;
  private RecyclerView searchFeeds;
  private List<Feeds> feedsList = new ArrayList<>();
  private List<String[]> feedList = new ArrayList<>();
  private FeedsAdapter postsAdapter;
  private Intent data;
  private String name = "", id = "";
  private List<String> autocompleteList = new ArrayList<>();
  private AutoCompleteTextView autoCompleteTextView;
  private Typeface proxima;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_in_feeds);
    data = getIntent();
    name = data.getExtras().getString("value1");
    id = data.getExtras().getString("value");
    Constants.CATID = "";
    Constants.CATID = id;
    Log.d("string", name);
    Log.d("string", id);
    initializeViews();
    intializeEventListeners();
  }

  /* initialize views*/
  private void initializeViews() {
    searchIcon = (ImageView) findViewById(R.id.feeds_search_back);
    backIcon = (ImageView) findViewById(R.id.feeds_search_icon);
    searchFeeds = (RecyclerView) findViewById(R.id.feeds_search_list);
    autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.home_search_autocomplete);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    searchFeeds.setLayoutManager(layoutManager);
    searchFeeds.setHasFixedSize(true);
    postsAdapter = new FeedsAdapter(this, feedsList);
    searchFeeds.setAdapter(postsAdapter);
    new FeedsTask().execute();

    autoCompleteTextView.setText(name);

    if (Constants.searchMap.size() > 0) {
      autocompleteList.clear();
      for (String key : Constants.searchMap.keySet()) {
        autocompleteList.add(key);
      }
    }

    proxima = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
    ArrayAdapter<String> adapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_dropdown_item_1line,
            autocompleteList);
    autoCompleteTextView.setAdapter(adapter);
    autoCompleteTextView.setThreshold(3);
    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemName = autoCompleteTextView.getText().toString();
        if (parent != null && parent.getChildAt(0) != null) {
          String neighbourName = autocompleteList.get(position);
          String neighbourid = Constants.searchMap.get(itemName);
          Log.d("catid", neighbourid);
          Log.d("neighbourName", neighbourName);
          feedsList.clear();
          Constants.CATID = "";
          Constants.CATID = neighbourid;
          new FeedsTask().execute();
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
     /* asynchronous task to set data to adapter*/

  class FeedsTask extends AsyncTask<Void, String, Void> {

    FeedsAdapter feeds;

    @Override
    protected void onPostExecute(Void result) {
      // Toast.makeText(MainActivity.this, "Loading completed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
      //feeds = (FeedsAdapter) postsRecycler.getAdapter();
    }

    @Override
    protected void onProgressUpdate(String... values) {
      feedsList.add(new Feeds(values[0], values[1], values[2], values[3], values[4], values[5], values[6]
          , values[7], values[8], values[9], values[10], values[11]));
      postsAdapter.notifyDataSetChanged();
    }

    @Override
    protected Void doInBackground(Void... params) {
      List<Postpojos> posts = Constants.postpojosMap.get(Constants.CATID);
      try {
        for (int i = 0; i < posts.size(); i++) {
          publishProgress(posts.get(i).getPostId(), posts.get(i).getPostImage()
              , posts.get(i).getPostMessage()
              , posts.get(i).getPostDate(), posts.get(i).getPostTime()
              , posts.get(i).getUser().getName(), posts.get(i).getUser().getImage()
              , posts.get(i).getUser().getId(), String.valueOf(posts.get(i).getPostUserLiked())
              , String.valueOf(posts.get(i).getPostLikesCount())
              , String.valueOf(posts.get(i).getPostComments().size())
              , posts.get(i).getPostType());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

  }

}
