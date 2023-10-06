package com.example.dietapp.ui.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;
import com.example.dietapp.databinding.FragmentDailyBinding;
import com.example.dietapp.ui.dialog.IDialogReturn;
import com.example.dietapp.ui.dialog.IngredientDialog;
import com.example.dietapp.ui.dialog.IngredientUpdateDialog;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.SimpleNutrientTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DailyFragment extends Fragment implements IDialogReturn {

    IngredientTable ingredientTable;
    CustomTable nutrientTable;
    FloatingActionButton fab;
    private FragmentDailyBinding binding;

    private Controller con;


    MealData dailyMeal;
    public DailyFragment() {

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DailyViewModel dailyViewModel =
                new ViewModelProvider(this).get(DailyViewModel.class);

        binding = FragmentDailyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        IngredientDialog dialog = new IngredientDialog(this);
        IngredientUpdateDialog updateDialog = new IngredientUpdateDialog(this, getChildFragmentManager());

        LinearLayout mealList = binding.dailyList;
        ingredientTable = new IngredientTable(getContext(), updateDialog);
        mealList.addView(ingredientTable, 2);
        nutrientTable = new SimpleNutrientTable(getContext());
        mealList.addView(nutrientTable, 6);

        Button addIngredient = binding.addDailyIngredientButton;
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getChildFragmentManager(), "dialog");
            }
        });

        fab = binding.acceptReqularConsume;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con.applyDailyMeal(dailyMeal);
                fab.setVisibility(View.GONE);
            }
        });
        fab.setVisibility(View.GONE);
        loadData();
        return root;
    }
    private void loadData() {
        con = Controller.getInstance(getContext());
        dailyMeal = con.getDailyMeal().clone();

        for (int i = 0; i < dailyMeal.ingredients.size(); i++) {
            ingredientTable.addIngredient(dailyMeal.ingredients.get(i), 1);
            nutrientTable.addIngredient(dailyMeal.ingredients.get(i), 1);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void addIngredient(Ingredient ingredient) {
        fab.setVisibility(View.VISIBLE);
        dailyMeal.ingredients.add(ingredient);
        ingredientTable.addIngredient(ingredient, 1);
        nutrientTable.addIngredient(ingredient, 1);
    }
    @Override
    public void updateIngredient(Ingredient ingredient, int deltaAmount) {
        fab.setVisibility(View.VISIBLE);
        dailyMeal.updateIngredient(ingredient);
        ingredientTable.addIngredient(ingredient, 1);
        ingredient.amount = deltaAmount;
        nutrientTable.addIngredient(ingredient, 1);

    }
}