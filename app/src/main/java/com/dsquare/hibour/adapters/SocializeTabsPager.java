package com.dsquare.hibour.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dsquare.hibour.fragments.SocializePager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dsquare Android on 2/10/2016.
 */
public class SocializeTabsPager extends FragmentStatePagerAdapter {

  private List<String> tabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
  private Map<String, SocializePager> tabToFragmentMap;

  // Build a Constructor and assign the passed Values to appropriate values in the class
  public SocializeTabsPager(FragmentManager fm) {
    super(fm);
    tabToFragmentMap = new HashMap<>();
  }

  public void updateTabs(List<String> tabs) {
    this.tabs = tabs;
  }

  //This method return the fragment for the every position in the View Pager
  @Override
  public Fragment getItem(int position) {
    String tabName = tabs.get(position);
    SocializePager fg = tabToFragmentMap.get(tabName);
    if (fg == null) {
      fg = new SocializePager();
      Bundle args = new Bundle();
      args.putString(SocializePager.CATEGORY_BUNDLE_ARGUMENT, tabName);
      fg.setArguments(args);
      tabToFragmentMap.put(tabName, fg);
    }
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
