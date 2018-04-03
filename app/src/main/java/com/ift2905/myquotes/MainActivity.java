package com.ift2905.myquotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Quote> mRandomQuoteArrayList;
    public int mCurrentQuotePosition;
    private QuoteFragmentPagerAdapter mQuoteFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
