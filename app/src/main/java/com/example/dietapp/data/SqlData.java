package com.example.dietapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SqlData {

    private String[] ingredientColumns = new String[] {"id", "name", "calories", "protein", "carbs", "fat", "fiber",
            "vitaminA", "vitaminB1", "vitaminB2", "vitaminB5", "vitaminB6", "vitaminB12", "vitaminC", "vitaminE", "vitaminK",
            "calcium", "iron", "magnesium", "phosphor", "potassium", "zinc"};
    private static SqlData INSTANCE;
    SQLiteDatabase db;
    Controller con;

    private SqlData(String path, Controller con) {
        this.con = con;
        db = SQLiteDatabase.openDatabase(path, null, 0);
        checkDBVersion();
    }

    public static SqlData getInstance(String path, Controller con) {
        if (INSTANCE == null) {
            INSTANCE = new SqlData(path, con);
        }
        return INSTANCE;
    }

    private void checkDBVersion() {
        int version = con.getSettingsInteger(SettingsNames.DB_VERSION);
        switch (version) {
            case 0:
                db.execSQL("CREATE TABLE DailyConsumption (id INTEGER PRIMARY KEY, foodID INTEGER, amount INTEGER, UNIQUE(foodId))");
            case 1:
                db.execSQL("CREATE TABLE Meal (id INTEGER PRIMARY KEY, title TEXT, description TEXT)");
                db.execSQL("CREATE TABLE MealIngredients (id INTEGER PRIMARY KEY, mealID INTEGER, foodID INTEGER, amount INTEGER)");
                con.setSettingsInteger(SettingsNames.DB_VERSION, 2);
        }
    }

    public Ingredient[] searchIngredients(String searchText, int limit) {
        List<Ingredient> resultList = new ArrayList<>();

        Cursor cursor = db.query(
                "Food",
                ingredientColumns,
                "name LIKE ?",
                new String[] {"%" + searchText + "%"},
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
                    ing.nutrients[i-2] = cursor.getFloat(cursor.getColumnIndexOrThrow(ingredientColumns[i]));
                }
                resultList.add(ing);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList.toArray(new Ingredient[resultList.size()]);
    }

    public Ingredient[] getDailyFood() {
        List<Ingredient> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM DailyConsumption dc JOIN Food ON dc.foodID == Food.id", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ingredient ing = new Ingredient();
                ing.id = cursor.getInt(cursor.getColumnIndexOrThrow("Food.id"));
                ing.name = cursor.getString(cursor.getColumnIndexOrThrow("Food.name"));
                ing.amount = cursor.getInt(cursor.getColumnIndexOrThrow("dc.amount"));
                for (int i = 2; i < ingredientColumns.length; i++) {
                    ing.nutrients[i-2] = cursor.getFloat(cursor.getColumnIndexOrThrow("Food." + ingredientColumns[i]));
                }
                resultList.add(ing);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList.toArray(new Ingredient[resultList.size()]);
    }

    public void setDailyFood(int[] foodIDs, int[] amounts) {
        String sql = "INSERT OR IGNORE INTO DailyConsumption (foodID, amount) VALUES ";
        for (int i = 0; i < foodIDs.length; i++) {
            sql += "(" + foodIDs[i] + ", " + amounts[i] + "),";
        }
        sql = sql.subSequence(0, sql.length() - 1) + ";";

        db.execSQL(sql);
    }

    public Ingredient[] getMeal(int mealID) {
        List<Ingredient> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM MealIngredients dc JOIN Food ON dc.foodID == Food.id WHERE mealID = " + mealID + ";", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ingredient ing = new Ingredient();
                ing.id = cursor.getInt(cursor.getColumnIndexOrThrow("Food.id"));
                ing.name = cursor.getString(cursor.getColumnIndexOrThrow("Food.name"));
                ing.amount = cursor.getInt(cursor.getColumnIndexOrThrow("dc.amount"));
                for (int i = 2; i < ingredientColumns.length; i++) {
                    ing.nutrients[i-2] = cursor.getFloat(cursor.getColumnIndexOrThrow("Food." + ingredientColumns[i]));
                }
                resultList.add(ing);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList.toArray(new Ingredient[resultList.size()]);
    }

    public void saveMeal(MealData meal, int[] foodIDs, int[] amounts) {

        if (meal.mealID == -1) {
            ContentValues values = new ContentValues();
            values.put("title", meal.title);
            values.put("description", meal.description);
            meal.mealID = (int) db.insert("Meal", null, values);
        } else {
            db.execSQL("DELETE FROM MealIngredients WHERE id =" + meal.mealID + ";");
        }

        String sql = "INSERT INTO MealIngredients (mealID, foodID, amount) VALUES ";
        for (int i = 0; i < foodIDs.length; i++) {
            sql += "(" + meal.mealID + ", " + foodIDs[i] + ", " + amounts[i] + "),";
        }
        sql = sql.subSequence(0, sql.length() - 1) + ";";
        db.execSQL(sql);
    }
}