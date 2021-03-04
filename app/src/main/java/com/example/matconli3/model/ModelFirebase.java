package com.example.matconli3.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {

    final static String RECIPES_COLLECTION = "recipes";

    public static void getAllRecipesSince(long since, final Model.Listener<List<Recipe>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(since, 0);

        db.collection(RECIPES_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated", ts)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("TAG", "", e);
                            listener.onComplete(null);
                        }

                        List<Recipe> recipes = new LinkedList<>();
                        List<Recipe> recipesToDelete = new LinkedList<>();

                        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                case MODIFIED:
                                    SnapshotMetadata metadata = documentChange.getDocument().getMetadata();
                                    if (!metadata.hasPendingWrites()) {
                                        Map<String, Object> json = documentChange.getDocument().getData();
                                        Recipe recipe = factory(documentChange.getDocument().getId(), json);
                                        recipes.add(recipe);
                                    }
                                    break;
                                case REMOVED:
                                    Map<String, Object> json = documentChange.getDocument().getData();
                                    Recipe recipe = factory(documentChange.getDocument().getId(), json);
                                    recipesToDelete.add(recipe);
                                    break;
                            }
                        }

                        if (!recipesToDelete.isEmpty()) {
                            Model.instance.deleteRecipes(recipesToDelete);
                        }

                        listener.onComplete(recipes);
                    }
                });
    }




    public static void getAllRecipes(final Model.Listener<List<Recipe>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(RECIPES_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Recipe> recData = null;
                if (task.isSuccessful()){
                    recData = new LinkedList<Recipe>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Recipe recipe = doc.toObject(Recipe.class);
                        recData.add(recipe);
                    }
                }
                listener.onComplete(recData);
            }
        });
    }



    public static void addRecipe(final Recipe recipe, final Model.Listener<Boolean> listener) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(RECIPES_COLLECTION).document().set(toJson(recipe)).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void updateRecipe(Recipe recipe, final Model.CompListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> recipeJson = toJson(recipe);
        db.collection(RECIPES_COLLECTION).document(recipe.id).set(recipeJson, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) listener.onComplete();
            }
        });
    }

    public static void deleteRecipe(String recId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(RECIPES_COLLECTION).document(recId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Failed to delete recipe", task.getException());
                }
            }
        });
    }

    private static Recipe factory(String id, Map<String, Object> json) {
        String title = (String) json.get("title");
        String imageUrl = (String) json.get("imageUrl");
        String description = (String) json.get("description");
        String location = (String) json.get("location");
        String ownerId = (String) json.get("ownerId");
        String ownerName = (String) json.get("ownerName");
        Recipe recipe = new Recipe(id, ownerId, ownerName, title, location, description, imageUrl);
        Timestamp ts = (Timestamp) json.get("lastUpdated");
        if (ts != null) recipe.lastUpdated = ts.getSeconds();
        return recipe;
    }

    private static Map<String, Object> toJson(Recipe recipe) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", recipe.title);
        result.put("location", recipe.location);
        result.put("description", recipe.description);
        result.put("imageUrl", recipe.avatar);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        result.put("ownerName", recipe.OwenrName);
        result.put("ownerId", recipe.Ownerid);
        return result;
    }

}
