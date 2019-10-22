package com.daveace.salesdiary.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.interfaces.Summarizable;
import com.daveace.salesdiary.model.SalesSummaryFigureDatum;
import com.daveace.salesdiary.util.BarChartUtil;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.REPORT_TYPE;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORTS;

public class SummaryChartFragment extends BaseFragment implements Summarizable, BackIconActionBarMarker {

    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.summaryChart)
    HorizontalBarChart summaryBarChart;

    private Bundle args;
    @SuppressLint("StaticFieldLeak")
    private static SummaryChartFragment instance = null;
    private static final float GROUP_SPACE = 0.10f;
    private static final float BAR_SPACE = 0.02f;
    private static final float BAR_WIDTH = 0.25f;
    private static final float FROM_X = 0f;
    private static final float LABEL_ANGLE = 0f;
    private static final float Y_INTERVAL = 100f;

    public static SummaryChartFragment getInstance(@NonNull final Bundle args) {
        if (instance == null) {
            instance = new SummaryChartFragment();
        }
        instance.setArguments(args);
        return instance;
    }

    public void add(@NonNull FragmentTransaction ft, int resId) {
        if (!instance.isAdded()) {
            ft.add(resId, instance).commitAllowingStateLoss();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.component_summary_chart;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.sales_summary);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = getArguments();
        initUI();
    }

    private void initUI() {
        String chartDescription = args.getString(REPORT_TYPE);
        List<Product> products = Objects.requireNonNull
                (args.getParcelableArrayList(EVENT_RELATED_PRODUCTS));
        List<SalesEvent> events = Objects.requireNonNull
                (args.getParcelableArrayList(SALES_EVENTS_REPORTS));
        List<SalesSummaryFigureDatum> data = getSalesSummaryFigureData(events, products);
        List<String> productNames = getProductNames(data);
        List<Double> costFigures = getCostFigures(data);
        List<Double> salesFigures = getSalesFigures(data);
        List<Double> profitFigures = getProfitFigures(data);
        LinkedHashMap<String, List<Double>> figures = new LinkedHashMap<>();
        List<Integer> colors = new ArrayList<>();
        Collections.addAll(colors, R.color.deep_green, R.color.orange, R.color.bright_blue);
        figures.put(getString(R.string.salesLabel), salesFigures);
        figures.put(getString(R.string.costLabel), costFigures);
        figures.put(getString(R.string.profitLabel), profitFigures);
        constructChart(summaryBarChart, chartDescription, productNames, figures, colors);
    }

    private void constructChart(HorizontalBarChart chart, String desc, List<String> productNames,
                                LinkedHashMap<String, List<Double>> data, List<Integer> colors) {
        BarChartUtil.BarDimensionProperties properties = createDimensionProperties();
        BarChartUtil.constructChart(
                chart,
                desc,
                productNames,
                data,
                colors,
                properties
        );
    }

    private BarChartUtil.BarDimensionProperties createDimensionProperties() {
        return new BarChartUtil.BarDimensionProperties.Builder()
                .setBarSpace(BAR_SPACE)
                .setBarWidth(BAR_WIDTH)
                .setGroupSpace(GROUP_SPACE)
                .setXStartPoint(FROM_X)
                .setYInterval(Y_INTERVAL)
                .setXLabelAngle(LABEL_ANGLE)
                .build();
    }

}
