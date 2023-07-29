package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.NutriAttribs;

public class ExtendedNutrientTable extends CustomTable {

    private Ingredient dayNutrients;
    private Ingredient previousIntake;

    public ExtendedNutrientTable(Context context) {
        super(context);
    }
    @Override
    protected void initView(Context context) {
        super.initView(context);
        dayNutrients = con.getTodayIntake();
        previousIntake = con.getPreviousIntake();

        createNutrientTable(context, new String[]{"Nutrient", "Today", "7-Day"});
        Log.i("TEST", "Wurks");
    }
    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {
        CustomTableRow row = new CustomTableRow(context);
        row.addNormalField(context, attrb.name, 15, false);
        float val = dayNutrients.nutrients[nutriIndex];
        row.addColorField(context, combineUnit(val, attrb), 10, NutriAttribs.getColorVal(val, nutriIndex));
        float weekVal = (previousIntake.nutrients[nutriIndex] * 6 + dayNutrients.nutrients[nutriIndex]) / 7;
        row.addColorField(context, combineUnit(weekVal, attrb), 10, NutriAttribs.getColorVal(weekVal, nutriIndex));
        addView(row);
    }
    @Override
    public void addIngredient(Ingredient ing, float factor) {
        addNutrients(dayNutrients, ing, factor);

        for (int i = 0; i < nutrientRows.length; i++) {
            float val = dayNutrients.nutrients[i];
            nutrientRows[i].updateValues(new float[]{val}, new int[]{NutriAttribs.getColorVal(val, i)});
            float weekVal = (previousIntake.nutrients[i] * 6 + dayNutrients.nutrients[i]) / 7;
            nutrientRows[i].updateValues(new float[]{weekVal}, new int[]{NutriAttribs.getColorVal(weekVal, i)});
        }
    }
}