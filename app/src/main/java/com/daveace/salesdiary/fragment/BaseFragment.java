package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.activity.BaseActivity;
import com.daveace.salesdiary.activity.MainActivity;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.daveace.salesdiary.util.FragmentUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedHashMap;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;
    private FireStoreHelper fireStoreHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createWithLoadingIndicator(getLayout(), container);
        fireStoreHelper = FireStoreHelper.getInstance();
        showOrHideActionBar(this);
        reConfigureDrawerLayoutSwipe(this.getActivity(), this);
        reconfigureActionbar(this.getActivity(), this);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull((getActivity()))
                .setTitle(getTitle());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroy();
    }

    private View createWithLoadingIndicator(int resId, ViewGroup parent) {
        swipeRefreshLayout = new SwipeRefreshLayout(Objects.requireNonNull(getContext()));
        swipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        swipeRefreshLayout.setEnabled(false);
        View view = LayoutInflater.from(getContext()).inflate(resId, parent, false);
        swipeRefreshLayout.addView(view);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary, null));
        return swipeRefreshLayout;
    }

    public void setLoading(boolean loading) {
        if (loading)
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(loading);
    }


    protected void replaceFragment(BaseFragment fragment, boolean addToBackStack, Bundle bundle) {
        FragmentUtil.replaceFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragment, bundle, addToBackStack);
    }

    void removeFragment(BaseFragment fragment) {
        FragmentUtil.takeOffBackStack(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragment);
    }

    private void hideActionBar() {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects
                    .requireNonNull(getActivity()))
                    .getSupportActionBar())
                    .hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void showActionBar() {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar())
                    .show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showOrHideActionBar(Fragment fragment) {
        if (fragment instanceof SignUpFragment ||
                fragment instanceof LoginFragment) {
            hideActionBar();
        } else {
            showActionBar();
        }
    }

    private void reConfigureDrawerLayoutSwipe(FragmentActivity activity, Fragment fragment) {
        if (activity instanceof BaseActivity) {
            if (fragment instanceof SignUpFragment
                    || fragment instanceof LoginFragment) {
                ((BaseActivity) activity).getDrawerLayout()
                        .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                ((BaseActivity) activity).getDrawerLayout()
                        .setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }
    }

    private void reconfigureActionbar(FragmentActivity activity, Fragment fragment) {
        if (activity instanceof BaseActivity) {
            ActionBar actionBar = ((BaseActivity) activity).getSupportActionBar();
            if (fragment instanceof BackIconActionBarMarker) {
                Objects.requireNonNull(actionBar).setHomeAsUpIndicator(R.drawable.ic_back_white);
            } else {
                Objects.requireNonNull(actionBar).setHomeAsUpIndicator(R.drawable.ic_menu);
            }
        }
    }

    FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    String getUserId() {
        return Objects.requireNonNull(getFirebaseAuth()
                .getCurrentUser()).getUid();
    }

    FireStoreHelper getFireStoreHelper() {
        return fireStoreHelper;
    }

    LinkedHashMap<String, String> getImageStrings() {
        LinkedHashMap<String, String> imageStrings = new LinkedHashMap<>();
        if (getActivity() != null) {
            imageStrings = ((MainActivity) getActivity()).getImageStrings();
        }
        return imageStrings;
    }

    void clearImageStrings(){
        if (getActivity() != null){
            getImageStrings().clear();
        }
    }

    public abstract int getLayout();

    public abstract CharSequence getTitle();
}