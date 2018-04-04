package com.ift2905.myquotes;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Quote quote;

    public ArrayList<Quote> mRandomQuoteArrayList;
    public int mCurrentQuotePosition;

    private final int mNumberOfFragment = 1000;

    private QuoteFragmentPagerAdapter mQuoteFragmentPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialise une liste de random quote vide
        mRandomQuoteArrayList = new ArrayList<Quote>(0);

        RunAPI run;
        for (int i=0; i<mNumberOfFragment; i++){
            run = new RunAPI();
            run.execute();
            
            mRandomQuoteArrayList.add(quote);
        }

        /*// TMP BEG

        for (int i=0; i<mNumberOfFragment; i++){
            mRandomQuoteArrayList.add(new Quote("quote : "+i, "- jonathan", "management","id"+i));
        }
        // TMP END*/



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuoteFragmentPagerAdapter = new QuoteFragmentPagerAdapter(getSupportFragmentManager(), mNumberOfFragment);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mQuoteFragmentPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class RunAPI extends AsyncTask<String, Object, Quote> {

        @Override
        protected Quote doInBackground(String... strings) {

            QuoteAPI web = new QuoteAPI(Category.MANAGEMENT);
            try {
                quote = web.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return quote;
        }
    }
}
