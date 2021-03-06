package com.ift2905.myquotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        // If the setting changed is the Categories
        // Restarts the activity to get only pref categories
        if(s.equals("pref_cat_list")){

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("settings", true);
            startActivity(intent);
        }

        // If the setting changed is the Theme
        // Restarts the activity to force update the theme
        if(s.equals("pref_theme")) {
            String theme = sharedPreferences.getString("pref_theme", "");

            Activity activity = getActivity();
            activity.setTheme(SettingsResources.getTheme(theme));

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("settings", true);
            startActivity(intent);
        }

        // Quote of the day
        if(s.equals("pref_qod_activate")){

            Log.d("QOD", "Quote of the day pref has change");

            if(sharedPreferences.getBoolean("pref_qod_activate", false)) {
                Intent intent = new Intent(getContext(), NotificationService.class);
                getActivity().startService(intent);
            }
            else {
                Log.d("QOD", "stop service");
                Intent intent = new Intent(getContext(), NotificationService.class);
                getActivity().stopService(intent);
            }
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
