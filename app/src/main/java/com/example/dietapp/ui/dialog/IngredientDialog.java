package com.example.dietapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;
import com.example.dietapp.ui.meals.Meal;

import java.lang.reflect.Array;
import java.util.Arrays;

public class IngredientDialog extends DialogFragment {

    IDialogReturn caller;
    Ingredient[] ingredients;

    public IngredientDialog(IDialogReturn caller) {
        this.caller = caller;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        Controller con = Controller.getInstance(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.add_ingredient, null);

        RadioGroup ingredientList = inflate.findViewById(R.id.searchedIngredients);
        SearchView search = inflate.findViewById(R.id.searchIngredient);
        search.onActionViewExpanded();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() <= 1) {
                    return false;
                }
                Log.i("Ingredients", newText);
                ingredients = con.searchIngredients(newText, 10);
                Log.i("Ingredients", Arrays.toString(ingredients));
                ingredientList.removeAllViews();
                for (Ingredient ing : ingredients) {
                    RadioButton view = new RadioButton(new ContextThemeWrapper(getContext(), com.google.android.material.R.style.Base_Theme_Material3_Dark));
                    view.setText(ing.name);
                    ingredientList.addView(view);
                }

                return true;
            }
        });

        EditText amount = inflate.findViewById(R.id.ingredientAmount);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_DietApp_Dialog);
        builder.setView(inflate)
            .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    int selectedRadioButtonId = ingredientList.getCheckedRadioButtonId();
                    if (selectedRadioButtonId != -1) {
                        MealData meal = con.getMeal(Controller.TEMP_MEAL);

                        Ingredient ing = ingredients[ingredientList.indexOfChild(inflate.findViewById(selectedRadioButtonId))];
                        ing.amount = Integer.parseInt(amount.getText().toString());
                        meal.ingredients.add(ing);
                        meal.calculateTotalNutrients();
                        caller.update();
                    }
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
        return builder.create();
    }
}
