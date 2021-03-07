package com.example.matconli3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.matconli3.model.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        View loginButton = view.findViewById(R.id.login_now_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                TextView email = view.findViewById(R.id.login_user_email);
                TextView password = view.findViewById(R.id.login_user_password);

                if(email.getText().toString().isEmpty()){
                    email.setError("Email is Require");
                    return;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError("password is Require");
                    return;
                }
                if(password.length() < 6)
                {
                    password.setError("Password must be more than 6 characters");
                }
                mViewModel.login(email.getText().toString(), password.getText().toString(), new UserModel.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        if (data) {
                            view.findViewById(R.id.login_error_msg).setVisibility(View.INVISIBLE);
                            NavController navController = Navigation.findNavController(view);
//                             // added
//
//                            Navigation.findNavController(buttonView).navigate(R.id.action_login_to_category);
//
//                            //added
                            navController.navigateUp();


                        } else {
                            view.findViewById(R.id.login_error_msg).setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
        // added
        Button login=view.findViewById(R.id.login_now_button);
        login.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_login_to_category));
        // added

        View navigateToRegistrationButton = view.findViewById(R.id.login_navigate_to_registration_button);
        navigateToRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                NavDirections directions = LoginFragmentDirections.actionGlobalRegisterFragment();
                navController.navigate(directions);

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }
}