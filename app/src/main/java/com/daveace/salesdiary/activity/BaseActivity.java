package com.daveace.salesdiary.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daveace.salesdiary.R;

import java.util.LinkedHashMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private DrawerLayout drawerLayout;
    private final LinkedHashMap<String, String> base64ImageStrings = new LinkedHashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        unbinder = ButterKnife.bind(this);
        setDrawerLayout(drawerLayout);
        setSupportActionBar(getToolbar());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        setStatusBarColor(R.color.colorPrimary);

    }

    @Override
    protected void onDestroy() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources()
                    .getColor(color, null));
        }
    }

    private void setDrawerLayout(DrawerLayout drawerLayout) {
        if (drawerLayout != null){
            this.drawerLayout = drawerLayout;
        }
    }

    protected LinkedHashMap<String,String> getBase64ImageStrings(){
        return base64ImageStrings;
    }

    abstract int getLayout();

    abstract Toolbar getToolbar();

    public abstract DrawerLayout getDrawerLayout();

}
