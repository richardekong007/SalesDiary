package com.daveace.salesdiary;

import android.content.Context;

import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;


public class SalesEventInterpretation  {

    private Context ctx;
    private String interpretation;
    private SalesEvent salesEvent;
    private Product product;

    private static final String LOSS = "loss\u0020";
    private static final String PROFIT = "profit\u0020";


    private SalesEventInterpretation(final Builder builder) {
        ctx = builder.ctx;
        salesEvent = builder.salesEvent;
        product = builder.product;
    }

    private void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public String getInterpretation() {
        return this.interpretation;
    }

    public SalesEvent getSalesEvent() {
        return this.salesEvent;
    }

    public Product getProduct() {
        return this.product;
    }

    public void interpret() {
        double profit = salesEvent.getSalesPrice() - salesEvent.getCostPrice();
        setInterpretation(
                String.format(
                        this.ctx.getString(R.string.interpret_template),
                        product.getName(),
                        profit < 0 ? LOSS : PROFIT,
                        profit < 0 ? -(profit):profit,
                        salesEvent.getSales(),
                        salesEvent.getSalesPrice(),
                        salesEvent.getCostPrice()
                ));
    }

    public static class Builder {
        private Context ctx;
        private SalesEvent salesEvent;
        private Product product;

        public Builder setContext(final Context ctx) {
            this.ctx = ctx;
            return this;
        }

        public Builder setSalesEvent(final SalesEvent salesEvents) {
            this.salesEvent = salesEvents;
            return this;
        }

        public Builder setProduct(final Product product) {
            this.product = product;
            return this;
        }

        public SalesEventInterpretation build() {
            SalesEventInterpretation salesEventInterpretation =
                    new SalesEventInterpretation(this);
            if (salesEventInterpretation.ctx == null)
                throw new IllegalStateException("Context must not be null");
            if (salesEventInterpretation.salesEvent == null)
                throw new IllegalStateException("SalesEvents must not be null");
            if (salesEventInterpretation.product == null)
                throw new IllegalStateException("Products must not be null");
            return salesEventInterpretation;
        }

    }

}
