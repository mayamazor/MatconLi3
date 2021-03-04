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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class RecipeFragment extends Fragment {
   RecipeAdapter adapter;
    RecyclerView list;
    List<Recipe> data = new LinkedList<Recipe>();
    RecipeListViewModel viewModel;
    LiveData<List<Recipe>> liveData;

    SwipeRefreshLayout swipeRefresh;

    interface Delegate{
        void onItemSelected(Recipe recipe);
    }

    Delegate parent;
    public RecipeFragment() {

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Delegate){
            parent = (Delegate) getActivity();
        } else{
            throw new RuntimeException(context.toString() + "must implement Delegate");
        }
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        Button loginBtn = view.findViewById(R.id.login_now_button);
        FirebaseAuth fauth = FirebaseAuth.getInstance();

        list = view.findViewById(R.id.recommend_list_list);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        adapter = new RecipeAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG", "ROW WAS CLICKED"+ position);
                Recipe recipe = data.get(position);
                parent.onItemSelected(recipe);
            }
        });


        swipeRefresh = view.findViewById(R.id.students_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new Model.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        swipeRefresh.setRefreshing(true);

        //Live data is getting from localDb
        liveData = viewModel.getData();

        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                data = recipes;
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent= null;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView idTv;
        TextView titleTv;
        TextView locationTv;
        TextView descriptionTv;
        ImageView imageView;
        Recipe recipe;

        public RecipeViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            idTv = itemView.findViewById(R.id.row_id);
            titleTv = itemView.findViewById(R.id.row_title_tv);
            locationTv = itemView.findViewById(R.id.row_location_tv);
            descriptionTv = itemView.findViewById(R.id.row_descroption_tv);
            imageView = itemView.findViewById(R.id.row_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onClick(position);
                        }
                    }
                }
            });
        }

        void bind(Recipe recipe){
            idTv.setText(recipe.id);
            titleTv.setText(recipe.title);
            locationTv.setText(recipe.location);
            descriptionTv.setText(recipe.description);
            if(recipe.avatar !=null && recipe.avatar!= "" ){
                Picasso.get().load(recipe.avatar).placeholder(R.drawable.picture_food).into(imageView);
            } else{
                imageView.setImageResource(R.drawable.picture_food);
            }
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }

    class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
        private OnItemClickListener listener;

        void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        // what happen when create a view of row
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.list_row, viewGroup, false);
            RecipeViewHolder viewHolder = new RecipeViewHolder(view,listener);
            return viewHolder;
        }

        //take a row and connect her data
        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
           Recipe recipe = data.get(position);
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}