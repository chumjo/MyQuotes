package com.ift2905.myquotes;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class QuoteFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfFragment;
    private ArrayList<Quote> mQuoteList;
    private Context context;

    public QuoteFragmentPagerAdapter(FragmentManager fm, int numberOfFragment,
                                     ArrayList<Quote> quoteArrayList, Context context) {
        super(fm);
        this.numberOfFragment = numberOfFragment;
        this.context = context;
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
            quote = RandomQuoteInitialList.getRandomQuoteFromIntialList(prefCategories);
            mQuoteList.add(quote);

            /***** DEBUGGING LOG - REMOVE!!! *****/
            //Log.d("MY_QUOTES_DEBUG", "quote from initial list");
        }

        return QuoteFragment.newInstance(position, quote);
    }

    @Override
    public int getCount() {
        return numberOfFragment;
    }
}
