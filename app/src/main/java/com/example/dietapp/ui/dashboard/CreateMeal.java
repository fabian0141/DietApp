package com.example.dietapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.ui.dialog.IDialogReturn;
import com.example.dietapp.ui.dialog.IngredientDialog;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.MealNutrientTable;

import java.util.ArrayList;
import java.util.List;

public class CreateMeal extends AppCompatActivity implements IDialogReturn {

    CustomTable ingredientTable;
    CustomTable nutrientTable;

    private int mealID = -1;

    private Controller con;
    List<Integer> foodIDs = new ArrayList();
    List<Integer> amounts = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create Meal");
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
        loadData();
    }

    private void loadData() {
        Intent intent = getIntent(); // gets the previously created intent
        mealID = intent.getIntExtra("mealID", -1);

        con = new Controller(getBaseContext());
        if (mealID != -1) {
            Ingredient[] ings = con.getMeal(mealID);

            for (int i = 0; i < ings.length; i++) {
                foodIDs.add(ings[i].id);
                amounts.add(ings[i].amount);

                ingredientTable.addIngredient(ings[i]);
                nutrientTable.addIngredient(ings[i]);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.create_meal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.createMealApply) {
            //con.saveMeal(mealID, foodIDs.stream().mapToInt(i->i).toArray(), amounts.stream().mapToInt(i->i).toArray());
            finish();
            return true;
        } else if (item.getItemId() == R.id.createMealCancel) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        foodIDs.add(ingredient.id);
        amounts.add(ingredient.amount);
        ingredientTable.addIngredient(ingredient);
        nutrientTable.addIngredient(ingredient);
    }
}