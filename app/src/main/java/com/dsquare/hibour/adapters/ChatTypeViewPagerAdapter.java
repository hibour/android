package com.dsquare.hibour.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.dsquare.hibour.fragments.BaseChatFragment;
import com.dsquare.hibour.fragments.NearByUserChat;
import com.dsquare.hibour.fragments.PreviousChat;

import java.util.HashMap;

/**
 * Adapter to handle fragment motion.
 */
public class ChatTypeViewPagerAdapter extends FragmentStatePagerAdapter {
  private String titles[];
  private HashMap<Integer, BaseChatFragment> fragmentHashMap = new HashMap<>();
  public ChatTypeViewPagerAdapter(FragmentManager fm, String[] titles) {
    super(fm);
    this.titles = titles;
  }

  @Override
  public Fragment getItem(int position) {
    BaseChatFragment fragment = new NearByUserChat();
    switch (position) {
      case 0:
        fragment = new NearByUserChat();
        fragmentHashMap.put(position, fragment);
        return fragment;
      case 1:
        fragment = new PreviousChat();
        fragmentHashMap.put(position, fragment);
        return fragment;
    }
    fragmentHashMap.put(position, fragment);
    return fragment;
  }

  @Override
  public int getCount() {
    return titles.length;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return titles[position];
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    super.destroyItem(container, position, object);
    fragmentHashMap.remove(position);
  }

  public BaseChatFragment getFragmentAtPosition(int position) {
    if (fragmentHashMap.containsKey(position))
      return fragmentHashMap.get(position);
    return null;
  }
}
