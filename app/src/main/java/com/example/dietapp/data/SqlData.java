package com.example.dietapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlData {

    private String[] ingredientColumns = new String[]{"id", "name", "calories", "protein", "carbs", "fat", "fiber",
            "vitaminA", "vitaminB1", "vitaminB2", "vitaminB5", "vitaminB6", "vitaminB12", "vitaminC", "vitaminE", "vitaminK",
            "calcium", "iron", "magnesium", "phosphor", "potassium", "zinc"};
    SQLiteDatabase db;
    Controller con;

    public SqlData(String path, Controller con) {
        this.con = con;
        db = SQLiteDatabase.openDatabase(path, null, 0);
        checkDBVersion();
    }

    private void checkDBVersion() {
        int version = con.getSettingsInteger(SettingsNames.DB_VERSION);
        switch (version) {
            case 0:
                db.execSQL("CREATE TABLE DailyConsumption (id INTEGER PRIMARY KEY, foodID INTEGER, amount INTEGER)");
            case 1:
                db.execSQL("CREATE TABLE Meal (id INTEGER PRIMARY KEY, title TEXT, description TEXT)");
                db.execSQL("CREATE TABLE MealIngredients (id INTEGER PRIMARY KEY, mealID INTEGER, foodID INTEGER, amount INTEGER)");
            case 2:
                db.execSQL("CREATE TABLE DailyIntake (id INTEGER PRIMARY KEY, time INTEGER, foodID INTEGER, amount INTEGER)");
                con.setSettingsInteger(SettingsNames.DB_VERSION, 3);
        }
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
    public MealData getDailyMeal() {
        ArrayList<Ingredient> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM DailyConsumption dc JOIN Food ON dc.foodID == Food.id", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ingredient ing = new Ingredient();
                ing.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                ing.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                ing.amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                for (int i = 2; i < ingredientColumns.length; i++) {
                    ing.nutrients[i - 2] = cursor.getFloat(cursor.getColumnIndexOrThrow(ingredientColumns[i]));
                }
                resultList.add(ing);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return MealData.loadMeal(-1, resultList);
    }
    public void applyDailyMeal(MealData dailyMeal) {
        db.execSQL("DELETE FROM DailyConsumption;");

        String sql = "INSERT INTO DailyConsumption (foodID, amount) VALUES ";
        for (int i = 0; i < dailyMeal.ingredients.size(); i++) {
            sql += "(" + dailyMeal.ingredients.get(i).id + ", " + dailyMeal.ingredients.get(i).amount + "),";
        }
        sql = sql.subSequence(0, sql.length() - 1) + ";";

        db.execSQL(sql);
    }
    public MealData getMeal(int mealID) {
        ArrayList<Ingredient> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM MealIngredients dc JOIN Food ON dc.foodID == Food.id WHERE mealID = " + mealID + ";", null);
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
                resultList.add(ing);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return getMealInfo(MealData.loadMeal(mealID, resultList));
    }
    private MealData getMealInfo(MealData data) {
        Cursor cursor = db.rawQuery("SELECT * FROM Meal WHERE id = " + data.mealID + ";", null);
        Log.i("DATABASE", Arrays.toString(cursor.getColumnNames()));
        cursor.moveToFirst();
        data.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        data.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        cursor.close();
        return data;
    }

    // TODO: Can be improved here
    // delete all data of meal instead of updating it
    public void saveMeal(MealData meal) {
        ContentValues values = new ContentValues();
        values.put("title", meal.title);
        values.put("description", meal.description);

        if (meal.mealID == -1) {
            meal.mealID = (int) db.insert("Meal", null, values);
        } else {
            db.update("Meal", values, "id = ?", new String[]{"" + meal.mealID});
            db.execSQL("DELETE FROM MealIngredients WHERE mealID =" + meal.mealID + ";");
        }

        String sql = "INSERT INTO MealIngredients (mealID, foodID, amount) VALUES ";
        for (int i = 0; i < meal.ingredients.size(); i++) {
            sql += "(" + meal.mealID + ", " + meal.ingredients.get(i).id + ", " + meal.ingredients.get(i).amount + "),";
        }
        sql = sql.subSequence(0, sql.length() - 1) + ";";
        db.execSQL(sql);
    }
    public MealData[] getMealPreviews(String search) {
        List<MealData> resultList = new ArrayList<>();

        Cursor cursor = db.query(
                "Meal",
                new String[]{"id", "title"},
                "title LIKE ?",
                new String[]{"%" + search + "%"},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                resultList.add(MealData.previewMeal(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title"))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList.toArray(new MealData[resultList.size()]);
    }
    public void deleteMeal(int id) {
        db.execSQL("DELETE FROM Meal WHERE id =" + id + ";");
        db.execSQL("DELETE FROM MealIngredients WHERE mealID =" + id + ";");
    }
    public Ingredient[] getWeekIntake() {
        List<Ingredient> resultList = new ArrayList<>();
        int curtime = getCurrentDay();
        Cursor cursor = db.rawQuery("SELECT * FROM (SELECT * FROM DailyIntake WHERE time > " + (curtime - 8) + " ORDER BY time ASC) di JOIN Food ON di.foodID == Food.id;", null);
        //Log.i("DATABASE", Arrays.toString(cursor.getColumnNames()));
        Ingredient ing = null;
        int time = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int foodTime = cursor.getInt(cursor.getColumnIndexOrThrow("time"));
                if (time < foodTime) {
                    time = foodTime;
                    ing = new Ingredient();
                    resultList.add(ing);
                    ing.id = time;
                }
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                for (int i = 2; i < ingredientColumns.length; i++) {
                    ing.nutrients[i - 2] += cursor.getFloat(cursor.getColumnIndexOrThrow(ingredientColumns[i])) * amount / 100;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Add placeholder for today
        if (time < curtime) {
            resultList.add(null);
        }
        return resultList.toArray(new Ingredient[resultList.size()]);
    }

    public void addTodayIntake(List<Ingredient> ings, float portion) {
        String sql = "INSERT INTO DailyIntake (time, foodID, amount) VALUES ";
        int curtime = getCurrentDay();
        for (int i = 0; i < ings.size(); i++) {
            sql += "(" + curtime + ", " + ings.get(i).id + ", " + ings.get(i).amount * portion + "),";
        }
        sql = sql.subSequence(0, sql.length() - 1) + ";";
        db.execSQL(sql);
    }

    private void initTodayIntake() {

    }

    private void getDailyIntake() {

    }

    public ArrayList<MealData> getMealPreview(ArrayList<Integer> mealIDs) {
        ArrayList<MealData> resultList = new ArrayList<>();

        Cursor cursor = db.query(
                "Meal",
                new String[]{"id", "title"},
                "id LIKE ?",
                mealIDs.stream().map(integer -> "" + integer).toArray(String[]::new),
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                resultList.add(MealData.previewMeal(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title"))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return resultList;
    }

    private int getCurrentDay() {
        return (int)(System.currentTimeMillis() / 86400000);

    }
}