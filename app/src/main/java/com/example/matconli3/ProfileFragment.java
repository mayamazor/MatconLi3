package com.example.matconli3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;



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

        userUsername = view.findViewById(R.id.profile_fragment_username_text_view);
        userEmail = view.findViewById(R.id.profile_fragment_email_text_view);
        //userProfileImage = view.findViewById(R.id.profile_fragment_profile_image_view);

        editProfileBtn = view.findViewById(R.id.profile_fragment_edit_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toEditProfilePage();
            }
        });

        myRecipesBook = view.findViewById(R.id.profile_fragment_my_recipes_book_btn);
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

        logoutBtn= view.findViewById(R.id.profile_fragment_logout_btn);
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