package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.daveace.salesdiary.Adapter.SalesReportAdapter;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.alert.ErrorAlert;
import com.daveace.salesdiary.entity.Customer;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENTS_RELATED_CUSTOMER;
import static com.daveace.salesdiary.interfaces.Constant.EVENTS_RELATED_CUSTOMERS;
import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.REPORT_TYPE;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORTS;

public class PeriodicReportFragment extends BaseFragment
        implements SalesReportAdapter.MoreClickListener, BackIconActionBarMarker {

    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.periodicReports)
    RecyclerView periodicReportRecyclerView;

    private String reportHeader;
    private List<SalesEvent> salesEvents = new ArrayList<>();
    private List<Product> relatedProducts = new ArrayList<>();
    private List<Customer> relatedCustomers = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        initUI();
        setHasOptionsMenu(true);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_periodic_report;
    }

    @Override
    public CharSequence getTitle() {
        if (reportHeader != null && reportHeader.length() > 0) {
            return reportHeader;
        }
        return getString(R.string.periodic_report_title);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cash_flow_report_, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cashFlowItem:
                Bundle args = new Bundle();
                args.putParcelableArrayList(SALES_EVENTS_REPORTS,
                        (ArrayList<? extends Parcelable>) salesEvents);
                args.putParcelableArrayList(EVENT_RELATED_PRODUCTS,
                        (ArrayList<? extends Parcelable>) relatedProducts);
                args.putParcelableArrayList(EVENTS_RELATED_CUSTOMERS,
                        (ArrayList<? extends Parcelable>) relatedCustomers);
                args.putString(REPORT_TYPE, reportHeader);
                replaceFragment(new CashFlowFragment(), true, args);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(SalesEvent event, Product relatedProduct,
                        Customer relatedCustomer) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SALES_EVENTS_REPORT, event);
        bundle.putParcelable(EVENT_RELATED_PRODUCT, relatedProduct);
        bundle.putParcelable(EVENTS_RELATED_CUSTOMER, relatedCustomer);
        replaceFragment(new SalesEventDetailsFragment(), true, bundle);
    }

    private void initUI() {
        if (!isDataLoaded()) {
            ErrorAlert.Builder()
                    .setContext(getContext())
                    .setRootView(rootView)
                    .setMessage(String.format(getString(R.string.no_report), reportHeader))
                    .setActionCommand("Retry")
                    .setAction(view -> getFragmentManager().popBackStackImmediate())
                    .build()
                    .show();
            return;
        }
        SalesReportAdapter adapter = new SalesReportAdapter(salesEvents);
        adapter.setRelatedProducts(relatedProducts);
        adapter.setRelatedCustomer(relatedCustomers);
        adapter.setMoreClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        periodicReportRecyclerView.hasFixedSize();
        periodicReportRecyclerView.setItemAnimator(new DefaultItemAnimator());
        periodicReportRecyclerView.setLayoutManager(linearLayoutManager);
        periodicReportRecyclerView.setAdapter(adapter);

    }

    private void loadData() {
        if (getArguments() == null)
            return;
        salesEvents = getArguments().getParcelableArrayList(SALES_EVENTS_REPORTS);
        relatedProducts = getArguments().getParcelableArrayList(EVENT_RELATED_PRODUCTS);
        relatedCustomers = getArguments().getParcelableArrayList(EVENTS_RELATED_CUSTOMERS);
        reportHeader = (getArguments().getString(REPORT_TYPE));

    }

    private boolean isDataLoaded() {
        return (salesEvents.size() > 0
                && relatedProducts.size() > 0
                && relatedCustomers.size() > 0);
    }
}
