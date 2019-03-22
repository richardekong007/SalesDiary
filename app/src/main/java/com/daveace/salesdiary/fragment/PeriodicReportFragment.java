package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

public class PeriodicReportFragment extends BaseFragment {

    @BindView(R.id.reportHeader)
    TextView reportHeaderTextView;

    @BindView(R.id.periodicReports)
    RecyclerView periodicReportRecyclerView;

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

    private void initUI(){
        List<SalesEvent> salesEvents = new ArrayList<>();
        List<Product> relatedProducts = new ArrayList<>();
        List<Customer> relatedCustomers = new ArrayList<>();
        if (getArguments() != null){
            salesEvents = getArguments().getParcelableArrayList(ReportPickerFragment.SALES_EVENTS_REPORTS);
            relatedProducts = getArguments().getParcelableArrayList(ReportPickerFragment.EVENT_RELATED_PRODUCTS);
            relatedCustomers = getArguments().getParcelableArrayList(ReportPickerFragment.EVENTS_RELATED_CUSTOMERS);
            reportHeaderTextView.setText(getArguments().getString(ReportPickerFragment.REPORT_TYPE));
        }
        SalesReportAdapter adapter = new SalesReportAdapter(salesEvents);
        adapter.setRelatedProducts(relatedProducts);
        adapter.setRelatedCustomer(relatedCustomers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        periodicReportRecyclerView.hasFixedSize();
        periodicReportRecyclerView.setItemAnimator(new DefaultItemAnimator());
        periodicReportRecyclerView.setLayoutManager(linearLayoutManager);
        periodicReportRecyclerView.setAdapter(adapter);

    }
}
