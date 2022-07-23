package com.example.sunshine.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.sunshine.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
    }
}
