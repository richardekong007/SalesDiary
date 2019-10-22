package com.daveace.salesdiary.interfaces;

import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.model.SalesSummaryFigureDatum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public interface Summarizable {

    default List<String> getProductNames(List<SalesSummaryFigureDatum> data) {
        return data.stream().map(SalesSummaryFigureDatum::getProduct).collect(toList());
    }

    default List<Double> getCostFigures(List<SalesSummaryFigureDatum> data) {
        return data.stream()
                .map(SalesSummaryFigureDatum::getCostPrice)
                .collect(toList());
    }

    default List<Double> getSalesFigures(List<SalesSummaryFigureDatum> data) {
        return data.stream()
                .map(SalesSummaryFigureDatum::getSalesPrice)
                .collect(toList());
    }

    default List<Double> getProfitFigures(List<SalesSummaryFigureDatum> data) {
        return data.stream()
                .map(datum -> datum.getSalesPrice() - datum.getCostPrice())
                .collect(toList());
    }

    default double getTotalProfit(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(events -> events.getSalesPrice() - events.getCostPrice())
                .filter(profits -> profits > 0)
                .reduce(0.0, (initProfit, nextProfit) -> initProfit + nextProfit);
    }

    default double getTotalLoss(List<SalesEvent> salesEvents) {
        double totalLoss = salesEvents.stream()
                .map(event -> event.getSalesPrice() - event.getCostPrice())
                .filter(amount -> amount < 0)
                .reduce(0.0, (initAmount, nextAmount) -> initAmount + nextAmount);
        return (totalLoss < 0) ? -(totalLoss) : totalLoss;
    }

    default double getTotalCost(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(SalesEvent::getCostPrice)
                .reduce(0.0, (initCost, nextCost) -> initCost + nextCost);
    }

    default double getTotalSalesAmount(List<SalesEvent> salesEvents) {
        return salesEvents.stream()
                .map(SalesEvent::getSalesPrice)
                .reduce(0.0, (initPrice, nextPrice) -> initPrice + nextPrice);
    }

    default double getTotalOutFlow(List<SalesEvent> salesEvents) {
        return getTotalCost(salesEvents) + getTotalLoss(salesEvents);
    }

    default double getTotalInflow(List<SalesEvent> salesEvents) {
        return getTotalSalesAmount(salesEvents) - getTotalLoss(salesEvents);
    }


    default ArrayList<SalesSummaryFigureDatum> getSalesSummaryFigureData(List<SalesEvent> events, List<Product> products) {

        ArrayList<SalesSummaryFigureDatum> data = new ArrayList<>();
        events.forEach(event -> products.forEach(product -> {
            if (event.getProductId().equals(product.getId())) {
                SalesSummaryFigureDatum datum = new SalesSummaryFigureDatum.Builder()
                        .setId(events.indexOf(event) + 1)
                        .setProduct(product.getName())
                        .setProfit(event.getSalesPrice() - event.getCostPrice())
                        .setSalesPrice(event.getSalesPrice())
                        .setCostPrice(event.getCostPrice())
                        .setTotalSales(event.getSales())
                        .setLeft(event.getLeft())
                        .setMostRecentDate(event.getDate())
                        .build();
                data.add(datum);
            }
        }));
        return mergeByProduct(data);
    }

    default void sortByProduct(List<SalesSummaryFigureDatum> data) {
        Collections.sort(data, (datum1, datum2) -> datum1.getProduct().compareTo(datum2.getProduct()));
    }

    default SalesSummaryFigureDatum findMostRecentDuplicateDataByProduct(SalesSummaryFigureDatum datum, ArrayList<SalesSummaryFigureDatum> data) {
        return data.stream()
                .filter(datum1 -> datum1.getProduct().equals(datum.getProduct()))
                .max((d1, d2) -> d1.getMostRecentDate().compareTo(d2.getMostRecentDate()))
                .get();
    }

    default ArrayList<SalesSummaryFigureDatum> mergeByProduct(ArrayList<SalesSummaryFigureDatum> data) {
        ArrayList<Integer> duplicateIndices = new ArrayList<>();
        sortByProduct(data);
        data.forEach(datum -> {
            int index = data.indexOf(datum);
            int prevIndex = index - 1;
            if (prevIndex >= 0 && prevIndex < data.size()) {
                SalesSummaryFigureDatum prevDatum = data.get(prevIndex);
                if (datum.getProduct().equals(prevDatum.getProduct())) {
                    duplicateIndices.add(prevIndex);
                    SalesSummaryFigureDatum mostRecentDatum = findMostRecentDuplicateDataByProduct(datum, data);
                    datum.setCostPrice(datum.getCostPrice() + prevDatum.getCostPrice())
                            .setSalesPrice(datum.getSalesPrice() + prevDatum.getSalesPrice())
                            .setProfit(datum.getProfit() + prevDatum.getProfit())
                            .setTotalSales(datum.getTotalSales() + prevDatum.getTotalSales())
                            .setLeft(mostRecentDatum.getLeft())
                            .setMostRecentDate(mostRecentDatum.getMostRecentDate());
                }
            }
        });
        removeDuplicates(data, duplicateIndices);
        assignIds(data);
        return data;
    }

    default void removeDuplicates(ArrayList<SalesSummaryFigureDatum> data, ArrayList<Integer> indices) {
        data.removeAll(indices.stream().map(data::get).collect(toList()));
    }

    default void assignIds(ArrayList<SalesSummaryFigureDatum> data) {
        data.forEach(datum -> datum.setId(data.indexOf(datum) + 1));
    }

    default float getPercentageValue(float value1, float value2) {
        return (value1 / (value1 + value2)) * 100;
    }

}
