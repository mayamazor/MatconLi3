package com.example.matconli3.model;

import android.annotation.SuppressLint;
import android.content.Context;
import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.matconli3.MyApplication;
import com.example.matconli3.model.User.User;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    ModelFirebase modelFirebase=new ModelFirebase();

    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    public Model(){

    }

public LiveData<List<Recipe>> getAllRecipes(){
    LiveData<List<Recipe>> liveData = (LiveData<List<Recipe>>) AppLocalDb.db.recipeDao().getAllRecipes();
    refreshAllRecipes(null);
    return liveData;
}
    public LiveData<List<Recipe>> getUserRecommends(User currentUser) {
        return AppLocalDb.db.recipeDao().getUserRecipes(currentUser.id);
    }

    public void refreshAllRecipes(final CompListener listener){
        long lastUpdated = MyApplication.context.getSharedPreferences("TAG",MODE_PRIVATE).getLong("RecipesLastUpdateDate",0);

        ModelFirebase.getAllRecipesSince(lastUpdated,new Listener<List<Recipe>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Recipe> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Recipe recipe : data){
                            AppLocalDb.db.recipeDao().insertAll(recipe);
                            if (recipe.lastUpdated > lastUpdated) lastUpdated = recipe.lastUpdated;
                        }
                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG", MODE_PRIVATE).edit();
                        edit.putLong("RecipesLastUpdateDate",lastUpdated);
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    public void updateRecipe(Recipe recipe,Listener<Boolean> listener) {
        modelFirebase.updateRecipe(recipe, (CompListener) listener);


    }



    public void addRec(Recipe recipe,Listener<Boolean> listener) {
        modelFirebase.addRecipe(recipe,listener);


    }

    public void deleteRecipe(Recipe recipe) {
        modelFirebase.deleteRecipe(recipe.id);
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteRecipes(final List<Recipe> recipes) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                for (Recipe recipe : recipes) {
                    AppLocalDb.db.recipeDao().delete(recipe);
                    Log.d("TAG","deleted");
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("TAG", "deleted recipes");
            }
        }.execute("");
    }



}
