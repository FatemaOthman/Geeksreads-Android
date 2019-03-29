package com.example.geeksreads;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class Follow_Adapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public Follow_Adapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * addFragment: Function that adds fragment with title to the Fragments List and Title List
     *
     * @param fragment
     * @param title
     */
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    /**
     * getPageTitle: Function that returns Title of a Fragment at a given position
     * @param position
     * @return Fragment Title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    /**
     * getItem: Function that returns a Fragment at a given position
     * @param position
     * @return Fragment
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**
     * getCount: Function that returns number of Fragments
     * @return Number of Fragments
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
