package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;
import com.example.dietapp.data.NutriAttribs;
import com.example.dietapp.ui.dialog.IngredientDialog;
import com.example.dietapp.ui.dialog.IngredientUpdateDialog;

import java.util.ArrayList;

import kotlin.Pair;

public class IngredientTable extends CustomTable {

    private IngredientUpdateDialog dialog;

    public IngredientTable(Context context, IngredientUpdateDialog dialog) {
        super(context);
        this.dialog = dialog;
        addTitleRow(context, new String[]{"Name", "Amount"}, new int[] {15, 5});
    }

    public void update(MealData meal) {
        super.update();
        addTitleRow(getContext(), new String[]{"Name", "Amount"}, new int[] {15, 5});

        for (Ingredient ing : meal.ingredients) {
            CustomTableRow row = new CustomTableRow(getContext());
            row.addClickableField(getContext(), ing.name, 15, false);
            row.addClickableField(getContext(), ing.amount + " g", 5, true);
            if (dialog != null) {
                row.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.update(ing);
                    }
                });
            }
            addView(row);
        }
    }

    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {}
}