package com.alisher.android.stayalive.parse.com;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Samsung on 11/16/2015.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "YQc1U6dBf8v2iHg3IFNfGd9UovrjCHCApdfMOvRn", "dy7VNLhPHiKNNGmne23I9BV4OHqEIeKorfveZ1gH");
    }
}
