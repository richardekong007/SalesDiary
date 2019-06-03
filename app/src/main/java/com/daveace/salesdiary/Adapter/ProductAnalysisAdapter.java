package com.daveace.salesdiary.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SalesEventInterpretation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAnalysisAdapter extends
        RecyclerView.Adapter<ProductAnalysisAdapter.AnalysisViewHolder> {

    private List<SalesEventInterpretation> salesEventInterpretations;

    private OnProductClickListener listener;

    public ProductAnalysisAdapter(List<SalesEventInterpretation> salesEventInterpretations) {
        this.salesEventInterpretations = salesEventInterpretations;
    }

    @NonNull
    @Override
    public ProductAnalysisAdapter.AnalysisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_analysis_content, parent, false);
        ButterKnife.bind(this, view);
        return new ProductAnalysisAdapter.AnalysisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAnalysisAdapter.AnalysisViewHolder holder, int position) {
        SalesEventInterpretation eventInterpretation =
                salesEventInterpretations.get(position);
        holder.productName.setText(eventInterpretation.getProduct().getName());
        holder.itemView.setOnClickListener(view ->
                listener.onProductClick(eventInterpretation));
    }

    @Override
    public int getItemCount() {
        return salesEventInterpretations.size();
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    class AnalysisViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.productName)
        TextView productName;

        AnalysisViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnProductClickListener {
        void onProductClick(SalesEventInterpretation eventInterpretation);
    }
}
