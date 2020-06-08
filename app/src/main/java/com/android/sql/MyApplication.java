package com.android.sql;

import android.app.Application;
import android.graphics.Typeface;

public class MyApplication extends Application {
    private static MyApplication instance;
    private  Typeface typeface;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = (MyApplication) getApplicationContext();
        typeface = Typeface.createFromAsset(instance.getAssets(), "fonts/led.ttf");//下载的字体
    }

    public static  MyApplication getInstace() {
        return instance;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

}
