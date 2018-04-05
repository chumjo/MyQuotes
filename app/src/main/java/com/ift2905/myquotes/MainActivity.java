package com.ift2905.myquotes;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Quote quote;

    public ArrayList<Quote> mRandomQuoteArrayList;
    public int mCurrentQuotePosition;
    public int mMaxQuotePosition;

    private final int mNumberOfFragment = 1000;

    private QuoteFragmentPagerAdapter mQuoteFragmentPagerAdapter;
    private ViewPager mViewPager;

    // TMP
    public int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialise une liste de random quote vide
        mRandomQuoteArrayList = new ArrayList<Quote>(0);
        mCurrentQuotePosition = 0;
        mMaxQuotePosition = 0;

        for (int i=0; i<2; i++){
            quote = null;
            RunAPI run = new RunAPI();
            run.execute();
            while(quote == null);
            Log.d("OUPS", "on est sortie " + (quote == null));
            //mRandomQuoteArrayList.add(new Quote("quote : "+i, "- jonathan", Category.MANAGEMENT,"id"+i));
            mRandomQuoteArrayList.add(quote);
        }

        /*RunAPI run = new RunAPI();
        run.execute();*/

        count = 100;
        // TMP END

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuoteFragmentPagerAdapter = new QuoteFragmentPagerAdapter(getSupportFragmentManager(), mNumberOfFragment, mRandomQuoteArrayList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mQuoteFragmentPagerAdapter);

        // Page Change listener
        // Changes the different page counts
        // Laucnhes a query for a new quote if needed
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public boolean scrollingRight = false;
            public float lastPositionOffset = 0.0f;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                /*
                if(lastPositionOffset - positionOffset < -0.6) {
                    if(position <= mMmaxQuotePosition - 5){
                        mViewPager.setCurrentItem(mMmaxQuotePosition - 5);
                    }
                }
                else {
                    lastPositionOffset = positionOffset;
                }
                */
            }

            @Override
            public void onPageSelected(int position) {

                mCurrentQuotePosition = position;
                if(position > mMaxQuotePosition){

                    mMaxQuotePosition = mCurrentQuotePosition;

                    // TODO

                    RunAPI run = new RunAPI();
                    run.execute();

                    if(quote == null) {
                        Log.d("OUPS", "Erreur");
                    }

                    // API query for new quote
                    // TMP add a quote
                    mRandomQuoteArrayList.add(quote);
                    count++;
                }

                Log.d("DEBUG", "count = " + count);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }}
        );
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
                //mRandomQuoteArrayList.add(quote);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return quote;
        }
    }
}
