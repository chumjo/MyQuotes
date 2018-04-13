package com.ift2905.myquotes;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        return quotesOfCategory.get(i);
    }

    public static Quote getQuoteOfTheDay(String[] preferences, int date) {

        int size = quotes.size();

        int i = date % size;

        return quotes.get(i);
    }

    public static int getInitialQuoteListSize() {
        return quotes.size();
    }
}
