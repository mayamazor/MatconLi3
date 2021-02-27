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
    MutableLiveData<List<Recipe>> getAllRecipes();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipe);
    @Delete
    void delete(Recipe recipe);

}
