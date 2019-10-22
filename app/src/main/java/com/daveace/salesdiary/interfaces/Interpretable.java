package com.daveace.salesdiary.interfaces;

import android.content.Context;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.model.SalesSummaryFigureDatum;

import java.util.ArrayList;
import java.util.List;

import static com.daveace.salesdiary.interfaces.Constant.SPACE;
import static java.util.stream.Collectors.toList;

public interface Interpretable extends Summarizable {

    default String getTotalSalesInterpretation(Context ctx, ArrayList<SalesSummaryFigureDatum> data) {
        double totalProfit = data.stream().map(SalesSummaryFigureDatum::getProfit)
                .reduce(0.0, (p1, p2) -> p1 + p2);
        double totalSales = data.stream().map(SalesSummaryFigureDatum::getSalesPrice)
                .reduce(0.0, (s1, s2) -> s1 + s2);
        double totalCost = data.stream().map(SalesSummaryFigureDatum::getCostPrice)
                .reduce(0.0, (c1, c2) -> c1 + c2);
        double qtyOfProfitableProduct = data.stream().reduce((d1, d2) -> d1.getProfit() > d2.getProfit() ? d1 : d2)
                .get().getTotalSales();
        String mostProfitableProduct = getMostProfitableProduct(data);
        return String.format(ctx.getString(R.string.interpretation_template2),
                totalProfit > 0 ? "profit" : "loss",
                totalProfit < 0 ? -(totalProfit) : totalProfit,
                totalSales,
                totalCost,
                qtyOfProfitableProduct,
                mostProfitableProduct);
    }

    default String getRemark(Context ctx, ArrayList<SalesSummaryFigureDatum> data) {
        String mostProfitableProduct = getMostProfitableProduct(data);
        String leastProfitableProduct = getLeastProfitableProduct(data);
        List<String> nonProfitableProducts = getProductsWithLosses(data);
        StringBuilder strBuilder = new StringBuilder();
        if (mostProfitableProduct.length() > 0)
            strBuilder.append(String.format(ctx.getString(R.string.interpretation_remark_on_profit),
                    mostProfitableProduct));
        if (leastProfitableProduct.length() > 0)
            strBuilder.append(String.format(ctx.getString(R.string.interpretation_remark_on_least_profit),
                    leastProfitableProduct));
        if (nonProfitableProducts.size() > 0) {
            strBuilder.append(ctx.getString(R.string.interpretation_remark_on_losses))
                    .append(SPACE);
            nonProfitableProducts.forEach(product -> {
                int index = nonProfitableProducts.indexOf(product);
                int secondPerUltimateIndex = nonProfitableProducts.size() - 2;
                int lastIndex = nonProfitableProducts.size() - 1;
                if (index == 0 && index == lastIndex) {
                    strBuilder.append(String.format("%s.", product));
                } else if (index == lastIndex) {
                    strBuilder.append(String.format(SPACE + "and" + SPACE + "%s", product));
                } else if (index == secondPerUltimateIndex) {
                    strBuilder.append(String.format("%s" + SPACE, product));
                } else {
                    strBuilder.append(String.format("%s," + SPACE, product));
                }
            });
        }
        return strBuilder.toString();
    }

    default String getMostProfitableProduct(ArrayList<SalesSummaryFigureDatum> data) {
        return data.stream().reduce((d1, d2) -> d1.getProfit() > d2.getProfit() ? d1 : d2)
                .get().getProduct();
    }

    default String getLeastProfitableProduct(ArrayList<SalesSummaryFigureDatum> data) {
        return data.stream()
                .filter(datum -> datum.getProfit() > 0)
                .reduce((d1, d2) -> d1.getProfit() < d2.getProfit() ? d1 : d2)
                .get().getProduct();
    }

    default List<String> getProductsWithLosses(ArrayList<SalesSummaryFigureDatum> data) {
        return data.stream()
                .filter(datum -> datum.getProfit() < 0)
                .map(SalesSummaryFigureDatum::getProduct)
                .collect(toList());
    }
}
