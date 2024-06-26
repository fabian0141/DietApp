package com.example.dietapp.ui.meals;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
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
    IngredientTable ingredientTable;
    MealNutrientTable nutrientTable;

    int totalAmount = 0;
    int mealID = -1;
    TextView totalAmountTV;
    EditText consumedAmount;
    MealData mealData;
    MealData dayMeal;
    ActivityResultLauncher<Intent> launcher;
    public Meal() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                switch (result.getResultCode()) {
                    case RESULT_OK:
                        mealID = result.getData().getIntExtra("newMealID", -1);
                        loadMeal();
                        break;
                    case RESULT_CANCELED:
                        loadMeal();
                        break;
                    case -2:
                        finish();
                        break;
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalAmountTV = findViewById(R.id.totalAmount);
        consumedAmount = findViewById(R.id.consumedAmount);

        LinearLayout mealList = findViewById(R.id.mealList);
        ingredientTable = new IngredientTable(getBaseContext(), null);
        mealList.addView(ingredientTable, 7);
        nutrientTable = new MealNutrientTable(getBaseContext(), false);
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

        totalAmountTV.setText("" + totalAmount);

        ingredientTable.update(mealData);

        updatePortion(totalAmount / 2);
    }

    private void updatePortion(int portion) {
        consumedAmount.setText("" + portion);
        nutrientTable.update(mealData, dayMeal, (float)portion / totalAmount);
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
            con.addTodayIntake(mealData, Float.parseFloat(consumedAmount.getText().toString()) / totalAmount);
            finish();
            return true;
        } else if (item.getItemId() == R.id.mealEdit) {
            Intent editMeal = new Intent(this, CreateMeal.class);
            editMeal.putExtra("mealID", mealData.mealID);
            launcher.launch(editMeal);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}