package com.daveace.salesdiary.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.daveace.salesdiary.Adapter.ProductsAdapter;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.USERS;

public class ProductCatalogFragment extends BaseFragment implements ProductsAdapter.OnProductClickListener {

    @BindView(R.id.products)
    RecyclerView productsRecyclerView;
    @BindView(R.id.addButton)
    FloatingActionButton addButton;
    private ProductsAdapter adapter;

    static final String PRODUCT_BUNDLE = "PRODUCT_BUNDLE";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_catalog_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(Objects.requireNonNull(manager).getSearchableInfo(getActivity()
                .getComponentName()));
        customizeSearchView(searchView);
        listenForQueryText(searchView);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onProductClick(Product product, View view) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_BUNDLE, product);
        replaceFragment(new EditProductFragment(), false, bundle);
    }

    private void customizeSearchView(SearchView searchView) {
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        ImageView searchCloseIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(getResources().getColor(R.color.white, null));
        searchCloseIcon.setColorFilter(getResources().getColor(R.color.white, null));
        searchView.setQueryHint(getString(R.string.search_catalog));
    }

    private void listenForQueryText(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void initUI() {
        loadProducts();
        addButton.setOnClickListener(view ->
                replaceFragment(new InventoryFragment(), false, null)
        );
    }

    private void loadProducts() {
        setLoading(true);
        String userId = getUserId();
        CollectionReference reference = getFireStoreHelper().readDocsFromSubCollection(USERS, userId, PRODUCTS);
        reference
                .get()
                .addOnCompleteListener(task -> {
                    List<Product> products = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Product product = doc.toObject(Product.class);
                            if (Objects.requireNonNull(product).isAvailable()) {
                                products.add(product);
                            }
                        }
                        setupRecycleView(products);
                        setLoading(false);
                    }
                });
    }

    private void setupRecycleView(List<Product> products) {
        adapter = new ProductsAdapter(products);
        adapter.setProductLongClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        productsRecyclerView.setLayoutManager(manager);
        productsRecyclerView.hasFixedSize();
        productsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productsRecyclerView.setAdapter(adapter);
    }
}