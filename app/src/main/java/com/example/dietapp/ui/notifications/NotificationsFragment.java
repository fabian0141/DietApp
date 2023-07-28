package com.example.dietapp.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;
import com.example.dietapp.databinding.FragmentNotificationsBinding;
import com.example.dietapp.ui.dialog.IDialogReturn;
import com.example.dietapp.ui.dialog.IngredientDialog;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.SimpleNutrientTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment implements IDialogReturn {

    CustomTable ingredientTable;
    CustomTable nutrientTable;
    FloatingActionButton fab;
    private FragmentNotificationsBinding binding;

    private Controller con;


    MealData dailyMeal;
    public NotificationsFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        IngredientDialog dialog = new IngredientDialog(this);

        LinearLayout mealList = binding.dailyList;
        ingredientTable = new IngredientTable(getContext());
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
            ingredientTable.addIngredient(dailyMeal.ingredients.get(i));
            nutrientTable.addIngredient(dailyMeal.ingredients.get(i));
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
        ingredientTable.addIngredient(ingredient);
        nutrientTable.addIngredient(ingredient);
    }}