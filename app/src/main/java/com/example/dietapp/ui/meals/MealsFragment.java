package com.example.dietapp.ui.meals;

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
import com.example.dietapp.databinding.FragmentMealsBinding;

import java.util.ArrayList;

public class MealsFragment extends Fragment {

    private FragmentMealsBinding binding;
    private Controller con;
    private LinearLayout mealList;

    private String searchQuery = "";

    private int queriedMealsAmount = 0;
    private int offsetMeals = 0;

    public MealsFragment() {
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MealsViewModel dailyViewModel =
                new ViewModelProvider(this).get(MealsViewModel.class);

        binding = FragmentMealsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        con = Controller.getInstance(getContext());

        addSearch();
        mealList = binding.mealList;
        return root;
    }

    private void showMealPreviews() {
        mealList.removeAllViews();
        ArrayList<MealData> meals = con.getMeals(searchQuery, offsetMeals);
        if (meals.size() == 0)
            return;

        queriedMealsAmount = meals.size();

        final float scale = getResources().getDisplayMetrics().density;
        int seperatorHeight = (int) (1 * scale + 0.5f);
        int seperatorPadding = (int) (5 * scale + 0.5f);
        int i = 0;
        while (true) {
            final int mealID = meals.get(i).mealID;
            MealPreview nutriRow = new MealPreview(getContext(), meals.get(i).title);
            nutriRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent meal = new Intent(v.getContext(), Meal.class);
                    meal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    meal.putExtra("mealID", mealID);
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

        Button moreMeals = binding.showMoreMealsB;
        if (queriedMealsAmount % 20 == 0) {
            moreMeals.setVisibility(View.VISIBLE);
            moreMeals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offsetMeals = queriedMealsAmount;
                    showMealPreviews();
                }
            });
        } else {
            moreMeals.setVisibility(View.GONE);
        }
    }

    private void addSearch() {
        SearchView search = binding.searchView;
        search.setQuery(searchQuery, false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                offsetMeals = 0;
                showMealPreviews();
                return true;
            }
        });
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