package com.example.matconli3;

import android.os.Bundle;

import androidx.core.widget.ListViewAutoScrollHelper;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.matconli3.model.Model;
import com.example.matconli3.model.Recipe;

import java.util.LinkedList;
import java.util.List;


public class RecipeFragment extends Fragment {
 List<Recipe> recList=new LinkedList<Recipe>();
    ProgressBar pb;
    Button addBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ListView list=view.findViewById(R.id.recipes_list);
         pb=view.findViewById(R.id.recipe_list_progress);
         addBtn=view.findViewById(R.id.recipe_add_btn);
       // pb.setVisibility(View.INVISIBLE);
        MyAdapter adapter=new MyAdapter();
        list.setAdapter(adapter);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRecipe();
            }
        });
        return view;
    }
static int id=0;
    private void addNewRecipe() {
        Recipe re=new Recipe();
        re.setId(""+id);
        re.setName("name "+id);
        Model.instance.addRecipe(re,null);
    }

    void reloadData(){
       // pb.setVisibility(View.VISIBLE);
        //addBtn.setEnabled(false);
        Model.instance.getAllRecipes(new Model.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> data) {
                recList=data;
                pb.setVisibility(View.INVISIBLE);
                addBtn.setEnabled(true);
            }

        });
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
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
            TextView tv=view.findViewById(R.id.listRow_recipe1); //// to change
            Recipe re=recList.get(i);
            tv.setText(re.getName());
            return view;
        }
    }
}