package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeDetailsFragment extends AddFragment {
    //    Recipe recipe;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
//        editbtn.setVisibility(View.INVISIBLE);
//        descriptionEdit.setEnabled(false);
//        savebtn.setVisibility(View.INVISIBLE);
//        cancelbtn.setVisibility(View.INVISIBLE);
//
//        final String recipeName = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeName();
//        Log.d("recipeName:",recipeName);
//
//        Model.instance.getRecipe(recipeName, new Model.GetRecipeListener() {
//            @Override
//            public void onComplete(Recipe re) {
//                recipe = re;
//                descriptionEdit.setText(recipe.getName());
//                if (re.getImageUrl() != null){
//                    Picasso.get().load(re.getImageUrl()).placeholder(R.drawable.picture_food).into(avatarImageView);
//                }
//            }
//        });

        return view;
    }

}
