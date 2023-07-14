package com.example.dietapp.ui.dashboard;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.example.dietapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class MealPreview extends LinearLayout {

    public MealPreview(Context context) {
        super(context);
        initView();
    }

    public MealPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.meal_preview, this);
    }
}