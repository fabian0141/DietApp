package com.example.dietapp.ui.meals;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dietapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class MealPreview extends LinearLayout {
    TextView titleTV;

    public MealPreview(Context context, String title) {
        super(context);
        initView(context, title);
    }

    public MealPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, "Name");
    }

    private void initView(Context context, String title) {
        View view = inflate(context, R.layout.meal_preview, this);
        titleTV = view.findViewById(R.id.titleMealPreview);
        titleTV.setText(title);
    }

    public void setView(String title) {
        titleTV.setText(title);
    }
}