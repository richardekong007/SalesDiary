package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SalesEventInterpretation;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;

public class StatsInterpretationFragment extends BaseFragment implements BackIconActionBarMarker {

    @BindView(R.id.salesBars)
    BarChart salesBars;

    @BindView(R.id.salesPie)
    PieChart salesPie;

    @BindView(R.id.status)
    TextView statusText;

    @BindView(R.id.interpretation)
    TextView interpretationText;

    private Bundle args;
    private static final float GROUP_SPACE = 0.06f;
    private static final float BAR_SPACE = 0.02f;
    private static final float BAR_WIDTH = 0.22f;
    private static final float FROM_X = -0.5f;
    private static final float Y_INTERVAL = 100f;
    private static final String OUT_OF_STOCK = "Out of Stock";
    private static final String IN_STOCK = "In Stock";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = getArguments();
        initUI();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_stats_interpretation;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.stats_interpretationTitle);
    }

    private void initUI() {
        SalesEvent event = args.getParcelable(SALES_EVENTS_REPORT);
        Product product = args.getParcelable(EVENT_RELATED_PRODUCT);
        setupBars(event, product);
        setupPie(Objects.requireNonNull(event));
        setProductAvailabilityStatus(statusText, Objects.requireNonNull(product).getStock());
        setInterpretation(event, product);
    }

    private void setupBars(SalesEvent event, Product product) {
        String productName = Objects.requireNonNull(product).getName();
        List<BarEntry> salesEntries = new ArrayList<>();
        List<BarEntry> costEntries = new ArrayList<>();
        List<BarEntry> profitEntries = new ArrayList<>();

        salesEntries.add(new BarEntry(0f, (float) Objects.requireNonNull(event).getSalesPrice()));
        costEntries.add(new BarEntry(0f, (float) Objects.requireNonNull(event).getCostPrice()));
        profitEntries.add(new BarEntry(0f, (float) (event.getSalesPrice() - event.getCostPrice())));

        BarDataSet costDataSet = new BarDataSet(costEntries, getString(R.string.costLabel));
        BarDataSet saleDataSet = new BarDataSet(salesEntries, getString(R.string.salesLabel));
        BarDataSet profitDataSet = new BarDataSet(profitEntries, getString(R.string.profitLabel));

        paintBars(costDataSet, saleDataSet, profitDataSet);
        BarData barData = createBarData(costDataSet, saleDataSet, profitDataSet);
        styleBars(salesBars, barData, productName);
        salesBars.invalidate();
    }

    private void setupPie(SalesEvent salesEvent) {
        double percentageSold = getPercentageValue(salesEvent.getSales(), salesEvent.getLeft());
        double percentageLeft = getPercentageValue(salesEvent.getLeft(), salesEvent.getSales());
        List<PieEntry> productAvailabilityEntries = new ArrayList<>();
        Collections.addAll(productAvailabilityEntries,
                new PieEntry((float) percentageSold, "Sold"),
                new PieEntry((float) percentageLeft, "Left"));
        PieDataSet dataSet = new PieDataSet(productAvailabilityEntries, "Product Availability");
        dataSet.setValueFormatter(new PercentFormatter());
        PieData data = new PieData(dataSet);
        salesPie.setData(data);
        designChart(salesPie, dataSet);
    }

    private void designChart(PieChart chart, PieDataSet dataSet) {
        Description desc = new Description();
        desc.setText("Stock");
        stylePieDescription(desc);
        Legend legend = chart.getLegend();
        stylePieLegend(legend);
        paintPie(dataSet);
        chart.setDescription(desc);
        chart.setCenterText(desc.getText());
        chart.setCenterTextColor(getResources().getColor(R.color.colorPrimary, null));
        chart.invalidate();
    }

    private void paintPie(PieDataSet dataSet) {
        int[] colors = new int[]{R.color.blue, R.color.orange};
        dataSet.setColors(colors, getContext());
    }

    private void stylePieDescription(Description desc) {
        desc.setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private void stylePieLegend(Legend legend) {
        legend.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }

    private void styleBarAxis(BarChart barChart, List<String> xAxisLabels) {
        styleBarXAxis(barChart, xAxisLabels);
        styleBarYAxis(barChart);
    }

    private void styleBarXAxis(BarChart barChart, List<String> xAxisLabels) {
        XAxis xAxis = barChart.getXAxis();
        IAxisValueFormatter productFormatter =
                createProductFormatter(xAxisLabels);
        xAxis.setValueFormatter(productFormatter);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        xAxis.setGridColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private void styleBarYAxis(BarChart barChart) {
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        LargeValueFormatter formatter = new LargeValueFormatter();
        yAxisLeft.setGranularity(Y_INTERVAL);
        yAxisLeft.setValueFormatter(formatter);
        yAxisLeft.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        yAxisRight.setValueFormatter(formatter);
        yAxisRight.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        yAxisRight.setGridColor(getResources().getColor(R.color.colorPrimary, null));
        yAxisRight.setGridColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private void setBarDescription(String text) {
        Description desc = new Description();
        desc.setText(text);
        desc.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        salesBars.setDescription(desc);
    }

    private BarData createBarData(BarDataSet costDataSet, BarDataSet saleDataSet, BarDataSet profitDataSet) {
        BarData barData = new BarData(costDataSet, saleDataSet, profitDataSet);
        barData.setBarWidth(BAR_WIDTH);
        barData.setDrawValues(false);
        return barData;
    }

    private void styleBars(BarChart barChart, BarData barData, String label) {
        barChart.setData(barData);
        barChart.groupBars(FROM_X, GROUP_SPACE, BAR_SPACE);
        placeBarLegends();
        styleBarAxis(barChart, Collections.singletonList(label));
        setBarDescription(label);
        barChart.setFitBars(true);
    }

    private void paintBars(BarDataSet costDataSet, BarDataSet saleDataSet, BarDataSet profitDataSet) {
        costDataSet.setColors(new int[]{R.color.orange}, getActivity());
        saleDataSet.setColors(new int[]{R.color.green}, getActivity());
        profitDataSet.setColors(new int[]{R.color.blue}, getActivity());
    }

    private void placeBarLegends() {
        Legend legend = salesBars.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private IAxisValueFormatter createProductFormatter(List<String> productNames) {
        Object[] names = productNames.toArray();
        return (value, axis) -> String.valueOf(names[(int) value]);
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
        interpretationText.setText(interpretation);
    }

}
