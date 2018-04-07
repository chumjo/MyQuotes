package com.ift2905.myquotes;

import android.content.Context;
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
    DBHelper dbh ;

    public QuoteAPI(Category category, Context context) {
        this.category = category;
        url_begin = "http://quotes.rest/quote/search.json?maxlength=430&category=";
        url_final = url_begin + category;
        dbh = new DBHelper(context);

        /***** DEBUGGING LOG - REMOVE!!! *****/
        Log.d("MY_QUOTES_DEBUG", url_final);
    }

    public Quote run() throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url_final).header("X-TheySaidSo-Api-Secret","zFq239fxPR_Y_MxmgZ1rlAeF").build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        Moshi moshi = new Moshi.Builder().build();

        JsonAdapter<Root> jsonAdapter = moshi.adapter(Root.class);

        Root root = jsonAdapter.fromJson(json);

        Quote quote = new Quote(root.contents.quote,
                                root.contents.author,
                                category,
                                root.contents.id);

        dbh.addQuoteToFavorites(quote);

        /***** DEBUGGING LOG - TEST DUPLICITY - REMOVE!!! *****/
        /*quote = new Quote("A leader is best when people barely know he exists,"+
        "when his work is done, his aim fulfilled, they will say: We did it ourselves.",
        "Lao Tzu",Category.management,"6jubfcxQj5R23L7Bl7lxNAeF");
        dbh.addQuoteToFavorites(quote);*/

        return quote;
    }
}
