package com.daveace.salesdiary;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class SalesDiaryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
