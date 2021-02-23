package com.example.matconli3.model;

import android.os.AsyncTask;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    ModelFirebase modelFirebase=new ModelFirebase();
    ModelSql modelSql=new ModelSql();
    private Model(){
    }
    public interface GetAllRecipesListener{
        void onComplete( List<Recipe> data);
    }
    public void getAllRecipes(GetAllRecipesListener listener){
        modelFirebase.getAllRecipes(listener);

    }

    public interface AddRecipeListener{
        void onComplete();

    }
    public void addRecipe(Recipe recipe,AddRecipeListener listener){

        modelFirebase.addRecipe(recipe,listener);

    }

}
