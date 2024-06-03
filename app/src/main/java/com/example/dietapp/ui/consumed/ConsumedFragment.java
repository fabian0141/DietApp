package com.example.dietapp.ui.consumed;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietapp.data.Controller;
import com.example.dietapp.data.MealData;
import com.example.dietapp.databinding.FragmentConsumedBinding;
import com.example.dietapp.ui.meals.ConsumedMeal;
import com.example.dietapp.ui.meals.Meal;
import com.example.dietapp.ui.meals.MealPreview;

import java.util.ArrayList;

public class ConsumedFragment extends Fragment {

    private FragmentConsumedBinding binding;
    private Controller con;
    private LinearLayout mealList;

    private String searchQuery = "";

    private int queriedMealsAmount = 0;
    private int offsetMeals = 0;

    public ConsumedFragment() {
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConsumedViewModel consumedViewModel =
                new ViewModelProvider(this).get(ConsumedViewModel.class);

        binding = FragmentConsumedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        con = Controller.getInstance(getContext());

        mealList = binding.todaysMeals;
        return root;
    }

    private void showMealPreviews() {
        mealList.removeAllViews();
        ArrayList<MealData> meals = con.getConsumedMeals();
        if (meals.size() == 0)
            return;

        queriedMealsAmount = meals.size();

        final float scale = getResources().getDisplayMetrics().density;
        int seperatorHeight = (int) (1 * scale + 0.5f);
        int seperatorPadding = (int) (5 * scale + 0.5f);
        int i = 0;
        while (true) {
            final int mealID = meals.get(i).mealID;
            final float portion = meals.get(i).portion;
            final int consumedID = meals.get(i).consumedID;

            ConsumedPreview nutriRow = new ConsumedPreview(getContext(), meals.get(i).title, meals.get(i).portion);
            nutriRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent meal = new Intent(v.getContext(), ConsumedMeal.class);
                    meal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    meal.putExtra("mealID", mealID);
                    meal.putExtra("portion", portion);
                    meal.putExtra("consumedID", consumedID);
                    v.getContext().startActivity(meal);
                }
            });
            mealList.addView(nutriRow);

            i++;
            if (i >= meals.size())
                break;

            View seperator = new View(getContext());
            seperator.setBackgroundColor(Color.GRAY);
            FrameLayout.LayoutParams seperatorParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, seperatorHeight);
            seperatorParams.setMargins(seperatorPadding,0,seperatorPadding,0);
            mealList.addView(seperator, seperatorParams);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showMealPreviews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}