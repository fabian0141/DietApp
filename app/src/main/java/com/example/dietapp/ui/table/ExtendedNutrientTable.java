package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;
import com.example.dietapp.data.NutriAttribs;

public class ExtendedNutrientTable extends CustomTable {

    private MealData todayMeal;
    private MealData weekMeal;

    public ExtendedNutrientTable(Context context) {
        super(context);

    }

    public void update(MealData todayMeal, MealData weekMeal) {
        super.update();
        this.todayMeal = todayMeal;
        this.weekMeal = weekMeal;
        createNutrientTable(getContext(), new String[]{"Nutrient", "Today", "7-Day"});
    }

    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {
        CustomTableRow row = new CustomTableRow(context);
        row.addNormalField(context, attrb.name, 15, false);
        float val = todayMeal.nutrients[nutriIndex];
        row.addColorField(context, combineUnit(val, attrb), 10, NutriAttribs.getColorVal(val, nutriIndex));
        float weekVal = (weekMeal.nutrients[nutriIndex] * 6 + todayMeal.nutrients[nutriIndex]) / 7;
        row.addColorField(context, combineUnit(weekVal, attrb), 10, NutriAttribs.getColorVal(weekVal, nutriIndex));
        addView(row);
    }
}