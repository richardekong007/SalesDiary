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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private FirebaseAuth fbAuth;
    private List<SalesEvent> salesEvents;
    private List<Product> relatedProducts;
    private List<Customer> relatedCustomers;

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
        loadEvents();
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

    private void loadEvents() {
        FireStoreHelper.getInstance()
                .readDocsFromSubCollection(USERS, fbAuth.getCurrentUser().getUid(), SALESEVENTS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SalesEvent> theSalesEvents = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult()) {
                            theSalesEvents.add(doc.toObject(SalesEvent.class));
                        }
                        loadSalesEvents(theSalesEvents);
                    }
                });
    }

    private void loadSalesEvents(List<SalesEvent> theSaleEvents) {
        salesEvents = new ArrayList<>();
        salesEvents = theSaleEvents;
        loadRelatedProducts(salesEvents);
        loadRelatedCustomers(salesEvents);
    }

    private void loadRelatedProducts(List<SalesEvent> theSalesEvents) {
        relatedProducts = new ArrayList<>();
        FireStoreHelper.getInstance()
                .readDocsFromSubCollection(USERS, fbAuth.getCurrentUser().getUid(), PRODUCTS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Product product = doc.toObject(Product.class);
                            for (SalesEvent event : theSalesEvents)
                                if (event.getProductId().equals(Objects.requireNonNull(product).getId()))
                                    relatedProducts.add(product);
                        }
                    }
                });
    }

    private void loadRelatedCustomers(List<SalesEvent> theSalesEvents) {
        relatedCustomers = new ArrayList<>();
        FireStoreHelper.getInstance()
                .readDocsFromSubCollection(USERS, fbAuth.getCurrentUser().getUid(), CUSTOMERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Customer customer = doc.toObject(Customer.class);
                            for (SalesEvent event : theSalesEvents)
                                if (event.getCustomerId().equals(Objects.requireNonNull(customer).getId()))
                                    relatedCustomers.add(customer);
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