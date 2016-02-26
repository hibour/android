package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.dsquare.hibour.fragments.home.TabType;
import com.dsquare.hibour.interfaces.NavDrawerCallback;

/**
 * A simple {@link Fragment} subclass.
 */


public class Home extends Fragment implements PostsTypesDialog.CategoryChooserListener {

    private NavDrawerCallback callback;
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

        }

    /* initialize event listeners*/
    private void initializeEventListeners() {
        feedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createPost.getVisibility() == View.GONE) {
                    createPost.setVisibility(View.VISIBLE);
                }
                applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
                showTab(TabType.FEED);
            }
        });

        socializeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCurrentStateToAppBarIcons(R.drawable.socialize_filled, socializeIcon);
                if (createPost.getVisibility() == View.GONE) {
                    createPost.setVisibility(View.VISIBLE);
                }
                showTab(TabType.SOCIALIZE);
            }
        });

        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCurrentStateToAppBarIcons(R.mipmap.ic_chat_filed, channelsIcon);
                showTab(TabType.MESSAGE);
            }
        });

        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCurrentStateToAppBarIcons(R.drawable.more_filled, moreIcon);
                callback.drawerOpen();
            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomBar1.getVisibility() == View.VISIBLE) {
                   // bottomBar1.setVisibility(View.GONE);
                    createPost.setVisibility(View.GONE);
                }
                categoriesDialog = new PostsTypesDialog();
                categoriesDialog.show(getActivity().getSupportFragmentManager(), "categories");
                categoriesDialog.setTargetFragment(Home.this, 0);
            }
        });
//        searchIcon.setOnClickListener(this);
    }

    private void loadDefaultFragment() {
        applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
        showTab(TabType.FEED);
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

    private Fragment activeFragment = null;
    /* replace tab's fragment*/
    private void showTab(TabType type) {
        boolean isNewFragment = false;
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(type.toString());
        if (fragment == null) {
            isNewFragment = true;
            fragment = getFragment(type);
            if (fragment == null) {
                return;
            }
        }

        switch (type) {
            case NEWPOST:
                Bundle args = new Bundle();
                args.putString(NewPost.CATEGORY_BUNDLE_ARG, categoryName);
                fragment.setArguments(args);
                break;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isNewFragment) {
            fragmentTransaction.add(R.id.home_fragment_container, fragment, type.toString());
        } else {
            fragmentTransaction.show(fragment);
        }
        if (activeFragment != null && activeFragment != fragment) {
            fragmentTransaction.hide(activeFragment);
        }
        fragmentTransaction.commit();
        activeFragment = fragment;
    }

    @Nullable
    private Fragment getFragment(TabType type) {
        Fragment fragment = null;
        switch (type) {
            case FEED:
                fragment = new Posts();
                break;
            case SUGGESTIONS:
                fragment = new Suggestions();
                break;
            case CLASSIFIEDS:
                fragment = new Classifieds();
                break;
            case NEWPOST:
                fragment = new NewPost();
                break;
            case SOCIALIZE:
                fragment = new Socialize();
                break;
            case MESSAGE:
                fragment = new Message();
                break;
        }
        return fragment;
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
        this.categoryName = categoryName;
        dialog.dismiss();
        showTab(TabType.NEWPOST);
    }
}
