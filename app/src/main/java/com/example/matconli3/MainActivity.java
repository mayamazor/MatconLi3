package com.example.matconli3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matconli3.model.Recipe;
import com.example.matconli3.model.User.UserFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements RecipeFragment.Delegate {

    RecyclerView list;
    NavController navCtrl;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navCtrl);

        BottomNavigationView bottonNav = findViewById(R.id.home_bottom_nav);

        NavigationUI.setupWithNavController(bottonNav, navCtrl);

    }

    @Override
    public void onItemSelected(Recipe recipe) {
        NavController navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        NavGraphDirections.ActionGlobalRecipeDetailsFragment directions = RecipeFragmentDirections.actionGlobalRecipeDetailsFragment(recipe);
        navCtrl.navigate(directions);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        FirebaseAuth fauth = FirebaseAuth.getInstance();

        if (fauth.getCurrentUser() != null) {
            getMenuInflater().inflate(R.menu.logut_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.recipes_list_menu, menu);      ///// change
        }
        return true;
    }

    MainActivityViewModel mViewModel;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.login_now_button:
                Log.d("TAG", "fragment handle login menu");
                navCtrl.navigate(R.id.action_global_loginFragment);
                return true;

            case R.id.logout_btn:
                Log.d("TAG", "fragment handle logout menu");
                UserFirebase.logout();
                navCtrl.navigate(R.id.action_global_recipeFragment);
                return true;

            case R.id.Add_button_menu:
                Log.d("TAG", "fragment handle add menu");
                navCtrl.navigate(R.id.addFragment);
                return true;

        }
        if (item.getItemId() == android.R.id.home) {
            navCtrl.navigateUp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


