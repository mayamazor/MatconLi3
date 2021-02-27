package com.example.matconli3.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.matconli3.MyApplication;

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
   MutableLiveData<List<Recipe>> recipeList;
    public MutableLiveData<List<Recipe>> getAllRecipes(){
        if(recipeList==null){
            recipeList=modelSql.getAllRecipes();
        }

        return recipeList;
    }

    public void refreshAllRecipes(GetAllRecipesListener listener){
        //1.get local last update date
      SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
      Long lastUpdated=sp.getLong("lastUpdated",0);
        //2. get all updated record from firebase from the last update date
        modelFirebase.getAllRecipes(lastUpdated,new GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> result) {

                //3. insert the new updates to he local db
                long lastU=0;
                for (Recipe r:result) {
                    modelSql.addRecipe(r,null);
                    if(r.getLastUpdated()>lastU){
                        lastU=r.getLastUpdated();
                    }
                }


                //4. update the local last update date
               SharedPreferences.Editor editor= sp.edit();
                editor.putLong("lastUpdated",lastU);
                editor.commit();


                //5. return the updates data to the listeners





            }
        });



//        modelFirebase.getAllRecipes(new GetAllRecipesListener() {
//            @Override
//            public void onComplete(List<Recipe> result) {
//                recipeList.setValue(result);
//                listener.onComplete(null);
//            }
//        });
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
