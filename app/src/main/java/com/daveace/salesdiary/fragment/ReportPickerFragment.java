package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.LinearLayout;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.Customer;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.daveace.salesdiary.util.ReportUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.CUSTOMERS;
import static com.daveace.salesdiary.interfaces.Constant.DAILY_SALES_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.EVENTS_RELATED_CUSTOMERS;
import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.GENERAL_SALES_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.MONTHLY_SALES_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.QUARTERLY_SALES_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.REPORT_TYPE;
import static com.daveace.salesdiary.interfaces.Constant.SALESEVENTS;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORTS;
import static com.daveace.salesdiary.interfaces.Constant.SEMESTER_SALES_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.USERS;
import static com.daveace.salesdiary.interfaces.Constant.WEEKLY_SALES_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.YEARLY_SALES_REPORT;

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
    private final List<SalesEvent> salesEvents = new ArrayList<>();
    private final List<Product> relatedProducts = new ArrayList<>();
    private final List<Customer> relatedCustomers = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData(SALESEVENTS, salesEvents, SalesEvent.class);
        loadData(PRODUCTS, relatedProducts, Product.class);
        loadData(CUSTOMERS, relatedCustomers, Customer.class);
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

        dailyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getDailySalesEventReports(salesEvents), DAILY_SALES_REPORT));
        weeklyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getWeeklySalesEventReports(salesEvents), WEEKLY_SALES_REPORT));
        monthlyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getMonthlySalesEventReports(salesEvents), MONTHLY_SALES_REPORT));
        quarterlyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getQuarterlySalesEventReports(salesEvents), QUARTERLY_SALES_REPORT));
        semesterReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getSemesterSalesEventReports(salesEvents), SEMESTER_SALES_REPORT));
        yearlyReportCardView.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getYearlySalesEventReports(salesEvents), YEARLY_SALES_REPORT));
        generalReportLayout.setOnClickListener(view ->
                transferSaleReports(ReportUtil.getGeneralSalesReports(salesEvents), GENERAL_SALES_REPORT));
    }

    private <T> void loadData(String subCollection, List<T> docs, Class<T> entityClass) {
        CollectionReference reference = FireStoreHelper.getInstance()
                .readDocsFromSubCollection(USERS, getUserId(), subCollection);
        reference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.exists())
                                docs.add(doc.toObject(entityClass));
                        }
                    }
                });
    }

    private void transferSaleReports(List<SalesEvent> theSalesEvents, String reportType) {
        Bundle salesEventBundle = new Bundle();
        salesEventBundle.putString(REPORT_TYPE, reportType);
        salesEventBundle.putParcelableArrayList(SALES_EVENTS_REPORTS, (ArrayList<? extends Parcelable>) theSalesEvents);
        salesEventBundle.putParcelableArrayList(EVENT_RELATED_PRODUCTS, (ArrayList<? extends Parcelable>) relatedProducts);
        salesEventBundle.putParcelableArrayList(EVENTS_RELATED_CUSTOMERS, (ArrayList<? extends Parcelable>) relatedCustomers);
        replaceFragment(new PeriodicReportFragment(), false, salesEventBundle);
    }

}