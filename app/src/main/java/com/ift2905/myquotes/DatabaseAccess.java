package com.ift2905.myquotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Retreive external database
 * Code strongly inspired by: http://www.javahelps.com/2015/04/import-and-use-external-database-in.html
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    // To make it compatible with MyQuotes
    static final String TABLE_INITIAL_QUOTES = "initial_quotes";

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all initial quotes from the database.
     *
     * @return a List of quotes
     */
    public ArrayList<Quote> getInitialQuotes() {
        Cursor cursor = database.query(TABLE_INITIAL_QUOTES, null, null, null, null, null, null);
        ArrayList<Quote> quotes = new ArrayList<Quote>();
        Quote quote;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                quote = new Quote(cursor.getString(1),cursor.getString(2), Category.valueOf(cursor.getString(3)),cursor.getString(0));
                quotes.add(quote);
            }
        }
        cursor.close();
        database.close();

        return quotes;
    }
}