package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;

import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.NutriAttribs;

public class ExtendedNutrientTable extends CustomTable {

    private Ingredient dayNutrients;
    private Ingredient weekNutrients;

    public ExtendedNutrientTable(Context context) {
        super(context);
    }
    @Override
    protected void initView(Context context) {
        super.initView(context);
        dayNutrients = new Ingredient();
        weekNutrients = new Ingredient();

        createNutrientTable(context, new String[]{"Nutrient", "Today", "7-Day"});
    }
    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {
        CustomTableRow row = new CustomTableRow(context);
        row.addNormalField(context, attrb.name, 15, false);
        row.addColorField(context, combineUnit(dayNutrients.nutrients[nutriIndex], attrb), 10, Color.rgb(200,0,0));
        row.addColorField(context, combineUnit(weekNutrients.nutrients[nutriIndex], attrb), 10, Color.rgb(200,0,0));
        addView(row);
    }

    @Override
    public void addIngredient(Ingredient ing, int amount) {
        addNutrients(dayNutrients, ing, amount);
        addNutrients(weekNutrients, ing, amount);

        for (int i = 0; i < nutrientRows.length; i++) {
            nutrientRows[i].updateValues(new float[]{dayNutrients.nutrients[i]}, new int[]{Color.rgb(200,0,0)});
            nutrientRows[i].updateValues(new float[]{weekNutrients.nutrients[i]}, new int[]{Color.rgb(200,0,0)});
        }
    }
}