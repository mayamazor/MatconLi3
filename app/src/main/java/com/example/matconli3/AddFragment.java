package com.example.matconli3;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matconli3.model.AppLocalDb;
import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.StoreModel;
import com.example.matconli3.model.User.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import java.util.UUID;
import com.example.matconli3.R;




public class AddFragment extends Fragment {
    public AddFragment() {}
    View view;
    EditText recipeTitleInput;
    EditText recipeIngredientsInput;
    EditText recipeInstructionsInput;
    EditText address;
    ImageView addImage;
    Spinner chooseCategory;
    Uri addImageUri;
    Bitmap addImageBitmap;
    String category;
    static int REQUEST_CODE = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);
        addImage = view.findViewById(R.id.newRecipe_add_img_icon_activity_imageView);
        recipeTitleInput = view.findViewById(R.id.new_recipe_fragment_title_text_view);
        recipeIngredientsInput = view.findViewById(R.id.new_recipe_fragment_Ingredients_edit_text);
        recipeInstructionsInput = view.findViewById(R.id.new_recipe_fragment_Instructions_edit_text);
        chooseCategory = (Spinner) view.findViewById(R.id.planets_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MyApplication.context,
               R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseCategory.setAdapter(adapter);

        addImage.setOnClickListener((view) -> {
            chooseImageFromGallery();
        });

        Button uploadBtn = view.findViewById(R.id.new_recipe_fragment_upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addImageUri != null && recipeTitleInput != null && recipeIngredientsInput != null && recipeInstructionsInput != null )
                    saveRecipe();
                else
                    Toast.makeText(getContext(), "Please fill all fields and add a photo", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    void saveRecipe() {
        final Recipe newRecipe = generateNewRecipe();

        StoreModel.uploadImage(addImageBitmap, new StoreModel.Listener() {
            @Override
            public void onSuccess(String url) {
                newRecipe.recipeImgUrl = url;
                Model.instance.addRecipe(newRecipe, new Model.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        NavController navCtrl = Navigation.findNavController(view);
                        navCtrl.navigateUp();
                    }
                });
            }

            @Override
            public void onFail() {
                Snackbar.make(view, "Failed to create post recipe and save it in databases", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private Recipe generateNewRecipe() {
        Recipe newRecipe = new Recipe();
        newRecipe.id = UUID.randomUUID().toString();
        newRecipe.title = recipeTitleInput.getText().toString();
        newRecipe.recIngredients = recipeIngredientsInput.getText().toString();
        newRecipe.recContent = recipeInstructionsInput.getText().toString();
        newRecipe.recipeImgUrl = null;
        newRecipe.userId = User.getInstance().id;
       // newRecipe.recipeImgUrl = User.getInstance().profileImageUrl;
        newRecipe.username = User.getInstance().name;
        newRecipe.categoryId = chooseCategory.getSelectedItem().toString();
        newRecipe.lat = Model.instance.getLocation().latitude;
        newRecipe.lon = Model.instance.getLocation().longitude;
        return newRecipe;
    }

    void chooseImageFromGallery() {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "New post recipe Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( data != null && resultCode == RESULT_OK) {
            addImageUri = data.getData();
            addImage.setImageURI(addImageUri);
            addImageBitmap = uriToBitmap(addImageUri);
        } else {
            Toast.makeText(getActivity(), "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
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