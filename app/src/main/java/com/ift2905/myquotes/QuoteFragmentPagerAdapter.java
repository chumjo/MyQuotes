package com.ift2905.myquotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class QuoteFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfFragment;
    private ArrayList<Quote> mQuoteList;
    private RegularOrFavoriteQuote regularOrFavoriteQuote;

    public QuoteFragmentPagerAdapter(FragmentManager fm, int numberOfFragment,
                                     ArrayList<Quote> quoteArrayList,
                                     RegularOrFavoriteQuote regularOrFavoriteQuote) {
        super(fm);
        this.numberOfFragment = numberOfFragment;
        this.mQuoteList = quoteArrayList;
        this.regularOrFavoriteQuote = regularOrFavoriteQuote;
    }

    @Override
    public Fragment getItem(int position) {

        Quote quote;

        if(position < mQuoteList.size()) {
            quote = mQuoteList.get(position);

            /***** DEBUGGING LOG - REMOVE!!! *****/
            Log.d("MY_QUOTES_DEBUG", "quote from the web");

        } else {
            quote = RandomQuoteInitialList.getRandomQuoteFromIntialList(MainActivity.preferences);

            /***** DEBUGGING LOG - REMOVE!!! *****/
            Log.d("MY_QUOTES_DEBUG", "quote from initial list");
        }

        switch (regularOrFavoriteQuote) {
            case REGULAR_QUOTE:
                return QuoteFragment.newInstance(position, quote);
            case FAVORITE_QUOTE:
                return FavoriteQuoteFragment.newInstance(position, quote);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfFragment;
    }
}
