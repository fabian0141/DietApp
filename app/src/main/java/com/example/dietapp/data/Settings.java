package com.example.dietapp.data;

import android.content.Context;
import android.content.SharedPreferences;


public class Settings {
    private static Settings INSTANCE;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private Settings(Context context) {
        prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    public static Settings getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Settings(context);
        }
        return INSTANCE;
    }
    public int getInteger(SettingsNames name) {
        return prefs.getInt(name.toString(), 0);
    }
    public void setInteger(SettingsNames name, int value) {
        editor.putInt(name.toString(), value);
        editor.apply();
    }
}
