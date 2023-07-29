package com.example.dietapp.data;

import com.example.dietapp.ui.dashboard.Meal;

import java.util.List;

public class Persistent {

    private Ingredient previousIntake;
    private Ingredient[] weekIntake;
    private Ingredient todayIntake;
    private MealData dailyMeal;
    private Ingredient dailyIntake;


    public Persistent(MealData dailyMeal, Ingredient[] weekIntake) {
        this.dailyMeal = dailyMeal;
        dailyIntake = new Ingredient();
        for (int i = 0; i < dailyMeal.ingredients.size(); i++) {
            for (int j = 0; j < dailyMeal.ingredients.get(0).nutrients.length; j++) {
                dailyIntake.nutrients[j] += dailyMeal.ingredients.get(i).nutrients[j];
            }
        }

        this.weekIntake = weekIntake;
        previousIntake = new Ingredient();
        for (int i = 0; i < weekIntake.length - 1; i++) {
            for (int j = 0; j < weekIntake[0].nutrients.length; j++) {
                previousIntake.nutrients[j] += weekIntake[i].nutrients[j];
            }
        }
    }

    public void init(Controller con) {
        if (weekIntake[weekIntake.length - 1] == null) {
            //weekIntake[weekIntake.length - 1] = dailyIntake.clone();
            weekIntake[weekIntake.length - 1] = new Ingredient();
            todayIntake = weekIntake[weekIntake.length-1];
            con.addTodayIntake(dailyMeal.ingredients, 1);
        } else {
            todayIntake = weekIntake[weekIntake.length-1];
        }
    }
    public Ingredient getDailyIntake() {
        return dailyIntake;
    }
    public MealData getDailyMeal() {
        return dailyMeal;
    }
    public Ingredient getTodayIntake() {
        return todayIntake;
    }
    public Ingredient getPreviousIntake() {
        return previousIntake;
    }

    public void applyDailyMeal(MealData dailyMeal) {
        for (int i = 0; i < dailyMeal.ingredients.size(); i++) {
            for (int j = 0; j < todayIntake.nutrients.length; j++) {
                todayIntake.nutrients[j] += dailyMeal.ingredients.get(i).nutrients[j];
            }
        }

        for (int i = 0; i < this.dailyMeal.ingredients.size(); i++) {
            for (int j = 0; j < todayIntake.nutrients.length; j++) {
                todayIntake.nutrients[j] -= this.dailyMeal.ingredients.get(i).nutrients[j];
            }
        }

        this.dailyMeal = dailyMeal.clone();
        dailyIntake = new Ingredient();
        for (int i = 0; i < dailyMeal.ingredients.size(); i++) {
            for (int j = 0; j < dailyMeal.ingredients.get(0).nutrients.length; j++) {
                dailyIntake.nutrients[j] += dailyMeal.ingredients.get(i).nutrients[j];
            }
        }
    }

    public void addTodayIntake(List<Ingredient> ings, float portion) {
        for (int i = 0; i < ings.size(); i++) {
            for (int j = 0; j < todayIntake.nutrients.length; j++) {
                todayIntake.nutrients[j] += ings.get(i).nutrients[j] * portion;
            }
        }
    }
}
