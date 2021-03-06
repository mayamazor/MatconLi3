package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }


    ImageView pasta;
    ImageView pizza;
    ImageView salads;
    ImageView meat;
    ImageView desserts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pasta = view.findViewById(R.id.home_fragment_pasta);
        pizza = view.findViewById(R.id.home_fragment_pizza);
        salads = view.findViewById(R.id.home_fragment_salads);
        meat = view.findViewById(R.id.home_fragment_meat);
        desserts = view.findViewById(R.id.home_fragment_dessert);

        pasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionCategoryToRecipesList action = HomeFragmentDirections.actionCategoryToRecipesList("Pasta");
                Navigation.findNavController(view).navigate(action);
            }
        });

        pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionCategoryToRecipesList action = HomeFragmentDirections.actionCategoryToRecipesList("Pizza");
                Navigation.findNavController(view).navigate(action);
            }
        });

        salads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionCategoryToRecipesList action = HomeFragmentDirections.actionCategoryToRecipesList("Salads");
                Navigation.findNavController(view).navigate(action);
            }
        });

        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionCategoryToRecipesList action = HomeFragmentDirections.actionCategoryToRecipesList("Meat");
                Navigation.findNavController(view).navigate(action);
            }
        });

        desserts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionCategoryToRecipesList action = HomeFragmentDirections.actionCategoryToRecipesList("Dessert");
                Navigation.findNavController(view).navigate(action);
            }
        });


        return view;
    }
}
