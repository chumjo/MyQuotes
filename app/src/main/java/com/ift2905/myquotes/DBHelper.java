package com.ift2905.myquotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by augus on 05/04/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    static final String DB_NAME = "quotes.db";
    static final int DB_VERSION = 1;

    static final String TABLE_QUOTES = "quotes";
    static final String Q_ID = "_id";
    static final String Q_QUOTE = "quote";
    static final String Q_AUTHOR = "author";
    static final String Q_CATEGORY = "category";

    //private static SQLiteDatabase db = null;
    public static SQLiteDatabase db = null;

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        if (db==null){
            db = getWritableDatabase();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_QUOTES
                +" ( "+Q_ID + " text, "
                + Q_QUOTE+ " text, "
                + Q_AUTHOR+ " text, "
                +Q_CATEGORY+ " text )";
        Log.d("SQL",sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_QUOTES);
        onCreate(db);
    }

    public void addQuote(Quote quote){
        //int nb = 0;
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put(Q_ID,quote.getId());
        cv.put(Q_QUOTE,quote.getQuote());
        cv.put(Q_AUTHOR,quote.getAuthor());
        cv.put(Q_CATEGORY, quote.getCategory().toString());
        try{
            db.insertOrThrow(TABLE_QUOTES, null, cv);
            //nb++;
        } catch (SQLException e){
            e.printStackTrace();
        }
        //return nb;
    }

    public Cursor quotesList(){
        Cursor c;
        c = db.rawQuery("select * from " + TABLE_QUOTES + " ORDER BY " + Q_CATEGORY + " ASC",null);
        return c;
    }
}

