package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;
import com.example.dietapp.data.NutriAttribs;

public class SimpleNutrientTable extends CustomTable {
    private MealData meal;

    public SimpleNutrientTable(Context context) {
        super(context);
    }

    public void update(MealData meal) {
        super.update();
        this.meal = meal;
        createNutrientTable(getContext(), new String[]{"Nutrient", "Today"});
    }

    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {
        CustomTableRow row = new CustomTableRow(context);
        row.addNormalField(context, attrb.name, 15, false);
        float val = meal.nutrients[nutriIndex];
        row.addColorField(context, combineUnit(val, attrb), 10, NutriAttribs.getColorVal(val, nutriIndex));
        addView(row);
    }
}
