package com.example.matconli3;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;

import java.util.LinkedList;
import java.util.List;


public class RecipeListViewModel extends ViewModel {
    LiveData<List<Recipe>> liveData;

    public LiveData<List<Recipe>> getData() {
        if (liveData == null) {
            liveData = Model.instance.getAllRecipes();
        }
        return liveData;
    }


    public void refresh(Model.CompListener listener) {
        Model.instance.refreshAllRecipes(listener);
    }

    public LiveData<List<Recipe>> getDataByCategory(String categoryId){
        if (liveData == null)
            liveData = Model.instance.getAllRecipesPerCategory(categoryId);
        return liveData;
    }
}
