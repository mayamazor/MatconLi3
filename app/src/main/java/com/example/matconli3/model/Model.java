package com.example.matconli3.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.matconli3.MyApplication;
import com.example.matconli3.model.User.User;
import com.google.android.gms.maps.model.LatLng;

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

    public Recipe getRecipeById(String recipeId) {
        Recipe recipe = AppLocalDb.db.recipeDao().GetRecipeById(recipeId);
        refreshAllRecipes(null);
        return recipe;
    }
//    public LiveData<List<Recipe>> getUserRecipes(User currentUser) {
//        return AppLocalDb.db.recipeDao().getUserRecipes(currentUser.id);
//    }

    public LiveData<List<Recipe>> getAllRecipesPerUser(String userId) {
        LiveData<List<Recipe>> liveData = AppLocalDb.db.recipeDao().getUserRecipes(userId);
        refreshAllRecipes(null);
        return liveData;
    }

    public void refreshAllRecipes(final CompListener listener){
        long lastUpdated = MyApplication.context.getSharedPreferences("TAG",Context.MODE_PRIVATE).getLong("RecipesLastUpdateDate",0);

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
                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG",Context.MODE_PRIVATE).edit();
                        edit.putLong("RecipesLastUpdateDate",lastUpdated);
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        cleanLocalDb();
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void cleanLocalDb() {
        ModelFirebase.getDeletedRecipesId(new Listener<List<String>>() {
            @Override
            public void onComplete(final List<String> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (String id : data) {
                            Log.d("TAG", "deleted id: " + id);
                            AppLocalDb.db.recipeDao().deleteByRecipeId(id);
                        }
                        return "";
                    }
                }.execute("");
            }
        });
    }
//    public void updateRecipe(Recipe recipe,Listener<Boolean> listener) {
//        modelFirebase.updateRecipe(recipe, (CompListener) listener);
//
//
//    }
    public void updateUserProfile(String username, String profileImgUrl, Listener<Boolean> listener) {
        modelFirebase.updateUserProfile(username, profileImgUrl, listener);
    }

    @SuppressLint("StaticFieldLeak")
    public void addRecipe(final Recipe recipe,Listener<Boolean> listener) {
        modelFirebase.addRecipe(recipe,listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.recipeDao().insertAll(recipe);
                return "";
            }
        }.execute();

    }


    @SuppressLint("StaticFieldLeak")
    public void deleteRecipe(final Recipe recipe, Listener<Boolean> listener) {
        modelFirebase.deleteRecipe(recipe, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.recipeDao().deleteRecipe(recipe);
                return "";
            }
        }.execute();
    }

//    @SuppressLint("StaticFieldLeak")
//    public void deleteRecipes(final List<Recipe> recipes) {
//        new AsyncTask<String, String, String>() {
//            @Override
//            protected String doInBackground(String... strings) {
//                for (Recipe recipe : recipes) {
//                    AppLocalDb.db.recipeDao().delete(recipe);
//                    Log.d("TAG","deleted");
//                }
//                return "";
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                Log.d("TAG", "deleted recipes");
//            }
//        }.execute("");
//    }

    public void setUserAppData(String email) {
        ModelFirebase.setUserAppData(email);
    }

    public LatLng getLocation() {
        if (ActivityCompat.checkSelfPermission(MyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return new LatLng(0, 0);
        }


        LocationManager locationManager = (LocationManager) MyApplication.context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location location = locationManager.getLastKnownLocation(provider);
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public LiveData<List<Recipe>> getAllRecipesPerCategory(String categoryId) {
        LiveData<List<Recipe>> liveData = AppLocalDb.db.recipeDao().getAllRecipesPerCategory(categoryId);
        refreshAllRecipes(null);
        return liveData;
    }


}
