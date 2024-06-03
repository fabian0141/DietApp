package com.example.dietapp.data;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.View;

import com.example.dietapp.R;
import com.example.dietapp.ui.meals.Meal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Controller {

    public final static int TODAY_MEALS = -2;
    public final static int WEEK_MEALS = -3;
    public final static int TEMP_MEAL = -4;

    private SqlData db;
    private Settings set;
    private static Controller INSTANCE;

    private MealData todaysCombinedMeal;
    private MealData weeksCombinedMeal;
    private MealData tempMeal;
    private ArrayList<MealData> todaysMeals;


    private Controller(Context context) {
        String dbPath = context.getApplicationInfo().dataDir + "/food.db";
        checkForDatabase(context, dbPath);
        set = new Settings(context);
        db = new SqlData(dbPath);
        int newDBVersion = db.checkDBVersion(set.getInteger(SettingsNames.DB_VERSION));
        if (newDBVersion > 0) {
            set.setInteger(SettingsNames.DB_VERSION, 1);
        }
        init();
    }

    public static Controller getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Controller(context);
        }
        return INSTANCE;
    }

    private void init() {
        db.removeOldDailyIntakes();
        db.removeUnusedDeprecatedMeals();

        todaysCombinedMeal = db.getTodaysCombinedMeal();
        weeksCombinedMeal = db.getLastWeeksCombinedMeal();
        todaysMeals = db.getTodaysMeals();
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

    public void addTodayIntake(MealData meal, float portion) {
        db.addTodayIntake(meal, portion);
        todaysCombinedMeal = db.getTodaysCombinedMeal();
        todaysMeals = db.getTodaysMeals();
    }

    public void removeTodayIntake(int consumedID) {
        db.removeTodayIntake(consumedID);
        todaysCombinedMeal = db.getTodaysCombinedMeal();
        todaysMeals = db.getTodaysMeals();
    }

    public MealData getMeal(int mealID) {
        switch (mealID) {
            case TODAY_MEALS:
                return todaysCombinedMeal;
            case WEEK_MEALS:
                return weeksCombinedMeal;
            case TEMP_MEAL:
                return tempMeal;
            default:
                return db.getMeal(mealID);
        }
    }

    public int saveMeal(MealData mealData, boolean isTemp) {
        if (isTemp) {
            tempMeal = mealData;
            return -1;
        }
        return db.saveMeal(mealData);
    }

    public void deleteMeal(int id) {
        db.deleteMeal(id);
    }

    public ArrayList<MealData> getMeals(String filter, int offsetMeals) {
        return db.getMeals(filter, offsetMeals);
    }

    public ArrayList<MealData> getConsumedMeals() {
        return todaysMeals;
    }


}