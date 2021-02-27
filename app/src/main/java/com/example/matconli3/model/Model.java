package com.example.matconli3.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    ModelFirebase modelFirebase=new ModelFirebase();
    ModelSql modelSql=new ModelSql();
    private Model(){
    }
//    public interface Listener<T>{
//        void onComplete(T result);
//    }
    public interface GetAllRecipesListener{
        void onComplete( List<Recipe> data);
    }
   MutableLiveData<List<Recipe>> recipeList=new MutableLiveData<List<Recipe>>();
    public MutableLiveData<List<Recipe>> getAllRecipes(){

        return recipeList;
    }

    public void refreshAllRecipes(GetAllRecipesListener listener){
        modelFirebase.getAllRecipes(new GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> result) {
                recipeList.setValue(result);
                listener.onComplete(null);
            }
        });
      //  return recipeList;
    }

    ///added
    public interface GetRecipeListener{
        void onComplete( Recipe recipe);
    }
    public void getRecipe(String id,GetRecipeListener listener)
    {
        modelFirebase.getRecipe(id, listener);
    }

    public interface AddRecipeListener{
        void onComplete();

    }
    public void addRecipe(Recipe recipe,AddRecipeListener listener){

        modelFirebase.addRecipe(recipe,listener);

    }
    public interface UpdateRecipeListener extends AddRecipeListener{
        void onComplete();

    }
    public void updateRecipe(Recipe recipe,UpdateRecipeListener listener){

        modelFirebase.updateRecipe(recipe,listener);

    }


    interface DeleteListener extends AddRecipeListener{}
    public void deleteRecipe(Recipe recipe,DeleteListener listener){

        modelFirebase.delete(recipe,listener);

    }
    public interface UploadImageListener{
        public void onComplete(String url);
    }
    public  void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener){
        modelFirebase.uploadImage( imageBmp, name, listener);
    }

}
