package com.example.augus.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.augus.test.theysaidso.Quotes;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView tv1;
    TextView tv2;
    TextView tv3;
    Quotes quotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        RunAPI run = new RunAPI();
        run.execute();
    }

    public class RunAPI extends AsyncTask<String, Object, Quotes> {

        @Override
        protected Quotes doInBackground(String... strings) {

            QuoteAPI web = new QuoteAPI();
            try{
                quotes = web.run();
            }catch (IOException e){
                e.printStackTrace();
            }
            return quotes;
        }

        @Override
        protected void onPostExecute(Quotes quotes) {
            super.onPostExecute(quotes);
            tv1.setText(quotes.author);
            tv2.setText(quotes.quote);
            tv3.setText(quotes.category);

        }
    }
}
