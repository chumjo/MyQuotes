package com.ift2905.myquotes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;

import static com.ift2905.myquotes.R.id.container;
import static java.lang.Math.random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public Quote quote;

    public DrawerLayout drawer;

    public int nb_init_quotes = 3;

    public ArrayList<Quote> mRandomQuoteArrayList;
    public int mCurrentQuotePosition;
    public int mMaxQuotePosition;

    private final int mNumberOfFragment = 1000;

    private QuoteFragmentPagerAdapter mQuoteFragmentPagerAdapter;
    private ViewPager mViewPager;

    // TMP
    public int count;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialise une liste de random quote vide
        mRandomQuoteArrayList = new ArrayList<Quote>(0);
        mCurrentQuotePosition = 0;
        mMaxQuotePosition = 0;

        RunAPI run = new RunAPI(Category.management);
        try{
            while(run.execute().get().size() == 0);
            nb_init_quotes = 1;
        }catch (Exception e) {
            e.printStackTrace();
        }

        count = 100;
        // TMP END

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuoteFragmentPagerAdapter = new QuoteFragmentPagerAdapter(getSupportFragmentManager(), mNumberOfFragment, mRandomQuoteArrayList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mQuoteFragmentPagerAdapter);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);

                        if(menuItem.getTitle().equals("Favorites")) {
                            Intent intent = new Intent(MainActivity.this,FavoritesActivity.class);
                            startActivity(intent);
                        }

                        // close drawer when item is tapped
                        drawer.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

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

                    String[] preferences = {};
                    Category category = randomQuoteFromPreferences(preferences);

                    RunAPI run = new RunAPI(category);
                    try{
                        run.execute();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    // API query for new quote
                    // TMP add a quote
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
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Context context = getApplicationContext();
            CharSequence texte = "buton camera clicked";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, texte, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
            toast.show();
            // Handle the home
        } else if (id == R.id.nav_favorites) {
            Context context = getApplicationContext();
            CharSequence texte = "buton clicked";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, texte, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        } else if (id == R.id.nav_settings) {
            Context context = getApplicationContext();
            //Intent intent = new Intent(context,Settings.class);
            //startActivity(intent);

        } else if (id == R.id.nav_about) {
            Context context = getApplicationContext();
            //Intent intent = new Intent(context,About.class);
            //startActivity(intent);

        } else if (id == R.id.nav_share) {
            Context context = getApplicationContext();
            CharSequence texte = "buton clicked";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, texte, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
            toast.show();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public class RunAPI extends AsyncTask<String, Object, ArrayList<Quote>> {

        String[] preferences = {};
        Category category = randomQuoteFromPreferences(preferences);

        public RunAPI(Category category){
            super();
            this.category = category;
        }

        @Override
        protected ArrayList<Quote> doInBackground(String... strings) {

            for (int i=0; i<nb_init_quotes; i++){
                this.category = randomQuoteFromPreferences(preferences);
                QuoteAPI web = new QuoteAPI(category,MainActivity.this);

                try {
                    quote = web.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mRandomQuoteArrayList.add(quote);
            }

            return mRandomQuoteArrayList;
        }
    }

    public Category randomQuoteFromPreferences(String[] preferences) {

        if(preferences.length == 0) {
            String[] default_preferences = {"inspire","management","sport","love","funny","art"};
            preferences = default_preferences;
        }
        int i = (int) Math.floor(Math.random()*preferences.length);

        return Category.valueOf(preferences[i]);

    }
}
