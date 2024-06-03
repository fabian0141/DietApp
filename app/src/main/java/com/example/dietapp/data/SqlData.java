package com.example.dietapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlData {

    private String[] ingredientColumns = new String[]{"id", "name", "calories", "protein", "carbs", "fat", "fiber",
            "vitaminA", "vitaminB1", "vitaminB2", "vitaminB5", "vitaminB6", "vitaminB12", "vitaminC", "vitaminE", "vitaminK",
            "calcium", "iron", "magnesium", "phosphor", "potassium", "zinc"};
    private SQLiteDatabase db;

    public SqlData(String path) {
        db = SQLiteDatabase.openDatabase(path, null, 0);
    }

    public int checkDBVersion(int version) {
        try {
            switch (version) {
                case 0:
                    db.execSQL("CREATE TABLE Meal (id INTEGER PRIMARY KEY, title TEXT, description TEXT, " +
                            "lastUse DATE DEFAULT CURRENT_TIMESTAMP, deprecatedMeal INTEGER(1) DEFAULT 0)");

                    db.execSQL("CREATE TABLE MealIngredient (id INTEGER PRIMARY KEY, mealID INTEGER, foodID INTEGER, amount INTEGER)");
                    db.execSQL("CREATE TABLE DailyIntake (id INTEGER PRIMARY KEY, time INTEGER, mealID INTEGER, portion REAL)");
                    return 1;
            }
        } catch (SQLException e) {
             e.printStackTrace();
        }


        return -1;
    }
    public Ingredient[] searchIngredients(String searchText, int limit) {
        List<Ingredient> resultList = new ArrayList<>();

        Cursor cursor = db.query(
                "Food",
                ingredientColumns,
                "name LIKE ?",
                new String[]{"%" + searchText + "%"},
                null,
                null,
                null,
                "" + limit
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ingredient ing = new Ingredient();
                ing.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ing.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                for (int i = 2; i < ingredientColumns.length; i++) {
                    ing.nutrients[i - 2] = cursor.getFloat(cursor.getColumnIndexOrThrow(ingredientColumns[i]));
                }
                resultList.add(ing);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList.toArray(new Ingredient[resultList.size()]);
    }

    public MealData getMeal(int mealID) {
        MealData meal = MealData.createEmptyMeal();
        Cursor cursor = db.rawQuery("SELECT * FROM Meal WHERE id = " + mealID + " ORDER BY lastUse DESC LIMIT 20;", null);

        cursor.moveToFirst();
        meal.mealID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        meal.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        meal.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        cursor.close();

        addMealIngredients(meal);
        return meal;
    }

    public int saveMeal(MealData meal) {
        if (meal.mealID > -1) {
            deprecateMeal(meal.mealID);
        }
        return insertMeal(meal);
    }

    private int insertMeal(MealData meal) {
        ContentValues values = new ContentValues();
        values.put("title", meal.title);
        values.put("description", meal.description);
        values.put("lastUse", System.currentTimeMillis());
        meal.mealID = (int) db.insert("Meal", null, values);

        String sql = "INSERT INTO MealIngredient (mealID, foodID, amount) VALUES ";
        for (int i = 0; i < meal.ingredients.size(); i++) {
            sql += "(" + meal.mealID + ", " + meal.ingredients.get(i).id + ", " + meal.ingredients.get(i).amount + "),";
        }
        sql = sql.subSequence(0, sql.length() - 1) + ";";
        db.execSQL(sql);
        return meal.mealID;
    }

    private void  deprecateMeal(int mealID) {
        ContentValues values = new ContentValues();
        values.put("deprecatedMeal", 1);
        db.update("Meal", values, "id = ?", new String[]{"" + mealID});
    }

    public void deleteMeal(int id) {
        db.execSQL("DELETE FROM Meal WHERE id =" + id + ";");
        db.execSQL("DELETE FROM MealIngredient WHERE mealID =" + id + ";");
    }

    public MealData getTodaysCombinedMeal() {
        MealData combMeal = MealData.createEmptyMeal();

        int curTime = getCurrentDay();
        Cursor cursor = db.rawQuery("SELECT * FROM (SELECT mealID, portion FROM DailyIntake WHERE time = " +
                curTime + " ORDER BY time ASC) di JOIN MealIngredient mi ON di.mealID = mi.mealID JOIN Food ON mi.foodID = Food.id;", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                float portion = cursor.getFloat(cursor.getColumnIndexOrThrow("portion"));
                for (int i = 2; i < ingredientColumns.length; i++) {
                    combMeal.nutrients[i - 2] += cursor.getFloat(cursor.getColumnIndexOrThrow(ingredientColumns[i])) * amount / 100 * portion;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return combMeal;
    }

    public MealData getLastWeeksCombinedMeal() {
        MealData combMeal = MealData.createEmptyMeal();

        int startTime = getCurrentDay() - 1;
        int endTime = getCurrentDay() - 6;
        Cursor cursor = db.rawQuery("SELECT * FROM (SELECT mealID, portion FROM DailyIntake " +
                "WHERE time BETWEEN " + startTime + " AND " + endTime + " ORDER BY time ASC) di " +
                "JOIN MealIngredient mi ON di.mealID = mi.mealID JOIN Food ON mi.foodID == Food.id;", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                float portion = cursor.getFloat(cursor.getColumnIndexOrThrow("portion"));
                for (int i = 2; i < ingredientColumns.length; i++) {
                    combMeal.nutrients[i - 2] += cursor.getFloat(cursor.getColumnIndexOrThrow(ingredientColumns[i])) * amount / 100 * portion;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return combMeal;
    }

    public void addTodayIntake(MealData meal, float portion) {
        db.execSQL("INSERT INTO DailyIntake (time, mealID, portion) VALUES (" + getCurrentDay() + "," + meal.mealID + ", " + portion + ");");
    }

    public void removeTodayIntake(int consumedID) {
        db.execSQL("DELETE FROM DailyIntake WHERE id =" + consumedID + ";");
    }

    public ArrayList<MealData> getMeals(String filter, int offsetMeals) {
        ArrayList<MealData> meals = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Meal WHERE title LIKE '%" + filter + "%' AND deprecatedMeal = 0 ORDER BY lastUse DESC LIMIT " + (20 + offsetMeals) + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MealData meal = MealData.createEmptyMeal();
                meal.mealID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                meal.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                meal.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                meals.add(meal);
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (MealData meal : meals) {
            addMealIngredients(meal);
        }

        return meals;
    }

    private void addMealIngredients(MealData meal) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM MealIngredient dc JOIN Food ON dc.foodID = Food.id WHERE mealID = " + meal.mealID + ";", null);
        Log.i("DATABASE", Arrays.toString(cursor.getColumnNames()));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ingredient ing = new Ingredient();
                ing.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ing.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                ing.amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                for (int i = 2; i < ingredientColumns.length; i++) {
                    ing.nutrients[i - 2] = cursor.getFloat(cursor.getColumnIndexOrThrow(ingredientColumns[i]));
                }
                ingredients.add(ing);
            } while (cursor.moveToNext());
        }

        cursor.close();

        meal.ingredients = ingredients;
        meal.calculateTotalNutrients();
    }

    private int getCurrentDay() {
        return (int)(System.currentTimeMillis() / 86400000);

    }

    public void removeOldDailyIntakes() {
        int lastRelevantDay = getCurrentDay() - 6;
        db.execSQL("DELETE FROM DailyIntake WHERE time <" + lastRelevantDay + ";");
    }

    public void removeUnusedDeprecatedMeals() {
        db.execSQL("DELETE FROM Meal WHERE deprecatedMeal = 1 AND id NOT IN (SELECT mealID FROM DailyIntake)");
    }

    public ArrayList<MealData> getTodaysMeals() {
        ArrayList<MealData> meals = new ArrayList<>();

        int curTime = getCurrentDay();
        Cursor cursor = db.rawQuery("SELECT di.mealID, di.id, Meal.title, di.portion FROM (SELECT id, mealID, portion FROM DailyIntake WHERE time = " +
                curTime + " ORDER BY time ASC) di JOIN Meal ON di.mealID = Meal.id;", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MealData meal = MealData.createEmptyMeal();
                meal.mealID = cursor.getInt(cursor.getColumnIndexOrThrow("mealID"));
                meal.consumedID = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                meal.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                meal.portion = cursor.getFloat(cursor.getColumnIndexOrThrow("portion"));
                meals.add(meal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return meals;
    }
}