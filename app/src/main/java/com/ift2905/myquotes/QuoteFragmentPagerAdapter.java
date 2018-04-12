package com.ift2905.myquotes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class QuoteFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfFragment;
    private ArrayList<Quote> mQuoteList;
    private Context context;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

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

        } else {
            quote = RandomQuoteInitialList.getRandomQuoteFromIntialList(SettingRessources.getPrefCategories(context));
            mQuoteList.add(quote);
        }

        return QuoteFragment.newInstance(quote);
    }

    @Override
    public int getCount() {
        return numberOfFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
