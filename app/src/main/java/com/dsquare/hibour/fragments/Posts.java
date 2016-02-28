package com.dsquare.hibour.fragments;


import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.SearchInFeeds;
import com.dsquare.hibour.adapters.FeedsPagerAdapter;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.database.table.FeedsTable;
import com.dsquare.hibour.interfaces.PostsCallback;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posts.PostData;
import com.dsquare.hibour.pojos.posts.PostLikedUser;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.SlidingTabLayout;
import com.dsquare.hibour.utils.UIHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class Posts extends HibourBaseTabFragment implements View.OnClickListener, PostsCallback {
    private NetworkDetector networkDetector;
    private Gson gson;
    private PostsClient postsClient;
    private AutoCompleteTextView autoCompleteTextView;
    private FeedsPagerAdapter mPagerAdapter;
    private RelativeLayout searchLayout;
    private TextView textView,invite;
    private ImageView searchIcon, navigationBack;
    private List<String> autocompleteList = new ArrayList<>();
    private Typeface proxima;
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private RelativeLayout noFeedsLayout;
    private DialogFragment welcomeDialog;
    private Context context;
    private Hibour application;
    private ProgressDialog newpostDialogue;
    private CoordinatorLayout coordinatorLayout;
    private UIHelper uiHelper;
    private View searchBar;
    private DatabaseHandler dbHandler;

    public Posts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        uiHelper = new UIHelper(getContext());
        initializeViews(view);
        initializeEventListeners();
        refreshPosts();
        return view;
    }

    /* initializeViews*/
    private void initializeViews(View view){
        dbHandler = new DatabaseHandler(getActivity().getApplicationContext());
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
            .coordinatorLayout);
        noFeedsLayout = (RelativeLayout)view.findViewById(R.id.no_feeds_found_layout);
        proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
        postsClient = new PostsClient(getActivity());
        gson = new Gson();
        networkDetector = new NetworkDetector(getActivity());
        application =  Hibour.getInstance(getActivity());
        searchBar = view.findViewById(R.id.messages_search_bar);
        navigationBack = (ImageView) view.findViewById(R.id.navigation_back);
        autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.home_search_autocomplete);
        searchLayout = (RelativeLayout)view.findViewById(R.id.home_search_layout);
        searchIcon = (ImageView)view.findViewById(R.id.home_search_icon);
        textView = (TextView)view.findViewById(R.id.home_fragment_title);
        invite = (TextView)view.findViewById(R.id.invite_button);

        pager = (ViewPager) view.findViewById(R.id.posts_pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = mPagerAdapter.getItem(position);
                if (fragment instanceof FeedsPager) {
                    ((FeedsPager) fragment).refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabs = (SlidingTabLayout) view.findViewById(R.id.posts_tabs);
        tabs.setDistributeEvenly(false);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.newbrand);
            }
        });
        tabs.setTabsBackgroundColor(getResources().getColor(R.color.white));
    }

    private void initializeEventListeners() {
        invite.setOnClickListener(this);
        searchIcon.setOnClickListener(this);
        navigationBack.setOnClickListener(this);
    }

    @Override
    public void onVisible() {
        super.onVisible();
        if (coordinatorLayout != null) {
            refreshPosts();
        }
    }

    private void showSearchView() {
        uiHelper.collapse(tabs);
        invite.setVisibility(View.GONE);
        searchBar.setVisibility(View.VISIBLE);
        autoCompleteTextView.setText("");
        autoCompleteTextView.requestFocus();
        uiHelper.showKeyboard();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_dropdown_item_1line,
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
                    Intent intent = new Intent(getActivity(), SearchInFeeds.class);
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

    private boolean hideSearchView() {
        if (tabs.getVisibility() == View.GONE) {
            uiHelper.expand(tabs);
            searchBar.setVisibility(View.GONE);
            invite.setVisibility(View.VISIBLE);
            uiHelper.hideKeyboard();
            return true;
        }
        return false;
    }

    /* get all posts from server*/
    private void refreshPosts() {
        if(networkDetector.isConnected()){
            postsClient.getAllPosts(application.getUserId(), new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parsePostsDetails(jsonObject);
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("posts error", error.toString());
                }
            });
        }else{
            Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
    }


   /* public void welcomeDialog() {
        Log.d("posts", "sharedpreferences");
        if (application.getIsFirst()) {
            Log.d("posts", "if");
            openWelcomeDialog();
        } else {

        }
    }*/

    /*private void openWelcomeDialog() {
        Log.d("Posts", "welcome");
        welcomeDialog = new WelcomeDialog();
        Log.d("Postss","choose dialog");
        welcomeDialog.show(getActivity().getSupportFragmentManager(), "chooser dialog");
        welcomeDialog.setTargetFragment(this, 0);
    }*/

    @Override
    public void openDialog(DialogFragment dialogFragment) {
        Log.d("posts", "close dialog");
        application.setIsFirst(false);
     /*   AlertDialog closeDialog = new AlertDialog.Builder(getActivity()).create();
        closeDialog.dismiss();*/
        sendPostData("Hi Iam new here");
        welcomeDialog.dismiss();
    }

    @Override
    public void closeDialog(DialogFragment dialogFragment) {
        application.setIsFirst(false);
        welcomeDialog.dismiss();
    }


    /* parse posts details*/
    private void parsePostsDetails(JSONObject jsonObject) {
        Log.d("post data", jsonObject.toString());
        PostData posts = gson.fromJson(jsonObject.toString(), PostData.class);
        dbHandler.insertFeeds(posts);
        List<FeedsTable> feedsData = dbHandler.getFeeds();
        Log.d("feeds size", dbHandler.getFeeds().size() + "");
        List<Postpojos> postpojos = posts.getData();

        Map<String, List<FeedsTable>> postsMap = new LinkedHashMap<>();
        Map<String, List<FeedsTable>> postpojosMap = new LinkedHashMap<>();
        Map<String, List<PostLikedUser>> postlikesMap = new LinkedHashMap<>();
        Map<String, String> categoriesMap = new HashMap<>();
        Map<String, String> searchMap = new LinkedHashMap<String, String>();
        Map<String, Map<String, String>> postTypesMap = new LinkedHashMap<>();
        Set<String> postTypesSet = new HashSet<>();

        if (feedsData.size() > 0) {
            postTypesSet.add("All");
            for (FeedsTable p : feedsData) {
                //postlikesMap.put(p., p.getPostLikedUsers());

                List<FeedsTable> data1 = new ArrayList<>();
                data1.add(p);
                postpojosMap.put(p.postid, data1);

                searchMap.put(p.description, p.postid);

                String postType = p.posttype;
                List<FeedsTable> postslist = postsMap.get(postType);
                if (postslist == null) {
                    postslist = new ArrayList<>();
                    postsMap.put(postType, postslist);
                }
                postslist.add(p);

                if (!TextUtils.isEmpty(postType)) {
                    postTypesSet.add(postType);
                }
            }

            List<String> postTypeList = new ArrayList<>();
            postTypeList.addAll(postTypesSet);
            Collections.sort(postTypeList);

            Constants.postsMap = postsMap;
            Constants.postpojosMap = postpojosMap;
            Constants.postlikesMap = postlikesMap;
            Constants.searchMap = searchMap;

            updatePager(postTypeList);

            autocompleteList.clear();
            for (String key : searchMap.keySet()) {
                autocompleteList.add(key);
            }
        } else {
            noFeedsLayout.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.GONE);
            pager.setVisibility(View.GONE);
        }
        /*if(postpojos.size()>0){
            postTypesSet.add("All");
            for (Postpojos p : postpojos) {
                postlikesMap.put(p.getPostId(), p.getPostLikedUsers());

                List<Postpojos> data1 = new ArrayList<>();
                data1.add(p);
                postpojosMap.put(p.getPostId(), data1);

                searchMap.put(p.getPostMessage(), p.getPostId());

                String postType = p.getPostTypeName() ;
                List<Postpojos> postslist = postsMap.get(postType);
                if (postslist == null) {
                    postslist = new ArrayList<>();
                    postsMap.put(postType, postslist);
                }
                postslist.add(p);

                if (!TextUtils.isEmpty(postType)) {
                    postTypesSet.add(postType);
                }
            }

            List<String> postTypeList = new ArrayList<>();
            postTypeList.addAll(postTypesSet);
            Collections.sort(postTypeList);

            Constants.postsMap = postsMap;
            Constants.postpojosMap = postpojosMap;
            Constants.postlikesMap = postlikesMap;
            Constants.searchMap = searchMap;

            updatePager(postTypeList);

            autocompleteList.clear();
            for (String key : searchMap.keySet()) {
                autocompleteList.add(key);
            }
        }else{
            noFeedsLayout.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.GONE);
            pager.setVisibility(View.GONE);
        }*/
    }

    //  send data to server
    private void sendPostData(String postMessage) {
        if (networkDetector.isConnected()) {
            String cat_str = "0";
            newpostDialogue = ProgressDialog.show(getActivity(), "", getResources()
                .getString(R.string.progress_dialog_text));
            postsClient.insertonPost(application.getUserId(), cat_str, "0", postMessage, ""
                , "1", "", new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parsePostDetails(jsonObject);
                    closePostDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("govt", error.toString());
                    closePostDialog();
                }
            });
        } else {
            Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
    }

    /* parse insert post  data*/
    private void parsePostDetails(JSONObject jsonObject) {
        Log.d("json", jsonObject.toString());
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Post update successfully!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Post updation failed!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }

    }

    /* close post dialog*/
    private void closePostDialog() {
        if (newpostDialogue != null) {
            if (newpostDialogue.isShowing()) {
                newpostDialogue.dismiss();
                newpostDialogue = null;
            }
        }
    }

    /*set pager adapter*/
    private void updatePager(List<String> postTypes) {
        if (mPagerAdapter == null) {
            mPagerAdapter = new FeedsPagerAdapter(getFragmentManager());
        }
        mPagerAdapter.updatePostTypes(postTypes);
        if (postTypes.size() < 4)
            tabs.setDistributeEvenly(true);
        else
            tabs.setDistributeEvenly(false);
        pager.setAdapter(mPagerAdapter);
        tabs.setViewPager(pager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_button:
                inviteFriends(getString(R.string.invite_msg));
                break;
            case R.id.home_search_icon:
                if (tabs.getVisibility() == View.VISIBLE)
                    showSearchView();
                else
                    hideSearchView();
//                if(searchLayout.getVisibility()==View.GONE){
//                    searchLayout.setVisibility(View.VISIBLE);
//                    textView.setVisibility(View.GONE);
//                    invite.setVisibility(View.GONE);
//                }else{
//                    searchLayout.setVisibility(View.GONE);
//                    textView.setVisibility(View.VISIBLE);
//                    invite.setVisibility(View.VISIBLE);
//                }
                break;
            case R.id.navigation_back:
                hideSearchView();
                break;
        }
    }
    /*invite friends*/
    private void inviteFriends(String invitationMessage) {
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
        sendIntent.setType("text/plain");

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            Log.d("Package Name", packageName);
            if (packageName.contains("android.talk")
                    || packageName.contains("facebook")
                    || packageName.contains("whatsapp")
                    || packageName.contains("mms")
                    || packageName.contains("android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
                intent.setPackage(packageName);
                targetShareIntents.add(intent);
            }
        }
        if (!targetShareIntents.isEmpty()) {
            System.out.println("Have Intent");
            Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to invite friends");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
        } else {

        }
    }


}
