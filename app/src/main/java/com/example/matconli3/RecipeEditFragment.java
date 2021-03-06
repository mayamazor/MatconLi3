package com.example.matconli3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.ModelFirebase;
import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.StoreModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import static android.app.Activity.RESULT_OK;


import java.util.Date;


public class RecipeEditFragment extends Fragment {
    public RecipeEditFragment() {}

    View view;
    ImageView imageView;
    Bitmap imageBitmap;
    TextView idTv;
    TextView OwnerIdTv;
    TextView OwnerNameTv;
    TextView titleTV;
    TextView locationTv;
    TextView descriptionTV;
    private Recipe recipe;
    ModelFirebase fire;
    Boolean take=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_recipe_edit, container, false);
        recipe = RecipeEditFragmentArgs.fromBundle(getArguments()).getRecipe();


        Button takePhotoBtn = view.findViewById(R.id.edit_rec_takePhoto_btn);
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                take=true;


            }
        });
        titleTV = view.findViewById(R.id.edit_rec_title);
        locationTv = view.findViewById(R.id.edit_rec_location);
        descriptionTV= view.findViewById(R.id.edit_rec_description);
        imageView= view.findViewById(R.id.edit_rec_image);

        titleTV.setText(recipe.title);
        locationTv.setText(recipe.location);
        descriptionTV.setText(recipe.description);
        Picasso.get().load(recipe.avatar).placeholder(R.drawable.picture_food).into(imageView);

        Button saveBtn = view.findViewById(R.id.edit_rec_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe();
            }
        });
        return view;


    }
    void saveRecipe(){

        final String title= titleTV.getText().toString();
        final String location= locationTv.getText().toString();
        final String description= descriptionTV.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String userId = auth.getCurrentUser().getUid();

        Date date = new Date();

        if(take) {
            StoreModel.uploadImage(imageBitmap, "Recipe_photo" + date.getTime(), new StoreModel.Listener() {
                @Override
                public void onSuccess(final String url) {
                    Log.d("TAG", "url: " + url);
                    final Recipe newRecipe = new Recipe(userId, "", title, location, description, url);

                    if (!recipe.id.isEmpty()) {
                        newRecipe.setId(recipe.id);
                    }
                    fire.updateRecipe(newRecipe, new Model.CompListener() {
                        @Override
                        public void onComplete() {
                            NavController navController = Navigation.findNavController(view);

                            if (!recipe.id.isEmpty())
                                navController.navigate(R.id.profileFragment);
                            else
                                navController.navigateUp();
                        }
                    });
                }

                @Override
                public void onFail() {

                    Snackbar mySnackbar = Snackbar.make(view, R.string.fail_to_save_recipe, Snackbar.LENGTH_LONG);
                    mySnackbar.show();
                }
            });
        }
        else{
            final Recipe newRecipe = new Recipe(userId, "", title,location, description,recipe.avatar);

            if (!recipe.id.isEmpty()) {
                newRecipe.setId(recipe.id);
            }

            fire.updateRecipe(newRecipe, new Model.CompListener() {
                @Override
                public void onComplete() {
                    NavController navController = Navigation.findNavController(view);

                    if (!recipe.id.isEmpty())
                        navController.navigate(R.id.profileFragment);
                    else
                        navController.navigateUp();
                }

            });
        }

    }


    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = 0;

    void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = rotateImage((Bitmap) extras.get("data"));
            imageView.setImageBitmap(imageBitmap);
        }
    }
    public static Bitmap rotateImage(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(view);

        switch (item.getItemId()){
            case R.id.login_now_button:
                Log.d("TAG","fragment handle login menu");
                navController.navigate(R.id.action_global_loginFragment);
                return true;

            case R.id.logout_btn:
                Log.d("TAG","fragment handle logout menu");
                navController.navigate(R.id.action_global_recipeFragment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}