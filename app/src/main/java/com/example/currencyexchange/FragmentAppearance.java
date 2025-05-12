package com.example.currencyexchange;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class FragmentAppearance extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_appearance, rootKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(
                R.layout.fragment_settings_with_toolbar, container, false
        );
        Toolbar toolbar = root.findViewById(R.id.toolbar_settings);

        toolbar.setTitle("Внешний вид");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
        View prefs = super.onCreateView(inflater, container, savedInstanceState);
        ((ViewGroup) root.findViewById(R.id.settings_container)).addView(prefs);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager
                .getDefaultSharedPreferences(requireContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager
                .getDefaultSharedPreferences(requireContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        switch (key) {
            case "pref_dark_theme":
                boolean dark = prefs.getBoolean(key, false);
                AppCompatDelegate.setDefaultNightMode(
                        dark
                                ? AppCompatDelegate.MODE_NIGHT_YES
                                : AppCompatDelegate.MODE_NIGHT_NO
                );
                requireActivity().recreate();
                break;
            case "pref_font_size":
            case "pref_auto_hide_appbar":
                requireActivity().recreate();
                break;
        }
    }
}
