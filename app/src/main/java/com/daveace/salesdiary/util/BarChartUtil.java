package com.daveace.salesdiary.util;

import android.content.Context;

import com.daveace.salesdiary.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment;
import static com.github.mikephil.charting.components.Legend.LegendOrientation;
import static com.github.mikephil.charting.components.Legend.LegendVerticalAlignment;

public class BarChartUtil {

    public static <T extends BarChart> void constructChart(
            T chart,
            String desc,
            List<String> xAxisLabels,
            Map<String, List<Double>> data,
            List<Integer> colors,
            BarDimensionProperties properties) {

        LinkedHashMap<String, List<BarEntry>> groupedEntries = constructBarEntries(data);
        List<BarDataSet> barDataSets = constructDataSets(groupedEntries);
        BarData barData = constructBarData(barDataSets);
        chart.setData(barData);
        setColors(chart.getContext(), barDataSets, colors);
        setData(chart, barData);
        if (properties != null) {
            styleChart(chart, desc, xAxisLabels, properties);
            groupBars(chart,properties);
        }
        chart.invalidate();
    }

    private static LinkedHashMap<String, List<BarEntry>> constructBarEntries(Map<String, List<Double>> data) {
        LinkedHashMap<String, List<BarEntry>> groupedEntries = new LinkedHashMap<>(data.size());
        data.entrySet().forEach(figuresEntry -> {
            List<BarEntry> barEntries = new ArrayList<>();
            figuresEntry.getValue().forEach(figure -> {
                int index = figuresEntry.getValue().indexOf(figure);
                barEntries.add(new BarEntry((float) index, Float.valueOf(String.valueOf(figure))));
            });
            groupedEntries.put(figuresEntry.getKey(), barEntries);
        });
        return groupedEntries;
    }

    private static List<BarDataSet> constructDataSets(Map<String, List<BarEntry>> groupedEntries) {
        List<BarDataSet> barDataSets = new ArrayList<>(groupedEntries.size());
        groupedEntries.entrySet().forEach(entry -> {
            BarDataSet barDataSet = new BarDataSet(entry.getValue(), entry.getKey());
            barDataSets.add(barDataSet);
        });
        return barDataSets;
    }

    private static void setColors(Context context, List<BarDataSet> barDataSets, List<Integer> colors) {
        if (!(barDataSets.size() == colors.size())) return;
        barDataSets.forEach(barDataSet -> barDataSet
                .setColors(new int[]{
                        colors.get(barDataSets.indexOf(barDataSet))
                }, context));
    }

    private static BarData constructBarData(List<BarDataSet> barDataSets) {
        BarDataSet[] barDataSetsArray = barDataSets.toArray(new BarDataSet[0]);
        return new BarData(barDataSetsArray);
    }

    private static <T extends BarChart> void setData(T chart, BarData data) {
        if (data != null) {
            chart.setData(data);
        }
    }

    private static <T extends BarChart> void styleChart(T chart, String desc, List<String> xAxisLabels, BarDimensionProperties properties) {
        setBarWidth(chart, properties.barWidth);
        setDrawValues(chart);
        styleLegend(chart);
        styleAxis(chart, xAxisLabels, properties);
        styleDescription(chart, desc);
        chart.setFitBars(true);
        if (xAxisLabels != null && xAxisLabels.size() > 0) {
            chart.setVisibleXRangeMaximum(xAxisLabels.size());
        }
    }

    private static <T extends BarChart> void groupBars(T chart,BarDimensionProperties properties){
        chart.groupBars(properties.xStartPoint,properties.groupSpace,properties.barSpace);
    }

    private static <T extends BarChart> void setBarWidth(T chart, float barWidth) {
        chart.getData().setBarWidth(barWidth);
    }

    public static <T extends BarChart> void placeLegend(T chart, LegendOrientation orientation, LegendHorizontalAlignment x, LegendVerticalAlignment y) {
        if (chart != null) {
            Legend legend = chart.getLegend();
            legend.setOrientation(orientation);
            legend.setHorizontalAlignment(x);
            legend.setVerticalAlignment(y);
            chart.invalidate();
        }
    }

    private static <T extends BarChart> void setDrawValues(T chart) {
        chart.getData().setDrawValues(false);
    }


    private static <T extends BarChart> void styleLegend(T chart) {
        Legend legend = chart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setTextColor(chart.getContext().getResources().getColor(R.color.colorPrimary, null));
    }

    private static <T extends BarChart> void styleAxis(T chart, List<String> xAxisLabels, BarDimensionProperties properties) {
        styleYAxis(chart, properties.yInterval);
        styleXAxis(chart, getXAxisFormatter(xAxisLabels), properties.xLabelAngle);
    }

    private static <T extends BarChart> void styleYAxis(T chart, float yInterval) {
        LargeValueFormatter formatter = new LargeValueFormatter();
        styleRightYAxis(chart, formatter);
        styleLeftYAxis(chart, formatter, yInterval);
    }

    private static <T extends BarChart> void styleRightYAxis(T chart, LargeValueFormatter formatter) {
        YAxis axisRight = chart.getAxisRight();
        axisRight.setValueFormatter(formatter);
        axisRight.setTextColor(chart.getContext().getResources().getColor(R.color.colorPrimary, null));
        axisRight.setGridColor(chart.getContext().getResources().getColor(R.color.colorPrimary, null));
        axisRight.setGridColor(chart.getContext().getResources().getColor(R.color.colorPrimary, null));
    }

    private static <T extends BarChart> void styleLeftYAxis(T chart, LargeValueFormatter formatter, float yInterval) {
        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setGranularity(yInterval);
        axisLeft.setValueFormatter(formatter);
        axisLeft.setTextColor(chart.getContext().getResources().getColor(R.color.colorPrimary, null));
    }

    private static <T extends BarChart> void styleXAxis(T chart, IAxisValueFormatter valueFormatter, float labelAngle) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelRotationAngle(labelAngle);
        xAxis.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(valueFormatter);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(chart.getContext().getResources().getColor(R.color.colorPrimary, null));
        xAxis.setGridColor(chart.getContext().getResources().getColor(R.color.colorPrimary, null));
    }

    private static <T extends BarChart> void styleDescription(T chart, String str) {
        Description description = new Description();
        description.setText(str);
        description.setTextColor(chart.getContext().getResources().getColor(R.color.colorPrimary, null));
        chart.setDescription(description);
    }

    private static IAxisValueFormatter getXAxisFormatter(List<String> xAxisLabels) {
        if (xAxisLabels != null && xAxisLabels.size() > 0) {
            Object[] labels = xAxisLabels.toArray();
            return (value, axis) -> String.valueOf(labels[(int) value]);
        }
        return new IndexAxisValueFormatter();
    }

    public static class BarDimensionProperties {
        final float barWidth;
        final float xStartPoint;
        final float groupSpace;
        final float barSpace;
        final float xLabelAngle;
        final float yInterval;

        BarDimensionProperties(final Builder builder) {
            this.barWidth = builder.barWidth;
            this.xStartPoint = builder.xStartPoint;
            this.groupSpace = builder.groupSpace;
            this.barSpace = builder.barSpace;
            this.xLabelAngle = builder.xLabelAngle;
            this.yInterval = builder.yInterval;
        }

        public static class Builder {
            float barWidth;
            float xStartPoint;
            float groupSpace;
            float barSpace;
            float xLabelAngle;
            float yInterval;

            public Builder setBarWidth(final float barWidth) {
                this.barWidth = barWidth;
                return this;
            }

            public Builder setXStartPoint(final float xStartPoint) {
                this.xStartPoint = xStartPoint;
                return this;
            }

            public Builder setGroupSpace(final float groupSpace) {
                this.groupSpace = groupSpace;
                return this;
            }

            public Builder setBarSpace(final float barSpace) {
                this.barSpace = barSpace;
                return this;
            }

            public Builder setXLabelAngle(final float xLabelAngle) {
                this.xLabelAngle = xLabelAngle;
                return this;
            }

            public Builder setYInterval(final float yInterval) {
                this.yInterval = yInterval;
                return this;
            }

            public BarDimensionProperties build() {
                return new BarDimensionProperties(this);
            }

        }

    }

}
