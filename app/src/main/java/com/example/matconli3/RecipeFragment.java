package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.StoreModel;
import com.example.matconli3.model.User.User;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;


public class RecipeFragment extends Fragment {

    Recipe recipe;
    View view;
    Button edit;
    Button delete;
    TextView categoryTitle;
    TextView recipeName;
    TextView ingredientsTitle;
    TextView ingredientsList;
    TextView instructionTitle;
    TextView instructionList;
    ImageButton recImg;


    public RecipeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe, container, false);


        categoryTitle = view.findViewById(R.id.Category_title);
        recipeName = view.findViewById(R.id.Rec_Name_txt);
        ingredientsTitle = view.findViewById(R.id.Ingredients_title);
        ingredientsList = view.findViewById(R.id.ingredients_list_txt);
        instructionTitle = view.findViewById(R.id.Instructions_title);
        instructionList = view.findViewById(R.id.instructions_list_txt);
        recImg = view.findViewById(R.id.Rec_img_btn);
        instructionList.setMovementMethod(new ScrollingMovementMethod());
        ingredientsList.setMovementMethod(new ScrollingMovementMethod());


        recipe = RecipeFragmentArgs.fromBundle(getArguments()).getRecipe();
        if (recipe !=null){

            categoryTitle.setText(recipe.categoryId);
            recipeName.setText(recipe.title);
            ingredientsList.setText(recipe.recIngredients);
            instructionList.setText(recipe.recContent);
            if (recipe.recipeImgUrl != null)
            {
                Picasso.get().load(recipe.recipeImgUrl).placeholder(R.drawable.pic2).into(recImg);
            }else {
                recImg.setImageResource(R.drawable.ic_launcher_background);
            }

        }
        edit = view.findViewById(R.id.edit_rec_btn);
        edit.setVisibility(view.INVISIBLE);
        delete = view.findViewById(R.id.delete_rec_btn);
        delete.setVisibility(view.INVISIBLE);

        if (recipe.userId.equals(User.getInstance().id))
        {
            edit.setVisibility(view.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toEditRecipePage(recipe);
                }
            });

            delete.setVisibility(view.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRecipe(recipe);
                }
            });
        }


        return view;
    }



    private void toEditRecipePage(Recipe recipe) {

        NavController navController = Navigation.findNavController(getActivity(),R.id.main_nav_host);
        RecipeFragmentDirections.ActionRecipeToRecipeEdit directions = RecipeFragmentDirections.actionRecipeToRecipeEdit(recipe);
        navController.navigate(directions);

    }

    private void deleteRecipe(Recipe recipeToDelete) {

        Model.instance.deleteRecipe(recipeToDelete, new Model.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                StoreModel.deleteImage(recipe.recipeImgUrl, new StoreModel.Listener() {
                    @Override
                    public void onSuccess(String url) {
                        NavController navController = Navigation.findNavController(view);
                        navController.navigateUp();
                    }

                    @Override
                    public void onFail() {

                        Snackbar.make(view,"Failed to delete recipe in database",Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}