package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.daveace.salesdiary.Adapter.ProductAnalysisAdapter;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SalesEventInterpretation;
import com.daveace.salesdiary.dialog.SalesEventInterpretationDialog;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.REPORT_TYPE;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORTS;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENT_INTERPRETATION;
import static java.util.stream.Collectors.toList;

public class SummaryFragment extends BaseFragment implements
        BackIconActionBarMarker, ProductAnalysisAdapter.OnProductClickListener {

    @BindView(R.id.analysis_contents)
    RecyclerView analysisContentsRecyclerView;

    private static final float GROUP_SPACE = 0.06f;
    private static final float BAR_SPACE = 0.02f;
    private static final float BAR_WIDTH = 0.25f;
    private static final float FROM_X = -0.015f;
    private static final float LABEL_ANGLE = 60f;
    private static final float Y_INTERVAL = 1000f;

    private Bundle args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        args = getArguments();
        initUI(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_report_preview, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.previewItem:
                //display the preview report fragment
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_summary;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.sales_summary);
    }

    @Override
    public void onProductClick(SalesEventInterpretation eventInterpretation) {
        Bundle bundle = new Bundle();
        bundle.putString(SALES_EVENT_INTERPRETATION
                , eventInterpretation.getInterpretation());
        bundle.putParcelable(SALES_EVENTS_REPORT,
                eventInterpretation.getSalesEvent());
        bundle.putParcelable(EVENT_RELATED_PRODUCT,
                eventInterpretation.getProduct());
        SalesEventInterpretationDialog.getInstance(getFragmentManager(), bundle);
    }

    private void setupRecyclerView(List<SalesEvent> events, List<Product> products) {
        List<SalesEventInterpretation> eventInterpretations =
                getSalesEventInterpretations(events, products);
        ProductAnalysisAdapter adapter =
                new ProductAnalysisAdapter(eventInterpretations);
        adapter.setOnProductClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        analysisContentsRecyclerView.hasFixedSize();
        analysisContentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        analysisContentsRecyclerView.setLayoutManager(layoutManager);
        analysisContentsRecyclerView.setAdapter(adapter);

    }

    private void initUI(View view) {
        if (args == null) return;
        BarChart summaryBarChart = view.findViewById(R.id.summaryChart);
        String chartDescription = args.getString(REPORT_TYPE);
        List<Product> products = Objects.requireNonNull
                (args.getParcelableArrayList(EVENT_RELATED_PRODUCTS));
        List<SalesEvent> events = Objects.requireNonNull
                (args.getParcelableArrayList(SALES_EVENTS_REPORTS));
        List<String> productNames = getProductNames(events, products);
        List<Double> costFigures = getCostFigures(events);
        List<Double> salesFigures = getSalesFigures(events);
        List<Double> profitFigures = getProfitFigures(events);
        List<BarEntry> costEntries = new ArrayList<>();
        List<BarEntry> salesEntries = new ArrayList<>();
        List<BarEntry> profitEntries = new ArrayList<>();

        for (int i = 0; i < costFigures.size(); i++) {
            salesEntries.add(new BarEntry((float) i, Float
                    .valueOf(String.valueOf(salesFigures.get(i)))));
            costEntries.add(new BarEntry((float) i, Float
                    .valueOf(String.valueOf(costFigures.get(i)))));
            profitEntries.add(new BarEntry((float) i, Float
                    .valueOf(String.valueOf(profitFigures.get(i)))));

        }

        BarDataSet costDataSet = new BarDataSet(costEntries, getString(R.string.costLabel));
        BarDataSet saleDataSet = new BarDataSet(salesEntries, getString(R.string.salesLabel));
        BarDataSet profitDataSet = new BarDataSet(profitEntries, getString(R.string.profitLabel));

        costDataSet.setColors(new int[]{R.color.orange}, getActivity());
        saleDataSet.setColors(new int[]{R.color.green}, getActivity());
        profitDataSet.setColors(new int[]{R.color.blue}, getActivity());

        BarData barData = new BarData(costDataSet, saleDataSet, profitDataSet);
        barData.setBarWidth(BAR_WIDTH);
        barData.setDrawValues(false);

        summaryBarChart.setData(barData);
        summaryBarChart.groupBars(FROM_X, GROUP_SPACE, BAR_SPACE);

        Legend legend = summaryBarChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setTextColor(getResources().getColor(R.color.colorPrimary, null));

        XAxis xAxis = summaryBarChart.getXAxis();
        YAxis axisLeft = summaryBarChart.getAxisLeft();
        YAxis axisRight = summaryBarChart.getAxisRight();
        xAxis.setLabelRotationAngle(LABEL_ANGLE);
        axisLeft.setGranularity(Y_INTERVAL);

        IAxisValueFormatter productFormatter = createProductFormatter(productNames);
        LargeValueFormatter formatter = new LargeValueFormatter();
        xAxis.setValueFormatter(productFormatter);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        xAxis.setGridColor(getResources().getColor(R.color.colorPrimary, null));

        axisLeft.setValueFormatter(formatter);
        axisLeft.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        axisRight.setValueFormatter(formatter);
        axisRight.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        axisRight.setGridColor(getResources().getColor(R.color.colorPrimary, null));
        axisRight.setGridColor(getResources().getColor(R.color.colorPrimary, null));

        Description desc = new Description();
        desc.setText(chartDescription);
        desc.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        summaryBarChart.setFitBars(true);
        summaryBarChart.setDescription(desc);
        summaryBarChart.invalidate();

        setupRecyclerView(events, products);
    }

    private List<SalesEventInterpretation>
    getSalesEventInterpretations(List<SalesEvent> events, List<Product> products) {
        final List<SalesEventInterpretation> interpretations =
                new ArrayList<>();
        events.forEach(event -> products.forEach(product -> {
            if (event.getProductId().equals(product.getId())) {
                SalesEventInterpretation eventInterpretation =
                        new SalesEventInterpretation
                                .Builder()
                                .setContext(getActivity())
                                .setSalesEvent(event)
                                .setProduct(product)
                                .build();
                eventInterpretation.interpret();
                interpretations.add(eventInterpretation);
            }
        }));
        return interpretations;
    }

    private List<String> getProductNames(List<SalesEvent> events,
                                         List<Product> products) {
        final List<String> productNames = new ArrayList<>();
        events.forEach(event -> products.forEach(product -> {
            if (event.getProductId().equals(product.getId())) {
                productNames.add(product.getName());
            }
        }));
        return productNames;
    }

    private List<Double> getCostFigures(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(SalesEvent::getCostPrice)
                .collect(toList());
    }

    private List<Double> getSalesFigures(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(SalesEvent::getSalesPrice)
                .distinct()
                .collect(toList());
    }

    private List<Double> getProfitFigures(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(event -> event.getSalesPrice() - event.getCostPrice())
                .collect(toList());
    }

    private IAxisValueFormatter createProductFormatter(List<String> productNames) {
        Object[] names = productNames.toArray();
        return (value, axis) -> String.valueOf(names[(int) value]);
    }

}
