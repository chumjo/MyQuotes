package com.ift2905.myquotes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ift2905.myquotes.theysaidso.Root;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by augus on 02/04/2018.
 */

public class QuoteAPI {
    private String url_begin;
    private String url_final;
    private Category category;
    private Context context;
    DBHelper dbh ;

    public QuoteAPI(Category category, Context context) {
        this.category = category;
        this.context = context;
        url_begin = "http://quotes.rest/quote/search.json?category=";
        url_final = url_begin + category;
        dbh = new DBHelper(context);

        /***** DEBUGGING LOG - REMOVE!!! *****/
        //Log.d("MY_QUOTES_DEBUG", url_final);
    }

    public Quote run() throws IOException {

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

        if(connected) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url_final).header("X-TheySaidSo-Api-Secret", "zFq239fxPR_Y_MxmgZ1rlAeF").build();
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            Moshi moshi = new Moshi.Builder().build();

            JsonAdapter<Root> jsonAdapter = moshi.adapter(Root.class);

            Root root = jsonAdapter.fromJson(json);
            if (root.contents != null) {
                quote = new Quote(root.contents.quote,
                    root.contents.author,
                    category,
                    root.contents.id);
            }
        }
        return quote;
    }
}
