package com.example.dietapp.data;

public enum SettingsNames {
    DB_VERSION("DB_VERSION"),
    RECENT_MEAL_1("RECENT_MEAL_1"),
    RECENT_MEAL_2("RECENT_MEAL_2"),
    RECENT_MEAL_3("RECENT_MEAL_3");


    private final String str;
    SettingsNames(final String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
