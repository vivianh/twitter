package com.codepath.apps.twitter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitter.fragments.HomeTimelineFragment;
import com.codepath.apps.twitter.fragments.MentionsTimelineFragment;

public class TwitterFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "HOME", "MENTIONS" };
    // private Context context;
    private HomeTimelineFragment homeTimelineFragment;
    private MentionsTimelineFragment mentionsTimelineFragment;

    public TwitterFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        // this.context = context;
        homeTimelineFragment = new HomeTimelineFragment();
        mentionsTimelineFragment = new MentionsTimelineFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return homeTimelineFragment;
        } else {
            return mentionsTimelineFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
