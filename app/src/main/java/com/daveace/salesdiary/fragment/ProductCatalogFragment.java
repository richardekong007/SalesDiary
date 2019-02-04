package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.View;

import com.daveace.salesdiary.Adapter.ProductsAdapter;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.menu.PopupMenuBuilder;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.USERS;

public class ProductCatalogFragment extends BaseFragment implements ProductsAdapter.ProductLongClickListener {

    @BindView(R.id.products)
    RecyclerView productsRecyclerView;
    @BindView(R.id.addButton)
    FloatingActionButton addButton;

    private FirebaseAuth fbAuth;
    private FireStoreHelper fireStoreHelper;

    static final String PRODUCT_BUNDLE = "PRODUCT_BUNDLE";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        fireStoreHelper = FireStoreHelper.getInstance();
        initUI();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_product_catalog;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.catalog_title);
    }

    @Override
    public void setOnItemLongClick(Product product, View view) {
        setupMenu(product, view);
    }

    private void initUI() {
        setupProductRecyclerView();
        addButton.setOnClickListener(view ->
                replaceFragment(new InventoryFragment(), false, null)
        );
    }

    private void setupProductRecyclerView() {
        setLoading(true);
        String userId = fbAuth.getCurrentUser().getUid();
        CollectionReference reference = fireStoreHelper.readDocsFromSubCollection(USERS, userId, PRODUCTS);
        reference
                .get()
                .addOnCompleteListener(task -> {
                    List<Product> products = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Product product = doc.toObject(Product.class);
                            products.add(product);
                        }
                        ProductsAdapter adapter = new ProductsAdapter(products);
                        adapter.setProductLongClickListener(this);
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        productsRecyclerView.setLayoutManager(manager);
                        productsRecyclerView.hasFixedSize();
                        productsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        productsRecyclerView.setAdapter(adapter);
                        setLoading(false);
                    }
                });
    }

    private void setupMenu(Product product, View view) {
        PopupMenu popupMenu = PopupMenuBuilder
                .from(getActivity(), view, R.menu.menu_catalog_action)
                .build();
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.editItem:
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(PRODUCT_BUNDLE, product);
                    replaceFragment(new EditProductFragment(), false, bundle);
                    break;
                case R.id.deleteItem:
                    String userId = fbAuth.getCurrentUser().getUid();
                    fireStoreHelper.delete(USERS, userId, PRODUCTS, product.getId());
                    refresh();//not good enough
                    break;
                case R.id.refreshItem:
                    refresh();//not good enough
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    private void refresh() {
        setupProductRecyclerView();
    }
}
