package com.fangzuo.assist.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 王璐阳 on 2017/5/15.
 */

public class StripAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;
    ArrayList<String> tabsName;
    public StripAdapter(FragmentManager fm, ArrayList<Fragment> fragments,ArrayList<String> tabsName) {
        super(fm);
        this.fragments = fragments;
        this.tabsName = tabsName;
    }

    @Override
    public CharSequence getPageTitle(int position) { //必须复写这个方法，开源控件PagerSlidingTabStrip需要通过它获取标签标题
        return tabsName.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
