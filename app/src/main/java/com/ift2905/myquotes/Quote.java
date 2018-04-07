package com.ift2905.myquotes;

import android.support.annotation.NonNull;

public class Quote {

    // Attributs
    private String quote;
    private String author;
    private Category category;
    private String id;

    // Getters
    public String getQuote() {
        return "\"" + quote + "\"";
    }

    public String getAuthor() {

        if(author == null || author.equals(""))
            return "- unkown author";
        else
            return "- " + author;
    }

    public Category getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    // Constructors
    public Quote(String quote, String author, Category category, String id)
    {
        this.quote = quote;
        this.author = author;
        this.category = category;
        this.id = id;
    }

    public static boolean equals(Quote q1, Quote q2) {

        return q1.getId().equals(q2.getId());

    }
}
