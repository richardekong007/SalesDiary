package com.daveace.salesdiary.util;

import com.daveace.salesdiary.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.mikephil.charting.components.Legend.LegendVerticalAlignment;
import static com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment;
import static com.github.mikephil.charting.components.Legend.LegendOrientation;

public class PieChartUtil {

    public static void constructPieChart(PieChart chart, String description, Map<String, Float> data, int... colors) {
        Description desc = new Description();
        int centerTextColor = chart.getContext().getColor(R.color.colorPrimary);
        desc.setText(description);
        setPieData(chart, data, description);
        styleDescription(chart, desc);
        stylePieLegend(chart);
        paintPieChart(chart, ((PieDataSet) chart.getData().getDataSet()), colors);
        chart.setDescription(desc);
        chart.setCenterText(desc.getText());
        chart.setCenterTextColor(centerTextColor);
        chart.invalidate();
    }

    private static void setPieData(PieChart chart, Map<String, Float> data, String description) {
        List<PieEntry> entries = new ArrayList<>();
        data.entrySet().forEach(entry -> entries.add(
                new PieEntry(entry.getValue(), entry.getKey())
        ));
        PieDataSet dataSet = new PieDataSet(entries, description);
        dataSet.setValueFormatter(new PercentFormatter());
        PieData pieData = new PieData(dataSet);
        chart.setData(pieData);
    }

    private static void paintPieChart(PieChart chart, PieDataSet dataSet, int... colors) {
        dataSet.setColors(colors, chart.getContext());
    }

    private static void stylePieLegend(PieChart chart) {
        Legend legend;
        if (chart != null) {
            legend = chart.getLegend();
            legend.setTextColor(chart.getContext().getResources()
                    .getColor(R.color.colorPrimary, null));
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        }
    }

    public static void positionLegend(PieChart chart, LegendOrientation orientation,
                                   LegendHorizontalAlignment x, LegendVerticalAlignment y) {
        if (chart != null){
            Legend legend = chart.getLegend();
            legend.setOrientation(orientation);
            legend.setHorizontalAlignment(x);
            legend.setVerticalAlignment(y);
            chart.invalidate();
        }
    }

    private static void styleDescription(PieChart chart, Description desc) {
        if (chart != null) {
            desc.setTextColor(chart.getContext().getResources()
                    .getColor(R.color.colorPrimary, null));
        }
    }
}
