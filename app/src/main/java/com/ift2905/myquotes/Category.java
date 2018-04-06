package com.ift2905.myquotes;

public enum Category {

    // The different categories of quotes
    inspire,
    management,
    sport,
    love,
    funny,
    art;

    public String toString()
    {
        String toString = "";

        switch (this)
        {
            case inspire :
                toString = "inspire";
                break;
            case management:
                toString = "management";
                break;
            case sport :
                toString = "sport";
                break;
            case love:
                toString = "love" ;
                break;
            case funny :
                toString = "funny";
                break;
            case art :
                toString = "art";
                break;
        }

        return toString;
    }
}
