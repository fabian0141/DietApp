package com.example.dietapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.widget.SearchView;

import com.example.dietapp.data.Controller;
import com.example.dietapp.data.MealData;
import com.example.dietapp.data.SettingsNames;
import com.example.dietapp.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private Controller con;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        con = Controller.getInstance(getContext());

        Button showAllMeals = binding.showAllMeals;
        showAllMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allMeals = new Intent(v.getContext(), AllMeals.class);
                v.getContext().startActivity(allMeals);
            }
        });
        ArrayList<MealData> meals = con.getRecentMealPreview();
        loadRecentMeal(meals, new MealPreview[] {binding.recentMeal1, binding.recentMeal2, binding.recentMeal3});
        //binding.recommendedMeal1.setOnClickListener(new CustomListener());
        //binding.recommendedMeal2.setOnClickListener(new CustomListener());
        //binding.recommendedMeal3.setOnClickListener(new CustomListener());
        return root;
    }

    private void loadRecentMeal(ArrayList<MealData> meals, MealPreview[] prevs) {
        for (int i = 0; i < prevs.length; i++) {
            if (meals.size() > i) {
                prevs[i].setView(meals.get(i).title);
                prevs[i].setOnClickListener(new CustomListener(meals.get(i).mealID));
            } else {
                prevs[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class CustomListener implements View.OnClickListener {

    private int id;
    public CustomListener(int id) {
        this.id = id;
    }
    @Override
    public void onClick(View v) {
        Intent meal = new Intent(v.getContext(), Meal.class);
        meal.putExtra("mealID", id);
        v.getContext().startActivity(meal);
    }
}