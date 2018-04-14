package com.ift2905.myquotes;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * PagerAdapter for mViewPager QuoteFragments
 */

public class QuoteFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfFragment;
    private ArrayList<Quote> mQuoteList;
    private Context context;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    /**
     * PagerAdapter for mViewPager
     * @param fm
     * @param numberOfFragment
     * @param quoteArrayList
     * @param context
     */
    public QuoteFragmentPagerAdapter(FragmentManager fm, int numberOfFragment,
                                     ArrayList<Quote> quoteArrayList, Context context) {
        super(fm);
        this.numberOfFragment = numberOfFragment;
        this.context = context;
        this.mQuoteList = quoteArrayList;
    }

    /**
     * Returns the quote at "position" to be displayed by the mViewPager
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {

        Quote quote;

        if(position < mQuoteList.size()) {
            quote = mQuoteList.get(position);

        } else {
            quote = RandomQuoteInitialList.getRandomQuoteFromIntialList(SettingRessources.getPrefCategories(context));
            mQuoteList.add(quote);
        }

        return QuoteFragment.newInstance(quote);
    }

    /**
     * Returns the total number of QuoteFragments
     * @return
     */
    @Override
    public int getCount() {
        return numberOfFragment;
    }

    /**
     * Checks if QuoteFragment instatiated
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Get QuoteFragment at position
     * @param position
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
