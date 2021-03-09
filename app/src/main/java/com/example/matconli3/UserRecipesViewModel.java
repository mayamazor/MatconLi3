package com.example.matconli3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;

import java.util.List;

public class UserRecipesViewModel extends ViewModel {

    LiveData<List<Recipe>> liveData;

    public LiveData<List<Recipe>> getData(){
        if (liveData == null)
            liveData = Model.instance.getAllRecipes();
        return liveData;
    }

    public LiveData<List<Recipe>> getDataByUser(String userId){
        if (liveData == null)
            liveData = Model.instance.getAllRecipesPerUser(userId);
        return liveData;
    }

    public void refresh(Model.CompListener listener){
        Model.instance.refreshAllRecipes(listener);
    }
}
