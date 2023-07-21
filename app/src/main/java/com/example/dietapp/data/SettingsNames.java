package com.example.dietapp.data;

public enum SettingsNames {
    DB_VERSION("DB_VERSION");

    private final String str;
    SettingsNames(final String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
