package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.NavDrawerCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TextView inviteBtn;
    private NavDrawerCallback callback;
    private boolean isHome = true;
    private ImageView feedIcon, socializeIcon, newPostIcon, channelsIcon, moreIcon,searchIcon;
    private AutoCompleteTextView autoCompleteTextView;
    private RelativeLayout searchLayout;
    private TextView textView,invite;
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
        feedIcon = (ImageView) view.findViewById(R.id.home_feed);
        socializeIcon = (ImageView) view.findViewById(R.id.home_socialize_icon);
        newPostIcon = (ImageView) view.findViewById(R.id.home_new_post);
        channelsIcon = (ImageView) view.findViewById(R.id.home_channels);
        moreIcon = (ImageView) view.findViewById(R.id.home_more_icon);
//        autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.home_search_autocomplete);
//        searchLayout = (RelativeLayout)view.findViewById(R.id.home_search_layout);
//        searchIcon = (ImageView)view.findViewById(R.id.home_search_icon);
//        textView = (TextView)view.findViewById(R.id.home_fragment_title);
//        invite = (TextView)view.findViewById(R.id.invite_button);
    }

    /* initialize event listeners*/
    private void initializeEventListeners() {
//        inviteBtn.setOnClickListener(this);
        feedIcon.setOnClickListener(this);
        socializeIcon.setOnClickListener(this);
        newPostIcon.setOnClickListener(this);
        channelsIcon.setOnClickListener(this);
        moreIcon.setOnClickListener(this);
//        searchIcon.setOnClickListener(this);
    }

    private void loadDefaultFragment() {
        replaceContainer(3);
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
            case R.id.home_feed:
                applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
                replaceContainer(0);
                break;
            case R.id.home_socialize_icon:
                applyCurrentStateToAppBarIcons(R.drawable.socialize_filled, socializeIcon);
                replaceContainer(4);
                break;
            case R.id.home_new_post:
                LinearLayout bottomBar = (LinearLayout) this.getActivity().findViewById(R.id.home_bottom_menu);
                if (bottomBar.getVisibility() == View.VISIBLE) {
                    applyCurrentStateToAppBarIcons(R.drawable.cancel_filled, newPostIcon);

                    //mask the rest of the screen
                    FrameLayout screenrest = (FrameLayout) this.getActivity().findViewById(R.id.home_fragment_container);
                    screenrest.setBackgroundColor(getResources().getColor(R.color.black_transparent));

                    bottomBar.setVisibility(View.GONE);

                    replaceContainer(3);
                } else {
                    //TODO: Need to clean this up
                    applyCurrentStateToAppBarIcons(R.drawable.post, newPostIcon);

                    ImageView socializeIcon = (ImageView) this.getActivity().findViewById(R.id.home_socialize_icon);
                    socializeIcon.setImageResource(R.drawable.socialize_filled);

                    replaceContainer(4);

                    bottomBar.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.home_channels:
                applyCurrentStateToAppBarIcons(R.drawable.channels_filled, channelsIcon);
                break;
            case R.id.home_more_icon:
                applyCurrentStateToAppBarIcons(R.drawable.more_filled, moreIcon);
                callback.drawerOpen();
                break;
//            case R.id.home_search_icon:
//
//                break;

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
        newPostIcon.setImageResource(R.drawable.post);
        channelsIcon.setImageResource(R.drawable.channels);
        moreIcon.setImageResource(R.drawable.more);

        icon.setImageResource(res);
    }

    /* replace tab's fragment*/
    public void replaceContainer(int id) {
        Fragment fragment = null;
        switch (id) {
            case 0:
                isHome = false;
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
                fragment = new CreatePost();
                break;
            case 4:
                isHome = true;
                fragment = new Socialize();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.home_fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }
}
