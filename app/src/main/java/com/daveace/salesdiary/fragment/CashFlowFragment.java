package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.interfaces.Summarizable;
import com.daveace.salesdiary.util.MediaUtil;
import com.daveace.salesdiary.util.PieChartUtil;
import com.github.mikephil.charting.charts.PieChart;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.PROFIT_LOSS_CHART_TITLE;
import static com.daveace.salesdiary.interfaces.Constant.REPORT_TYPE;
import static com.daveace.salesdiary.interfaces.Constant.SALES_COST_CHART_TITLE;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORTS;
import static com.daveace.salesdiary.interfaces.Constant.SPACE;
import static com.daveace.salesdiary.interfaces.Constant.TOTAL_COST;
import static com.daveace.salesdiary.interfaces.Constant.TOTAL_INFLOW;
import static com.daveace.salesdiary.interfaces.Constant.TOTAL_LOSS;
import static com.daveace.salesdiary.interfaces.Constant.TOTAL_OUTFLOW;
import static com.daveace.salesdiary.interfaces.Constant.TOTAL_PROFIT;
import static com.daveace.salesdiary.interfaces.Constant.TOTAL_SALES;

public class CashFlowFragment extends BaseFragment implements BackIconActionBarMarker, Summarizable {

    @BindView(R.id.inflowText)
    TextView inflowText;
    @BindView(R.id.outflowText)
    TextView outflowText;
    @BindView(R.id.totalSales)
    TextView salesText;
    @BindView(R.id.totalProfit)
    TextView profitText;
    @BindView(R.id.totalLoss)
    TextView lossText;
    @BindView(R.id.totalCost)
    TextView costText;
    @BindView(R.id.moreButton)
    AppCompatButton moreButton;

    private PieChart salesCostPieChart;
    private PieChart profitLossPieChart;
    private Bundle args;
    private String reportHeader;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        args = getArguments();
        if (view != null) {
            profitLossPieChart = view.findViewById(R.id.profit_loss_pie);
            salesCostPieChart = view.findViewById(R.id.sales_cost_pie);
        }
        initUI();
        return view;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_cashflow;
    }

    @Override
    public CharSequence getTitle() {
        if (reportHeader != null) {
            int spacePosition = reportHeader.indexOf(SPACE);
            reportHeader = reportHeader.substring(0, spacePosition);
            final String PATH_HEADER = SPACE + getString(R.string.cash_flow);
            reportHeader = reportHeader.concat(PATH_HEADER);
            return reportHeader;
        }
        return getString(R.string.periodic_cash_flow);
    }

    private void initUI() {
        if (args == null) return;
        reportHeader = Objects.requireNonNull(args.getString(REPORT_TYPE)).concat(getString(R.string.cash_flow));
        List<SalesEvent> salesEvents = args.getParcelableArrayList(SALES_EVENTS_REPORTS);
        setFigures(salesEvents);
        constructSalesCostPie(salesEvents);
        constructProfitLossPie(salesEvents);
        moreButton.setOnClickListener(view -> {
            Bundle bundle = makeBundle(salesEvents);
            replaceFragment(new VisualizedSaleSummaryFragment(), true, bundle);
        });

    }

    private void constructProfitLossPie(List<SalesEvent> events) {
        Map<String, Float> data = new LinkedHashMap<>();
        int[] colors = new int[]{R.color.bright_blue, R.color.red};
        Float totalProfit = getPercentageValue((float) getTotalProfit(events),
                (float) getTotalLoss(events)
        );
        Float totalLoss = getPercentageValue((float) getTotalLoss(events),
                (float) getTotalProfit(events)
        );
        data.put(getString(R.string.profitLabel), totalProfit);
        data.put(getString(R.string.lossLabel), totalLoss);
        PieChartUtil.constructPieChart(
                profitLossPieChart,
                getString(R.string.profit_and_loss),
                data,
                colors
        );
    }

    private void constructSalesCostPie(List<SalesEvent> events) {
        Map<String, Float> data = new LinkedHashMap<>();
        int[] colors = new int[]{R.color.green, R.color.orange};
        Float totalSales = getPercentageValue((float) getTotalSalesAmount(events),
                (float) getTotalCost(events)
        );
        Float totalCost = getPercentageValue((float) getTotalCost(events),
                (float) getTotalSalesAmount(events)
        );
        data.put(getString(R.string.salesLabel), totalSales);
        data.put(getString(R.string.costLabel), totalCost);
        PieChartUtil.constructPieChart(
                salesCostPieChart,
                getString(R.string.sales_and_cost),
                data,
                colors
        );
    }

    private void setFigures(List<SalesEvent> salesEvents) {
        inflowText.setText(String.valueOf(getTotalInflow(salesEvents)));
        outflowText.setText(String.valueOf(getTotalOutFlow(salesEvents)));
        profitText.setText(String.valueOf(getTotalProfit(salesEvents)));
        lossText.setText(String.valueOf(getTotalLoss(salesEvents)));
        salesText.setText(String.valueOf(getTotalSalesAmount(salesEvents)));
        costText.setText(String.valueOf(getTotalCost(salesEvents)));
    }

    private Bundle makeBundle(List<SalesEvent> salesEvents) {
        Bundle bundle = new Bundle();
        byte[] salesCostPieBytes = getSalesCostPieBytes();
        bundle.putParcelableArrayList(SALES_EVENTS_REPORTS,
                args.getParcelableArrayList(SALES_EVENTS_REPORTS));
        bundle.putParcelableArrayList(EVENT_RELATED_PRODUCTS,
                args.getParcelableArrayList(EVENT_RELATED_PRODUCTS));
        bundle.putString(REPORT_TYPE, args.getString(REPORT_TYPE));
        bundle.putDouble(TOTAL_INFLOW, getTotalInflow(salesEvents));
        bundle.putDouble(TOTAL_OUTFLOW, getTotalOutFlow(salesEvents));
        bundle.putFloat(TOTAL_COST, (float) getTotalCost(salesEvents));
        bundle.putFloat(TOTAL_LOSS, (float) getTotalLoss(salesEvents));
        bundle.putFloat(TOTAL_PROFIT, (float) getTotalProfit(salesEvents));
        bundle.putFloat(TOTAL_SALES, (float) getTotalSalesAmount(salesEvents));
        bundle.putString(PROFIT_LOSS_CHART_TITLE,
                profitLossPieChart.getDescription().getText());
        bundle.putString(SALES_COST_CHART_TITLE,
                salesCostPieChart.getDescription().getText());
        getImageStrings().put(salesCostPieChart.getDescription().getText(),
                MediaUtil.encodeBytes(salesCostPieBytes)
        );
        return bundle;
    }

    private byte[] getSalesCostPieBytes() {
        int width = salesCostPieChart.getWidth();
        int height = salesCostPieChart.getHeight();
        return MediaUtil.toByteArray(
                MediaUtil.createBitmap(
                        salesCostPieChart,
                        width,
                        height
                )
        );
    }

}