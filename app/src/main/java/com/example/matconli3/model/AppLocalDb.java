package com.example.matconli3.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.matconli3.MainActivity;
import com.example.matconli3.MyApplication;

@Database(entities = {Recipe.class}, version = 5)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipesDao recipeDao();
}
public class AppLocalDb{

    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}