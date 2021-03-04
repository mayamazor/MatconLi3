package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class RecipeFragment extends Fragment {
 RecipeListViewModel viewModel;
    ProgressBar pb;
    Button addBtn;
   MyAdapter adapter;
    SwipeRefreshLayout sref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TAG","onCreateView");
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        viewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);
        ListView list=view.findViewById(R.id.recipes_list);
        pb=view.findViewById(R.id.recipe_list_progress);
        addBtn=view.findViewById(R.id.recipe_add_btn);
        pb.setVisibility(View.INVISIBLE);
//        sref = view.findViewById(R.id.recipes_list_swipe);

        sref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sref.setRefreshing(true);
                reloadData();

            }
        });

        adapter=new MyAdapter();
        list.setAdapter(adapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String name = viewModel.getList().getValue().get(i).getName();
//                RecipeFragmentDirections.ActionRecipeToRecipeDetails direc = RecipeFragmentDirections.actionRecipeToRecipeDetails(name);
//                Navigation.findNavController(view).navigate(direc);
//            }
//        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Navigation.findNavController(v).navigate(R.id.action_recipe_to_add);
            }
        });
        viewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
           adapter.notifyDataSetChanged();
            }
        });
        return view;
    }


    void reloadData(){
        pb.setVisibility(View.VISIBLE);
        addBtn.setEnabled(false);
//        Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
//            @Override
//            public void onComplete() {
//                pb.setVisibility(View.INVISIBLE);
//                addBtn.setEnabled(true);
//                sref.setRefreshing(false);
//            }
//        });
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(viewModel.getList().getValue()==null)
            {
                return 0;
            }
            return viewModel.getList().getValue().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=getLayoutInflater().inflate(R.layout.list_row,null);
            }
            TextView tv=view.findViewById(R.id.listRow_recipe1);
            ImageView imv=view.findViewById(R.id.listrow_imageview);
            Recipe re=viewModel.getList().getValue().get(i);
            tv.setText(re.getName());
            imv.setImageResource(R.drawable.picture_food);
            if(re.getImageUrl()!=null)
            {
                Picasso.get().load(re.getImageUrl()).placeholder(R.drawable.picture_food).into(imv);
            }
            return view;
        }
    }
}