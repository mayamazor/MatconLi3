
package com.example.matconli3;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.matconli3.model.User.UserModel;


public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    View view;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_register, container, false);
        final View registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                TextView email = view.findViewById(R.id.register_user_email);
                TextView password = view.findViewById(R.id.register_user_password);
                TextView name = view.findViewById(R.id.register_user_name);
                final NavController navController = Navigation.findNavController(view);

                if (name.getText().toString().isEmpty()) {
                    name.setError("name is required");
                    return;
                }

                if (password.getText().toString().isEmpty()) {
                    password.setError("password is Require");
                    return;
                }

                if (password.length() < 6) {
                    password.setError("Password must be more than 6 characters");
                    return;
                }

                if(!TextUtils.isEmpty(email.getText()) && Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches())
                {
                    mViewModel.register(
                            email.getText().toString(),
                            password.getText().toString(),
                            name.getText().toString(),
                            new UserModel.Listener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {
                                    if (data) {

                                        navController.navigateUp();

                                        navController.navigateUp();
                                    }
                                }
                            });
                }
                else{
                    AlertDialogFragment dialogFragment= AlertDialogFragment.newInstance("Error","check the email");
                    dialogFragment.show(getChildFragmentManager(), "TAG");
                }






            }

        });

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // TODO: Use the ViewModel
    }

}




