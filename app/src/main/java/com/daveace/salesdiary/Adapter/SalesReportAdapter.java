package com.daveace.salesdiary.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.dialog.SalesEventDetailsDialog;
import com.daveace.salesdiary.entity.Customer;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.fragment.ReportPickerFragment;
import com.daveace.salesdiary.interfaces.Constant;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.daveace.salesdiary.interfaces.Constant.EVENTS_RELATED_CUSTOMER;
import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENT_DATE_FORMAT;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.SalesReportViewHolder> {


    private List<SalesEvent> salesEvents;
    private List<Product> relatedProducts;
    private List<Customer> relatedCustomers;
    private MoreClickListener moreClickListener;

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
        Context ctx = holder.itemView.getContext();
        Drawable productDrawable = ctx.getResources().getDrawable(R.mipmap.stock, null);
        try {
            SalesEvent salesEvent = salesEvents.get(position);
            Product relatedProduct = relatedProducts.get(position);
            Customer relatedCustomer = relatedCustomers.get(position);
            holder.salesDate.setText(new SimpleDateFormat(
                    SALES_EVENT_DATE_FORMAT, Locale.getDefault()).format(salesEvent.getDate()));
            holder.productName.setText(relatedProduct.getName());
            Glide.with(ctx).load(productDrawable).into(holder.productImage);
            holder.moreButton.setOnClickListener(view -> {
                moreClickListener.onClick(salesEvent, relatedProduct, relatedCustomer);
            });
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void setRelatedProducts(List<Product> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public void setMoreClickListener(MoreClickListener listener) {
        moreClickListener = listener;
    }

    public void setRelatedCustomer(List<Customer> relatedCustomers) {
        this.relatedCustomers = relatedCustomers;
    }

    @Override
    public int getItemCount() {
        return salesEvents.size();
    }

    class SalesReportViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.productImage)
        ImageView productImage;
        @BindView(R.id.salesDate)
        TextView salesDate;
        @BindView(R.id.productName)
        TextView productName;
        @BindView(R.id.moreButton)
        AppCompatButton moreButton;

        private SalesReportViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface MoreClickListener {
        void onClick(SalesEvent event, Product relatedProduct, Customer relatedCustomer);
    }

}
