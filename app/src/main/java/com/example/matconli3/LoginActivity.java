package com.example.matconli3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.matconli3.model.ModelFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    EditText emailInput;
    EditText passwordInput;
    Button loginBtn;
    Button registerBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = firebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            ModelFirebase.setUserAppData(firebaseAuth.getCurrentUser().getEmail());
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        this.setTitle("Login");

        emailInput = findViewById(R.id.login_activity_email);
        passwordInput = findViewById(R.id.login_activity_password);

        registerBtn = findViewById(R.id.login_activity_register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                toRegisterPage();
            }
        });

        loginBtn = findViewById(R.id.login_activity_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ModelFirebase.loginUser(emailInput.getText().toString(), passwordInput.getText().toString(), new ModelFirebase.Listener<Boolean>() {
                    @Override
                    public void onComplete()
                    {

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFail()
                    {

                    }
                });
            }
        });


    }

    private void toRegisterPage()
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}