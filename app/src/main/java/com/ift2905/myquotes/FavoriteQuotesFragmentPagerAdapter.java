package com.ift2905.myquotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * PagerAdapter to display the fragments of the Favorite quotes into it's ViewPager
 */

public class FavoriteQuotesFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final int numberOfFragment;
    private ArrayList<Quote> mQuoteList;

    /**
     * PagerAdapter for Favorite Quotes ViewPager
     * @param fm
     * @param numberOfFragment
     * @param quoteArrayList
     */
    public FavoriteQuotesFragmentPagerAdapter(FragmentManager fm, int numberOfFragment,
                                     ArrayList<Quote> quoteArrayList) {
        super(fm);
        this.numberOfFragment = numberOfFragment;
        this.mQuoteList = quoteArrayList;
    }

    /**
     * Returns the quote at "position" to be displayed by the ViewPager
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {

        Quote quote = mQuoteList.get(position);

        return FavoriteQuoteFragment.newInstance(position, quote);
    }

    /**
     * Returns the total number of Favorite Quotes Fragments
     * @return
     */
    @Override
    public int getCount() {
        return numberOfFragment;
    }
}
