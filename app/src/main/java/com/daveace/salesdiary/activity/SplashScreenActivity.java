package com.daveace.salesdiary.activity;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.daveace.salesdiary.R;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.splashImage)
    AppCompatImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        loadSplashScreen();
    }

    private void loadSplashScreen() {
        final int LOAD_TIME = 3000;
        new Handler().postDelayed(() -> {
                    Intent signUpIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(signUpIntent);
                    finish();
                }
                , LOAD_TIME);
    }
}
