package com.example.matconli3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matconli3.model.User.User;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

//    private ProfileViewModel viewModel;

    TextView userUsername;
    TextView userEmail;
    ImageView userProfileImage;
    Button editProfileBtn;
    Button myRecipesBook;

    Button logoutBtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        userUsername = view.findViewById(R.id.profile_fragment_username);
        userEmail = view.findViewById(R.id.profile_fragment_email);
        //userProfileImage = view.findViewById(R.id.profile_fragment_profile_image_view);

        editProfileBtn = view.findViewById(R.id.profile_fragment_edit);
        editProfileBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toEditProfilePage();
            }
        });

        myRecipesBook = view.findViewById(R.id.profile_fragment_my_recipes);
        myRecipesBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ProfileFragmentDirections.ActionProfileToUserRecipes action = ProfileFragmentDirections.actionProfileToUserRecipes(User.getInstance().id);
                Navigation.findNavController(view).navigate(action);
            }
        });
       //   myRecipesBook.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profile_to_userRecipes));

        logoutBtn= view.findViewById(R.id.profile_fragment_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { toLoginPage();}

        });


        setUserProfile();
        return view;
    }


    private void toLoginPage()
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this.getActivity(), LoginActivity.class));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void toEditProfilePage()
    {
        NavController navCtrl = Navigation.findNavController(getActivity(), R.id.main_nav_host);
        NavDirections directions = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment();
        navCtrl.navigate(directions);
    }

    public void setUserProfile()
    {
        userUsername.setText(User.getInstance().name);
        userEmail.setText(User.getInstance().email);

//        if (User.getInstance().profileImageUrl != null)
//        {
//            Picasso.get().load(User.getInstance().profileImageUrl).noPlaceholder().into(userProfileImage);
//        }
    }



}