package com.example.matconli3.model;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.matconli3.MyApplication;
import com.example.matconli3.model.User.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    final static String CATEGORY_COLLECTION = "category";


    public interface Listener<T>
    {
        void onComplete();
        void onFail();
    }

    public static void loginUser(final String email, String password, final Listener<Boolean> listener)
    {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (email != null && !email.equals("") && password != null && !password.equals(""))
        {
            if (firebaseAuth.getCurrentUser() != null)
            {
                firebaseAuth.signOut();
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>()
            {
                @Override
                public void onSuccess(AuthResult authResult)
                {
                    Toast.makeText(MyApplication.context, "Welcome!", Toast.LENGTH_SHORT).show();
                    setUserAppData(email);
                    listener.onComplete();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyApplication.context, "Failed to login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            });
        }
        else {
            Toast.makeText(MyApplication.context, "Please fill all data fields", Toast.LENGTH_SHORT).show();
        }
    }

    public static void registerUserAccount(final String username, String password, final String email, final Listener<Boolean> listener)  // final Uri imageUri,
    {
        Log.d("TAG","addreg"+username);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }
        if (firebaseAuth.getCurrentUser() == null &&
                username != null && !username.equals("") &&
                password != null && !password.equals("") &&
                email != null && !email.equals("") ){

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MyApplication.context, "User registered", Toast.LENGTH_SHORT).show();
                    uploadUserData(username, email);  //, imageUri
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyApplication.context, "Failed registering user", Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            });
        }
        else {
            Toast.makeText(MyApplication.context, "Please fill all input fields ", Toast.LENGTH_SHORT).show();
            listener.onFail();
        }
    }

    public static void getAllRecipesSince(long since, final Model.Listener<List<Recipe>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(since, 0);

        db.collection(RECIPES_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated", ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Recipe> recipesData = null;
                if (task.isSuccessful()){
                    recipesData = new LinkedList<Recipe>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String,Object> json = doc.getData();
                        Recipe recipe;
                    }
                }
                listener.onComplete(recipesData);
                Log.d("TAG","refresh " + recipesData.size());
            }
        });
    }


    public static void addRecipe(final Recipe recipe, final Model.Listener<Boolean> listener) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(RECIPES_COLLECTION).document(recipe.getId()).set(toMap(recipe)).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void deleteRecipe(Recipe recipe, final Model.Listener<Boolean> listener) {
       final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(RECIPES_COLLECTION).document(recipe.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map<String,Object> deleted = new HashMap<>();
                deleted.put("recipeId", recipe.getId());
                db.collection("deleted").document(recipe.getId()).set(deleted).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
            }
        });
    }

    public static void getDeletedRecipesId(final Model.Listener<List<String>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deleted").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> deletedRecipsIds = null;
                if (task.isSuccessful()){
                    deletedRecipsIds = new LinkedList<String>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        String deleted = (String) doc.getData().get("recipeId");
                        deletedRecipsIds.add(deleted);
                    }
                }
                listener.onComplete(deletedRecipsIds);
            }
        });
    }

    public static Map<String, Object> toMap(Recipe recipe){
        HashMap<String, Object> map = new HashMap<>();
        map.put("recipeId", recipe.id);
        map.put("recipeName", recipe.title);
        map.put("categoryId", recipe.categoryId);
        map.put("recIngredients", recipe.recIngredients);
        map.put("recContent", recipe.recContent);
        map.put("recipeImgUrl", recipe.recipeImgUrl);
        map.put("userId", recipe.userId);
        map.put("username", recipe.username);
        map.put("lastUpdated", FieldValue.serverTimestamp());
        map.put("lat",recipe.lat);
        map.put("lon",recipe.lon);
        return map;
    }

    private static Recipe from_Map(Map<String,Object> json){
        Recipe newRecipe = new Recipe();
        newRecipe.id = (String) json.get("recipeId");
        newRecipe.title = (String) json.get("recipeName");
        newRecipe.categoryId = (String) json.get("categoryId");
        newRecipe.recIngredients = (String) json.get("recIngredients");
        newRecipe.recContent = (String) json.get("recContent");
        newRecipe.recipeImgUrl = (String) json.get("recipeImgUrl");
        newRecipe.userId = (String) json.get("userId");
        newRecipe.username = (String) json.get("username");
        newRecipe.lon = (double) json.get("lon");
        Timestamp ts = (Timestamp)json.get("lastUpdated");
        if (ts != null)
            newRecipe.lastUpdated = ts.getSeconds();
        return newRecipe;
    }







    public static void setUserAppData(final String email)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db.collection("userProfileData").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful()){
                    User.getInstance().name = (String) task.getResult().get("username");
                    User.getInstance().passsord = (String) task.getResult().get("password");
                    User.getInstance().address = (String) task.getResult().get("address");
                    User.getInstance().email = email;
                    User.getInstance().id = firebaseAuth.getUid();
                }
            }
        });
    }


    private static void uploadUserData(final String username, final String email)  //, Uri imageUri
    {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");
    }


    public static String getExtension(Uri uri)
    {
        try{
            ContentResolver contentResolver = MyApplication.context.getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

        } catch (Exception e) {
            Toast.makeText(MyApplication.context, "Register page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static void updateUserProfile(String username, String profileImgUrl, final Model.Listener<Boolean> listener)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> json = new HashMap<>();
        if (username != null)
            json.put("username", username);
        else json.put("username", User.getInstance().name);
        json.put("email", User.getInstance().email);

        db.collection("userProfileData").document(User.getInstance().email).set(json).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (listener != null)
                    listener.onComplete(task.isSuccessful());
            }
        });
    }

}
