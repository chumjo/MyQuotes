package com.ift2905.myquotes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by augus on 07/04/2018.
 */

public class FavoritesViewPagerFragment extends Fragment {

    ViewPager vp_favorites;
    QuoteFragmentPagerAdapter quoteFragmentPagerAdapter;
    ArrayList<Quote> list_favorite_quotes = DBHelper.getFaroriteQuotes();

    public FavoritesViewPagerFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite_viewpager, container, false);
        vp_favorites = (ViewPager) rootView.findViewById(R.id.favorites_viewpager);
        quoteFragmentPagerAdapter = new QuoteFragmentPagerAdapter(getFragmentManager(),
                list_favorite_quotes.size(),list_favorite_quotes, RegularOrFavoriteQuote.FAVORITE_QUOTE);
        vp_favorites.setAdapter(quoteFragmentPagerAdapter);

        return rootView;
    }
}
