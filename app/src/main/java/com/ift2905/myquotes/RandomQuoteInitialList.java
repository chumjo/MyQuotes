package com.ift2905.myquotes;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by augus on 06/04/2018.
 */

public class RandomQuoteInitialList {

    DatabaseAccess bda;
    ArrayList<Quote> quotes;

    public RandomQuoteInitialList(Context context) {
        bda = DatabaseAccess.getInstance(context);
        DatabaseAccess dba = DatabaseAccess.getInstance(context);
        dba.open();
        quotes = dba.getInitialQuotes();
        dba.close();
    }

    public Quote getRandomQuoteFromIntialList(String[] preferences) {

        Quote quote = null;
        ArrayList<Quote> quotesOfCategory = new ArrayList<Quote>(0);

        // Loop while no quote from random category found
        while(quotesOfCategory.size() == 0) {
            Category category = MainActivity.randomQuoteFromPreferences(preferences);

            for(Quote init_quote : quotes) {
                if(init_quote.getCategory() == category) {
                    quotesOfCategory.add(init_quote);
                }
            }
        }


        return quote;
    }

    /*
    DatabaseAccess dba = DatabaseAccess.getInstance(this);
    dba.open();
    quotes = dba.getInitialQuotes();
    dba.close();
    */
}
