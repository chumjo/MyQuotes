package com.ift2905.myquotes;

/**
 * Created by Jonathan on 2018-04-07.
 */

public class SettingRessources {

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
