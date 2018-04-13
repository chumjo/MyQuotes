package com.ift2905.myquotes;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Forms the view of the favorite quotes at Favorite Quotes ListView
 */

public class FavoriteQuoteFragment extends Fragment {

    public Quote quote;
    public ImageButton btn_delete;
    public ImageButton btn_share;
    public ImageButton btn_search;
    public int position;

    public FavoriteQuoteFragment() {
        super();
    }

    public static FavoriteQuoteFragment newInstance(int position, Quote quote) {

        FavoriteQuoteFragment fragment = new FavoriteQuoteFragment();
        fragment.quote = quote;
        fragment.position = position;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite_quote, container, false);

        // Set the image category icon
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue,true);
        @ColorInt int color = typedValue.data;

        ImageView imgView = (ImageView) rootView.findViewById(R.id.imgCategory);
        imgView.setImageDrawable(getResources().getDrawable(SettingRessources.getIcon(quote.getCategory())));
        imgView.setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        // Fills the textview with the quote and the author
        TextView textViewQuote = (TextView) rootView.findViewById(R.id.quote);
        TextView textViewAuthor = (TextView) rootView.findViewById(R.id.author);
        textViewQuote.setText(quote.getQuote());
        textViewAuthor.setText(quote.getAuthor());

        // Get the buttons
        btn_delete = (ImageButton) rootView.findViewById(R.id.btn_delete);
        btn_share = (ImageButton) rootView.findViewById(R.id.btn_share);
        btn_search = (ImageButton) rootView.findViewById(R.id.btn_search);

        // Adds a listener for the favorite delete button
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Deletion alert
                DBHelper.deleteQuoteFromFavorites(quote.getId());
                AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                adb.setTitle(R.string.delete_one);
                adb.setMessage(R.string.delete_one_question);
                adb.setNegativeButton(R.string.cancel_btn, null);
                adb.setPositiveButton(R.string.ok_btn, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove quote from Favorites Database
                        DBHelper.deleteQuoteFromFavorites(quote.getId());

                        // Uncheck Favorite's star if quote displayed in MainActivity ViewPager (mViewPager)
                        ((MainActivity)getActivity()).unCheckFavoriteState(quote.getId());

                        // Remove all exitant FavoriteQuoteFragments and FavoritesViewPagerFragments
                        // to be recretated
                        for(Fragment fragment:getFragmentManager().getFragments()){
                            if(fragment instanceof FavoriteQuoteFragment)
                                getFragmentManager().beginTransaction().remove(fragment).commit();
                            if(fragment instanceof FavoritesViewPagerFragment)
                                getFragmentManager().beginTransaction().remove(fragment).commit();
                        }

                        // Verify if Favorites Database is not empty
                        if(DBHelper.getFaroriteQuotes().size() != 0) {
                            // Recreate FavoritesViewPagerFragment without deleted quote
                            Fragment frag_fav_vp = new FavoritesViewPagerFragment();

                            // Position of next item
                            Bundle bundle=new Bundle();
                            bundle.putInt("position_selected",position);
                            frag_fav_vp.setArguments(bundle);

                            // Replace current fragment
                            android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container_main, frag_fav_vp, "FRAG_FAV_VP");
                            ft.commit();

                        // If Favorites Database is empty
                        } else {
                            // Remove FavoritesListFragment to be recreated empty
                            for(Fragment fragment:getFragmentManager().getFragments()) {
                                if (fragment instanceof FavoritesListFragment)
                                    getFragmentManager().beginTransaction().remove(fragment).commit();
                            }
                            // Recreate empty FavoritesListFragment
                            Fragment frag_fav_list = new FavoritesListFragment();

                            android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container_main, frag_fav_list, "FRAG_FAV_LIST");
                            ft.commit();
                        }
                    }});
                adb.show();
            }
        });

        // Adds a listener to the share button
        btn_share.setOnClickListener(new View.OnClickListener() {

            String textDesc = getResources().getString(R.string.share_desc) +
                    quote.getAuthor().substring(1);
            String textBody = quote.getQuote() + "\n" + quote.getAuthor();

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "@string/app_name");
                sendIntent.putExtra(Intent.EXTRA_TEXT, textBody);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, textDesc));
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String searchFor= "https://wikipedia.org/wiki/"+quote.getAuthor();
                Intent viewSearch = new Intent(Intent.ACTION_WEB_SEARCH);
                viewSearch.putExtra(SearchManager.QUERY, searchFor);
                startActivity(viewSearch);
            }
        });

        return rootView;
    }
}
