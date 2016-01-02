package com.dsquare.hibour.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dsquare.hibour.fragments.Posts;

/**
 * Created by Aditya Ravikanti on 1/2/2016.
 */
public class HomeTabsPager extends FragmentPagerAdapter {

    private int numbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private String[] tabNames ;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public HomeTabsPager(FragmentManager fm,int numbOfTabs,
                                String[] tabNames) {
        super(fm);
        this.numbOfTabs = numbOfTabs;
        this.tabNames = tabNames;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Fragment fg = new Posts();
        return fg;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return numbOfTabs;
    }
}
