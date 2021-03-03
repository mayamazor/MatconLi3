package com.example.matconli3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterFragment extends Fragment {


    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // initialising all views through id defined above
        emailTextView = view.findViewById(R.id.email);
        passwordTextView = view.findViewById(R.id.passwd);
        Btn = view.findViewById(R.id.btnregister);
        progressbar = view.findViewById(R.id.progressBar2);

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_pop);
            }
        });

        return view;
    }

    private void registerNewUser() {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MyApplication.context.getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MyApplication.context.getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }


        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MyApplication.context.getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        } else {

                            // Registration failed
                            Toast.makeText(
                                    MyApplication.context.getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}