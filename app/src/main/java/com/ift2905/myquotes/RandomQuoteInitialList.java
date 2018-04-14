package com.ift2905.myquotes;

import android.content.Context;

import java.util.ArrayList;

/**
 * Returns one random quote from "initial_quotes.db"
 */

public class RandomQuoteInitialList {

    public DatabaseAccess dba;
    public static ArrayList<Quote> quotes;

    /**
     * List of all initial quotes
     * @param context
     */
    public RandomQuoteInitialList(Context context) {
        dba = DatabaseAccess.getInstance(context);
        dba.open();
        quotes = dba.getInitialQuotes();
        dba.close();
    }

    /**
     * List of intial quotes by categories set in preferences
     * @param preferences
     * @return
     */
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

        return quotesOfCategory.get(i);
    }

    /**
     * Quote of the Day from "initial_quotes.db"
     * @param date
     * @return
     */
    public static Quote getQuoteOfTheDay(int date) {

        int size = quotes.size();

        int i = date % size;

        return quotes.get(i);
    }
}
