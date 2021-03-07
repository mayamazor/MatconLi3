package com.example.matconli3;

import androidx.lifecycle.LiveData;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;

import java.util.List;

public class MapsFragmentViewModel {

    LiveData<List<Recipe>> liveData;
    Recipe recipe;

    public LiveData<List<Recipe>> getData(){
        if (liveData == null)
            liveData = Model.instance.getAllRecipes();
        return liveData;
    }

    public Recipe getRecipeById(String id){
        if (recipe == null)
            recipe = Model.instance.getRecipeById(id);
        return recipe;
    }

    public void refresh(Model.CompListener listener){
        Model.instance.refreshAllRecipes(listener);
    }

}
