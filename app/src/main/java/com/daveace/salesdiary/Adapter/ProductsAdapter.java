package com.daveace.salesdiary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.R;

import static android.view.View.VISIBLE;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductItemViewHolder>
        implements Filterable {

    private List<Product> products;
    private List<Product> filteredProducts;
    private OnProductClickListener productLongClickListener;


    public ProductsAdapter(List<Product> products) {
        this.products = products;
        this.filteredProducts = products;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_detail, parent, false);
        ButterKnife.bind(this, contentView);
        return new ProductItemViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductItemViewHolder holder, int position) {
        Product product = filteredProducts.get(position);
        Context ctx = holder.itemView.getContext();
        holder.productName.setText(product.getName());
        holder.priceElement.setText(String.valueOf(product.getCost()));
        holder.stockElement.setText(String.valueOf(product.getStock()));
        Glide.with(ctx).load(ctx.getResources().getDrawable(R.mipmap.stock, null))
                .into(holder.productImageElement);
        if (!product.isAvailable()) {
            holder.statusTextView.setVisibility(VISIBLE);
            holder.statusTextView.setText(
                    ctx.getString(R.string.out_of_stock_hint)
            );
            holder.statusTextView.setTextColor(
                    ctx.getColor(R.color.red)
            );
        }
        holder.itemLayout.setOnClickListener(view ->
                productLongClickListener.onProductClick(product, holder.itemLayout)
        );
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchValue = constraint.toString();
                if (searchValue.isEmpty()) {
                    filteredProducts = products;
                } else {
                    List<Product> theFilteredProducts = new ArrayList<>();
                    for (Product product : products) {
                        String productName = product.getName();
                        if (productName.contains(searchValue)
                                || productName.toLowerCase().contains(searchValue)
                                || productName.toUpperCase().contains(searchValue))
                            theFilteredProducts.add(product);
                    }

                    filteredProducts = theFilteredProducts;

                }
                FilterResults results = new FilterResults();
                results.values = filteredProducts;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredProducts = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setProductLongClickListener(OnProductClickListener listener) {
        this.productLongClickListener = listener;
    }

    class ProductItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemLayout)
        LinearLayout itemLayout;
        @BindView(R.id.productImageElement)
        ImageView productImageElement;
        @BindView(R.id.productElement)
        TextView productName;
        @BindView(R.id.priceElement)
        TextView priceElement;
        @BindView(R.id.stockElement)
        TextView stockElement;
        @BindView(R.id.status_text)
        TextView statusTextView;

        ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product, View view);
    }
}
