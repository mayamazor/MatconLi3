package com.example.matconli3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matconli3.model.Gallery;
import com.example.matconli3.model.ModelFirebase;


public class RegisterActivity extends AppCompatActivity {

    TextView title;
    EditText userName;
    //ImageView profileImageView;
    EditText email;
    EditText password;
    Button registerBtn;
    Button loginBtn;
    Uri profileImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.register_activity_username_edit_text);
        password = findViewById(R.id.register_activity_pass_edit_text);
        email = findViewById(R.id.register_activity_email_edit_text);
       // profileImageView = findViewById(R.id.register_add_img_icon_activity_imageView);
        registerBtn = findViewById(R.id.register_activity_register_btn);

//        profileImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Gallery.chooseImageFromGallery(RegisterActivity.this);
//            }
//        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ModelFirebase.registerUserAccount(userName.getText().toString(), password.getText().toString(), email.getText().toString(), new ModelFirebase.Listener<Boolean>() {   // profileImageUri,
                    @Override
                    public void onComplete()
                    {
                        RegisterActivity.this.finish();
                    }

                    @Override
                    public void onFail()
                    {

                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == RESULT_OK){
            profileImageUri = data.getData();
            //profileImageView.setImageURI(profileImageUri);
        }
        else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }
}