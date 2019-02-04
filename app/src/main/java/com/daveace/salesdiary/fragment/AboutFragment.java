package com.daveace.salesdiary.fragment;

import com.daveace.salesdiary.R;

public class AboutFragment extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_about;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.about_title);
    }
}
