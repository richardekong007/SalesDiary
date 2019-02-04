package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindViews;
import butterknife.OnClick;
import com.daveace.salesdiary.R;

public class ReportPickerFragment extends BaseFragment {

    @BindViews({R.id.dailyReportAction, R.id.weeklyReportAction, R.id.monthlyReportAction,
            R.id.quarterlyReportAction, R.id.semesterReportAction, R.id.yearlyReportAction,
            R.id.generalReportAction})
    List<ViewGroup> viewGroups;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_report_picker;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.select_report_title);
    }

    @OnClick(R.id.dailyReportAction)
    public void onDailyReportItemClick() {

    }

    @OnClick(R.id.weeklyReportAction)
    public void onWeeklyReportItemClick() {

    }

    @OnClick(R.id.monthlyReportAction)
    public void onMonthlyReportItemClick() {

    }

    @OnClick(R.id.quarterlyReportAction)
    public void onquaterlyReportItemClick() {

    }

    @OnClick(R.id.semesterReportAction)
    public void onSemesterReportItemClick() {

    }

    @OnClick(R.id.yearlyReportAction)
    public void onYearlyReportItemClick() {

    }

    @OnClick(R.id.generalReportAction)
    public void onGeneralReportItemClick() {

    }

}
