package com.example.dietapp.ui.meals;

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
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.MealData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AllMeals extends AppCompatActivity {

    private Controller con;
    private LinearLayout mealList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_meals);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        con = Controller.getInstance(getBaseContext());

        SearchView search = findViewById(R.id.searchView);
        mealList = findViewById(R.id.createMealList);
        showMealPreviews();

        FloatingActionButton fab = findViewById(R.id.createMealFB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createMeal = new Intent(v.getContext(), CreateMeal.class);
                startActivity(createMeal);
            }
        });
    }

    private void showMealPreviews() {
        final MealData[] meals = con.getMealPreviews("");
        if (meals.length == 0)
            return;

        final float scale = getResources().getDisplayMetrics().density;
        int seperatorHeight = (int) (1 * scale + 0.5f);
        int seperatorPadding = (int) (5 * scale + 0.5f);
        int i = 0;
        while (true) {
            final int idx = i;
            MealPreview nutriRow = new MealPreview(getBaseContext(), meals[i].title);
            nutriRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent meal = new Intent(v.getContext(), Meal.class);
                    meal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    meal.putExtra("mealID", meals[idx].mealID);
                    v.getContext().startActivity(meal);
                }
            });
            mealList.addView(nutriRow);

            i++;
            if (i >= meals.length)
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