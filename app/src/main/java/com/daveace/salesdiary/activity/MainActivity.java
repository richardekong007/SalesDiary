package com.daveace.salesdiary.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.fragment.AboutFragment;
import com.daveace.salesdiary.fragment.HelpFragment;
import com.daveace.salesdiary.fragment.InventoryFragment;
import com.daveace.salesdiary.fragment.LoginFragment;
import com.daveace.salesdiary.fragment.ProductCatalogFragment;
import com.daveace.salesdiary.fragment.RecordSalesFragment;
import com.daveace.salesdiary.fragment.ReportPickerFragment;
import com.daveace.salesdiary.fragment.SignUpFragment;
import com.daveace.salesdiary.util.FragmentUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.content_layout)
    LinearLayoutCompat contentLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupNavigationHeader();
        setNavigationItemSelectedListener();
        init();
    }

    @Override
    int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationHeader() {
    }

    private void setNavigationItemSelectedListener() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_catalog:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new ProductCatalogFragment(), null, false);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.nav_inventory:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new InventoryFragment(), null, false);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.nav_record:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new RecordSalesFragment(), null, false);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.nav_Report:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new ReportPickerFragment(), null, false);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.nav_help:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new HelpFragment(), null, false);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.nav_about:
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new AboutFragment(), null, false);
                    drawerLayout.closeDrawers();
                    return false;
                case R.id.nav_exit:
                    signOut();
                    drawerLayout.closeDrawers();
                    return true;
            }
            return true;
        });
    }

    private void init() {
        FragmentUtil.replaceFragment(getSupportFragmentManager(), new SignUpFragment(), null, false);
    }

    private void signOut() {
        FirebaseAuth.getInstance()
                .signOut();
        FragmentUtil.replaceFragment(getSupportFragmentManager(), new LoginFragment(), null, false);
    }
}