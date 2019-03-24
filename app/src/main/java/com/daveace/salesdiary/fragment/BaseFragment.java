package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.activity.BaseActivity;
import com.daveace.salesdiary.util.FragmentUtil;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createWithLoadingIndicator(getLayout(), container);
        showOrHideActionBar(this);
        reConfigureDrawerLayoutSwipe(this.getActivity(), this);
        unbinder = ButterKnife.bind(this, view);
        retainInstance();
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
        swipeRefreshLayout = new SwipeRefreshLayout(getContext());
        swipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        swipeRefreshLayout.setEnabled(false);
        View view = LayoutInflater.from(getContext()).inflate(resId, parent, false);
        swipeRefreshLayout.addView(view);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary, null));
        return swipeRefreshLayout;
    }

    protected void setLoading(boolean loading) {
        if (loading)
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(loading);
    }

    protected boolean isLoading(boolean loading) {
        return swipeRefreshLayout.isRefreshing();
    }

    protected void replaceFragment(BaseFragment fragment, boolean addToBackStack, Bundle bundle) {
        FragmentUtil.replaceFragment(getActivity().getSupportFragmentManager(), fragment, bundle, addToBackStack);
    }

    protected void removeFragment(BaseFragment fragment) {
        FragmentUtil.takeOffBackStack(getActivity().getSupportFragmentManager(), fragment);
    }

    private void retainInstance() {
        FragmentUtil.retainFragmentInstance(getActivity().getSupportFragmentManager(), this);
    }

    private void hideActionBar() {
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void showActionBar() {
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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

    public abstract int getLayout();

    public abstract CharSequence getTitle();
}