package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SalesEventInterpretation;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.interfaces.Summarizable;
import com.daveace.salesdiary.util.BarChartUtil;
import com.daveace.salesdiary.util.PieChartUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;
import static com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment;
import static com.github.mikephil.charting.components.Legend.LegendOrientation;
import static com.github.mikephil.charting.components.Legend.LegendVerticalAlignment;

public class ProductStatisticsFragment extends BaseFragment implements Summarizable,BackIconActionBarMarker {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;

    @BindView(R.id.salesBars)
    BarChart salesBars;

    @BindView(R.id.salesPie)
    PieChart salesPie;

    @BindView(R.id.status)
    TextView statusText;

    @BindView(R.id.interpretation)
    TextView interpretationText;

    private Bundle args;
    private static final float GROUP_SPACE = 0.01f;
    private static final float BAR_SPACE = 0.01f;
    private static final float BAR_WIDTH = 0.16f;
    private static final float FROM_X = 0f;
    private static final float Y_INTERVAL = 50f;
    private static final float LABEL_ANGLE = 0f;
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
        setupBars(Objects.requireNonNull(event), product);
        setupPie(Objects.requireNonNull(event));
        setProductAvailabilityStatus(statusText, Objects.requireNonNull(product).getStock());
        setInterpretation(event, product);
    }

    private void setupBars(SalesEvent event, Product product) {
        String productName = Objects.requireNonNull(product).getName();
        List<String> productNames = Collections.singletonList(productName);
        LinkedHashMap<String, List<Double>> data = new LinkedHashMap<>();
        data.put(getString(R.string.salesLabel), Collections.singletonList(event.getSalesPrice()));
        data.put(getString(R.string.costLabel), Collections.singletonList(event.getCostPrice()));
        data.put(getString(R.string.profitLabel), Collections.singletonList(event.getSalesPrice() - event.getCostPrice()));
        List<Integer> colors = Arrays.asList(
                R.color.green, R.color.orange, R.color.blue
        );
        BarChartUtil.BarDimensionProperties properties = createDimensionProperties();
        BarChartUtil.constructChart(salesBars, productName, productNames, data, colors, properties);
        BarChartUtil.placeLegend(
                salesBars,
                LegendOrientation.HORIZONTAL,
                LegendHorizontalAlignment.CENTER,
                LegendVerticalAlignment.BOTTOM);

    }

    private void setupPie(SalesEvent salesEvent) {
        final String desc = "Stock";
        double percentageSold = getPercentageValue((float) salesEvent.getSales(),(float) salesEvent.getLeft());
        double percentageLeft = getPercentageValue((float) salesEvent.getLeft(), (float) salesEvent.getSales());
        LinkedHashMap<String, Float> data = new LinkedHashMap<>();
        data.put(getString(R.string.sold_label), (float) percentageSold);
        data.put(getString(R.string.left_label), (float) percentageLeft);
        int[] color = new int[]{ R.color.orange,R.color.blue};
        PieChartUtil.constructPieChart(salesPie, desc, data, color);
        PieChartUtil.positionLegend(
                salesPie,
                LegendOrientation.VERTICAL,
                LegendHorizontalAlignment.CENTER,
                LegendVerticalAlignment.BOTTOM
        );
    }

    private void setProductAvailabilityStatus(TextView textView, double stock) {
        textView.setText(stock < 1.0 ? OUT_OF_STOCK : IN_STOCK);
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
