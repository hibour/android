package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.dialogs.PostsTypesDialog;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */


public class Home extends Fragment implements View.OnClickListener, PostsTypesDialog.categoryChooserListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TextView inviteBtn;
    private NavDrawerCallback callback;
    private boolean isHome = true;
    private ImageView feedIcon, socializeIcon, newPostIcon, channelsIcon, moreIcon,postimage,searchIcon;
    private LinearLayout feedLayout, socializeLayout, messageLayout, moreLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private RelativeLayout searchLayout;
    private LinearLayout bottomBar1;
    private TextView textView,invite;
    private FloatingActionButton createPost;
    private DialogFragment categoriesDialog;
    private String categoryName = "";
    private CoordinatorLayout coordinatorLayout;
    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(view);
        initializeEventListeners();
        loadDefaultFragment();
        return view;
    }

    /*initialize views*/
    private void initializeViews(View view) {
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);
        createPost = (FloatingActionButton)view.findViewById(R.id.feeds_create_fab);
        feedIcon = (ImageView) view.findViewById(R.id.home_feed);
        socializeIcon = (ImageView) view.findViewById(R.id.home_socialize_icon);
//       newPostIcon = (ImageView) view.findViewById(R.id.home_new_post);
        channelsIcon = (ImageView) view.findViewById(R.id.home_channels);
        moreIcon = (ImageView) view.findViewById(R.id.home_more_icon);
        //postimage = (ImageView) view.findViewById(R.id.home_post);
        bottomBar1 = (LinearLayout) view.findViewById(R.id.home_bottom_menu);
        feedLayout = (LinearLayout) view.findViewById(R.id.home_feed_layout);
        socializeLayout = (LinearLayout) view.findViewById(R.id.home_socialize_layout);
        messageLayout = (LinearLayout) view.findViewById(R.id.home_message_layout);
        moreLayout = (LinearLayout) view.findViewById(R.id.home_more_layout);

        Constants.categoriesMap.put("6", "General");
        Constants.categoriesMap.put("2","Suggestions");
        Constants.categoriesMap.put("3","Classifieds");
        Constants.categoriesMap.put("4", "Crime & saftey");
        Constants.categoriesMap.put("5", "Lost & Found");
//        autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.home_search_autocomplete);
//        searchLayout = (RelativeLayout)view.findViewById(R.id.home_search_layout);
//        searchIcon = (ImageView)view.findViewById(R.id.home_search_icon);
//        textView = (TextView)view.findViewById(R.id.home_fragment_title);
//        invite = (TextView)view.findViewById(R.id.invite_button);

    }

    /* initialize event listeners*/
    private void initializeEventListeners() {
//        inviteBtn.setOnClickListener(this);
        feedLayout.setOnClickListener(this);
        socializeLayout.setOnClickListener(this);
//        newPostIcon.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        moreLayout.setOnClickListener(this);
//        postimage.setOnClickListener(this);
        createPost.setOnClickListener(this);
//        searchIcon.setOnClickListener(this);
    }

    private void loadDefaultFragment() {
        applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
        replaceContainer(0);
//        Fragment fragment = new NewPosts();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_button:
                inviteFriends(getString(R.string.invite_msg));
                break;
            case R.id.home_feed_layout:
                if(createPost.getVisibility()==View.GONE)
                    createPost.setVisibility(View.VISIBLE);
                applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
                replaceContainer(0);
                break;
            case R.id.home_socialize_layout:
                applyCurrentStateToAppBarIcons(R.drawable.socialize_filled, socializeIcon);
                if(createPost.getVisibility()==View.GONE)
                    createPost.setVisibility(View.VISIBLE);
                replaceContainer(4);
                break;
            case R.id.feeds_create_fab:
                if (bottomBar1.getVisibility() == View.VISIBLE) {
//                    newPostIcon.setVisibility(View.GONE);
                    bottomBar1.setVisibility(View.GONE);
//                postimage.setVisibility(View.VISIBLE);
                    createPost.setVisibility(View.GONE);
                }
                categoriesDialog = new PostsTypesDialog();

                categoriesDialog.show(getActivity().getSupportFragmentManager(), "categories");
                categoriesDialog.setTargetFragment(this, 0);
                //replaceContainer(3);
                break;
            case R.id.home_message_layout:
                applyCurrentStateToAppBarIcons(R.mipmap.ic_chat_filed, channelsIcon);
                replaceContainer(5);
                break;
            case R.id.home_more_layout:
                applyCurrentStateToAppBarIcons(R.drawable.more_filled, moreIcon);
                callback.drawerOpen();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }

    public void applyCurrentStateToAppBarIcons(int res, ImageView icon) {
        feedIcon.setImageResource(R.drawable.feed);
        socializeIcon.setImageResource(R.drawable.socialize);
        //  newPostIcon.setImageResource(R.drawable.post);
        channelsIcon.setImageResource(R.mipmap.ic_chat_gray);
        moreIcon.setImageResource(R.drawable.more);
        icon.setImageResource(res);
    }

    /* replace tab's fragment*/
    public void replaceContainer(int id) {
        Fragment fragment = null;
        switch (id) {
            case 0:
                isHome = false;
                Log.d("Home", "feed");
                fragment = new Posts();
                break;
            case 1:
                isHome = false;
                fragment = new Suggestions();
                break;
            case 2:
                isHome = false;
                fragment = new Classifieds();
                break;
            case 3:
                isHome = true;
                fragment = new NewPost();
                Bundle args = new Bundle();
                args.putString("category", categoryName);
                fragment.setArguments(args);
                break;
            case 4:
                isHome = true;
                fragment = new Socialize();
                break;
            case 5:
                isHome = true;
                fragment = new Message();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCancel(DialogFragment dialog) {
        if (bottomBar1.getVisibility() == View.GONE) {
            bottomBar1.setVisibility(View.VISIBLE);
            createPost.setVisibility(View.VISIBLE);
        }
        dialog.dismiss();
    }

    @Override
    public void onCategorySelected(String categoryName, DialogFragment dialog) {
        this.categoryName = "";
        this.categoryName = categoryName;
        dialog.dismiss();
        replaceContainer(3);
    }
}
