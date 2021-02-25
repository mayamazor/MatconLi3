package com.example.matconli3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;

import org.w3c.dom.Text;


public class AddFragment extends Fragment {
    ImageView avatarImageView;
    ImageButton editbtn;
    EditText descriptionEdit;
    Button savebtn;
    Button cancelbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add, container, false);
         avatarImageView = view.findViewById(R.id.addRecipe_food_icon);
         editbtn=view.findViewById(R.id.addRecipe_edit);
        descriptionEdit = view.findViewById(R.id.addRecipe_description_edit);
        savebtn=view.findViewById(R.id.addRecipe_save);
        cancelbtn=view.findViewById(R.id.addRecipe_cancel);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe();
            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
        return view;
    }

    private void saveRecipe() {
      Recipe recipe= new Recipe();
      recipe.setName(descriptionEdit.getText().toString());
        BitmapDrawable drawable= (BitmapDrawable)avatarImageView.getDrawable();
        Bitmap bitmap= drawable.getBitmap();

        Model.instance.uploadImage(bitmap,recipe.getName(), new Model.UploadImageListener() {
            @Override
            public void onComplete(String url) {
               if(url==null){

                }
               else{
                    recipe.setImageUrl(url);
                    Model.instance.addRecipe(recipe, new Model.AddRecipeListener(){
                        @Override
                        public void onComplete() {
                            Navigation.findNavController(savebtn).popBackStack();
                        }
                    });
               }
            }

        });
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void editImage() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            avatarImageView.setImageBitmap(imageBitmap);
        }
    }

}