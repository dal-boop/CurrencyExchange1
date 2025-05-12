package com.example.currencyexchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;


public class FragmentSettings extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_root, rootKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(
                R.layout.fragment_settings_with_toolbar, container, false
        );
        Toolbar toolbar = root.findViewById(R.id.toolbar_settings);
        toolbar.setTitle("Настройки");
        View prefs = super.onCreateView(inflater, container, savedInstanceState);
        ((ViewGroup) root.findViewById(R.id.settings_container)).addView(prefs);
        return root;
    }
}

