package com.example.matconli3;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }



}
