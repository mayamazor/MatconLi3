package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.squareup.picasso.Picasso;


public class RecipeEditFragment extends AddFragment {
    Recipe recipe;
    EditText textDescription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_recipe_edit, container, false);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        editbtn.setVisibility(View.VISIBLE);
        descriptionEdit.setEnabled(true);
        savebtn.setVisibility(View.VISIBLE);
        cancelbtn.setVisibility(View.VISIBLE);
        textDescription=view.findViewById(R.id.addRecipe_description_edit);
        textDescription.setVisibility(View.VISIBLE);

        final String recipeName = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeName();
        Log.d("recipeName:",recipeName);

        Model.instance.getRecipe(recipeName, new Model.GetRecipeListener() {
            @Override
            public void onComplete(Recipe re) {
                recipe = re;
                descriptionEdit.setText(recipe.getName());
                textDescription.setText(recipe.toString());
                if (re.getImageUrl() != null){
                    Picasso.get().load(re.getImageUrl()).placeholder(R.drawable.picture_food).into(avatarImageView);
                }
            }
        });
        return view;
    }
}