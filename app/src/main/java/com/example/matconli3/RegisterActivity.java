package com.example.matconli3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matconli3.model.ModelFirebase;


public class RegisterActivity extends AppCompatActivity {

    TextView title;
    EditText userName;
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

        userName = findViewById(R.id.register_activity_username);
        password = findViewById(R.id.register_activity_password);
        email = findViewById(R.id.register_activity_email_edit_text);
        registerBtn = findViewById(R.id.register_activity_register_btn);


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
        }
        else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }
}