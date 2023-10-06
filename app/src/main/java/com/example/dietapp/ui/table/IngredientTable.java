package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.NutriAttribs;
import com.example.dietapp.ui.dialog.IngredientDialog;
import com.example.dietapp.ui.dialog.IngredientUpdateDialog;

import java.util.ArrayList;

import kotlin.Pair;

public class IngredientTable extends CustomTable {

    private OnClickListener listener = null;
    private ArrayList<Pair<Integer, CustomTableRow>> rows;
    private IngredientUpdateDialog dialog;

    public IngredientTable(Context context, IngredientUpdateDialog dialog) {
        super(context);
        this.dialog = dialog;
    }
    @Override
    protected void initView(Context context) {
        super.initView(context);
        addTitleRow(context, new String[]{"Name", "Amount"}, new int[] {15, 5});
        rows = new ArrayList<>();
    }
    @Override
    protected void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex) {}
    @Override
    public void addIngredient(Ingredient ing, float factor) {

        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getFirst() == ing.id) {
                if (ing.amount == 0) {
                    rows.remove(i);
                    removeViewAt(i + 1);
                    return;
                }
                rows.get(i).getSecond().updateValues(new float[] {ing.amount}, null);
                return;
            }
        }

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
        rows.add(new Pair<>(ing.id, row));
    }
}