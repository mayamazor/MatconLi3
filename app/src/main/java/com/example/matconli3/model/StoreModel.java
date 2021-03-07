package com.example.matconli3.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.matconli3.model.User.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class StoreModel {

        public static class Listener{
            public void onSuccess(String url){};
            public void onFail(){};

        }

        public static void uploadImage(Bitmap imageBitmap, final Listener listener)
        {

            Date date = new Date();
            String imageName = User.getInstance().userUsername + date.getTime();
            FirebaseStorage storageReference = FirebaseStorage.getInstance();
            final StorageReference imageRef = storageReference.getReference().child("images").child(imageName);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onFail();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            listener.onSuccess(downloadUrl.toString());
                        }
                    });
                }
            });
        }

    public static void deleteImage(String imageUrl, final Listener listener)
    {

        Date date = new Date();
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        final StorageReference imageRef = storageReference.getReferenceFromUrl(imageUrl);

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                listener.onSuccess("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.onFail();
            }
        });
    }

}
