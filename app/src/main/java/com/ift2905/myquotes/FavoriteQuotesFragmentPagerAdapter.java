package com.ift2905.myquotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by augus on 08/04/2018.
 */

public class FavoriteQuotesFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfFragment;
    private ArrayList<Quote> mQuoteList;

    public FavoriteQuotesFragmentPagerAdapter(FragmentManager fm, int numberOfFragment,
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
            //Log.d("MY_QUOTES_DEBUG", "quote from the web");

        } else {
            quote = RandomQuoteInitialList.getRandomQuoteFromIntialList(MainActivity.preferences);
            mQuoteList.add(quote);

            /***** DEBUGGING LOG - REMOVE!!! *****/
            //Log.d("MY_QUOTES_DEBUG", "quote from initial list");
        }

        return FavoriteQuoteFragment.newInstance(position, quote);
    }

    @Override
    public int getCount() {
        return numberOfFragment;
    }
}
