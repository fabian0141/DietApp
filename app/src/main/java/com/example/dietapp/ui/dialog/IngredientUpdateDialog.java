package com.example.dietapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.dietapp.R;
import com.example.dietapp.data.Controller;
import com.example.dietapp.data.Ingredient;
import com.example.dietapp.data.MealData;

import java.util.Arrays;

public class IngredientUpdateDialog extends DialogFragment {

    IDialogReturn caller;
    Ingredient ingredient;
    AlertDialog.Builder builder;
    FragmentManager man;
    public IngredientUpdateDialog(IDialogReturn caller, FragmentManager man) {
        this.caller = caller;
        this.man = man;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Controller con = Controller.getInstance(getContext());
        MealData meal = con.getMeal(Controller.TEMP_MEAL);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.update_ingredient, null);
        EditText amount = inflate.findViewById(R.id.updateAmount);
        amount.setText("" + ingredient.amount);
        amount.requestFocus();

        builder = new AlertDialog.Builder(getActivity(), R.style.Theme_DietApp_Dialog);
        builder.setView(inflate)
            .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    int newAmount = Integer.parseInt(amount.getText().toString());
                    if (newAmount == 0) {
                        for (int i = 0; i < meal.ingredients.size(); i++) {
                            if (meal.ingredients.get(i) == ingredient) {
                                meal.ingredients.remove(i);
                                break;
                            }
                        }
                    } else {
                        ingredient.amount = newAmount;
                    }
                    meal.calculateTotalNutrients();
                    caller.update();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < meal.ingredients.size(); i++) {
                            if (meal.ingredients.get(i) == ingredient) {
                                meal.ingredients.remove(i);
                                break;
                            }
                        }
                        meal.calculateTotalNutrients();
                        caller.update();
                    }
                });
        return builder.create();
    }

    public void update(Ingredient ingredient) {
        this.ingredient = ingredient;
        show(man, "dialog");
    }
}
