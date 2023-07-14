package com.example.dietapp.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.dietapp.R;

public class AllMeals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_meals);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchView search = findViewById(R.id.searchView);

        LinearLayout mealList = findViewById(R.id.createMealList);

        final float scale = getResources().getDisplayMetrics().density;
        int seperatorHeight = (int) (1 * scale + 0.5f);
        int seperatorPadding = (int) (5 * scale + 0.5f);


        int i = 0;
        while (true) {
            MealPreview nutriRow = new MealPreview(getBaseContext());
            nutriRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent meal = new Intent(v.getContext(), Meal.class);
                    meal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(meal);
                }
            });
            mealList.addView(nutriRow);

            i++;
            if (i > 50)
                break;

            View seperator = new View(getBaseContext());
            seperator.setBackgroundColor(Color.GRAY);
            FrameLayout.LayoutParams seperatorParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, seperatorHeight);
            seperatorParams.setMargins(seperatorPadding,0,seperatorPadding,0);
            mealList.addView(seperator, seperatorParams);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}