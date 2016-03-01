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
import com.dsquare.hibour.fragments.home.TabType;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */


public class Home extends Fragment implements PostsTypesDialog.CategoryChooserListener, NewPost.PostsListener {

    private static final String NEW_POST_NAME = "newpost";
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
    private HibourBaseTabFragment activeFragment = null;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(view);
        //initializeEventListeners();
        loadDefaultFragment();
        return view;
    }

//        searchIcon.setOnClickListener(this);

   /* private void loadDefaultFragment() {
        applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
        showTab(TabType.FEED);
    }*/

   /* private void loadDefaultFragment() {
        applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
        showTab(TabType.FEED);
    }*/

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

        feedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(TabType.FEED);
            }
        });

        socializeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(TabType.SOCIALIZE);
            }
        });

        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(TabType.MESSAGE);
            }
        });

        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(TabType.MORE);
            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomBar1.getVisibility() == View.VISIBLE) {
                    bottomBar1.setVisibility(View.GONE);
                    createPost.setVisibility(View.GONE);
                }
                categoriesDialog = new PostsTypesDialog();
                categoriesDialog.show(getActivity().getSupportFragmentManager(), "categories");
                categoriesDialog.setTargetFragment(Home.this, 0);
            }
        });
    }

    /* initialize event listeners
    private void initializeEventListeners() {
        feedLayout.setOnClickListener(this);
        socializeLayout.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        moreLayout.setOnClickListener(this);
        createPost.setOnClickListener(this);
    }
*/
    private void loadDefaultFragment() {
        switch (Constants.FRAGMENT_POS) {
            case 0:
                if (createPost.getVisibility() == View.GONE) {
                    createPost.setVisibility(View.VISIBLE);
                }
                applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
                showTab(TabType.FEED);
                break;
            case 4:
                applyCurrentStateToAppBarIcons(R.drawable.socialize_filled, socializeIcon);
                if (createPost.getVisibility() == View.GONE) {
                    createPost.setVisibility(View.VISIBLE);
                }
                showTab(TabType.SOCIALIZE);
                break;
            case 5:
                applyCurrentStateToAppBarIcons(R.mipmap.ic_chat_filed, channelsIcon);
                showTab(TabType.MESSAGE);
                break;

        }
        //   applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
        // replaceContainer(Constants.FRAGMENT_POS);
//        Fragment fragment = new NewPosts();
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
    private void showTab(TabType type) {
        boolean isNewFragment = false;
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(type.toString());
        HibourBaseTabFragment hibourFragment = null;
        if (!(fragment instanceof HibourBaseTabFragment)) {
            isNewFragment = true;
            hibourFragment = getFragment(type);
        } else {
            hibourFragment = (HibourBaseTabFragment) fragment;
        }

        if (hibourFragment == null || activeFragment == hibourFragment) {
            return;
        }


        //createPost.setVisibility(View.VISIBLE);
        switch (type) {
            case FEED:
                createPost.setVisibility(View.VISIBLE);
                applyCurrentStateToAppBarIcons(R.drawable.feed_filled, feedIcon);
                break;
            case SOCIALIZE:
                createPost.setVisibility(View.GONE);
                applyCurrentStateToAppBarIcons(R.drawable.socialize_filled, socializeIcon);
                break;
            case MESSAGE:
                createPost.setVisibility(View.GONE);
                applyCurrentStateToAppBarIcons(R.mipmap.ic_chat_filed, channelsIcon);
                break;
            case MORE:
                createPost.setVisibility(View.GONE);
                applyCurrentStateToAppBarIcons(R.drawable.more_filled, moreIcon);
                break;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isNewFragment) {
            fragmentTransaction.add(R.id.home_fragment_container, hibourFragment, type.toString());
        } else {
            fragmentTransaction.show(hibourFragment);
        }
        if (activeFragment != null) {
            activeFragment.onHidden();
            fragmentTransaction.hide(activeFragment);
        }
        fragmentTransaction.commit();
        activeFragment = hibourFragment;
        activeFragment.onVisible();
    }

    @Nullable
    private HibourBaseTabFragment getFragment(TabType type) {
        HibourBaseTabFragment fragment = null;
        switch (type) {
            case FEED:
                fragment = new Posts();
                break;
            case SOCIALIZE:
                fragment = new Socialize();
                break;
            case MESSAGE:
                fragment = new Message();
                break;
            case MORE:
                fragment = new More();
                break;
        }
        return fragment;
    }

    @Override
    public void onCancel(DialogFragment dialog) {
        bottomBar1.setVisibility(View.VISIBLE);
        createPost.setVisibility(View.VISIBLE);
        dialog.dismiss();
    }

    @Override
    public void onCategorySelected(String categoryName, DialogFragment dialog) {
        this.categoryName = categoryName;
        dialog.dismiss();
        showNewPost();
    }

    @Override
    public void onNewPostCancelled() {
        closeNewPostFragment();
    }

    @Override
    public void onNewPostPosted() {
        closeNewPostFragment();
    }

    private void showNewPost() {
        Fragment fragment = new NewPost();
        Bundle args = new Bundle();
        args.putString(NewPost.CATEGORY_BUNDLE_ARG, categoryName);
        Log.d("Home",categoryName);
        fragment.setArguments(args);
        fragment.setTargetFragment(Home.this, 0);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_fragment_container, fragment, NEW_POST_NAME);
        fragmentTransaction.addToBackStack(NEW_POST_NAME);
        fragmentTransaction.commit();
        if (activeFragment != null) {
            activeFragment.onHidden();
        }
    }

    private void closeNewPostFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(NEW_POST_NAME);
        if (fragment == null) {
            return;
        }
        bottomBar1.setVisibility(View.VISIBLE);
        createPost.setVisibility(View.VISIBLE);
        fragmentManager.popBackStack();
        if (activeFragment != null) {
            activeFragment.onVisible();
        }
    }
}
