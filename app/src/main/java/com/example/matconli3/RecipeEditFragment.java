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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.StoreModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import static android.app.Activity.RESULT_OK;


import java.io.FileDescriptor;
import java.io.IOException;


public class RecipeEditFragment extends Fragment {
    public RecipeEditFragment() {}

    View view;
    Recipe recipe;
    EditText titleInput;
    EditText ingredientsInput;
    EditText instructionsInput;
    Button saveChangesBtn;
    ImageView recipeImageView;
    Spinner chooseCategory;
    Uri recipeImageUri;
    Bitmap recipeImgBitmap;
    static int REQUEST_CODE = 1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_recipe_edit, container, false);
        titleInput = view.findViewById(R.id.edit_recipe_fragment_name);
        ingredientsInput = view.findViewById(R.id.edit_recipe_fragment_Ingredients_edit_text);
        instructionsInput = view.findViewById(R.id.edit_recipe_fragment_make_it);
        recipeImageView = view.findViewById(R.id.edit_recipe_fragment_edit_img);
        chooseCategory = (Spinner) view.findViewById(R.id.edit_recipe_fragment_category_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MyApplication.context,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseCategory.setAdapter(adapter);

        recipe = RecipeFragmentArgs.fromBundle(getArguments()).getRecipe();

        if (recipe != null)
        {
            setEditRecipeHints();
        }

        recipeImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseImageFromGallery();
            }
        });

        saveChangesBtn = view.findViewById(R.id.edit_recipe_fragment_save);
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRecipe();
            }
        });

        return view;

    }

    void updateRecipe()
    {

        if (recipeImageUri != null)
        {
            StoreModel.uploadImage(recipeImgBitmap, new StoreModel.Listener() {
                @Override
                public void onSuccess(String url) {

                    Model.instance.addRecipe(generatedEditedRecipe(url), new Model.Listener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data)
                        {
                            NavController navCtrl = Navigation.findNavController(view);
                            navCtrl.navigateUp();
                            navCtrl.navigateUp();
                        }
                    });
                }

                @Override
                public void onFail()
                {
                    Snackbar.make(view, "Failed to edit post", Snackbar.LENGTH_LONG).show();
                }
            });
        }
        else {
            Model.instance.addRecipe(generatedEditedRecipe(null), new Model.Listener<Boolean>() {
                @Override
                public void onComplete(Boolean data)
                {
                    NavController navCtrl = Navigation.findNavController(view);
                    navCtrl.navigateUp();
                    navCtrl.navigateUp();
                }
            });
        }

    }

    private Recipe generatedEditedRecipe(String imageUrl)
    {

        Recipe editedRecipe = recipe;
        if (titleInput.getText().toString() != null && !titleInput.getText().toString().equals(""))
            editedRecipe.title = titleInput.getText().toString();
        else editedRecipe.title = recipe.title;
        if (ingredientsInput.getText().toString() != null && !ingredientsInput.getText().toString().equals(""))
            editedRecipe.recIngredients = ingredientsInput.getText().toString();
        else editedRecipe.recIngredients = recipe.recIngredients;
        if (instructionsInput.getText().toString() != null && !instructionsInput.getText().toString().equals(""))
            editedRecipe.recContent = instructionsInput.getText().toString();
        else editedRecipe.recContent = recipe.recContent;
        if (chooseCategory.getSelectedItem().toString() != null && !chooseCategory.getSelectedItem().toString().equals(""))
            editedRecipe.categoryId = chooseCategory.getSelectedItem().toString();
        else editedRecipe.categoryId = recipe.categoryId;

        if (imageUrl != null)
            editedRecipe.recipeImgUrl = imageUrl;

        return editedRecipe;
    }

    private void setEditRecipeHints()
    {
        if (recipe.recipeImgUrl != null)
        {
            Picasso.get().load(recipe.recipeImgUrl).noPlaceholder().into(recipeImageView);
        }
        titleInput.setText(recipe.title);
        instructionsInput.setText(recipe.recContent);
        ingredientsInput.setText(recipe.recIngredients);
    }

    private void chooseImageFromGallery()
    {

        try
        {
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Edit post Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == RESULT_OK){
            recipeImageUri = data.getData();
            recipeImageView.setImageURI(recipeImageUri);
            recipeImgBitmap = uriToBitmap(recipeImageUri);

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