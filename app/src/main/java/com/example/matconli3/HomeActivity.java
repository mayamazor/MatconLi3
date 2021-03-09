package com.example.matconli3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class HomeActivity extends AppCompatActivity {


    ImageView logo;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_home);

        logo = findViewById(R.id.home_activity_main_logo);
        progressBar = findViewById(R.id.progressBar);


        new Thread(){
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } finally {
                    toLoginPage();
                }
            }
        }.start();


    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }

    private void toLoginPage() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}