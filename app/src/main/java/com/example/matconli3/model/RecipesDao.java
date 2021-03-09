package com.example.matconli3.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipesDao {
    @Query("select * from Recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("select * from Recipe where categoryId = :categoryId")
    LiveData<List<Recipe>> getAllRecipesPerCategory(String categoryId);

    @Query("select * from Recipe where userId = :userId")
    LiveData<List<Recipe>> getUserRecipes(String userId);

    @Query("select * from Recipe where id = :recipeId")
    Recipe GetRecipeById(String recipeId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipe);
    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("select exists(select * from Recipe where id = :recipeId)")
    boolean isRecipeExists(String recipeId);

    @Query("delete from Recipe where id = :recipeId")
    void deleteByRecipeId(String recipeId);



}
