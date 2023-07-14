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

import com.example.dietapp.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button showAllMeals = binding.showAllMeals;
        showAllMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allMeals = new Intent(v.getContext(), AllMeals.class);
                v.getContext().startActivity(allMeals);
            }
        });

        binding.recentMeal1.setOnClickListener(new CustomListener());
        binding.recentMeal2.setOnClickListener(new CustomListener());
        binding.recentMeal3.setOnClickListener(new CustomListener());
        binding.recommendedMeal1.setOnClickListener(new CustomListener());
        binding.recommendedMeal2.setOnClickListener(new CustomListener());
        binding.recommendedMeal3.setOnClickListener(new CustomListener());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class CustomListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Intent meal = new Intent(v.getContext(), Meal.class);
        v.getContext().startActivity(meal);
    }
}