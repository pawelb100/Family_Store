package com.familystore.familystore.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.familystore.familystore.R;
import com.familystore.familystore.models.AppPreview;

public class SettingsManager {

    private SharedPreferences sharedPref;

    private Context context;

    public SettingsManager(SharedPreferences sharedPreferences, Context context) {
        this.sharedPref = sharedPreferences;
        this.context = context;
    }

    public AppPreview.Order getDefaultAppSorting() {
        return AppPreview.Order.valueOf(
                sharedPref.getString("default_app_sort", "PUBLISHED"));
    }

    public void setTheme() {
        // Theme set
        switch (sharedPref.getString("theme", "light")) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "system":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            default:
        }

        // Color set
        switch (sharedPref.getString("color", "classic")) {
            case "classic":
                context.setTheme(R.style.Theme_FamilyStoreClassic);
                break;
            case "blue":
                context.setTheme(R.style.Theme_FamilyStoreBlue);
                break;
            case "orange":
                context.setTheme(R.style.Theme_FamilyStoreOrange);
                break;
            case "deepblue":
                context.setTheme(R.style.Theme_FamilyStoreDeepBlue);
                break;
            case "deeporange":
                context.setTheme(R.style.Theme_FamilyStoreDeepOrange);
                break;
            case "bluegrey":
                context.setTheme(R.style.Theme_FamilyStoreBlueGrey);
                break;
            case "deepgreen":
                context.setTheme(R.style.Theme_FamilyStoreDeepGreen);
                break;
            default:
        }
    }
}
