package com.ift2905.myquotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public ArrayList<Quote> mRandomQuoteArrayList;
    public int mCurrentQuotePosition;

    private final int mNumberOfFragment = 1000;

    private QuoteFragmentPagerAdapter mQuoteFragmentPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialise une liste de random quote vide
        mRandomQuoteArrayList = new ArrayList<Quote>(0);

        // TMP BEG

        for (int i=0; i<mNumberOfFragment; i++){
            mRandomQuoteArrayList.add(new Quote("quote : "+i, "- jonathan", "management","id"+i));
        }
        // TMP END

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuoteFragmentPagerAdapter = new QuoteFragmentPagerAdapter(getSupportFragmentManager(), mNumberOfFragment, mRandomQuoteArrayList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mQuoteFragmentPagerAdapter);

        @SuppressLint("WrongViewCast") DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        @SuppressLint("WrongViewCast") DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            // Handle the camera action
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

        @SuppressLint("WrongViewCast") DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
