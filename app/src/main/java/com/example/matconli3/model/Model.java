package com.example.matconli3.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.example.matconli3.MyApplication;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    ModelFirebase modelFirebase=new ModelFirebase();
    ModelSql modelSql=new ModelSql();
    private Model(){
    }
    public interface Listener<T> {
        void onComplete(T result);
    }
    LiveData<List<Recipe>> recipeList;
    public LiveData<List<Recipe>> getAllRecipes(){
        if(recipeList==null){
            recipeList=modelSql.getAllRecipes();
            refreshAllRecipes(null);
        }

        return recipeList;
    }

    public interface GetAllRecipesListener{
        void onComplete();
    }


    public void refreshAllRecipes(final GetAllRecipesListener listener) {
        //1.get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);
        //2. get all updated record from firebase from the last update date
        modelFirebase.getAllRecipes(lastUpdated, new ModelFirebase.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> result) {

                //3. insert the new updates to he local db
                long lastU = 0;
                for (Recipe r : result) {
                    modelSql.addRecipe(r, null);
                    if (r.getLastUpdated() > lastU) {
                        lastU = r.getLastUpdated();
                    }
                }
                //4. update the local last update date
//               SharedPreferences.Editor editor= sp.edit();
//                editor.putLong("lastUpdated",lastU);
//                editor.commit();
                sp.edit().putLong("lastUpdated", lastU).commit();

                //5. return the updates data to the listeners
                if (listener != null) {
                    listener.onComplete();
                }
            }
        });
    }


    public interface GetRecipeListener{
        void onComplete(Recipe recipe);
    }
    public void getRecipe(String id,GetRecipeListener listener)
    {
        modelFirebase.getRecipe(id, listener);
    }

    public interface AddRecipeListener{
        void onComplete();

    }
    public void addRecipe(final Recipe recipe,final AddRecipeListener listener){

        modelFirebase.addRecipe(recipe,new AddRecipeListener() {
            @Override
            public void onComplete() {
                refreshAllRecipes(new GetAllRecipesListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
            }
        });


    }
    public interface UpdateRecipeListener extends AddRecipeListener{
    }
    public void updateRecipe(final Recipe recipe,final AddRecipeListener listener){

        modelFirebase.updateRecipe(recipe,listener);

    }


    interface DeleteListener extends AddRecipeListener{}

    public void deleteRecipe(Recipe recipe,DeleteListener listener){

        modelFirebase.delete(recipe,listener);

    }
    public interface UploadImageListener extends Listener<String>{


    }
    public  void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener){
        modelFirebase.uploadImage( imageBmp, name, listener);
    }

}
