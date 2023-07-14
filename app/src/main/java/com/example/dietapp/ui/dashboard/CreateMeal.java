package com.example.dietapp.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.R;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.ui.dialog.IDialogReturn;
import com.example.dietapp.ui.dialog.IngredientDialog;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.MealNutrientTable;

public class CreateMeal extends AppCompatActivity implements IDialogReturn {

    CustomTable ingredientTable;
    CustomTable nutrientTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IngredientDialog dialog = new IngredientDialog(this);

        LinearLayout mealList = findViewById(R.id.createMealList);
        ingredientTable = new IngredientTable(getBaseContext());
        mealList.addView(ingredientTable, 4);
        nutrientTable = new MealNutrientTable(getBaseContext());
        mealList.addView(nutrientTable, 8);

        Button addIngredient = findViewById(R.id.addIngredientButton);
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addIngredient(Ingredient ingredient, int amount) {
        Log.i("Ingredients", ingredient.id + " " + amount);
        ingredientTable.addIngredient(ingredient, amount);
        nutrientTable.addIngredient(ingredient, amount);
    }
}