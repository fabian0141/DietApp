package com.example.dietapp.ui.meals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.MealData;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.MealNutrientTable;

public class Meal extends AppCompatActivity {

    private Controller con;
    CustomTable ingredientTable;
    CustomTable nutrientTable;

    int totalAmount = 0;

    TextView totalAmountTV;
    EditText consumedAmount;
    MealData mealData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalAmountTV = findViewById(R.id.totalAmount);
        consumedAmount = findViewById(R.id.consumedAmount);

        LinearLayout mealList = findViewById(R.id.createMealList);
        ingredientTable = new IngredientTable(getBaseContext(), null);
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
            mealData = con.getMeal(mealID);
            TextView title = findViewById(R.id.mealTitle);
            title.setText(mealData.title);
            TextView description = findViewById(R.id.mealDescription);
            description.setText(mealData.description);

            for (int i = 0; i < mealData.ingredients.size(); i++) {
                totalAmount += mealData.ingredients.get(i).amount;
                ingredientTable.addIngredient(mealData.ingredients.get(i), 0.5f);
                nutrientTable.addIngredient(mealData.ingredients.get(i), 0.5f);
            }
        }

        totalAmountTV.setText("" + totalAmount);
        consumedAmount.setText("" + totalAmount / 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.addMeal) {
            con.addTodayIntake(mealData.ingredients, Float.parseFloat(consumedAmount.getText().toString()) / totalAmount);
            con.addRecentMeal(mealData.mealID);
            finish();
            return true;
        } else if (item.getItemId() == R.id.mealEdit) {
            Intent editMeal = new Intent(this, CreateMeal.class);
            editMeal.putExtra("mealID", mealData.mealID);
            startActivity(editMeal);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}