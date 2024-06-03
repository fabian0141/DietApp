package com.example.dietapp.ui.meals;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.MealData;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.MealNutrientTable;

public class ConsumedMeal extends AppCompatActivity {

    private Controller con;
    IngredientTable ingredientTable;
    MealNutrientTable nutrientTable;

    int totalAmount = 0;
    int mealID = -1;
    int consumedID = -1;
    float portion = 0;
    TextView consumedAmount;
    MealData mealData;
    MealData dayMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumed_meal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        consumedAmount = findViewById(R.id.consumedAmount);

        LinearLayout mealList = findViewById(R.id.mealList);
        ingredientTable = new IngredientTable(getBaseContext(), null);
        mealList.addView(ingredientTable, 7);
        nutrientTable = new MealNutrientTable(getBaseContext(), true);
        mealList.addView(nutrientTable, 10);

        loadData();

        consumedAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updatePortion(Integer.parseInt(consumedAmount.getText().toString()));
                }
                return false;
            }
        });

        loadMeal();
    }

    private void loadData() {
        Intent intent = getIntent();
        mealID = intent.getIntExtra("mealID", -1);
        portion = intent.getFloatExtra("portion", 0);
        consumedID = intent.getIntExtra("consumedID", -1);

        con = Controller.getInstance(getBaseContext());
        if (mealID == -1) {
            finish();
        }
    }

    public void loadMeal() {
        mealData = con.getMeal(mealID);
        dayMeal = con.getMeal(Controller.TODAY_MEALS);

        TextView title = findViewById(R.id.mealTitle);
        title.setText(mealData.title);
        TextView description = findViewById(R.id.mealDescription);
        description.setText(mealData.description);

        totalAmount = 0;
        for (int i = 0; i < mealData.ingredients.size(); i++) {
            totalAmount += mealData.ingredients.get(i).amount;
        }

        ingredientTable.update(mealData);

        updatePortion((int)(totalAmount * portion));
    }

    private void updatePortion(int portion) {
        consumedAmount.setText("" + portion);
        nutrientTable.update(mealData, dayMeal, (float)portion / totalAmount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.consumed_meal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.removeMeal) {
            con.removeTodayIntake(consumedID);
            finish();
            return true;
        } else if (item.getItemId() == R.id.mealEdit) {
        }
        return super.onOptionsItemSelected(item);
    }
}