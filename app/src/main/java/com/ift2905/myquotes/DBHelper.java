package com.ift2905.myquotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ift2905.myquotes.Category;
import com.ift2905.myquotes.Quote;

import java.util.ArrayList;

/**
 * Created by augus on 05/04/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "favorite_quotes.db";
    static final int DB_VERSION = 1;

    static final String TABLE_FAVORITE_QUOTES = "favorite_quotes";
    static final String Q_ID = "_id";
    static final String Q_QUOTE = "quote";
    static final String Q_AUTHOR = "author";
    static final String Q_CATEGORY = "category";

    //private static SQLiteDatabase db = null;
    public static SQLiteDatabase db = null;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        if (db==null){
            db = getWritableDatabase();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_FAVORITE_QUOTES
                +" ( "+Q_ID + " text UNIQUE, "
                + Q_QUOTE+ " text, "
                + Q_AUTHOR+ " text, "
                +Q_CATEGORY+ " text )";
        //Log.d("MY_QUOTES_DEBUG",sql);
        db.execSQL(sql);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_FAVORITE_QUOTES);
        onCreate(db);
    }

    public static void addQuoteToFavorites(Quote quote){
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(Q_QUOTE,quote.getQuote());
        cv.put(Q_AUTHOR,quote.getAuthor());
        cv.put(Q_CATEGORY, quote.getCategory().name());
        cv.put(Q_ID,quote.getId());
        try{
            //Log.d("@MY_QUOTES_DEBUG", "quote inserted (unique)");
            db.insertOrThrow(TABLE_FAVORITE_QUOTES, null, cv);
        } catch (SQLException e){
            e.printStackTrace();
            //Log.d("@MY_QUOTES_DEBUG", "quote not inserted (not unique)");
            return;
        }
    }

    public static void deleteQuoteFromFavorites(String id) {
        db.delete(TABLE_FAVORITE_QUOTES, Q_ID + " = ?", new String[]{id});
        //Log.d("@MY_QUOTES_DEBUG", "deleted from favorites");
        //db.close();
    }

    public static ArrayList<Quote> getFaroriteQuotes() {
        //db = this.getReadableDatabase();
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
        //db.close();
        return quotes;
    }

    public Cursor quotesList(){
        Cursor c;
        c = db.rawQuery("select * from " + TABLE_FAVORITE_QUOTES + " ORDER BY " + Q_CATEGORY + " ASC",null);
        return c;
    }
}

