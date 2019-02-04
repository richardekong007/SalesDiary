package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.View;

import com.daveace.salesdiary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PreviewReportFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.preview_title);
    }
}
