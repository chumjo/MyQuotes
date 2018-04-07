package com.ift2905.myquotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class QuoteFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfFragment;
    private ArrayList<Quote> mQuoteList;

    public QuoteFragmentPagerAdapter(FragmentManager fm, int numberOfFragment,
                                     ArrayList<Quote> quoteArrayList) {
        super(fm);
        this.numberOfFragment = numberOfFragment;
        this.mQuoteList = quoteArrayList;
    }

    @Override
    public Fragment getItem(int position) {

        Quote quote;

        if(position < mQuoteList.size()) {
            quote = mQuoteList.get(position);

            /***** DEBUGGING LOG - REMOVE!!! *****/
            Log.d("@string/debugging", "quote from the web");

        } else {
            quote = RandomQuoteInitialList.getRandomQuoteFromIntialList(MainActivity.preferences);

            /***** DEBUGGING LOG - REMOVE!!! *****/
            Log.d("@string/debugging", "quote from initial list");
        }

        return QuoteFragment.newInstance(position + 1, quote);
    }

    @Override
    public int getCount() {
        return numberOfFragment;
    }
}
