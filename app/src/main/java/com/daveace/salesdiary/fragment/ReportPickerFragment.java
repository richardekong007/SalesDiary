package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.daveace.salesdiary.util.ReportUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.daveace.salesdiary.interfaces.Constant.SALESEVENTS;
import static com.daveace.salesdiary.interfaces.Constant.USERS;

public class ReportPickerFragment extends BaseFragment {

    @BindView(R.id.dailyReportAction)
    CardView dailyReportCardView;
    @BindView(R.id.weeklyReportAction)
    CardView weeklyReportCardView;
    @BindView(R.id.monthlyReportAction)
    CardView monthlyReportCardView;
    @BindView(R.id.quarterlyReportAction)
    CardView quarterlyReportCardView;
    @BindView(R.id.semesterReportAction)
    CardView semesterReportCardView;
    @BindView(R.id.yearlyReportAction)
    CardView yearlyReportCardView;
    @BindView(R.id.generalReportAction)
    LinearLayout generalReportLayout;

    public static final String SALES_EVENTS_REPORT = "SALES_EVENTS_REPORT";

    private FirebaseAuth fbAuth;
    private List<SalesEvent> salesEvents;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        initUI();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_report_picker;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.select_report_title);
    }

    private void initUI() {
        loadSalesEvents();
        dailyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getDailySalesEventReports(salesEvents)));

        weeklyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getWeeklySalesEventReports(salesEvents)));
        monthlyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getMonthlySalesEventReports(salesEvents)));
        quarterlyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getQuarterlySalesEventReports(salesEvents)));
        semesterReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getSemesterSalesEventReports(salesEvents)));
        yearlyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getYearlySalesEventReports(salesEvents)));
        generalReportLayout.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getGeneralSalesReports(salesEvents)));
    }

    private void loadSalesEvents() {
        FireStoreHelper
                .getInstance()
                .readDocsFromSubCollection(
                        USERS,
                        fbAuth.getCurrentUser().getUid(),
                        SALESEVENTS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            SalesEvent event = doc.toObject(SalesEvent.class);
                            salesEvents.add(event);
                        }
                    }

                });
    }

    private void transferSaleReports(List<SalesEvent> theSalesEvents) {
        Bundle salesEventBundle = new Bundle();
        salesEventBundle.putParcelableArrayList(
                SALES_EVENTS_REPORT,
                (ArrayList<? extends Parcelable>) theSalesEvents
        );
        replaceFragment(new PeriodicReportFragment(), false, salesEventBundle);
    }


}
