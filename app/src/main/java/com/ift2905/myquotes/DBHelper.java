package com.ift2905.myquotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Managing the Favorites Quotes database with SQLite
 * Code strongly inspired by the class database demo
 */

public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "favorite_quotes.db";
    static final int DB_VERSION = 1;

    static final String TABLE_FAVORITE_QUOTES = "favorite_quotes";
    static final String Q_ID = "_id";
    static final String Q_QUOTE = "quote";
    static final String Q_AUTHOR = "author";
    static final String Q_CATEGORY = "category";
    static Context context;

    // Private static SQLiteDatabase db = null;
    public static SQLiteDatabase db = null;

    /**
     * Constructor that creates database
     * @param context
     */
    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        DBHelper.context = context;
        if (db==null){
            db = getWritableDatabase();
        }
    }

    /**
     * Function called on the creation of Favorite Quotes database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_FAVORITE_QUOTES
                +" ( "+Q_ID + " text UNIQUE, "
                + Q_QUOTE+ " text, "
                + Q_AUTHOR+ " text, "
                +Q_CATEGORY+ " text )";
        db.execSQL(sql);
    }

    /**
     * Upgrade database Favorite Quotes database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_FAVORITE_QUOTES);
        onCreate(db);
    }

    /**
     * Add quotes into Favorites Quotes database
     * Throws exception if quote is already in database and don't insert it
     * after exception app's execution continues
     * @param quote
     */
    public static void addQuoteToFavorites(Quote quote){
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(Q_QUOTE,quote.getQuote());
        cv.put(Q_AUTHOR,quote.getAuthor());
        cv.put(Q_CATEGORY, quote.getCategory().name());
        cv.put(Q_ID,quote.getId());
        try{
            db.insertOrThrow(TABLE_FAVORITE_QUOTES, null, cv);
        } catch (SQLException e){
            e.printStackTrace();
            return;
        }
    }

    /**
     * Delete quotes from Favorites Quotes database
     * @param id
     */
    public static void deleteQuoteFromFavorites(String id) {
        db.delete(TABLE_FAVORITE_QUOTES, Q_ID + " = ?", new String[]{id});
    }

    /**
     * Get an ArrayList with all the favorite quotes of the database
     * @return
     */
    public static ArrayList<Quote> getFaroriteQuotes() {
        Cursor cursor = db.query(TABLE_FAVORITE_QUOTES, null, null, null, null, null, null);
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
        return quotes;
    }
}

