package com.example.dietapp.data;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.View;

import com.example.dietapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Controller {
    SqlData db;
    Settings set;

    Persistent data;
    private static Controller INSTANCE;

    private Controller(Context context) {
        String dbPath = context.getApplicationInfo().dataDir + "/food.db";
        checkForDatabase(context, dbPath);
        set = new Settings(context);
        db = new SqlData(dbPath, this);
        data = new Persistent(this, db.getDailyMeal(), db.getWeekIntake());
    }

    public static Controller getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Controller(context);
        }
        return INSTANCE;
    }

    public Ingredient[] searchIngredients(String searchText, int limit) {
        return db.searchIngredients(searchText, limit);
    }
    private void checkForDatabase(Context context, String dbPath) {
        File file = new File(dbPath);
        if (!file.exists()) {
            Log.i("DATABASE", "Move Database to app directory");
            InputStream in = context.getResources().openRawResource(R.raw.food);

            try {
                FileOutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.close();
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: food.db", e);

            }
        }
    }
    public int getSettingsInteger(SettingsNames name) {
        return set.getInteger(name);
    }
    public void setSettingsInteger(SettingsNames name, int value) {
        set.setInteger(name, value);
    }
    public MealData getDailyMeal() {
        return data.getDailyMeal();
    }
    public Ingredient getDailyIntake() {
        return data.getDailyIntake();
    }

    public void applyDailyMeal(MealData dailyMeal) {
        data.applyDailyMeal(dailyMeal);
        db.applyDailyMeal(dailyMeal);
    }
    public void addTodayIntake(List<Ingredient> ings) {
        db.addTodayIntake();
    }
    public MealData getMeal(int mealID) {
        return db.getMeal(mealID);
    }

    public void saveMeal(MealData mealData) {
        db.saveMeal(mealData);
    }

    public MealData[] getMealPreviews(String search) {
        return db.getMealPreviews(search);
    }
    public void deleteMeal(int id) {
        db.deleteMeal(id);
    }

    public Ingredient getTodayIntake() {
        return data.getTodayIntake();
    }

    public Ingredient getPreviousIntake() {
        return data.getPreviousIntake();
    }

    public ArrayList<MealData> getRecentMealPreview() {
        ArrayList<Integer> mealIDs = new ArrayList<>();
        SettingsNames[] names = new SettingsNames[] {SettingsNames.RECENT_MEAL_1,
                SettingsNames.RECENT_MEAL_2, SettingsNames.RECENT_MEAL_3};
        for (int i = 0; i < 3; i++) {
            int mealID = getSettingsInteger(names[i]);
            if (mealID != -1) {
                mealIDs.add(mealID);
            }
        }
        return db.getMealPreview(mealIDs);
    }
}

// TODO: better database
// fix daily intake, adding to daily intake, persistent data
// add small meal functions, edit, amount
// update create meal ingredients
// recent meals