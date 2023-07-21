package com.example.dietapp.ui.table;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TableLayout;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.NutriAttribs;

import java.text.DecimalFormat;

public abstract class CustomTable extends TableLayout {

    private Controller con;
    private DecimalFormat nutriFormat = new DecimalFormat("#.##");
    protected CustomTableRow[] nutrientRows = new CustomTableRow[20];

    public CustomTable(Context context) {
        super(context);
        initView(context);
    }

    public abstract void addIngredient(Ingredient ing);

    protected void initView(Context context) {
        setBackgroundColor(Color.rgb(60, 65, 70));
        getData();
    }
    private void getData() {
        con = new Controller(getContext());
    }

    protected void addNutrients(Ingredient list, Ingredient added) {
        for (int i = 0; i < list.nutrients.length; i++) {
            list.nutrients[i] += added.nutrients[i] * added.amount / 100;
        }
    }

    protected void createNutrientTable(Context context, String[] titleRow) {
        addTitleRow(context, titleRow);
        int nutriIndex = addNutrientRows(context, NutriAttribs.general, 0);

        addSubtitleRow(context, "   Vitamins");
        nutriIndex = addNutrientRows(context, NutriAttribs.vitamins, nutriIndex);

        addSubtitleRow(context, "   Minerals");
        addNutrientRows(context, NutriAttribs.minerals, nutriIndex);
    }

    private int addNutrientRows(Context context, NutriAttribs[] attribs, int nutriIndex) {
        for (int i = 0; i < attribs.length; i++) {
            addNutrientRow(context, attribs[i], nutriIndex++);
        }
        return nutriIndex;
    }

    protected void addTitleRow(Context context, String[] titles) {
        CustomTableRow row = new CustomTableRow(context);
        row.addTitleField(context, titles[0], 15);

        for (int i = 1; i < titles.length; i++) {
            row.addTitleField(context, titles[i], 10);
        }
        addView(row);    }

    protected void addTitleRow(Context context, String[] titles, int[] weights) {
        CustomTableRow row = new CustomTableRow(context);
        row.addTitleField(context, titles[0], weights[0]);

        for (int i = 1; i < titles.length; i++) {
            row.addTitleField(context, titles[i], weights[i]);
        }
        addView(row);
    }

    protected void addSubtitleRow(Context context, String title) {
        CustomTableRow row = new CustomTableRow(context);
        row.addSubtitleField(context, title, 1);
        addView(row);
    }

    protected abstract void addNutrientRow(Context context, NutriAttribs attrb, int nutriIndex);

    protected String combineUnit(float nutrient, NutriAttribs attrb) {

        return nutriFormat.format(nutrient) + " " + attrb.unitName;
    }
}