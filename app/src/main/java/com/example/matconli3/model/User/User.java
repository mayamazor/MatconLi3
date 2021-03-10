package com.example.matconli3.model.User;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User  {

    private static User MyUser = null;
//    @PrimaryKey
//    @NonNull
    public String id = "";
    public String name = "";
    public String email = "";
    long lastUpdated;
    public String passsord;
    public String address;


    private User()
    {
        email = null;
        name = null;
        passsord = null;
        address = null;
        id = null;
    }


    public static User getInstance()
    {
        if (MyUser == null)
            MyUser = new User();

        return MyUser;
    }

}