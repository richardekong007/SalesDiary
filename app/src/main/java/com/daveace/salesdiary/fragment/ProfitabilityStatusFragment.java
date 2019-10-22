package com.daveace.salesdiary.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.interfaces.Summarizable;
import com.daveace.salesdiary.util.PieChartUtil;
import com.github.mikephil.charting.charts.PieChart;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

public class ProfitabilityStatusFragment extends BaseFragment implements BackIconActionBarMarker,
        Summarizable {

    @BindView(R.id.profitability_chart)
    PieChart profitabilityChart;

    @BindView(R.id.status_text)
    TextView statusTextView;

    private Bundle args;

    @SuppressLint("StaticFieldLeak")
    private static ProfitabilityStatusFragment instance = null;

    private final static String TOTAL_PROFIT = "TOTAL_PROFIT";
    private final static String TOTAL_LOSS = "TOTAL_LOSS";
    private final static String PROFIT_LABEL = "Profit";
    private final static String LOSS_LABEL = "Loss";
    private final static String PROFITABILITY_LEVEL = "Profitability Level";
    private final static String PROFITABLE = "Profitable";
    private final static String NOT_PROFITABLE = "Not Profitable";

    public static ProfitabilityStatusFragment getInstance(@NonNull Bundle args) {
        if (instance == null) {
            instance = new ProfitabilityStatusFragment();
        }
        instance.setArguments(args);
        return instance;
    }

    public FragmentTransaction add(@NonNull FragmentTransaction ft, int resId) {
        if (!instance.isAdded()) {
            ft.add(resId, instance);
        }
        return ft;
    }

    @Override
    public int getLayout() {
        return R.layout.child_fragment_profitability_status;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.interpretation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        args = getArguments();
        if (args == null) return;
        constructPie();
        setProfitabilityStatus(statusTextView);
    }

    private void constructPie() {
        Map<String, Float> data = new LinkedHashMap<>();
        int[] colors = new int[]{R.color.red, R.color.bright_blue};
        float percentageProfit = getPercentageValue(args.getFloat(TOTAL_PROFIT),
                args.getFloat(TOTAL_LOSS)
        );
        float percentageLoss = getPercentageValue(args.getFloat(TOTAL_LOSS),
                args.getFloat(TOTAL_PROFIT)
        );
        data.put(PROFIT_LABEL, percentageProfit);
        data.put(LOSS_LABEL, percentageLoss);
        PieChartUtil.constructPieChart(
                profitabilityChart, PROFITABILITY_LEVEL, data, colors
        );
    }

    private void setProfitabilityStatus(TextView textView) {
        if (args == null) return;
        double loss = args.getFloat(TOTAL_LOSS);
        double profit = args.getFloat(TOTAL_PROFIT);
        textView.setText(profit > loss ? PROFITABLE : NOT_PROFITABLE);
    }
}
