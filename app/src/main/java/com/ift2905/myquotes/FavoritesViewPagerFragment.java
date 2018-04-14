package com.ift2905.myquotes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Fragment containing ViewPager to display the list of Favorite quotes
 */

public class FavoritesViewPagerFragment extends Fragment {

    ViewPager vp_favorites;
    FavoriteQuotesFragmentPagerAdapter favoriteQuoteFragmentPagerAdapter;

    // List of favorite quotes from Favorite quotes database
    ArrayList<Quote> list_favorite_quotes = DBHelper.getFaroriteQuotes();

    /**
     * Constructor
     */
    public FavoritesViewPagerFragment() {
        super();
    }

    /**
     * Creation of Favorites Quotes ViewPager Fragment
     * Uses a bundle to get the position of the item selected in the Favorites ListView to be
     * displayed in the ViewPager as the currently visible element
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite_viewpager, container, false);
        vp_favorites = (ViewPager) rootView.findViewById(R.id.favorites_viewpager);
        favoriteQuoteFragmentPagerAdapter = new FavoriteQuotesFragmentPagerAdapter(getFragmentManager(),
                list_favorite_quotes.size(),list_favorite_quotes);
        vp_favorites.setAdapter(favoriteQuoteFragmentPagerAdapter);

        // Get position to display from Bundle
        int position = getArguments().getInt("position_selected");
        vp_favorites.setCurrentItem(position);

        return rootView;
    }
}
