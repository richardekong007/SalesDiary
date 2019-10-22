package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentTransaction;

import com.daveace.salesdiary.Adapter.SalesSummaryTableDataAdapter;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.dialog.SalesEventInterpretationDialog;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.interfaces.Summarizable;
import com.daveace.salesdiary.model.SalesSummaryFigureDatum;
import com.daveace.salesdiary.util.MediaUtil;
import com.daveace.salesdiary.util.TableUtil;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import de.codecrafters.tableview.TableView;

import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORTS;
import static com.daveace.salesdiary.interfaces.Constant.SALES_SUMMARY_INTERPRETATION;

public class SummaryTableFragment extends BaseFragment
        implements BackIconActionBarMarker, Summarizable,
        SalesSummaryTableDataAdapter.OnRowClickListener {

    @BindView(R.id.rootView)
    LinearLayoutCompat rootView;

    private Bundle args;
    private byte[] fragmentSnapshot;

    private static SummaryTableFragment instance = null;

    private static final String[] TABLE_HEADER = new String[]{"S/N", "Product", "Profit", "Sales", "Cost"};


    public static SummaryTableFragment getInstance(@NonNull final Bundle args) {
        if (instance == null){
            instance = new SummaryTableFragment();
        }
        instance.setArguments(args);
        return instance;
    }

    public void add(@NonNull FragmentTransaction ft, int resId){
        if (!instance.isAdded()){
            ft.add(resId,instance).commitAllowingStateLoss();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.component_summary_table;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.sales_summary);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = getArguments();
        initUI(view);
        fragmentSnapshot = MediaUtil.toByteArray(MediaUtil.createBitmap(rootView));
    }

    @Override
    public void rowClick(SalesSummaryFigureDatum datum) {
        Bundle args = new Bundle();
        args.putParcelable(SALES_SUMMARY_INTERPRETATION, datum);
        SalesEventInterpretationDialog.getInstance(getChildFragmentManager(), args);
    }

    private void initUI(View view) {
        TableView summaryTableView = view.findViewById(R.id.summaryTable);
        ArrayList<SalesSummaryFigureDatum> tableData = getSalesSummaryFigureData(
                Objects.requireNonNull(args.getParcelableArrayList(SALES_EVENTS_REPORTS)),
                args.getParcelableArrayList(EVENT_RELATED_PRODUCTS)
        );
        setupTableData(summaryTableView, tableData);
        ((SalesSummaryTableDataAdapter) summaryTableView.getDataAdapter())
                .setOnRowClickListener(this);
    }

    private void setupTableData(TableView<SalesSummaryFigureDatum> tableView,
                                ArrayList<SalesSummaryFigureDatum> tableData) {
        TableUtil.prepare(getActivity(), tableView, TABLE_HEADER, tableData);
    }

    byte[] getFragmentViewInBytes() {
        return fragmentSnapshot;
    }

}
