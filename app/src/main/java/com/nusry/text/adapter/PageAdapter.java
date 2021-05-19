package com.nusry.text.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nusry.text.fragments.MainLayout;
import com.nusry.text.fragments.ResultLayout;

public class PageAdapter extends FragmentPagerAdapter {

    private final int numOfTabs;



    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new MainLayout();
            case 1:
                return new ResultLayout();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }



}
