package com.daveace.salesdiary;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.livefront.bridge.Bridge;
import com.livefront.bridge.SavedStateHandler;

import icepick.Icepick;

public class SalesDiaryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
