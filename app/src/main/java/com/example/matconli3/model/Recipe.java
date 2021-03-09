package com.example.matconli3.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Recipe implements Serializable {
    @PrimaryKey
    @NonNull
    public String id ;
    public String title;
    public long lastUpdated;
    public String categoryId;
    public String recIngredients;
    public String recContent;
    public String recipeImgUrl;
    public String userId;
    public String username;

    @NonNull
    public double lat;

    @NonNull
    public double lon;


    public Recipe() {
        id = "";
        title = "";
        categoryId = "";
        recIngredients = "";
        recContent = "";
        recipeImgUrl = "";
        userId = "";
        username = "";
        lastUpdated = 0;

    }

    public Recipe(String id, String title, String categoryId, String recIngredients, String recContent, String recipeImgUrl, String userId, String username) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.recIngredients = recIngredients;
        this.recContent = recContent;
        this.recipeImgUrl = recipeImgUrl;
        this.userId = userId;
        this.username = username;
    }



    /////set

    public void setId(@NonNull String id) {
        this.id = id;
    }

//    public void setOwnerid(String name) {
//        this.Ownerid = Ownerid;
//    }
//
//    public void setOwenrName(String OwenrName) {
//        this.OwenrName = OwenrName;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//    public void setLocation(String location) {
//        this.location = location;
//    }
//    public void setDescription(String description) {
//        this.description = description;
//    }
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }

///// get

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRecIngredients() {
        return recIngredients;
    }

    public void setRecIngredients(String recIngredients) {
        this.recIngredients = recIngredients;
    }

    public String getRecContent() {
        return recContent;
    }

    public void setRecContent(String recContent) {
        this.recContent = recContent;
    }

    public String getRecipeImgUrl() {
        return recipeImgUrl;
    }

    public void setRecipeImgUrl(String recipeImgUrl) {
        this.recipeImgUrl = recipeImgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
//    public String getOwnerid() {
//        return Ownerid;
//    }
//
//    public String getOwenrName() {
//        return OwenrName;
//    }
//    public String getTitle() {
//        return title;
//    }
//    public String getLocation() {
//        return location;
//    }
//    public String getDescription() {
//        return description;
//    }
//    public String getAvatar() {
//        return avatar;
//    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }



}
