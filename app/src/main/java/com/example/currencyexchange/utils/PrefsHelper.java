package com.example.currencyexchange.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class PrefsHelper {
    private static final String PREFS_NAME  = "favorites_prefs";
    private static final String KEY_FAVORITES = "favorites";

    public static void saveFavorites(Context ctx, Set<String> favs) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(KEY_FAVORITES, favs).apply();
    }

    public static Set<String> getFavorites(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
    }

    public static Set<String> getFavoritesWithDefaults(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favs = prefs.getStringSet(KEY_FAVORITES, null);
        if (favs == null || favs.isEmpty()) {
            favs = new HashSet<>();
            favs.add("USD");
            favs.add("EUR");
            favs.add("GBP");
            saveFavorites(ctx, favs);
        }
        return favs;
    }
}
