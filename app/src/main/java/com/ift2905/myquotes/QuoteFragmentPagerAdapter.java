package com.ift2905.myquotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class QuoteFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfFragment;
    private ArrayList<Quote> mQuoteList;

    public QuoteFragmentPagerAdapter(FragmentManager fm, int numberOfFragment, ArrayList<Quote> quoteArrayList) {
        super(fm);
        this.numberOfFragment = numberOfFragment;
        this.mQuoteList = quoteArrayList;
    }

    @Override
    public Fragment getItem(int position) {

        return QuoteFragment.newInstance(position + 1, mQuoteList.get(position));
    }

    @Override
    public int getCount() {
        return numberOfFragment;
    }
}
