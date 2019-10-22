package com.daveace.salesdiary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SalesSummaryInterpretation;
import com.daveace.salesdiary.model.SalesSummaryFigureDatum;
import com.daveace.salesdiary.util.PieChartUtil;
import com.github.mikephil.charting.charts.PieChart;
import static com.github.mikephil.charting.components.Legend.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.SALES_SUMMARY_INTERPRETATION;

public class SalesEventInterpretationDialog extends BaseDialog {

    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.interpretation)
    TextView interpretationTextView;
    @BindView(R.id.status)
    TextView statusTextView;

    private static final String OUT_OF_STOCK = "Out of Stock";
    private static final String IN_STOCK = "In Stock";

    private Bundle args;
    private static final String TAG = "SALES_INTERPRETATION_DIALOG";


    public static SalesEventInterpretationDialog getInstance(FragmentManager mgr, final Bundle args) {
        SalesEventInterpretationDialog salesEventInterpretationDialog =
                new SalesEventInterpretationDialog();
        if (args != null) {
            salesEventInterpretationDialog.setArguments(args);
        }
        salesEventInterpretationDialog.show(mgr, TAG);
        return salesEventInterpretationDialog;
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
    public void onDestroy() {
        super.onDestroy();
    }

    private void initUI(View view) {
        if (args == null) return;
        SalesSummaryFigureDatum datum = args.getParcelable(SALES_SUMMARY_INTERPRETATION);
        PieChart productAvailabilityChart = view
                .findViewById(R.id.productAvailabilityChart);
        productName.setText(Objects.requireNonNull(datum).getProduct());
        setInterpretation(datum);
        setupChart(
                Objects.requireNonNull(getActivity()),
                productAvailabilityChart,
                datum
        );
        setProductAvailabilityStatus(statusTextView, datum.getLeft());
    }

    private void setupChart(Context ctx,PieChart chart, SalesSummaryFigureDatum datum) {
        float percentageSold = (float) getPercentageValue(datum.getTotalSales(), datum.getLeft());
        float percentageLeft = (float)getPercentageValue(datum.getLeft(), datum.getTotalSales());
        int[] colors = new int[]{R.color.blue, R.color.orange};
        String description = ctx.getString(R.string.product_availability);
        Map<String, Float> figures = new LinkedHashMap<>();
        figures.put(ctx.getString(R.string.sold_label),percentageSold);
        figures.put(ctx.getString(R.string.left_label),percentageLeft);
        PieChartUtil.constructPieChart(chart,description,figures,colors);
        PieChartUtil.positionLegend(
                chart,
                LegendOrientation.VERTICAL,
                LegendHorizontalAlignment.CENTER,
                LegendVerticalAlignment.BOTTOM
        );

    }

    private double getPercentageValue(double value1, double value2) {
        return (value1 / (value1 + value2)) * 100;
    }

    private void setProductAvailabilityStatus(TextView textView, double stock) {
        textView.setText(stock < 1.0 ? OUT_OF_STOCK : IN_STOCK);
    }

    private void setInterpretation(SalesSummaryFigureDatum datum) {
        String interpretation = new SalesSummaryInterpretation.Builder()
                .setContext(this.getContext())
                .setDatum(datum)
                .build()
                .interpret()
                .getInterpretation();
        interpretationTextView.setText(interpretation);
    }

}
