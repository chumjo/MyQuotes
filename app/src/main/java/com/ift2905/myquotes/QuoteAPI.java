package com.ift2905.myquotes;

import android.content.Context;

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

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url_final).header("X-TheySaidSo-Api-Secret","zFq239fxPR_Y_MxmgZ1rlAeF").build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        Moshi moshi = new Moshi.Builder().build();

        JsonAdapter<Root> jsonAdapter = moshi.adapter(Root.class);

        Root root = jsonAdapter.fromJson(json);

        Quote quote;

        if(root.contents == null) {
            if(RandomQuoteInitialList.getInitialQuoteListSize() == 0) {
                quote = new Quote("Blablabla",
                        "",
                        category,
                        "blablablaLOL");
            } else {
                quote = RandomQuoteInitialList.getRandomQuoteFromIntialList(SettingRessources.getPrefCategories(context));
            }
        } else {
            quote = new Quote(root.contents.quote,
                    root.contents.author,
                    category,
                    root.contents.id);
        }

        return quote;
    }
}
