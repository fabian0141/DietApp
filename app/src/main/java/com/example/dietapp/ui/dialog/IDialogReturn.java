package com.example.dietapp.ui.dialog;

import com.example.dietapp.data.Ingredient;

public interface IDialogReturn {
    void addIngredient(Ingredient ingredient);
    void updateIngredient(Ingredient ingredient, int deltaAmount);

}
