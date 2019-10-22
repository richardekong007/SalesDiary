package com.daveace.salesdiary;

import android.content.Context;

import com.daveace.salesdiary.model.SalesSummaryFigureDatum;

import static com.daveace.salesdiary.interfaces.Constant.SPACE;

public class SalesSummaryInterpretation {

    private Context context;
    private String interpretation;
    private SalesSummaryFigureDatum datum;

    private static final String LOSS = "loss\u0020";
    private static final String PROFIT = "profit\u0020";

    private SalesSummaryInterpretation(final Builder builder){
        this.context = builder.context;
        this.datum = builder.datum;
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public Context getContext() {
        return context;
    }

    public SalesSummaryFigureDatum getDatum() {
        return datum;
    }

    public SalesSummaryInterpretation interpret(){

        double profit = datum.getSalesPrice() - datum.getCostPrice();
        double projectedSales = datum.getCostPrice()/datum.getTotalSales() * datum.getLeft();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(
                this.context.getString(R.string.interpret_template),
                datum.getProduct(),
                profit < 0 ? LOSS : PROFIT,
                profit < 0 ? -(profit) : profit,
                datum.getTotalSales(),
                datum.getSalesPrice(),
                datum.getCostPrice()));
        if (datum.getLeft() > 0) {
            stringBuilder.append(SPACE)
                    .append(String.format(
                            this.context.getString(R.string.available_status_interpretation_template),
                            datum.getLeft(),
                            projectedSales));
        }
        setInterpretation(stringBuilder.toString());
        return this;
    }

    public String getInterpretation(){
        return this.interpretation;
    }

    public static class Builder{
        private Context context;
        private SalesSummaryFigureDatum datum;

        public Builder setContext(Context context){
            this.context = context;
            return this;
        }

        public Builder setDatum(SalesSummaryFigureDatum datum){
            this.datum = datum;
            return this;
        }

        public SalesSummaryInterpretation build(){
            SalesSummaryInterpretation theDatum = new SalesSummaryInterpretation(this);
            if (theDatum.context == null)
                throw new IllegalArgumentException("Context must be not null");
            if (theDatum.datum == null)
                throw new IllegalArgumentException("Datum must be not null");
            return theDatum;
        }
    }
}
