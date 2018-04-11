package com.ift2905.myquotes;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.CheckBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static com.ift2905.myquotes.R.id.container;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int time_notification = 1;       // default time in hours to receive a notification
    public Quote quote;
    public SharedPreferences sharedPreferences;
    public RandomQuoteInitialList randomQuoteInitialList;
    public DrawerLayout drawer;
    public int nb_init_quotes = 3;
    public ArrayList<Quote> mRandomQuoteArrayList;
    public int mCurrentQuotePosition;
    public int mMaxQuotePosition;
    private final int mNumberOfFragment = 1000;
    private QuoteFragmentPagerAdapter mQuoteFragmentPagerAdapter;
    private ViewPager mViewPager;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        randomQuoteInitialList = new RandomQuoteInitialList(this);

        // Initialise une liste de random quote vide
        mRandomQuoteArrayList = new ArrayList<Quote>(0);
        mCurrentQuotePosition = 0;
        mMaxQuotePosition = 0;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String theme = sharedPreferences.getString("pref_theme", "");
        setTheme(SettingRessources.getTheme(theme));

        for(int i=0; i<nb_init_quotes; i++) {
            quote = randomQuoteInitialList.getRandomQuoteFromIntialList(SettingRessources.getPrefCategories(this));
            if(quote != null) {
                Log.d("MY_QUOTES_DEBUG",quote.getQuote());
                mRandomQuoteArrayList.add(quote);
            }
            //mRandomQuoteArrayList.add(randomQuoteInitialList.getRandomQuoteFromIntialList(prefCategories));
        }

        RunAPI run = new RunAPI();
        try{
            run.execute();
            nb_init_quotes = 1;
        }catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuoteFragmentPagerAdapter = new QuoteFragmentPagerAdapter(
                getSupportFragmentManager(), mNumberOfFragment, mRandomQuoteArrayList, this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mQuoteFragmentPagerAdapter);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            if(bundle.containsKey("settings")) {
                Log.d("LOL", "Came here from settting");
                goSetting();
            }
            else if(bundle.containsKey("qod_key")){
                String [] qod = bundle.getStringArray("qod_key");
                Quote quote = stringArrToQuote(qod);
                goQod(quote);
            }
        }

        //-- NOTIFICATIONS --//
        sendNotification();
    }

    public void sendNotification(){

        int hour = 12;
        int minute = 30;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // check state in notification settings option
        if(!sharedPreferences.getBoolean("pref_qod_activate", false)){
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(this,AlarmeReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,100,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void setTime_notification(int time_notification){
        this.time_notification = time_notification;
    }

    // Uncheck the Favorite Star Icon in activty_main layout when quote removed from Favorites
    public void unCheckFavoriteState(String quote_id) {
        Log.d("MY_QUOTES_DEBUG","quote_id: "+quote_id);
        int position;
        Quote uncheckQuote = null;
        for(position=0; position<mRandomQuoteArrayList.size(); position++) {
            if(mRandomQuoteArrayList.get(position).getId().equals(quote_id)) {
                uncheckQuote = mRandomQuoteArrayList.get(position);
                if (uncheckQuote != null) {
                    Log.d("MY_QUOTES_DEBUG", "uncheckQuote: " + uncheckQuote.getQuote());
                } else {
                    Log.d("MY_QUOTES_DEBUG", "uncheckQuote: " + null);
                }
                break;
            }
        }

        if(uncheckQuote != null) {
            Log.d("MY_QUOTES_DEBUG","position: "+position);

            int currentPosition = mViewPager.getCurrentItem();

            Log.d("MY_QUOTES_DEBUG","current position: "+currentPosition);

            mViewPager.setCurrentItem(position);

            CheckBox checkBox = (CheckBox) mViewPager.getRootView().findViewById(R.id.chk_favorite);
            checkBox.setChecked(false);

            mViewPager.setCurrentItem(position+1);
            mViewPager.setCurrentItem(position+2);
            mViewPager.setCurrentItem(currentPosition);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        // Else we call the normal back pressed
        else{
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
            goSetting();
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

    public class RunAPI extends AsyncTask<String, Object, ArrayList<Quote>> {

        public RunAPI(){
            super();
        }

        @Override
        protected ArrayList<Quote> doInBackground(String... strings) {

            for (int i=0; i<nb_init_quotes; i++){

                QuoteAPI web = new QuoteAPI(randomQuoteFromPreferences(SettingRessources.getPrefCategories(MainActivity.this))
                        ,MainActivity.this);

                try {
                    quote = web.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(quote != null)
                    mRandomQuoteArrayList.add(quote);
            }

            return mRandomQuoteArrayList;
        }
    }

    public static Category randomQuoteFromPreferences(String[] preferences) {

        if(preferences.length == 0) {
            String[] default_preferences = {"inspire","management","sport","love","funny","art"};
            preferences = default_preferences;
        }
        int i = (int) Math.floor(Math.random()*preferences.length);

        return Category.valueOf(preferences[i]);

    }

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


    private void goHome(){
        getSupportFragmentManager().beginTransaction().remove(new Fragment());

        // removes all Fragments to get back to the main activity
        // hides the setting fragment, does not delete it
        removeAllFragments();
    }

    private void goFavorite(){

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

    private void goSetting(){

        removeAllFragments();

        android.app.Fragment frag_setting = new SettingsFragment();

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, frag_setting, "FRAG_SETTING");
        ft.show(frag_setting);
        ft.commit();
    }

    private void goAbout(){

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

    private void goQod(Quote quote){
        removeAllFragments();

        QuoteFragment frag_qod = QuoteFragment.newInstance(0, quote);
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, frag_qod, "FRAG_QOD");
        ft.show(frag_qod);
        ft.commit();
    }

    private Quote stringArrToQuote(String [] strArr){

        Quote quote = new Quote(strArr[0], strArr[1], Category.valueOf(strArr[2]), strArr[3]);
        return quote;
    }
}
