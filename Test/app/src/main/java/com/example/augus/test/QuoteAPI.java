package com.example.augus.test;

import android.util.Log;

import com.example.augus.test.theysaidso.Quotes;
import com.example.augus.test.theysaidso.Root;
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
    private String url;

    public QuoteAPI() {
        url = "http://quotes.rest/qod.json";
    }

    public Quotes run() throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        Moshi moshi = new Moshi.Builder().build();

        JsonAdapter<Root> jsonAdapter = moshi.adapter(Root.class);

        Root root = jsonAdapter.fromJson(json);

        Log.d("QUOTES", root.contents.quotes.get(0).toString());

        return root.contents.quotes.get(0);
    }
}
