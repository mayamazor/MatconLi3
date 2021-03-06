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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.User.User;
import com.example.matconli3.model.User.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.LinkedList;
import java.util.List;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    private ProfileViewModel viewModel;

    View view;
    RecipesListAdapter adapter;
    RecyclerView profileOutfitsList;
    List<Recipe> profileRecipesData = new LinkedList<Recipe>();

    RecipeFragment.Delegate parent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        if(auth.getCurrentUser()!=null) {
            view = inflater.inflate(R.layout.fragment_profile, container, false);

            User user = UserModel.instance.getCurrentUser();


            final TextView userName = view.findViewById(R.id.user_profile_user_name);

            if(auth.getCurrentUser()!=null) {
                String UserId = auth.getCurrentUser().getUid();
                final DocumentReference documentReference = fb.collection("users").document(UserId);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        userName.setText(documentSnapshot.getString("name"));
                    }
                });
            }

            profileOutfitsList = view.findViewById(R.id.profile_recipes_list);
            profileOutfitsList.setHasFixedSize(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            profileOutfitsList.setLayoutManager(layoutManager);

            adapter = new RecipesListAdapter();
            profileOutfitsList.setAdapter(adapter);

            adapter.setOnItemClickListener(new RecipeFragment.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    Recipe recipe = profileRecipesData.get(position);
                    parent.onItemSelected(recipe);
                }
            });

            LiveData<List<Recipe>> liveData = viewModel.getData();
            liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
                @Override
                public void onChanged(List<Recipe> recipes) {
                    profileRecipesData = recipes;
                    adapter.notifyDataSetChanged();
                }
            });

            View logoutButton = view.findViewById(R.id.user_profile_logout_button);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View buttonView) {
                    viewModel.logout();
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.recipeFragment);
                }
            });

            View mapBtn = view.findViewById(R.id.button_map);
            mapBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View buttonView) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MapsActivity.class);
                    getActivity().startActivity(intent);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeFragment.Delegate) {
            parent = (RecipeFragment.Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString()
                    + "profile recipes list parent activity must implement delegate");
        }

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }

    class RecipesListAdapter extends RecyclerView.Adapter<RecipeFragment.RecipeViewHolder> {
        private RecipeFragment.OnItemClickListener listener;

        void setOnItemClickListener(RecipeFragment.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecipeFragment.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_row, parent, false);
            RecipeFragment.RecipeViewHolder viewHolder = new RecipeFragment.RecipeViewHolder(view, listener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeFragment.RecipeViewHolder holder, int position) {
            Recipe recipe = profileRecipesData.get(position);
            holder.bind(recipe);
        }


        @Override
        public int getItemCount() {
            return profileRecipesData.size();
        }
    }


}