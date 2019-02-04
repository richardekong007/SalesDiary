package com.daveace.salesdiary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductAdapterViewHolder> {

    private List<Product> products;

    private ProductLongClickListener productLongClickListener;

    public ProductsAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View contentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_detail, parent, false);
        ButterKnife.bind(this, contentView);
        return new ProductAdapterViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterViewHolder holder, int position) {
        Context ctx = holder.itemView.getContext();
        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.priceElement.setText(String.valueOf(product.getCost()));
        holder.stockElement.setText(String.valueOf(product.getStock()));
        Glide.with(ctx).load(ctx.getResources().getDrawable(R.mipmap.stock))
                .into(holder.productImageElement);
        holder.itemLayout.setOnLongClickListener(view -> {
            productLongClickListener.setOnItemLongClick(product,holder.itemLayout);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProductLongClickListener(ProductLongClickListener listener) {
        this.productLongClickListener = listener;
    }

    public class ProductAdapterViewHolder extends RecyclerView.ViewHolder {
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
        public ProductAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ProductLongClickListener {
        void setOnItemLongClick(Product product,View view);
    }
}
