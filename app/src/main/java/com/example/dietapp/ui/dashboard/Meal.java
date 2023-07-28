package com.example.dietapp.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.MealData;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.MealNutrientTable;

import java.util.ArrayList;
import java.util.List;

public class Meal extends AppCompatActivity {

    private Controller con;
    CustomTable ingredientTable;
    CustomTable nutrientTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout mealList = findViewById(R.id.createMealList);
        ingredientTable = new IngredientTable(getBaseContext());
        mealList.addView(ingredientTable, 7);
        nutrientTable = new MealNutrientTable(getBaseContext());
        mealList.addView(nutrientTable, 10);

        loadData();
    }

    private void loadData() {
        Intent intent = getIntent();
        int mealID = intent.getIntExtra("mealID", -1);

        con = Controller.getInstance(getBaseContext());
        if (mealID == -1) {
            finish();
        } else {
            MealData mealData = con.getMeal(mealID);
            TextView title = findViewById(R.id.mealTitle);
            title.setText(mealData.title);
            TextView description = findViewById(R.id.mealDescription);
            description.setText(mealData.description);

            for (int i = 0; i < mealData.ingredients.size(); i++) {
                ingredientTable.addIngredient(mealData.ingredients.get(i));
                nutrientTable.addIngredient(mealData.ingredients.get(i));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_meal_menu, menu);
        return super.onCreateOptionsMenu(menu);
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