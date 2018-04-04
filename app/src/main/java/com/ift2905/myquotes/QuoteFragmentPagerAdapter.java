package com.ift2905.myquotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class QuoteFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfFragment;

    public QuoteFragmentPagerAdapter(FragmentManager fm, int numberOfFragment) {
        super(fm);
        this.numberOfFragment = numberOfFragment;
    }

    @Override
    public Fragment getItem(int position) {

        return QuoteFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return numberOfFragment;
    }
}
