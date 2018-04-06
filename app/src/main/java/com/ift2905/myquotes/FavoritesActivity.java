package com.ift2905.myquotes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by augus on 05/04/2018.
 */

public class FavoritesActivity extends AppCompatActivity {
    ListView list;
    TextView tv;
    DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity);

        tv = findViewById(R.id.textview1);

        list = (ListView) findViewById(R.id.lview2);
        dbh = new DBHelper(this);
        Cursor c = dbh.quotesList();

        String[] from = {DBHelper.Q_QUOTE, /*DBHelper.Q_AUTHOR,*/ DBHelper.Q_CATEGORY};
        int[] to = {/*0,*/android.R.id.text1,android.R.id.text2};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, from, to,0);

        list.setAdapter(sca);
    }
}
