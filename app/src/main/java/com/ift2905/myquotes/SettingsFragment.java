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


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Toast.makeText(getActivity(),"Pref changee : " + s, Toast.LENGTH_SHORT).show();

        String theme = sharedPreferences.getString("pref_theme", "");

        Activity activity = getActivity();
        activity.setTheme(SettingRessources.getTheme(theme));


        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("settings", true);
        startActivity(intent);
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
}
