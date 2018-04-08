package com.ift2905.myquotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuoteFragment extends Fragment {

    public Quote quote;
    public CheckBox chk_favorite;
    public ImageButton btn_share;

    public QuoteFragment() {
        super();
    }

    public static QuoteFragment newInstance(int sectionNumber, Quote quote) {

        QuoteFragment fragment = new QuoteFragment();
        fragment.quote = quote;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_quote, container, false);

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
        textViewQuote.setText("\""+quote.getQuote()+"\"");
        textViewAuthor.setText("- "+quote.getAuthor());

        // Get the buttons
        chk_favorite = (CheckBox) rootView.findViewById(R.id.chk_favorite);
        btn_share = (ImageButton) rootView.findViewById(R.id.btn_share);

        // Adds a listener for the favorite checkbox
        chk_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    // Adds the quote to the favorite database
                    DBHelper.addQuoteToFavorites(quote);
                }
                else {
                    // Removes the quote to the favorite database
                    DBHelper.deleteQuoteFromFavorites(quote.getId());
                }
            }
        });

        // Adds a listener to the share button
        btn_share.setOnClickListener(new View.OnClickListener() {

            String textDesc = getResources().getString(R.string.share_desc) +
                    quote.getAuthor().substring(1);
            String textBody = "\""+quote.getQuote()+"\"" + "\n" + "- "+quote.getAuthor();

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
