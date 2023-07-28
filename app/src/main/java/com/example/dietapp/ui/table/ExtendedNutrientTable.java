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
        row.addColorField(context, combineUnit(dayNutrients.nutrients[nutriIndex], attrb), 10, Color.rgb(200,0,0));
        float weekVal = (previousIntake.nutrients[nutriIndex] * 6 + dayNutrients.nutrients[nutriIndex]) / 7;
        row.addColorField(context, combineUnit(weekVal, attrb), 10, Color.rgb(200,0,0));
        addView(row);
    }

    @Override
    public void addIngredient(Ingredient ing) {
        addNutrients(dayNutrients, ing);

        for (int i = 0; i < nutrientRows.length; i++) {
            nutrientRows[i].updateValues(new float[]{dayNutrients.nutrients[i]}, new int[]{Color.rgb(200,0,0)});
            float weekVal = (previousIntake.nutrients[i] * 6 + dayNutrients.nutrients[i]) / 7;
            nutrientRows[i].updateValues(new float[]{weekVal}, new int[]{Color.rgb(200,0,0)});
        }
    }
}