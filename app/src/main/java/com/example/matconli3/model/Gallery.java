package com.example.matconli3.model;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.matconli3.MyApplication;

public class Gallery {
    static int REQUEST_CODE = 1;



    public static void chooseImageFromGallery(Activity sender)
    {

        try
        {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            sender.startActivityForResult(openGalleryIntent, REQUEST_CODE);
        }
        catch (Exception e)
        {
            Toast.makeText(MyApplication.context,  e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
