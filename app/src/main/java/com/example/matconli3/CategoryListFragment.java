package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matconli3.model.Category;
import com.example.matconli3.model.Model;

import java.util.List;


public class CategoryListFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        List<Category> data= Model.instance.getAllCategories();
        return view;
    }
}