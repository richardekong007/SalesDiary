package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.util.FragmentUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class PeriodicReportFragment extends BaseFragment {

    @BindView(R.id.reportHeader)
    TextView reportHeaderTextView;

    @BindView(R.id.periodicReports)
    RecyclerView periodicReportRecyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_periodic_report;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.periodic_report_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.summaryItem:
                FragmentUtil.replaceFragment((getActivity()).getSupportFragmentManager(),
                        new SummaryFragment(), null, false);
        }
        return super.onOptionsItemSelected(item);
    }
}
