package com.example.matconli3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.User.UserModel;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    public LiveData<List<Recipe>> getData() {
        return Model.instance.getUserRecipes(UserModel.instance.getCurrentUser());
    }

    public void logout() {
        UserModel.instance.logout();
    }
}