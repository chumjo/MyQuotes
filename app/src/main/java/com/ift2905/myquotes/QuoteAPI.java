package com.ift2905.myquotes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ift2905.myquotes.theysaidso.Root;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Get quote json from web API http://quotes.rest/
 * Parse it into a Quote object and return it
 */

public class QuoteAPI {
    private String url_begin;
    private String url_final;
    private Category category;
    private Context context;
    DBHelper dbh ;
    //private static boolean art_sport = true;

    // QuoteAPI constructor
    public QuoteAPI(Category category, Context context) {
        this.category = category;
        /*if (art_sport) {
            this.category = Category.sport;
            art_sport = false;
        } else {
            this.category = Category.art;
            art_sport = true;
        }*/
        this.context = context;
        url_begin = "http://quotes.rest/quote/search.json?category=";
        url_final = url_begin + category;
        dbh = new DBHelper(context);
    }

    // Asynchronous method to retrieve json from the web API
    public Quote run() throws IOException {

        // Check if connected to the web
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        Quote quote = null;

        // If connected, get json, parse it into Quote and return it. Else, return null
        if(connected) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url_final).header("X-TheySaidSo-Api-Secret", "zFq239fxPR_Y_MxmgZ1rlAeF").build();
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            Moshi moshi = new Moshi.Builder().build();

            JsonAdapter<Root> jsonAdapter = moshi.adapter(Root.class);

            Root root = jsonAdapter.fromJson(json);

            // If web API quota exceeded do nothing and return null in the end of the method
            if (root.contents != null) {
                quote = new Quote(root.contents.quote,
                    root.contents.author,
                    category,
                    root.contents.id);
                //DBHelper.addQuoteToFavorites(quote);
            }
        }

        return quote;
    }
}
