package com.example.matconli3;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.User.User;
import com.example.matconli3.model.User.UserModel;

public class RecipeDetailsViewModel {
    public User getCurrentUser() {
        return UserModel.instance.getCurrentUser();
    }

    public void deleteRecipe(Recipe recipe) {
       Model.instance.deleteRecipe(recipe);
    }
}
