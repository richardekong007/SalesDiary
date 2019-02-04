package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class OutflowFragment extends BaseFragment {

    @BindViews({R.id.totalCost, R.id.totalLoss})
    List<TextView> textViews;

    @BindView(R.id.moreButton)
    AppCompatButton moreButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_outflow;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.outflow_title);
    }

    @OnClick(R.id.moreButton)
    public void onMoreButtonClick() {

    }
}
