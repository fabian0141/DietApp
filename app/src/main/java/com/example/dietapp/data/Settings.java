package com.example.dietapp.data;

import android.content.Context;
import android.content.SharedPreferences;


public class Settings {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public Settings(Context context) {
        prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public int getInteger(SettingsNames name) {
        return prefs.getInt(name.toString(), -1);
    }
    public void setInteger(SettingsNames name, int value) {
        editor.putInt(name.toString(), value);
        editor.apply();
    }
}
