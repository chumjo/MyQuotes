package com.ift2905.myquotes;

public enum Category {

    // The different categories of quotes
    INSPIRATIONAL,
    MANAGEMENT,
    LIFE,
    SPORTS,
    LOVE,
    FUNNY;

    public static Category getCategory(String category)
    {
        switch (category)
        {
            case "inspire" :
                return  INSPIRATIONAL;
            case "management" :
                return MANAGEMENT;
            case "sport" :
                return SPORTS;
            case "love" :
                return LOVE;
            case "funny" :
                return FUNNY;
            default :
                return LIFE;
        }
    }
}
