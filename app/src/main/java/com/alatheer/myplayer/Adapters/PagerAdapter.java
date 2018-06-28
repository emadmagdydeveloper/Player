package com.alatheer.myplayer.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elashry on 26/06/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    public void AddFragment(List<Fragment> fragmentList)
    {
        this.fragmentList.addAll(fragmentList);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
