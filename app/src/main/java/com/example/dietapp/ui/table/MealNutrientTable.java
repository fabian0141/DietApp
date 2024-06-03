package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;

import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;
import com.example.dietapp.data.NutriAttribs;

public class MealNutrientTable extends CustomTable {


    private MealData meal;
    private MealData dayMeals;
    private float portion = 1;

    private boolean consumed;

    public MealNutrientTable(Context context, boolean consumed) {
        super(context);
        this.consumed = consumed;
    }

    public void update(MealData meal, MealData dayMeals, float portion) {
        super.update();
        this.meal = meal;
        this.dayMeals = dayMeals;
        this.portion = portion;
        createNutrientTable(getContext(), new String[]{"Nutrient", "Meal", "Today"});
    }

    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {
        CustomTableRow row = new CustomTableRow(context);
        row.addNormalField(context, attrb.name, 15, false);
        row.addNormalField(context, combineUnit(meal.nutrients[nutriIndex] * portion, attrb), 10, true);
        if (consumed) {
            row.addColorField(context, combineUnit(dayMeals.nutrients[nutriIndex], attrb), 10, Color.rgb(200,0,0));
        } else {
            row.addColorField(context, combineUnit(dayMeals.nutrients[nutriIndex] + meal.nutrients[nutriIndex] * portion, attrb), 10, Color.rgb(200,0,0));
        }
        addView(row);
        //nutrientRows[nutriIndex] = row;
    }
}