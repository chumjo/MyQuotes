package com.ift2905.myquotes;

/**
 * Created by Jonathan on 2018-04-07.
 */

public class SettingRessources {

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

        else return R.style.AppThemeColor1;
    }
}
