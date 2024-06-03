package com.example.dietapp.data;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MealData implements Cloneable {

    public int mealID;
    public int consumedID;
    public String title;
    public String description;
    public ArrayList<Ingredient> ingredients;
    public float[] nutrients;

    public float portion = 1;

    public MealData(int mealID, String title, String description, ArrayList<Ingredient> ingredients) {
        this.mealID = mealID;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        calculateTotalNutrients();
    }

    public MealData() {
        this.mealID = -1;
        this.title = "";
        this.description = "";
        this.ingredients = new ArrayList<>();
        nutrients = new float[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    }

    public static MealData createEmptyMeal() {
        return new MealData();
    }

    public void calculateTotalNutrients() {
        nutrients = new float[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (Ingredient ing : ingredients) {
            for (int i = 0; i < nutrients.length; i++) {
                nutrients[i] += ing.nutrients[i] * ing.amount / 100;
            }
        }
    }
}
