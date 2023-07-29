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

    public void addRecentMeal(int mealID) {
        int rm2 = getInteger(SettingsNames.RECENT_MEAL_1);
        int rm3 = getInteger(SettingsNames.RECENT_MEAL_2);
        if (rm2 == mealID) {
            return;
        }
        editor.putInt(SettingsNames.RECENT_MEAL_1.toString(), mealID);
        editor.putInt(SettingsNames.RECENT_MEAL_2.toString(), rm2);
        if (rm3 == mealID) {
            return;
        }
        editor.putInt(SettingsNames.RECENT_MEAL_3.toString(), rm3);
        editor.apply();
    }
}
