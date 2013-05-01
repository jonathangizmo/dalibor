package com.nhce.project.dalibor.androidclient;

import android.app.Application;
import android.content.Context;

public class Dalibor extends Application{
	
	public static final String PREFS_NAME = "Dalibor";
    private static Context context;

    public void onCreate(){
        super.onCreate();
        Dalibor.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Dalibor.context;
    }
}