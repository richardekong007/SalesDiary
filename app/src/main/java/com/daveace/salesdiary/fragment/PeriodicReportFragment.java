package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.daveace.salesdiary.Adapter.SalesReportAdapter;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.Customer;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.util.FragmentUtil;

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
        implements SalesReportAdapter.MoreClickListener {

    @BindView(R.id.periodicReports)
    RecyclerView periodicReportRecyclerView;

    private String reportHeader;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initUI();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_periodic_report;
    }

    @Override
    public CharSequence getTitle() {
        if (reportHeader != null && reportHeader.length() > 0){
            return  reportHeader;
        }
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

    @Override
    public void onClick(SalesEvent event, Product relatedProduct,
                        Customer relatedCustomer) {
        final String TAG = "SALES_EVENT_DETAILS_DIALOG";
        Bundle bundle = new Bundle();
        bundle.putParcelable(SALES_EVENTS_REPORT, event);
        bundle.putParcelable(EVENT_RELATED_PRODUCT, relatedProduct);
        bundle.putParcelable(EVENTS_RELATED_CUSTOMER, relatedCustomer);
//        SalesEventDetailsDialog.getInstance(bundle)
//                .show(getFragmentManager(), TAG);
        replaceFragment(new SalesEventDetailsFragment(),false,bundle);
    }

    private void initUI() {
        List<SalesEvent> salesEvents = new ArrayList<>();
        List<Product> relatedProducts = new ArrayList<>();
        List<Customer> relatedCustomers = new ArrayList<>();
        if (getArguments() != null) {
            salesEvents = getArguments().getParcelableArrayList(SALES_EVENTS_REPORTS);
            relatedProducts = getArguments().getParcelableArrayList(EVENT_RELATED_PRODUCTS);
            relatedCustomers = getArguments().getParcelableArrayList(EVENTS_RELATED_CUSTOMERS);
            reportHeader = (getArguments().getString(REPORT_TYPE));
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
}
