package com.example.matconli3;

import android.content.Intent;
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

import org.w3c.dom.Text;


public class AddFragment extends Fragment {
    ImageView avateImageView;
    ImageButton editbtn;
    EditText descriptionEdit;
    Button savebtn;
    Button cancelbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add, container, false);
         avateImageView = view.findViewById(R.id.addRecipe_food_icon);
         editbtn=view.findViewById(R.id.addRecipe_edit);
        descriptionEdit = view.findViewById(R.id.addRecipe_description_edit);
        savebtn=view.findViewById(R.id.addRecipe_save);
        cancelbtn=view.findViewById(R.id.addRecipe_cancel);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        Navigation.findNavController(v).navigate(R.id.action_add_to_recipe);
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
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void editImage() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


}