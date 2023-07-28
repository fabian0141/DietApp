package com.example.dietapp.data;

import com.example.dietapp.ui.dashboard.Meal;

import java.util.ArrayList;

public class MealData implements Cloneable {

    public int mealID;
    public String title;
    public String description;
    public ArrayList<Ingredient> ingredients;

    public static MealData newMeal() {
        MealData meal = new MealData();
        meal.mealID = -1;
        return meal;
    }

    public static MealData loadMeal(int id, ArrayList<Ingredient> ingredients) {
        MealData meal = new MealData();
        meal.mealID = id;
        meal.ingredients = ingredients;
        return meal;
    }

    public static MealData previewMeal(int id, String title) {
        MealData meal = new MealData();
        meal.mealID = id;
        meal.title = title;
        return meal;
    }

    @Override
    public MealData clone() {
        MealData clonedMeal = new MealData();
        clonedMeal.mealID = mealID;
        clonedMeal.title = title;
        clonedMeal.description = description;
        clonedMeal.ingredients = (ArrayList<Ingredient>) ingredients.clone();
        return clonedMeal;
    }
}
