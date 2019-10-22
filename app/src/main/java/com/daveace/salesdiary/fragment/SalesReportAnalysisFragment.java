package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SalesEventInterpretation;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;

public class SalesReportAnalysisFragment extends BaseFragment {

    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.interpretation)
    TextView interpretationTextView;
    @BindView(R.id.status)
    TextView statusTextView;

    private static final String OUT_OF_STOCK = "Out of Stock";
    private static final String IN_STOCK = "In Stock";

    private Bundle args;


    public static SalesReportAnalysisFragment getInstance(final Bundle args) {
        SalesReportAnalysisFragment salesReportAnalysisFragment =
                new SalesReportAnalysisFragment();
        if (args != null) {
            salesReportAnalysisFragment.setArguments(args);
        }
        return salesReportAnalysisFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        args = getArguments();
        initUI(view);
        return view;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_product_interpretation_content;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.sales_summary);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initUI(View view) {
        if (args == null) return;
        SalesEvent event = args.getParcelable(SALES_EVENTS_REPORT);
        Product product = args.getParcelable(EVENT_RELATED_PRODUCT);
        PieChart productAvailabilityChart = view
                .findViewById(R.id.productAvailabilityChart);
        productName.setText(Objects.requireNonNull(product).getName());
        setInterpretation(event, product);
        setupChart(productAvailabilityChart, Objects.requireNonNull(event));
        setProductAvailabilityStatus(statusTextView, event.getLeft());
    }

    private void setupChart(PieChart chart, SalesEvent salesEvent) {
        double percentageSold = getPercentageValue(salesEvent.getSales(), salesEvent.getLeft());
        double percentageLeft = getPercentageValue(salesEvent.getLeft(), salesEvent.getSales());
        List<PieEntry> productAvailabilityEntries = new ArrayList<>();
        Collections.addAll(productAvailabilityEntries,
                new PieEntry((float) percentageSold, "Sold"),
                new PieEntry((float) percentageLeft, "Left"));
        PieDataSet dataSet = new PieDataSet(productAvailabilityEntries, "Product Availability");
        dataSet.setValueFormatter(new PercentFormatter());
        PieData data = new PieData(dataSet);
        chart.setData(data);
        designChart(chart, dataSet);
    }

    private void designChart(PieChart chart, PieDataSet dataSet) {
        Description desc = new Description();
        desc.setText("Product Availability");
        styleDescription(desc);
        Legend legend = chart.getLegend();
        styleLegend(legend);
        paintChart(dataSet);
        chart.setDescription(desc);
        chart.setCenterText(desc.getText());
        chart.setCenterTextColor(getResources().getColor(R.color.colorPrimary, null));
        chart.invalidate();
    }

    private void paintChart(PieDataSet dataSet) {
        int[] colors = new int[]{R.color.blue, R.color.orange};
        dataSet.setColors(colors, getContext());
    }

    private void styleDescription(Description desc) {
        desc.setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private void styleLegend(Legend legend) {
        legend.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    }

    private double getPercentageValue(double value1, double value2) {
        return (value1 / (value1 + value2)) * 100;
    }

    private void setProductAvailabilityStatus(TextView textView, double stock) {
        textView.setText(stock < 1.0 ? OUT_OF_STOCK : IN_STOCK);
    }

    private void setInterpretation(SalesEvent event, Product product) {
        String interpretation = new SalesEventInterpretation
                .Builder()
                .setContext(getActivity())
                .setSalesEvent(event)
                .setProduct(product)
                .build()
                .interpret()
                .getInterpretation();
        interpretationTextView.setText(interpretation);
    }

}
