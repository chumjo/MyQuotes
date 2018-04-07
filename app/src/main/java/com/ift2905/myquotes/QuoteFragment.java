package com.ift2905.myquotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuoteFragment extends Fragment {

    public Quote quote;
    public CheckBox chk_favorite;

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
        TextView textViewQuote = (TextView) rootView.findViewById(R.id.quote);
        TextView textViewAuthor = (TextView) rootView.findViewById(R.id.author);
        textViewQuote.setText(quote.getQuote());
        textViewAuthor.setText(quote.getAuthor());

        chk_favorite = (CheckBox) rootView.findViewById(R.id.chk_favorite);

        chk_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    // Adds the quote to the favorite database
                }
                else {
                    // Removes the quote to the favorite database
                    
                }
            }
        });

        return rootView;
    }


}
