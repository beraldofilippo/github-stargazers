package com.beraldo.stargazers;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class StargazersApplication extends Application {
    private static StargazersApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Stetho.initializeWithDefaults(this);
    }

    public static StargazersApplication getInstance() {
        return instance;
    }
}
