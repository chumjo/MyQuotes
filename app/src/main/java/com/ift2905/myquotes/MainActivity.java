package com.ift2905.myquotes;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import static com.ift2905.myquotes.R.id.container;

/**
 * Main Activity of My Quotes app
 * The only one used in the app
 * All other views are fragments setted from this activity *
 * A lot of the present code was inspired from : Android Developers, StackOverflow and class demos
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Quote quote;
    public SharedPreferences sharedPreferences;
    public RandomQuoteInitialList randomQuoteInitialList;
    public DrawerLayout drawer;
    public int nb_init_quotes = 2;
    public ArrayList<Quote> mRandomQuoteArrayList;
    public int mCurrentQuotePosition;
    public int mMaxQuotePosition;
    private final int mNumberOfFragment = 1000;
    private QuoteFragmentPagerAdapter mQuoteFragmentPagerAdapter;
    private ViewPager mViewPager;

    /**
     * Creation of MainActivity initial view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set default values to preferences
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        // Get initial quotes list from "initial_quotes.db"
        randomQuoteInitialList = new RandomQuoteInitialList(this);

        // Initialise une liste de random quote vide
        mRandomQuoteArrayList = new ArrayList<Quote>(0);
        mCurrentQuotePosition = 0;
        mMaxQuotePosition = 0;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Set app theme
        String theme = sharedPreferences.getString("pref_theme", "");
        setTheme(SettingRessources.getTheme(theme));

        // Fill firt "nb_init_quotes" elements of the quotes list to be displayed in fragments
        // on mViewPager from "initial_quotes.db"
        for(int i=0; i<nb_init_quotes; i++) {
            String[] test = SettingRessources.getPrefCategories(this);
            // welcome quote
            if(i==0)
                quote = new Quote(getResources().getString(R.string.quote_welcome),getResources().getString(R.string.app_author),Category.love,"welcome_quote");
            // random quote
            else
                quote = randomQuoteInitialList.getRandomQuoteFromIntialList(SettingRessources.getPrefCategories(this));
            if(quote != null) {
                mRandomQuoteArrayList.add(quote);
            }
            //mRandomQuoteArrayList.add(randomQuoteInitialList.getRandomQuoteFromIntialList(prefCategories));
        }

        // Gets quotes from They Said So API
        RunAPI run = new RunAPI();
        try{
            run.execute();
            nb_init_quotes = 1;
        }catch (Exception e) {
            e.printStackTrace();
        }

        // Super constructor
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.activity_main);

        // Defines toolbar view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Creates adapter for mViewPager
        mQuoteFragmentPagerAdapter = new QuoteFragmentPagerAdapter(
                getSupportFragmentManager(), mNumberOfFragment, mRandomQuoteArrayList, this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mQuoteFragmentPagerAdapter);

        // Set up DrawerLayout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set up NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Page Change listener
        // Changes the different page counts
        // Laucnhes a query for a new quote if needed
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public boolean scrollingRight = false;
            public float lastPositionOffset = 0.0f;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * When a ViewPager page is swiped from right to left we
             * fetch a new quote to be displayed
             * @param position
             */
            @Override
            public void onPageSelected(int position) {

                mCurrentQuotePosition = position;
                if(position > mMaxQuotePosition){

                    mMaxQuotePosition = mCurrentQuotePosition;

                    RunAPI run = new RunAPI();
                    try{
                        run.execute();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Check if has bundle
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            if(bundle.containsKey("settings")) {
                goSettings();
            }
            else if(bundle.containsKey("qod_key")){
                int date = bundle.getInt("qod_key", 0);
                Quote quote = randomQuoteInitialList.getQuoteOfTheDay(date);
                goQod(quote);
            }
        }
    }

    /**
     * Uncheck the Favorite Star Icon in activty_main layout when quote removed from Favorites
     * @param quote_id
     */
    public void unCheckFavoriteState(String quote_id) {

        int position;
        Quote uncheckQuote = null;

        // Get quote position if displayed in MainActivity ViewPager (mViewPager)
        for(position=0; position<mRandomQuoteArrayList.size(); position++) {

            // If the id of the quote to remove from Favorites is in the list of displayed quotes
            // get its position
            if(mRandomQuoteArrayList.get(position).getId().equals(quote_id)) {
                uncheckQuote = mRandomQuoteArrayList.get(position);
                break;
            }
        }

        // If correspondence found (not null), the quote is displayed)
        if(uncheckQuote != null) {

            // Get position of quote currently displayed in mViewPager
            int currentPosition = mViewPager.getCurrentItem();

            // Switch current quote to the quote removed from Favorites
            mViewPager.setCurrentItem(position);

            // Get quote's Fragment
            QuoteFragment quoteFragment = (QuoteFragment) mQuoteFragmentPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem());

            // Get quote's Fragment CheckBox
            CheckBox checkBox = (CheckBox) quoteFragment.getView().findViewById(R.id.chk_favorite);

            // Uncheck Favorite's star
            checkBox.setChecked(false);

            // Return previously displayed quote to currently displayed position
            mViewPager.setCurrentItem(currentPosition);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Management of on back pressed
     */
    @Override
    public void onBackPressed() {

        // Get the fragments by tag
        Fragment frag_about = getSupportFragmentManager().findFragmentByTag("FRAG_ABOUT");
        Fragment frag_fav_list = getSupportFragmentManager().findFragmentByTag("FRAG_FAV_LIST");
        Fragment frag_fav_vp = getSupportFragmentManager().findFragmentByTag("FRAG_FAV_VP");
        Fragment frag_qod = getSupportFragmentManager().findFragmentByTag("FRAG_QOD");
        android.app.Fragment frag_setting = getFragmentManager().findFragmentByTag("FRAG_SETTING");

        //--- DRAWER ---//
        // If the drawer is open, we simply close it
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        //--- FRAG ABOUT ---//
        // Opens the drawer
        else if(frag_about != null && frag_about.isVisible()) {
            drawer.openDrawer(Gravity.START);
        }

        //--- FRAG FAVORITE LIST ---//
        // Opens the drawer
        else if(frag_fav_list != null && frag_fav_list.isVisible()) {
            drawer.openDrawer(Gravity.START);
        }

        //--- FRAG FAVORITE PAGER ---//
        // Return to the favorite list
        else if(frag_fav_vp != null && frag_fav_vp.isVisible()) {

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if(frag_fav_list != null){
                ft.replace(R.id.container_main, frag_fav_list);
                ft.commit();
            }
            else {
                for(Fragment fragment:getSupportFragmentManager().getFragments()){
                    if(fragment instanceof FavoriteQuoteFragment)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    if(fragment instanceof FavoritesViewPagerFragment)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                FavoritesListFragment fragment = new FavoritesListFragment();
                ft.replace(R.id.container_main, fragment, "FRAG_FAV_LIST");
                ft.commit();
            }
        }

        //--- SETTINGS ---//
        // Return to the favorite list
        else if(frag_setting != null && frag_setting.isVisible()) {
            drawer.openDrawer(Gravity.START);
        }

        //--- QOD ---//
        // Restart the main activity
        else if(frag_qod != null && frag_qod.isVisible()){
            goHome();
        }

        // Else we call the normal back pressed
        else{
            super.onBackPressed();
        }
    }

    /**
     * On the selection of Menu items
     * @param item
     * @return
     */
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

    /*
     * Navigation Menu on selected item :
     * Tansaction between fragments or activities
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //--- HOME ---//
        if (id == R.id.nav_home) {
            goHome();
        }

        //--- FAVORITES ---//
        else if (id == R.id.nav_favorites) {
            goFavorite();
        }

        //--- SETTINGS ---//
        else if (id == R.id.nav_settings) {
            goSettings();
        }

        //--- ABOUT ---//
        else if (id == R.id.nav_about) {
            goAbout();
        }

        // --SHARE MYQUOTES --//
        else if (id == R.id.nav_share) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app));
            startActivity(Intent.createChooser(intent, "Share with"));
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Asynchronous task to acquire quotes from the internet (run in background)
     */
    public class RunAPI extends AsyncTask<String, Object, ArrayList<Quote>> {

        public RunAPI(){
            super();
        }

        @Override
        protected ArrayList<Quote> doInBackground(String... strings) {

            for (int i=0; i<nb_init_quotes; i++){

                // Pass random category to QuoteAPI
                QuoteAPI web = new QuoteAPI(randomQuoteFromPreferences(SettingRessources.getPrefCategories(MainActivity.this))
                        ,MainActivity.this);

                // Run QuoteAPI
                try {
                    quote = web.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // If no quote acquired from the web API
                if(quote != null)
                    mRandomQuoteArrayList.add(quote);
            }

            return mRandomQuoteArrayList;
        }
    }

    /**
     * Generates random category from user's preferences
     * @param prefCategories
     * @return
     */
    public static Category randomQuoteFromPreferences(String[] prefCategories) {

        if(prefCategories.length == 0) {
            String[] defaultPrefCategories = {"inspire", "management", "sport", "love", "funny", "art"};
            prefCategories = defaultPrefCategories;
        }

        int i = (int) Math.floor(Math.random()*prefCategories.length);
        return Category.valueOf(prefCategories[i]);
    }

    /**
     * Remove all QuoteFragments
     */
    private void removeAllFragments(){

        for(Fragment fragment:getSupportFragmentManager().getFragments()){

            if(fragment instanceof QuoteFragment && !(fragment.getTag().equals("FRAG_QOD")));
            else {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }

        android.app.Fragment frag_setting = getFragmentManager().findFragmentByTag("FRAG_SETTING");

        if(frag_setting != null) {
            getFragmentManager().beginTransaction().remove(frag_setting).commit();
        }
    }

    /**
     * Go to Home view
     */
    private void goHome(){
        getSupportFragmentManager().beginTransaction().remove(new Fragment());

        TextView tv = (TextView) findViewById(R.id.title_app);
        tv.setText(R.string.app_name);

        // removes all Fragments to get back to the main activity
        // hides the setting fragment, does not delete it
        removeAllFragments();
    }

    /**
     * Go to Favorite view
     */
    private void goFavorite(){

        TextView tv = (TextView) findViewById(R.id.title_app);
        tv.setText(R.string.favorites);

        Fragment frag_fav_list = getSupportFragmentManager().findFragmentByTag("FRAG_FAV_LIST");

        if(frag_fav_list != null && frag_fav_list.isInLayout())
            return;
        else if(frag_fav_list == null)
            frag_fav_list = new FavoritesListFragment();

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, frag_fav_list, "FRAG_FAV_LIST");
        ft.show(frag_fav_list);
        ft.commit();
    }

    /**
     * Go to Settings view
     */
    private void goSettings(){

        TextView tv = (TextView) findViewById(R.id.title_app);
        tv.setText(R.string.app_name);

        removeAllFragments();

        android.app.Fragment frag_setting = new SettingsFragment();

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, frag_setting, "FRAG_SETTING");
        ft.show(frag_setting);
        ft.commit();
    }

    /**
     * Go to About view
     */
    private void goAbout(){

        TextView tv = (TextView) findViewById(R.id.title_app);
        tv.setText(R.string.app_name);

        Fragment frag_about = getSupportFragmentManager().findFragmentByTag("FRAG_ABOUT");

        if(frag_about != null && frag_about.isInLayout())
            return;
        else if(frag_about == null)
            frag_about = new AboutFragment();

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, frag_about, "FRAG_ABOUT");
        ft.show(frag_about);
        ft.commit();
    }

    /**
     * Go to Quote of The Day view
     * @param quote
     */
    private void goQod(Quote quote){

        TextView tv = (TextView) findViewById(R.id.title_app);
        tv.setText(R.string.qod);

        removeAllFragments();

        QuoteFragment frag_qod = QuoteFragment.newInstance(quote);
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, frag_qod, "FRAG_QOD");
        ft.show(frag_qod);
        ft.commit();
    }
}
