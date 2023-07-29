package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.NutriAttribs;

public class SimpleNutrientTable extends CustomTable {
    private Ingredient nutriList;

    public SimpleNutrientTable(Context context) {
        super(context);
    }
    @Override
    protected void initView(Context context) {
        super.initView(context);
        nutriList = new Ingredient();
        createNutrientTable(context, new String[]{"Nutrient", "Today"});
    }
    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {
        CustomTableRow row = new CustomTableRow(context);
        row.addNormalField(context, attrb.name, 15, false);
        float val = nutriList.nutrients[nutriIndex];
        row.addColorField(context, combineUnit(val, attrb), 10, NutriAttribs.getColorVal(val, nutriIndex));
        addView(row);
        nutrientRows[nutriIndex] = row;
    }
    @Override
    public void addIngredient(Ingredient ing, float factor) {
        addNutrients(nutriList, ing, factor);

        for (int i = 0; i < nutrientRows.length; i++) {
            float val = nutriList.nutrients[i];
            nutrientRows[i].updateValues(new float[]{val}, new int[]{NutriAttribs.getColorVal(val, i)});
        }
    }
}
