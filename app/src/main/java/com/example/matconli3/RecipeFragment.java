package com.example.matconli3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 List<Recipe> recList=new LinkedList<Recipe>();
    ProgressBar pb;
    Button addBtn;
   MyAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ListView list=view.findViewById(R.id.recipes_list);
         pb=view.findViewById(R.id.recipe_list_progress);
        pb.setVisibility(View.INVISIBLE);
       adapter=new MyAdapter();
        list.setAdapter(adapter);
        addBtn=view.findViewById(R.id.recipe_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Navigation.findNavController(v).navigate(R.id.action_recipe_to_add);
            }
        });
        reloadData();
        return view;
    }


    void reloadData(){
        pb.setVisibility(View.VISIBLE);
        addBtn.setEnabled(false);
        Model.instance.getAllRecipes(new Model.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> data) {
                recList=data;
                pb.setVisibility(View.INVISIBLE);
                addBtn.setEnabled(true);
                adapter.notifyDataSetChanged();
            }

        });
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(recList==null)
            {
                return 0;
            }
            return recList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
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
            Recipe re=recList.get(i);
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