package com.ift2905.myquotes;

public enum Category {

    // The different categories of quotes
    INSPIRATIONAL,
    MANAGEMENT,
    ART,
    SPORTS,
    LOVE,
    FUNNY;

    public String toString()
    {
        String toString = "";

        switch (this)
        {
            case INSPIRATIONAL :
                toString = "inspire";
                break;
            case MANAGEMENT:
                toString = "management";
                break;
            case SPORTS :
                toString = "sport";
                break;
            case LOVE:
                toString = "love" ;
                break;
            case FUNNY :
                toString = "funny";
                break;
            case ART :
                toString = "art";
                break;
        }

        return toString;
    }
}
