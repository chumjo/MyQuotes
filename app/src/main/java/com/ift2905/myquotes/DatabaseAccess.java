package com.ift2905.myquotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by augus on 06/04/2018.
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

    // NOT USED
    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    /*public List<String> getQuotes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM initial_quotes", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }*/

    /**
     * Read all initial quotes from the database.
     *
     * @return a List of quotes
     */
    public ArrayList<Quote> getInitialQuotes() {
        //db = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_INITIAL_QUOTES, null, null, null, null, null, null);
        ArrayList<Quote> quotes = new ArrayList<Quote>();
        Quote quote;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                quote = new Quote(cursor.getString(0),cursor.getString(1), Category.valueOf(cursor.getString(2)),cursor.getString(3));
                quotes.add(quote);
            }
        }
        cursor.close();
        database.close();
        return quotes;
    }
}

/*
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
    databaseAccess.open();
    ArrayList<Quote> quotes = databaseAccess.getInitialQuotes();
    databaseAccess.close();
*/