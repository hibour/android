package com.dsquare.hibour.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dsquare.hibour.fragments.FeedsPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aditya Ravikanti on 1/2/2016.
 */
public class FeedsPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> postTypes; // Store the number of postTypes, this will also be passed when the ViewPagerAdapter is created
    private Map<String, FeedsPager> postTypeToFragmentMap;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public FeedsPagerAdapter(FragmentManager fm) {
        super(fm);
        postTypes = new ArrayList<>();
        postTypeToFragmentMap = new HashMap<>();
    }

    public void updatePostTypes(List<String> postTypes) {
        this.postTypes = postTypes;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        String postType = postTypes.get(position);
        FeedsPager fg = postTypeToFragmentMap.get(postType);
        if (fg == null) {
            fg = new FeedsPager();
            Bundle args = new Bundle();
            args.putString(FeedsPager.CATEGORY_BUNDLE_ARGUMENT, postType);
            fg.setArguments(args);
            postTypeToFragmentMap.put(postType, fg);
        }
        return fg;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return postTypes.get(position);
    }

    @Override
    public int getCount() {
        return postTypes.size();
    }
}
