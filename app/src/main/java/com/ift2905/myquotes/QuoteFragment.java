package com.ift2905.myquotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuoteFragment extends Fragment {

    public Quote quote;
    public CheckBox chk_favorite;
    public Button btn_share;

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

        // Fills the textview with the quote and the author
        TextView textViewQuote = (TextView) rootView.findViewById(R.id.quote);
        TextView textViewAuthor = (TextView) rootView.findViewById(R.id.author);
        textViewQuote.setText(quote.getQuote());
        textViewAuthor.setText(quote.getAuthor());

        // Get the buttons
        chk_favorite = (CheckBox) rootView.findViewById(R.id.chk_favorite);
        btn_share = (Button) rootView.findViewById(R.id.btn_share);

        // Adds a listener for the
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



        return rootView;
    }


}
