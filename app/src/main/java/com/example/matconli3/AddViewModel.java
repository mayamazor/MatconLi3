package com.example.matconli3;

import com.example.matconli3.model.User.User;
import com.example.matconli3.model.User.UserModel;

public class AddViewModel {

    public User getCurrentUser() {
        return UserModel.instance.getCurrentUser();
    }
}
