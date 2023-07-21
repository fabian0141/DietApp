package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;

import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.NutriAttribs;

public class IngredientTable extends CustomTable {

    public IngredientTable(Context context) {
        super(context);
    }
    @Override
    protected void initView(Context context) {
        super.initView(context);
        addTitleRow(context, new String[]{"Name", "Amount"}, new int[] {15, 5});
    }
    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {
    }

    @Override
    public void addIngredient(Ingredient ing) {
        CustomTableRow row = new CustomTableRow(getContext());
        row.addNormalField(getContext(), ing.name, 15, false);
        row.addNormalField(getContext(), ing.amount + " g", 5, true);
        addView(row);
    }
}