package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.matconli3.model.Category;
import com.example.matconli3.model.Model;

import java.util.List;


public class HomeFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
       Button regbtn=view.findViewById(R.id.btn_register);
       regbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Navigation.findNavController(v).navigate(R.id.action_home_to_register);

           }
       });
        return view;
    }
}