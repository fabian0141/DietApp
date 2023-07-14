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
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.databinding.FragmentNotificationsBinding;
import com.example.dietapp.ui.dialog.IDialogReturn;
import com.example.dietapp.ui.dialog.IngredientDialog;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.IngredientTable;
import com.example.dietapp.ui.table.SimpleNutrientTable;

public class NotificationsFragment extends Fragment implements IDialogReturn {

    CustomTable ingredientTable;
    CustomTable nutrientTable;
    private FragmentNotificationsBinding binding;

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void addIngredient(Ingredient ingredient, int amount) {
        Log.i("Ingredients", ingredient.id + " " + amount);
        ingredientTable.addIngredient(ingredient, amount);
        nutrientTable.addIngredient(ingredient, amount);
    }}