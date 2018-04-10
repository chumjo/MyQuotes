package com.ift2905.myquotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        Log.d("SETTING", "changed : " + s);

        // If the setting changed is the Theme
        // Restarts the activity to force update the theme
        if(s.equals("pref_theme")) {
            String theme = sharedPreferences.getString("pref_theme", "");

            Activity activity = getActivity();
            activity.setTheme(SettingRessources.getTheme(theme));

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("settings", true);
            startActivity(intent);
        }

        if(s.equals("pref_cat_list")) {
            Set<String> set = new HashSet<String>();
            set = sharedPreferences.getStringSet("pref_cat_list", null);
            String[] new_preferences = null;
            if(set != null) {
                new_preferences = new String[set.size()];
                int i = 0;
                for(String str : set) {
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
            }
            MainActivity.preferences = new_preferences;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = super.onCreateView(inflater,container,savedInstanceState);
        view.getRootView().setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.white));
        addPreferencesFromResource(R.xml.preference);
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
