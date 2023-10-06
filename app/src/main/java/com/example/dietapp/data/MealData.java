package com.example.dietapp.data;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
        clonedMeal.ingredients = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            clonedMeal.ingredients.add(ingredients.get(i).clone());
        }
        return clonedMeal;
    }

    public void updateIngredient(Ingredient ingredient) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).id == ingredient.id) {
                if (ingredient.amount == 0) {
                    ingredients.remove(i);
                    return;
                }
                ingredients.get(i).amount = ingredient.amount;
            }
        }
    }
}
