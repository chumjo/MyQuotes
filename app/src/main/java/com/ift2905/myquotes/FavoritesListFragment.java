package com.ift2905.myquotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jonathan on 2018-04-06.
 */

public class FavoritesListFragment extends Fragment {

    public FavoritesListFragment(){ super(); }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite_listview, container, false);

        return rootView;
    }
}
