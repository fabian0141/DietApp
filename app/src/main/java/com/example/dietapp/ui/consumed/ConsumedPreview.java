package com.example.dietapp.ui.consumed;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dietapp.R;

/**
 * TODO: document your custom view class.
 */
public class ConsumedPreview extends LinearLayout {

    TextView titleTV;

    public ConsumedPreview(Context context, String title, float portion) {
        super(context);
        initView(context, title, portion);
    }

    public ConsumedPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, "Name", 0.0f);
    }

    private void initView(Context context, String title, float portion) {
        View view = inflate(context, R.layout.consumed_preview, this);
        titleTV = view.findViewById(R.id.consumedTitle);
        titleTV.setText(title);
        titleTV = view.findViewById(R.id.consumedPortion);
        titleTV.setText("Portion: " + portion);
    }

    public void setView(String title, float portion) {
        titleTV.setText(title);
    }
}