package com.example.matconli3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.StoreModel;
import com.example.matconli3.model.User.User;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    View view;
    ImageView profilePicImageView;
    EditText usernameInput;
    Button saveChangesBtn;
    Uri profileImageUrl;
    Bitmap postImgBitmap;
    static int REQUEST_CODE = 1;

    public EditProfileFragment()
    {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        profilePicImageView = view.findViewById(R.id.edit_fragment_add_img_icon_activity_imageView);
        usernameInput = view.findViewById(R.id.editProfile_fragment_username_edit_text);

        profilePicImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                chooseImageFromGallery();
            }
        });

        saveChangesBtn = view.findViewById(R.id.edit_profile_fragment_save_changes_btn);
        saveChangesBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                updateUserProfile();
            }
        });
        // change
       // setEditProfileHints();
        return view;
    }

    // change
//    private void setEditProfileHints()
//    {
//        if (User.getInstance().profileImageUrl != null)
//        {
//            Picasso.get().load(User.getInstance().profileImageUrl).noPlaceholder().into(profilePicImageView);
//        }
//        usernameInput.setHint(User.getInstance().name);
//    }

    void updateUserProfile()
    {
        final String username;
        if (usernameInput.getText().toString() != null && !usernameInput.getText().toString().equals(""))
            username = usernameInput.getText().toString();
        else username = User.getInstance().name;

        if (profileImageUrl != null)
        {
            StoreModel.uploadImage(postImgBitmap, new StoreModel.Listener() {
                @Override
                public void onSuccess(String url)
                {

                    Model.instance.updateUserProfile(username, url,new Model.Listener<Boolean>()
                    {
                        @Override
                        public void onComplete(Boolean data)
                        {
                            Model.instance.setUserAppData(User.getInstance().email);
                            NavController navCtrl = Navigation.findNavController(view);
                            navCtrl.navigateUp();
                            navCtrl.navigateUp();
                        }
                    });
                }

                @Override
                public void onFail()
                {
                    Snackbar.make(view, "Failed to edit profile", Snackbar.LENGTH_LONG).show();
                }
            });
        }
        else {
            Model.instance.updateUserProfile(username,null, new Model.Listener<Boolean>() {
                @Override
                public void onComplete(Boolean data)
                {
                    Model.instance.setUserAppData(User.getInstance().email);
                    NavController navCtrl = Navigation.findNavController(view);
                    navCtrl.navigateUp();
                    navCtrl.navigateUp();
                }
            });
        }

    }

    private void chooseImageFromGallery()
    {

        try{
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        }
        catch (Exception e){
            Toast.makeText(getContext(), "Edit profile Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            profileImageUrl = data.getData();
            profilePicImageView.setImageURI(profileImageUrl);
            postImgBitmap = uriToBitmap(profileImageUrl);

        }
        else {
            Toast.makeText(getContext(), "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap uriToBitmap(Uri selectedFileUri)
    {
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}