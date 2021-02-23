package com.example.matconli3.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    public void getAllRecipes(Model.GetAllRecipesListener listener) {
        List<Recipe> data=new LinkedList<Recipe>();
        listener.onComplete(data);
    }

    public void addRecipe(Recipe recipe, Model.AddRecipeListener listener) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();



// Add a new document with a generated ID
        db.collection("test").document(recipe.getId())
                .set(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
             Log.d("TAG", "recipe added succussfully");
            }
        });


    }
}
