package com.daveace.salesdiary.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.Customer;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.SalesReportViewHolder> {


    private List<SalesEvent> salesEvents;
    private List<Product> relatedProducts;
    private List<Customer> relatedCustomers;
    private static final String DATE_FORMAT = "MMM d, y";


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
        try{
            SalesEvent salesEvent = salesEvents.get(position);
            Product relatedProduct = relatedProducts.get(position);
            //Customer relatedCustomer = relatedCustomers.get(position);
            holder.salesDate.setText(new SimpleDateFormat(
                    DATE_FORMAT, Locale.getDefault()).format(salesEvent.getDate()));
            holder.productName.setText(relatedProduct.getName());
            Glide.with(ctx).load(productDrawable).into(holder.productImage);
            holder.moreButton.setOnClickListener(view->{
                //display full details of sales event in the dialog
            });
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void setRelatedProducts(List<Product> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public void setRelatedCustomer(List<Customer> relatedCustomers){
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

}