package com.example.dietapp.ui.meals;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;
import com.example.dietapp.ui.dialog.IDialogReturn;
import com.example.dietapp.ui.dialog.IngredientDialog;
import com.example.dietapp.ui.dialog.IngredientUpdateDialog;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.MealNutrientTable;

public class CreateMeal extends AppCompatActivity implements IDialogReturn {

    CustomTable ingredientTable;
    CustomTable nutrientTable;

    private MealData mealData = null;
    private EditText title;
    private EditText description;

    private Controller con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create Meal");
        setContentView(R.layout.activity_create_meal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IngredientDialog dialog = new IngredientDialog(this);
        IngredientUpdateDialog updateDialog = new IngredientUpdateDialog(this, getSupportFragmentManager());

        LinearLayout mealList = findViewById(R.id.createMealList);
        ingredientTable = new IngredientTable(getBaseContext(), updateDialog);
        mealList.addView(ingredientTable, 4);
        nutrientTable = new MealNutrientTable(getBaseContext());
        mealList.addView(nutrientTable, 8);

        title = findViewById(R.id.editMealTitle);
        description = findViewById(R.id.editMealDesc);

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
        Intent intent = getIntent();
        int mealID = intent.getIntExtra("mealID", -1);

        con = Controller.getInstance(getBaseContext());
        if (mealID == -1) {
            mealData = MealData.newMeal();
            mealData.mealID = -1;
        } else {
            mealData = con.getMeal(mealID);
            title.setText(mealData.title);
            description.setText(mealData.description);

            for (int i = 0; i < mealData.ingredients.size(); i++) {
                ingredientTable.addIngredient(mealData.ingredients.get(i), 1);
                nutrientTable.addIngredient(mealData.ingredients.get(i), 1);
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
            mealData.title = title.getText().toString();
            mealData.description = description.getText().toString();
            con.saveMeal(mealData);
            finish();
            return true;
        } else if (item.getItemId() == R.id.deleteMeal) {
            con.deleteMeal(mealData.mealID);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        mealData.ingredients.add(ingredient);
        ingredientTable.addIngredient(ingredient, 1);
        nutrientTable.addIngredient(ingredient, 1);
    }

    @Override
    public void updateIngredient(Ingredient ingredient, int deltaAmount) {
        mealData.updateIngredient(ingredient);
        ingredientTable.addIngredient(ingredient, 1);
        ingredient.amount = deltaAmount;
        nutrientTable.addIngredient(ingredient, 1);
    }
}