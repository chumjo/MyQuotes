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
 * Created by augus on 07/04/2018.
 */

public class FavoritesViewPagerFragment extends Fragment {

    ViewPager vp_favorites;
    FavoriteQuotesFragmentPagerAdapter favoriteQuoteFragmentPagerAdapter;
    ArrayList<Quote> list_favorite_quotes = DBHelper.getFaroriteQuotes();

    public FavoritesViewPagerFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite_viewpager, container, false);
        vp_favorites = (ViewPager) rootView.findViewById(R.id.favorites_viewpager);
        favoriteQuoteFragmentPagerAdapter = new FavoriteQuotesFragmentPagerAdapter(getFragmentManager(),
                list_favorite_quotes.size(),list_favorite_quotes);
        vp_favorites.setAdapter(favoriteQuoteFragmentPagerAdapter);

        int position = getArguments().getInt("position_selected");
        vp_favorites.setCurrentItem(position);

        return rootView;
    }
}
