package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.REPORT_TYPE;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORTS;

public class CashFlowFragment extends BaseFragment implements BackIconActionBarMarker {

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

    private PieChart saleCostPieChart;
    private PieChart profitLossPieChart;
    private Bundle args;
    private String reportHeader;

    private static final String SPACE = "\u0020";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        args = getArguments();
        profitLossPieChart = view.findViewById(R.id.profit_loss_pie);
        saleCostPieChart = view.findViewById(R.id.sales_cost_pie);
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
        if (args == null) {
            return;
        }
        reportHeader = Objects.requireNonNull(args.getString(REPORT_TYPE))
                .concat(getString(R.string.cash_flow));
        List<SalesEvent> salesEvents = args.getParcelableArrayList(SALES_EVENTS_REPORTS);
        double totalInflow = getTotalInflow(salesEvents);
        double totalOutFlow = getTotalOutFlow(salesEvents);
        float totalProfit = (float) getTotalProfit(Objects.requireNonNull(salesEvents));
        float totalLoss = (float) getTotalLoss(Objects.requireNonNull(salesEvents));
        float totalSales = (float) getTotalSalesAmount(salesEvents);
        float totalCost = (float) getTotalCost(salesEvents);
        List<PieEntry> profitLossEntries = new ArrayList<>();
        Collections.addAll(profitLossEntries,
                new PieEntry(getPercentage(totalProfit, totalLoss), getString(R.string.profitLabel)),
                new PieEntry(getPercentage(totalLoss, totalProfit), getString(R.string.lossLabel)));
        List<PieEntry> salesCostEntries = new ArrayList<>();
        Collections.addAll(salesCostEntries,
                new PieEntry(getPercentage(totalSales, totalCost), getString(R.string.salesLabel)),
                new PieEntry(getPercentage(totalCost, totalSales), getString(R.string.costLabel)));
        constructPieChart(profitLossPieChart, profitLossEntries,
                getString(R.string.profit_and_loss), R.color.blue, R.color.red);
        profitLossPieChart.setCenterText(getString(R.string.profit_and_loss));
        constructPieChart(saleCostPieChart, salesCostEntries,
                getString(R.string.sales_and_cost), R.color.green, R.color.orange);
        saleCostPieChart.setCenterText(getString(R.string.sales_and_cost));
        inflowText.setText(String.valueOf(totalInflow));
        outflowText.setText(String.valueOf(totalOutFlow));
        salesText.setText(String.valueOf(totalSales));
        costText.setText(String.valueOf(totalCost));
        profitText.setText(String.valueOf(totalProfit));
        lossText.setText(String.valueOf(totalLoss));

        moreButton.setOnClickListener(view->
            replaceFragment(new SummaryFragment(),true, args));

    }


    private void constructPieChart(PieChart pieChart, List<PieEntry> entries, String dataSetLabel, int... colors) {
        PieDataSet pieDataSet = new PieDataSet(entries, dataSetLabel);
        Description desc = new Description();
        pieChart.setDescription(desc);
        desc.setText(dataSetLabel);
        Legend pieChartLegend = pieChart.getLegend();
        pieDataSet.setValueFormatter(new PercentFormatter());
        PieData data = new PieData(pieDataSet);
        pieChart.setData(data);
        styleDescription(desc);
        pieChart.setCenterTextColor(getResources().getColor(R.color.colorPrimary, null));
        styleLegend(pieChartLegend);
        if (colors.length > 0) {
            pieDataSet.setColors(colors, getActivity());
        }
        pieChart.invalidate();

    }

    private void styleDescription(Description desc){
        desc.setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private void styleLegend(Legend pieChartLegend) {
        pieChartLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        pieChartLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        pieChartLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        pieChartLegend.setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private double getTotalProfit(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(events -> events.getSalesPrice() - events.getCostPrice())
                .filter(profits -> profits > 0)
                .reduce(0.0, (initProfit, nextProfit) -> initProfit + nextProfit);
    }

    private double getTotalLoss(List<SalesEvent> salesEvents) {
        double totalLoss = salesEvents.stream()
                .map(event -> event.getSalesPrice() - event.getCostPrice())
                .filter(amount -> amount < 0)
                .reduce(0.0, (initAmount, nextAmount) -> initAmount + nextAmount);
        return (totalLoss < 0) ? -(totalLoss) : totalLoss;
    }

    private double getTotalCost(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(SalesEvent::getCostPrice)
                .reduce(0.0, (initCost, nextCost) -> initCost + nextCost);
    }

    private double getTotalSalesAmount(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(SalesEvent::getSalesPrice)
                .reduce(0.0, (initPrice, nextPrice) -> initPrice + nextPrice);
    }

    private double getTotalOutFlow(List<SalesEvent> salesEvents) {
        return getTotalCost(salesEvents) + getTotalLoss(salesEvents);
    }

    private double getTotalInflow(List<SalesEvent> salesEvents) {
        return getTotalSalesAmount(salesEvents) - getTotalLoss(salesEvents);
    }

    private float getPercentage(double amount, double quotient) {
        float percentage = 0f;
        try {
            percentage = (float) (amount / (amount + quotient) * 100);
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return percentage;
    }

}
