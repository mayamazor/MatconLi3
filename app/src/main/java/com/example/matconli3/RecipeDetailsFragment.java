package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matconli3.model.Recipe;

public class RecipeDetailsFragment extends AddFragment {
    Recipe recipe;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= super.onCreateView(inflater,container,savedInstanceState);
        editbtn.setVisibility(View.INVISIBLE);
        descriptionEdit.setEnabled(false);
        savebtn.setVisibility(View.INVISIBLE);
        cancelbtn.setVisibility(View.INVISIBLE);

        return view;
    }

}