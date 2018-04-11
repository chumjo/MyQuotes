package com.ift2905.myquotes;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by augus on 06/04/2018.
 */

public class RandomQuoteInitialList {

    public DatabaseAccess dba;
    public static ArrayList<Quote> quotes;

    public RandomQuoteInitialList(Context context) {
        dba = DatabaseAccess.getInstance(context);
        dba.open();
        quotes = dba.getInitialQuotes();
        dba.close();
    }

    public static Quote getRandomQuoteFromIntialList(String[] preferences) {

        ArrayList<Quote> quotesOfCategory = new ArrayList<Quote>(0);

        // Loop while no quote from random category found
        while(quotesOfCategory.size() == 0) {
            Category category = MainActivity.randomQuoteFromPreferences(preferences);

            for(Quote init_quote : RandomQuoteInitialList.quotes) {
                if(init_quote.getCategory() == category) {
                    quotesOfCategory.add(init_quote);
                }
            }
        }

        int i = (int) Math.floor(Math.random()*quotesOfCategory.size());

        Quote quoteOfCategory = quotesOfCategory.get(i);

        for(int j=0; j<quotes.size(); j++)
            if(quotes.get(i).getId().equals(quoteOfCategory.getId()))
                quotes.remove(i);

        return quotesOfCategory.get(i);
    }

    public static int getInitialQuoteListSize() {
        return quotes.size();
    }
}
