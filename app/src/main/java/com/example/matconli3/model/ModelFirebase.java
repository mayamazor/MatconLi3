package com.example.matconli3.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    public void getAllRecipes(Long lastUpdated, Model.GetAllRecipesListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("recipes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Recipe> data= new LinkedList<Recipe>();
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot doc:task.getResult())
                    {
                        Recipe re=new Recipe();
                        re.fromMap(doc.getData());
                       // Recipe rp=doc.toObject(Recipe.class);
                        data.add(re);
                    }
                }
                listener.onComplete(data);
            }
        });
        List<Recipe> data=new LinkedList<Recipe>();
        listener.onComplete(data);
    }

    public void addRecipe(Recipe recipe, Model.AddRecipeListener listener) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Add a new document with a generated ID
        db.collection("recipes").document(recipe.getName())
                .set(recipe.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
             Log.d("TAG", "recipe added successfully");
             listener.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "fail adding recipe");
                listener.onComplete();
            }
        });


    }

    public void updateRecipe(Recipe recipe, Model.UpdateRecipeListener listener) {
        addRecipe(recipe,listener);
    }

    public void getRecipe(String id, final Model.GetRecipeListener listener) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("recipes").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Recipe recipe=null;
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc=task.getResult();
                   if(doc!=null)
                   {
                       recipe=task.getResult().toObject(Recipe.class);
                   }
                }
               listener.onComplete(recipe);
            }
        });
    }

    public void delete(Recipe recipe,Model.DeleteListener listener) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("recipes").document(recipe.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                listener.onComplete();
//            }
//        });
    }

    public  void uploadImage(Bitmap imageBmp, String name, Model.UploadImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }
}
