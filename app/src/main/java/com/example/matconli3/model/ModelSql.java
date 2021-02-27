package com.example.matconli3.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ModelSql {
    public interface GetAllRecipesListener{
        void onComplete( List<Recipe> data);
    }

    public MutableLiveData<List<Recipe>> getAllRecipes() {
        return AppLocalDb.db.recipeDao().getAllRecipes();
    }

    public interface AddRecipeListener{
        void onComplete();
    }
    public void addRecipe(Recipe recipe,AddRecipeListener listener){
        class MyAsyncTask extends AsyncTask {

            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.recipeDao().insertAll(recipe);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener !=null)
                {
                    listener.onComplete();
                }
            }
        }
        MyAsyncTask task=new MyAsyncTask();
        task.execute();

    }
}
