package com.dsquare.hibour.fragments;


import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.HomeTabsPager;
import com.dsquare.hibour.adapters.PostsAdapter;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posts.PostData;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.SlidingTabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Posts extends Fragment implements View.OnClickListener {
    private RecyclerView postsRecycler;
    private List<String[]> postsList = new ArrayList<>();
    private PostsAdapter postsAdapter;
    private NetworkDetector networkDetector;
    private Gson gson;
    private Hibour application;
    private PostsClient postsClient;
    private ProgressDialog postsDialog;
    private AutoCompleteTextView autoCompleteTextView;
    private RelativeLayout searchLayout;
    private TextView textView,invite;
    private ImageView searchIcon;
    private List<String> autocompleteList = new ArrayList<>();
    private Typeface proxima;
    private List<String> tabsList = new ArrayList<>();
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private RelativeLayout noFeedsLayout;
    public Posts() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        initializeViews(view);
        initializeEventListeners();
        getAllposts();
        return view;
    }



    /* initializeViews*/
    private void initializeViews(View view){
        noFeedsLayout = (RelativeLayout)view.findViewById(R.id.no_feeds_found_layout);
        proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
        postsClient = new PostsClient(getActivity());
        gson = new Gson();
        networkDetector = new NetworkDetector(getActivity());
        application =  Hibour.getInstance(getActivity());
//        postsRecycler = (RecyclerView)view.findViewById(R.id.post_posts_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        postsRecycler.setLayoutManager(layoutManager);
//        postsRecycler.setHasFixedSize(true);
        autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.home_search_autocomplete);
        searchLayout = (RelativeLayout)view.findViewById(R.id.home_search_layout);
        searchIcon = (ImageView)view.findViewById(R.id.home_search_icon);
        textView = (TextView)view.findViewById(R.id.home_fragment_title);
        invite = (TextView)view.findViewById(R.id.invite_button);
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
                }}
        });

        pager = (ViewPager)view.findViewById(R.id.posts_pager);
        tabs = (SlidingTabLayout)view.findViewById(R.id.posts_tabs);
        tabs.setDistributeEvenly(false);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
        tabs.setTabsBackgroundColor(getResources().getColor(R.color.gray));
    }
    private void initializeEventListeners() {
        invite.setOnClickListener(this);
        searchIcon.setOnClickListener(this);
    }
    /* get all posts from server*/
    private void getAllposts(){
        if(networkDetector.isConnected()){
            postsDialog = ProgressDialog.show(getActivity(),"","Please wait...");
            postsClient.getAllPosts(application.getUserId(),new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parsePostsDetails(jsonObject);
                    closePostsDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("posts error",error.toString());
                    closePostsDialog();
                }
            });
        }else{
            Toast.makeText(getActivity(),"Check network connection",Toast.LENGTH_LONG).show();
        }
    }
    /* parse posts details*/
    private void parsePostsDetails(JSONObject jsonObject) {
        Log.d("post data", jsonObject.toString());
        PostData posts = gson.fromJson(jsonObject.toString(), PostData.class);
        List<Postpojos> postpojos = posts.getData();
        if(postpojos.size()>0){
            if (Constants.postsMap.size() > 0) {
                Constants.postsMap.clear();
            }
            if (tabsList.size() > 0) {
                tabsList.clear();
            }
            tabsList.add("All");
            for (Postpojos p:postpojos) {

                Constants.postpojosMap.put(p.getPostId(),postpojos);
                    String key = " ";
                    if (Constants.categoriesMap.containsKey(p.getPostType()))
                        key = Constants.categoriesMap.get(p.getPostType());
                    if (!Constants.postsMap.containsKey(key)) {
                        List<Postpojos> data = new ArrayList<>();
                        data.add(p);
                        Log.d("key",key);
                        Constants.postsMap.put(key, data);
                    } else {
                        List<Postpojos> postslist = Constants.postsMap.get(key);
                        postslist.add(p);
                        Log.d("key",key);
                        Constants.postsMap.put(key, postslist);
                    }
                    if (!tabsList.contains(key))
                        tabsList.add(key);
                    String[] data = new String[8];
                    data[0] = p.getUser().getName();
                    data[1] = p.getPostId();
                    data[2] = p.getPostMessage();
                    data[3] = key;
                    data[4] = String.valueOf(p.getPostLikesCount());
                    data[5] = Arrays.toString(new int[]{p.getPostComments().size()})
                            .replaceAll("\\[|\\]", "");
                    data[6] = p.getPostId();
                    data[7] = String.valueOf(p.getPostUserLiked());

                    postsList.add(data);
                    Log.d("data",""+data);
                    autocompleteList.add(p.getPostMessage());

                    setPager();
            }
        }else{
            noFeedsLayout.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.GONE);
            pager.setVisibility(View.GONE);
        }

        //setAdapters(postsList);
    }
    /*set pager adapter*/
    private void setPager(){
        HomeTabsPager pagerAdapter = new HomeTabsPager(getFragmentManager(),tabsList);
        pager.setAdapter(pagerAdapter);
        tabs.setViewPager(pager);
    }
    private void setAdapters(List<String[]> postsList){
        postsRecycler.setAdapter(new PostsAdapter(getActivity(), postsList));
    }
    /* close posts dialog*/
    private void closePostsDialog(){
        if(postsDialog!=null){
            if(postsDialog.isShowing()){
                postsDialog.dismiss();
                postsDialog=null;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_button:
                inviteFriends(getString(R.string.invite_msg));
                break;
            case R.id.home_search_icon:
                if(searchLayout.getVisibility()==View.GONE){
                    searchLayout.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    invite.setVisibility(View.GONE);
                }else{
                    searchLayout.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    invite.setVisibility(View.VISIBLE);
                }
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
