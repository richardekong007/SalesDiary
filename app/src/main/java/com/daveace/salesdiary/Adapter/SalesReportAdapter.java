package com.daveace.salesdiary.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.SalesEvent;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.SalesReportViewHolder> {


    private List<SalesEvent> salesEvents;

    public SalesReportAdapter(List<SalesEvent> salesEvents) {
        this.salesEvents = salesEvents;
    }

    @NonNull
    @Override
    public SalesReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_periodic_report, parent, false);
        ButterKnife.bind(this, contentView);
        return new SalesReportViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReportViewHolder holder, int position) {
        SalesEvent salesEvent = salesEvents.get(position);
    }

    @Override
    public int getItemCount() {
        return salesEvents.size();
    }

    public class SalesReportViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.salesDate)
        TextView salesDate;
        @BindView(R.id.productName)
        TextView productName;
        @BindView(R.id.productCode)
        TextView productCode;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.quantitySold)
        TextView quantitySold;
        @BindView(R.id.leftOver)
        TextView leftOver;

        public SalesReportViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}