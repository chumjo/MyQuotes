package com.ift2905.myquotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jonathan on 2018-04-07.
 */

public class SettingRessources {

    static String[] getPrefCategories(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Changing quotes random requests according to user selection of categories
        Set<String> set;
        set = sharedPreferences.getStringSet("pref_cat_list", null);
        String[] new_preferences;
        if(set.size() != 0) {
            new_preferences = new String[set.size()];
            int i = 0;
            for (String str : set) {
                switch (str) {
                    case "Inspirational":
                        new_preferences[i] = "inspire";
                        break;
                    case "Management":
                        new_preferences[i] = "management";
                        break;
                    case "Sport":
                        new_preferences[i] = "sport";
                        break;
                    case "Love":
                        new_preferences[i] = "love";
                        break;
                    case "Funny":
                        new_preferences[i] = "funny";
                        break;
                    case "Art":
                        new_preferences[i] = "art";
                        break;
                }
                i++;
            }
        } else {
            new_preferences = new String[]{"inspire", "management", "sport", "love", "funny", "art"};
        }
        return new_preferences;
    }

    // Returns the drawable icon of correspondant category
    static int getIcon(Category category){

        switch (category){
            case art:
                return R.drawable.icon_art;
            case funny:
                return R.drawable.icon_funny;
            case inspire:
                return R.drawable.icon_inspire;
            case management:
                return R.drawable.icon_management;
            case sport:
                return R.drawable.icon_sport;
            case love:
                return R.drawable.icon_love;
        }

        return R.drawable.ic_launcher_background;
    }

    // Returns the theme
    static int getTheme(String theme){

        if(theme.equals("theme1"))
            return R.style.AppThemeColor1;
        else if(theme.equals("theme2"))
            return R.style.AppThemeColor2;
        else if(theme.equals("theme3"))
            return R.style.AppThemeColor3;
        else if(theme.equals("theme4"))
            return R.style.AppThemeColor4;
        else if(theme.equals("theme5"))
            return R.style.AppThemeColor5;
        else if(theme.equals("theme6"))
            return R.style.AppThemeColor6;
        else if(theme.equals("theme7"))
            return R.style.AppThemeColor7;
        else if(theme.equals("theme8"))
            return R.style.AppThemeColor8;

        else return R.style.AppThemeColor1;
    }
}
