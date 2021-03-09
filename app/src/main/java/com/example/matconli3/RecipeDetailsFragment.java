//package com.example.matconli3;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavController;
//import androidx.navigation.NavDirections;
//import androidx.navigation.Navigation;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.matconli3.model.Model;
//import com.example.matconli3.model.ModelFirebase;
//import com.example.matconli3.model.Recipe;
//import com.google.firebase.auth.FirebaseAuth;
//import com.squareup.picasso.Picasso;
//
//public class RecipeDetailsFragment extends Fragment {
//    private Recipe recipe;
//    TextView ownerId;
//    TextView title;
//    TextView location;
//    TextView description;
//    ImageView imageUrl;
//    private  RecipeDetailsViewModel viewModel;
//    ModelFirebase fire;
//    View view;
//
//    @Override
//    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//
//        if (auth.getCurrentUser() != null) {
//            view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
//            ownerId = view.findViewById(R.id.rec_details_recommendId);
//            title = view.findViewById(R.id.rec_details_title);
//            location = view.findViewById(R.id.rec_details_location);
//            description = view.findViewById(R.id.rec_details_description);
//            imageUrl = view.findViewById(R.id.rec_details_image);
//
////            recipe = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipe();
////            if (recipe != null) {
////                update_display();
////            }
//
//            View editBtn = view.findViewById(R.id.edit_btn);
//            View deleteBtn = view.findViewById(R.id.delete_btn);
//
//            if (ownerId != null && ownerId.getText().toString().equals((auth.getCurrentUser().getUid()))) {
//                editBtn.setVisibility(View.VISIBLE);
//                deleteBtn.setVisibility(View.VISIBLE);
//            }
//
//            deleteBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    fire.deleteRecipe(recipe.id);
//                    Log.d("TAG", "delete clicked");
//                    NavController navController = Navigation.findNavController(view);
//                    NavDirections updatedDirections = RecipeDetailsFragmentDirections.actionGlobalProfileFragment();
//                    navController.navigate(updatedDirections);
//
//
//                }
//            });
//
//            editBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("TAG", "edit recipe");
//                    NavController navController = Navigation.findNavController(view);
//                    NavDirections updatedDirections = RecipeDetailsFragmentDirections.actionGlobalRecipeEditFragment(recipe);
//                  navController.navigate(updatedDirections);
//
//                }
//            });
//
//
//            View closeBtn = view.findViewById(R.id.rec_details_close_btn);
//            closeBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    NavController navCtrl = Navigation.findNavController(v);
//                    navCtrl.popBackStack();
//                }
//            });
//
//            return view;
//        } else {
//            view = inflater.inflate(R.layout.fragment_login, container, false);
//            AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance("Sorry", "you must login befor");
//            dialogFragment.show(getChildFragmentManager(), "TAG");
//
//
//
//
//        }
//        return view;
//    }
//
//    private void update_display() {
//        ownerId.setText(recipe.Ownerid);
//        title.setText(recipe.title);
//        location.setText(recipe.location);
//        description.setText(recipe.description);
//        if (recipe.avatar != null && !recipe.avatar.isEmpty())
//            Picasso.get().load(recipe.avatar).placeholder(R.drawable.picture_food).into(imageUrl);
//        else
//            imageUrl.setImageResource(R.drawable.picture_food);
//
//    }
//
//    public RecipeDetailsFragment() {
//        // Required empty public constructor
//    }
//
//}