package com.example.matconli3.model;

import android.os.AsyncTask;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    private Model(){

    }
public interface GetAllRecipesListener{
        void onComplete( List<Recipe> data);
}
   public void getAllRecipes(GetAllRecipesListener listener){
        class MyAsyncTask extends AsyncTask {
            List<Recipe> data;
            @Override
            protected Object doInBackground(Object[] objects) {
                data=AppLocalDb.db.recipeDao().getAllRecipes();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {        //the main thread
                super.onPostExecute(o);
                listener.onComplete(data);
            }
        }
        MyAsyncTask task=new MyAsyncTask();
        task.execute();
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
