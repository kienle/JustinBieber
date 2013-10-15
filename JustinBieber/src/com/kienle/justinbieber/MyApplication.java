package com.kienle.justinbieber;

import android.app.Application;
import android.content.Context;

// Starting point of the application
public class MyApplication extends Application {

	private static MyApplication me;
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        me = this;
    }

    public Context getAppContext() {
        return context;
    }
    
    public static MyApplication getInstance() {
        return me;
   }

}
