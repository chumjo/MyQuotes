package com.ift2905.myquotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by augus on 07/04/2018.
 */

public class FavoriteQuoteFragment extends Fragment {

    public Quote quote;
    public ImageButton btn_delete;
    public ImageButton btn_share;
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

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String theme = sharedPref.getString("pref_theme", "");

        Context context = new ContextThemeWrapper(getActivity(), SettingRessources.getTheme(theme));
        LayoutInflater localInflater = inflater.cloneInContext(context);

        View rootView = inflater.inflate(R.layout.fragment_favorite_quote, container, false);

        // Fills the textview with the quote and the author
        TextView textViewQuote = (TextView) rootView.findViewById(R.id.quote);
        TextView textViewAuthor = (TextView) rootView.findViewById(R.id.author);
        textViewQuote.setText(quote.getQuote());
        textViewAuthor.setText(quote.getAuthor());

        // Get the buttons
        btn_delete = (ImageButton) rootView.findViewById(R.id.btn_delete);
        btn_share = (ImageButton) rootView.findViewById(R.id.btn_share);

        // Adds a listener for the favorite checkbox
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper.deleteQuoteFromFavorites(quote.getId());
                AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
                adb.setTitle("Deleting quote from Favorites");
                adb.setMessage("\nAre you sure you want to delete it?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.deleteQuoteFromFavorites(quote.getId());
                    }});
                adb.show();

                FavoriteQuoteFragment myFragment = (FavoriteQuoteFragment)getFragmentManager().findFragmentByTag("MY_FRAGMENT");
                if (myFragment != null && myFragment.isVisible()) {
                    // add your code here
                }
                /*FavoriteQuoteFragment fragment = getView().getFragment(index);
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container_main, get, "FRAG_FAV_VP");
                ft.commit();*/
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

        return rootView;
    }
}