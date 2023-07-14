package com.example.dietapp.data;

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

    private SqlData(String path) {
        db = SQLiteDatabase.openDatabase(path, null, 0);
    }

    public static SqlData getInstance(String path) {
        if (INSTANCE == null) {
            INSTANCE = new SqlData(path);
        }

        return INSTANCE;
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
}