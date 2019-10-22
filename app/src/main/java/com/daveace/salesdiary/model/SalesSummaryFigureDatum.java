package com.daveace.salesdiary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class SalesSummaryFigureDatum implements Parcelable {

    private int id;
    private String product;
    private double profit;
    private double salesPrice;
    private double costPrice;
    private double totalSales;
    private double left;
    private Date mostRecentDate;

    public static Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new SalesSummaryFigureDatum(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new SalesSummaryFigureDatum[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.product);
        dest.writeDouble(this.profit);
        dest.writeDouble(this.costPrice);
        dest.writeDouble(this.salesPrice);
        dest.writeDouble(this.totalSales);
        dest.writeDouble(this.left);
        dest.writeLong(this.mostRecentDate.getTime());
    }

    private SalesSummaryFigureDatum(Parcel source) {
        this.id = source.readInt();
        this.product = source.readString();
        this.profit = source.readDouble();
        this.costPrice = source.readDouble();
        this.salesPrice = source.readDouble();
        this.totalSales = source.readDouble();
        this.left = source.readDouble();
        this.mostRecentDate = new Date(source.readLong());
    }

    private SalesSummaryFigureDatum(final Builder builder) {
        this.id = builder.id;
        this.product = builder.product;
        this.profit = builder.profit;
        this.salesPrice = builder.salesPrice;
        this.costPrice = builder.costPrice;
        this.totalSales = builder.totalSales;
        this.left = builder.left;
        this.mostRecentDate = builder.mostRecentDate;
    }

    public SalesSummaryFigureDatum setId(int id) {
        if (id == 0) throw new IllegalStateException("id must no be zero");
        this.id = id;
        return this;
    }

    public SalesSummaryFigureDatum setProduct(String product) {
        this.product = product;
        return this;
    }

    public SalesSummaryFigureDatum setProfit(double profit) {
        this.profit = profit;
        return this;
    }

    public SalesSummaryFigureDatum setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
        return this;
    }

    public SalesSummaryFigureDatum setCostPrice(double costPrice) {
        this.costPrice = costPrice;
        return this;
    }

    public SalesSummaryFigureDatum setTotalSales(double totalSales) {
        this.totalSales = totalSales;
        return this;
    }

    public SalesSummaryFigureDatum setLeft(double left) {
        this.left = left;
        return this;
    }

    public SalesSummaryFigureDatum setMostRecentDate(Date mostRecentDate) {
        this.mostRecentDate = mostRecentDate;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public double getProfit() {
        return profit;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public double getLeft() {
        return left;
    }

    public Date getMostRecentDate() {
        return mostRecentDate;
    }

    public static class Builder {
        private int id;
        private String product;
        private double profit;
        private double salesPrice;
        private double costPrice;
        private double totalSales;
        private double left;
        private Date mostRecentDate;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setProduct(String product) {
            this.product = product;
            return this;
        }

        public Builder setProfit(double profit) {
            this.profit = profit;
            return this;
        }

        public Builder setSalesPrice(double salesPrice) {
            this.salesPrice = salesPrice;
            return this;
        }

        public Builder setCostPrice(double costPrice) {
            this.costPrice = costPrice;
            return this;
        }

        public Builder setTotalSales(double totalSales) {
            this.totalSales = totalSales;
            return this;
        }

        public Builder setLeft(double left) {
            this.left = left;
            return this;
        }

        public Builder setMostRecentDate(Date mostRecentDate) {
            this.mostRecentDate = mostRecentDate;
            return this;
        }

        public SalesSummaryFigureDatum build() {
            SalesSummaryFigureDatum tableData = new SalesSummaryFigureDatum(this);
            if (tableData.id == 0)
                throw new IllegalStateException("id must no be zero");
            if (tableData.product == null)
                throw new IllegalStateException("product must not be null");
            if (tableData.profit == 0.0)
                throw new IllegalStateException("Profit must not be zero");
            if (tableData.salesPrice == 0.0)
                throw new IllegalStateException("Sale Price must not be zero");
            if (tableData.costPrice == 0.0)
                throw new IllegalStateException("Cost price must not be zero");
            return tableData;
        }
    }
}