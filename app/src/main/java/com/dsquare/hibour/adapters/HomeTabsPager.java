package com.dsquare.hibour.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dsquare.hibour.fragments.FeedsPager;

import java.util.List;

/**
 * Created by Aditya Ravikanti on 1/2/2016.
 */
public class HomeTabsPager extends FragmentStatePagerAdapter {

    private List<String> tabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public HomeTabsPager(FragmentManager fm,List<String> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Fragment fg = new FeedsPager();
        Bundle args = new Bundle();
        args.putString("categoryName",tabs.get(position));
        fg.setArguments(args);
        return fg;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return tabs.size();
    }
}
