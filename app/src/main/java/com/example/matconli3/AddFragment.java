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
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.StoreModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class AddFragment extends Fragment {
    public AddFragment() {}

    AddViewModel mViewModel;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            view =  inflater.inflate(R.layout.fragment_add, container, false);



            imageView= view.findViewById(R.id.edit_rec_image);
            Button takePhotoBtn = view.findViewById(R.id.edit_rec_takePhoto_btn);
            takePhotoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });
            titleTV = view.findViewById(R.id.edit_rec_title);
            locationTv = view.findViewById(R.id.edit_rec_location);
            descriptionTV= view.findViewById(R.id.edit_rec_description);


            Button saveBtn = view.findViewById(R.id.edit_rec_save_btn);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveRecipe();
                }
            });
            return view;
        }
        else{
            view =  inflater.inflate(R.layout.fragment_login, container, false);
            AlertDialogFragment dialogFragment= AlertDialogFragment.newInstance("Sorry","you must login before");
            dialogFragment.show(getChildFragmentManager(), "TAG");
        }

        return view;
    }
    void saveRecipe(){
        final String id ;
        String OwnerId;
        String OwnerName = "";
        final String title= titleTV.getText().toString();
        final String location= locationTv.getText().toString();
        final String description= descriptionTV.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore fb = FirebaseFirestore.getInstance();

        final String userId = auth.getCurrentUser().getUid();

        Date date = new Date();
        if (imageBitmap != null) {

            StoreModel.uploadImage(imageBitmap, "OR_photo" + date.getTime(), new StoreModel.Listener() {
                @Override
                public void onSuccess(final String url) {
                    Log.d("TAG", "url: " + url);
                    Recipe recipe = new Recipe(userId, "", title, location, description, url);
                   Model.instance.addRec(recipe, new Model.Listener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data) {
                            NavController navCtrl = Navigation.findNavController(view);
                            navCtrl.navigateUp();
                        }
                    });
                }

                @Override
                public void onFail() {

//                    Snackbar mySnackbar = Snackbar.make(view, R.string.fail_to_save_recommend, Snackbar.LENGTH_LONG);
//                    mySnackbar.show();
                }
            });
        }
        else {
            AlertDialogFragment dialogFragment= AlertDialogFragment.newInstance("Sorry","you must load photo");
            dialogFragment.show(getChildFragmentManager(), "TAG");
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
//            case R.id.login_now_button:
//                Log.d("TAG","fragment handle login menu");
//                navController.navigate(R.id.action_global_loginFragment);
//                return true;
//
//            case R.id.logout_btn:
//                Log.d("TAG","fragment handle logout menu");
//                navController.navigate(R.id.action_global_recListFragment);
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
