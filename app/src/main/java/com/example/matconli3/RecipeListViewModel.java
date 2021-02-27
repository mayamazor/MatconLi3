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
   private MutableLiveData<List<Recipe>> recList;

public RecipeListViewModel(){
    Log.d("TAG","RecipeListViewModel");
   recList = Model.instance.getAllRecipes();
}
    MutableLiveData<List<Recipe>> getList(){
       return recList;
   }

}
