package com.dsquare.hibour.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dsquare.hibour.fragments.NearByUserChat;
import com.dsquare.hibour.fragments.PreviousChat;

/**
 * Adapter to handle fragment motion.
 */
public class ChatTypeViewPagerAdapter extends FragmentStatePagerAdapter {
  private String titles[];

  public ChatTypeViewPagerAdapter(FragmentManager fm, String[] titles) {
    super(fm);
    this.titles = titles;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return new NearByUserChat();
      case 1:
        return new PreviousChat();
    }
    return new NearByUserChat();
  }

  @Override
  public int getCount() {
    return titles.length;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return titles[position];
  }
}
