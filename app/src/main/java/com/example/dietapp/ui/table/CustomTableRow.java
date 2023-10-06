package com.example.dietapp.ui.table;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.dietapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class CustomTableRow extends TableRow {

    private DecimalFormat nutriFormat = new DecimalFormat("#.##");

    private final int textColor = Color.rgb(200,200,200);
    private List<TextView> valueFields = new ArrayList<>();
    private List<View> colorFields = new ArrayList<>();

    public CustomTableRow(Context context) {
        super(context);
        setOrientation(TableRow.HORIZONTAL);
    }

    public void updateValues(float[] values, int[] colorValues) {

        for (int i = 0; i < values.length; i++) {
            valueFields.get(i).setText(nutriFormat.format(values[i]) + " " +  valueFields.get(i).getText().toString().split(" ")[1]);
        }
        if (colorValues != null) {
            for (int i = 0; i < colorValues.length; i++) {
                colorFields.get(i).setBackgroundColor(colorValues[i]);
            }
        }
    }

    public void addTitleField(Context context, String text, int weight) {
        addField(context, text, weight, Color.rgb(80, 90, 100), 5, 5, 0,false, 0, false);
    }

    public void addSubtitleField(Context context, String text, int weight) {
        addField(context, text, weight, 0, 5, 5, 0,false, 0, false);
    }

    public void addNormalField(Context context, String text, int weight, boolean updatable) {
        addField(context, text, weight, Color.rgb(40, 45, 50), 5, 5, 0,false, 0, updatable);
    }

    public void addClickableField(Context context, String text, int weight, boolean updatable) {
        addField(context, text, weight, Color.rgb(40, 45, 50), 5, 5, 30, false, 0, updatable);
    }

    public void addColorField(Context context, String text, int weight, int colorValue) {
        addField(context, text, weight - 1, Color.rgb(40, 45, 50), 5, 0, 0,true, colorValue, true);
    }

    private void addField(Context context, String text, int weight, int color, int marginLeft, int marginRight, int padding, boolean hasColor, int colorValue, boolean updatable) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setBackgroundColor(color);
        tv.setPadding(15,padding,15,padding);

        LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        lp.weight = weight;
        lp.setMargins(marginLeft,5,marginRight,5);
        addView(tv, lp);
        if (updatable)
            valueFields.add(tv);

        if (hasColor) {
            View colorView = new TextView(getContext());
            colorView.setBackgroundColor(colorValue);

            LayoutParams colorLP = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            colorLP.weight = 1;
            colorLP.setMargins(0,5,5,5);
            addView(colorView, colorLP);
            if (updatable)
                colorFields.add(colorView);
        }
    }
}