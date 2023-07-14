package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;

import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.NutriAttribs;

public class MealNutrientTable extends CustomTable {


    private Ingredient nutriList;
    private Ingredient dayNutrients;

    public MealNutrientTable(Context context) {
        super(context);
    }
    @Override
    protected void initView(Context context) {
        super.initView(context);
        nutriList = new Ingredient();
        dayNutrients = new Ingredient();
        createNutrientTable(context, new String[]{"Nutrient", "Meal", "Today"});
    }
    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {
        CustomTableRow row = new CustomTableRow(context);
        row.addNormalField(context, attrb.name, 15, false);
        row.addNormalField(context, combineUnit(nutriList.nutrients[nutriIndex], attrb), 10, true);
        row.addColorField(context, combineUnit(dayNutrients.nutrients[nutriIndex], attrb), 10, Color.rgb(200,0,0));
        addView(row);
    }

    @Override
    public void addIngredient(Ingredient ing, int amount) {
        addNutrients(nutriList, ing, amount);
        addNutrients(dayNutrients, ing, amount);

        for (int i = 0; i < nutrientRows.length; i++) {
            nutrientRows[i].updateValues(new float[]{nutriList.nutrients[i]}, new int[]{Color.rgb(200,0,0)});
            nutrientRows[i].updateValues(new float[]{dayNutrients.nutrients[i]}, new int[]{Color.rgb(200,0,0)});
        }
    }
}