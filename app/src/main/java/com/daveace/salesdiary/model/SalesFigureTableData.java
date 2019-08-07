package com.daveace.salesdiary.model;

public class SalesFigureTableData {

        private int id;
        private String product;
        private double profit;
        private double salesPrice;
        private double costPrice;

        private SalesFigureTableData(final Builder builder) {
            this.id = builder.id;
            this.product = builder.product;
            this.profit = builder.profit;
            this.salesPrice = builder.salesPrice;
            this.costPrice = builder.costPrice;
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

    public static class Builder{
            private int id;
            private String product;
            private double profit;
            private double salesPrice;
            private double costPrice;

            public Builder setId(int id){
                this.id = id;
                return this;
            }

            public Builder setProduct(String product){
                this.product = product;
                return this;
            }

            public Builder setProfit(double profit){
                this.profit = profit;
                return this;
            }

            public Builder setSalesPrice(double salesPrice){
                this.salesPrice = salesPrice;
                return this;
            }

            public Builder setCostPrice(double costPrice){
                this.costPrice = costPrice;
                return this;
            }

            public SalesFigureTableData build(){
                SalesFigureTableData tableData = new SalesFigureTableData(this);
                if (tableData.id == 0) throw new IllegalStateException("id must no be zero");
                if (tableData.product == null) throw new IllegalStateException("product must not be null");
                if (tableData.profit == 0.0) throw new IllegalStateException("Profit must not be zero");
                if (tableData.salesPrice == 0.0) throw new IllegalStateException("Sale Price must not be zero");
                if (tableData.costPrice == 0.0) throw new IllegalStateException("Cost price must not be zero");
                return tableData;
            }
        }
    }