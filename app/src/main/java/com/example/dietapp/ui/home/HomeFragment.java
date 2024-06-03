package com.example.dietapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dietapp.data.Controller;
import com.example.dietapp.databinding.FragmentHomeBinding;
import com.example.dietapp.ui.table.CustomTable;
import com.example.dietapp.ui.table.ExtendedNutrientTable;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ExtendedNutrientTable nutriTable;

    private Controller con;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout list = binding.homeList;
        nutriTable = new ExtendedNutrientTable(getContext());
        list.addView(nutriTable);

        con = Controller.getInstance(getContext());
        return root;
    }

    private void loadTable() {

    }

    public void onStart() {
        super.onStart();

        nutriTable.update(con.getMeal(Controller.TODAY_MEALS), con.getMeal(Controller.WEEK_MEALS));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}