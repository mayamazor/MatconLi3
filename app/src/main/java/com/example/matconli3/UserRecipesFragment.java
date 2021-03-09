package com.example.matconli3;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class UserRecipesFragment extends Fragment {

    String userId;
    RecyclerView list;
    List<Recipe> data = new LinkedList<>();
    UserRecipesFragment.UserRecipesAdapter adapter;
    UserRecipesViewModel viewModel;
    LiveData<List<Recipe>> liveData;



    public UserRecipesFragment(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel = new ViewModelProvider(this).get( UserRecipesViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_recipes, container, false);

        userId =  UserRecipesFragmentArgs.fromBundle(getArguments()).getUserId();


        list= view.findViewById(R.id.my_recipes_list);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new  UserRecipesAdapter();

        list.setAdapter(adapter);

        adapter.setOnClickListener(new  UserRecipesFragment.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Recipe recipe = data.get(position);

                UserRecipesFragmentDirections.ActionUserRecipesToRecipeFragment action =  UserRecipesFragmentDirections.actionUserRecipesToRecipeFragment(recipe);
                Navigation.findNavController(view).navigate(action);
            }
        });

        //live data
        liveData = viewModel.getDataByUser(userId);
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {

                List<Recipe> reversedData = reverseData(recipes);
                data = reversedData;
                adapter.notifyDataSetChanged();
            }
        });


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.my_recipes_list_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new Model.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    private List<Recipe> reverseData(List<Recipe> recipes) {
        List<Recipe> reversedData = new LinkedList<>();
        for (Recipe recipe: recipes) {
            reversedData.add(0, recipe);
        }
        return reversedData;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    static class  UserRecipesViewHolder extends RecyclerView.ViewHolder {

        TextView recipeTitle;
        ImageView recipeImg;
        TextView username;
        ProgressBar progressBar;
        Recipe recipe;

        public  UserRecipesViewHolder(@NonNull View itemView, final  UserRecipesFragment.OnItemClickListener listener) {
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.my_recipes_list_row_title_text_view);
            recipeImg = itemView.findViewById(R.id.my_recipes_list_row_image_view);
            username = itemView.findViewById(R.id.my_recipes_list_row_username_text_view);
            progressBar = itemView.findViewById(R.id.my_recipes_list_row_progress_bar);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onClick(position);
                    }
                }
            });
        }

        public void bind(Recipe recipeToBind){
            recipeTitle.setText(recipeToBind.title);
            username.setText(recipeToBind.username);
            recipe = recipeToBind;
            if (recipeToBind.recipeImgUrl !=null)
            {
                Picasso.get().load(recipeToBind.recipeImgUrl).placeholder(R.drawable.pic2).into(recipeImg);
            }else {
                recipeImg.setImageResource(R.drawable.pic2);
            }

        }
    }

    interface OnItemClickListener {
        void onClick(int position);
    }


    class UserRecipesAdapter extends RecyclerView.Adapter< UserRecipesViewHolder> {

        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener){ this.listener=listener; }

        @NonNull
        @Override
        public UserRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //create row
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_user_recipes_row,parent,false);
            UserRecipesFragment.UserRecipesViewHolder userrecipeViewHolder = new UserRecipesFragment.UserRecipesViewHolder(view,listener);
            return userrecipeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull  UserRecipesViewHolder holder, int position) {
            Recipe recipe = data.get(position);
            holder.bind(recipe);
        }


        @Override
        public int getItemCount() {return data.size();}
    }
}