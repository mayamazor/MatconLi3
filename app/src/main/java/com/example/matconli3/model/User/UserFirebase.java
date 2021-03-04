package com.example.matconli3.model.User;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserFirebase {

    public static User getCurrentUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser == null ? null : factory(firebaseUser);
    }

    public static void register(final User user, String password, final UserModel.Listener<Boolean> listener) {

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "1");

                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success"+ auth.getCurrentUser().getDisplayName());

//                            updateUserProfile(user, listener);
                            DocumentReference documentReference = db.collection("users").document(auth.getCurrentUser().getUid());
                            Map<String,Object> userMap = new HashMap<>();
                            user.id= auth.getCurrentUser().getUid();
                            userMap.put("FullName", user.name);
                            userMap.put("Email", user.email);
                            userMap.put("id",user.id);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "Success add user to db" + user.id);
                                    listener.onComplete(true);

                                }
                            });
                        } else {
                            Log.w("TAG", "Failed to register user", task.getException());
                            if (listener != null) {
                                listener.onComplete(true);
                            }
                        }
                        listener.onComplete(true);
                    }
                });
    }

    public static void login(String email, String password, final UserModel.Listener<Boolean> listener) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (listener != null) {
                                listener.onComplete(true);
                                Log.i("TAG", "Success to login user");
                            }
                        } else {
                            Log.i("TAG", "Failed to login user", task.getException());
                            if (listener != null) {
                                listener.onComplete(false);
                            }
                        }
                    }
                });
    }

    public static void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

    private static void updateUserProfile(User user, final UserModel.Listener<Boolean> listener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(user.name).build();

        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete(task.isSuccessful());
            }
        });
    }

    private static User factory(FirebaseUser firUser) {
        return new User(
                firUser.getUid(),
                firUser.getDisplayName(),
                firUser.getEmail()
        );
    }

//    public static String getUser(String id){
//        final FirebaseFirestore db = FirebaseFirestore.getInstance();
//        return db.collection("users").document(id).getId();
//
//
//    }
}