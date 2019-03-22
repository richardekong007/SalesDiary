package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SubCollectionPath;
import com.daveace.salesdiary.dialog.RecordCustomerDialog;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.SALESEVENTS;
import static com.daveace.salesdiary.interfaces.Constant.USERS;
import static com.daveace.salesdiary.util.StringUtil.fieldsAreValid;

public class RecordSalesFragment extends BaseFragment implements RecordCustomerDialog.OnDoneClickListener {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.quantityLeft)
    TextView quantityLeftTextView;
    @BindView(R.id.quantitySold)
    TextView quantitySoldTextView;
    @BindView(R.id.cost)
    TextView costTextView;
    @BindView(R.id.product)
    TextInputEditText productInputText;
    @BindView(R.id.productCode)
    TextInputEditText productCodeInputText;
    @BindView(R.id.quantity)
    TextInputEditText quantityInputText;
    @BindView(R.id.price)
    TextInputEditText priceInputText;
    @BindView(R.id.customerInfoChip)
    Chip customerChip;
    @BindView(R.id.recordButton)
    AppCompatButton recordButton;

    private FirebaseAuth fbAuth;
    private FireStoreHelper fireStoreHelper;
    private PopupMenu productMenu;
    private List<Product> products;
    private Product selectedProduct;
    private String userId;
    private String customerId;
    private int selectedProductIndex;
    private double recordedQuantity;
    private double recordedPrice;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        fireStoreHelper = FireStoreHelper.getInstance();
        setupContentView();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_record_sales;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.record_sales_title);
    }

    @Override
    public void passCustomerId(String id) {
        customerId = id;
    }


    private void recordSalesEvent() {
        //save sales record and customer details to the database
        if (selectedProduct == null){
            Snackbar.make(rootView,getString(R.string.select_product),Snackbar.LENGTH_LONG).show();
            return;
        }

        double quantityLeft = selectedProduct.getStock();
        String productId = selectedProduct.getId();

        if (!fieldsAreValid(getActivity(), priceInputText, quantityInputText))
            return;

        if (selectedProduct.getStock() >= recordedQuantity && recordedQuantity > 0) {
            quantityLeft -= recordedQuantity;
        } else {
            Snackbar.make(rootView, getString(R.string.invalid_sales_quantity), Snackbar.LENGTH_LONG).show();
            return;
        }
        if (customerId == null) {
            Snackbar.make(rootView, getString(R.string.no_customer_flag), Snackbar.LENGTH_LONG).show();
            return;
        }
        selectedProduct.setStock(quantityLeft);
        SalesEvent salesEvent = SalesEvent.getInstance(productId, userId, customerId, recordedPrice,
                recordedQuantity, quantityLeft, new Date(), null);
        fireStoreHelper.update(USERS, userId, PRODUCTS, productId, selectedProduct);
        SubCollectionPath metaData =
                new SubCollectionPath(USERS, userId, SALESEVENTS, salesEvent.getId(), salesEvent);
        fireStoreHelper.addDocumentToSubCollection(metaData, rootView);

        if (selectedProduct.getStock() < 1.0) {
            fireStoreHelper.delete(USERS, userId, PRODUCTS, productId);
            Snackbar.make(rootView, selectedProduct.getName() + getString(R.string.out_of_stock), Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    private void setupContentView() {
        setupProductMenu();
        productInputText.setShowSoftInputOnFocus(false);
        productInputText.setOnClickListener(view -> productMenu.show());

        productMenu.setOnMenuItemClickListener(item -> {
            selectedProductIndex = item.getItemId();
            selectedProduct = products.get(selectedProductIndex);
            productInputText.setText(selectedProduct.getName());
            quantityLeftTextView.setText(String.valueOf(selectedProduct.getStock()));
            costTextView.setText(String.valueOf(selectedProduct.getCost()));
            productCodeInputText.setText(products.get(selectedProductIndex).getCode());
            displayFigures(getTotalSales(recordedQuantity, recordedPrice), quantitySoldTextView);
            return true;
        });
        quantityInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                displayFigures(selectedProduct.getStock(), quantityLeftTextView);
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                try {
                    recordedQuantity = Double.parseDouble(text.toString());
                    double remainingStock = selectedProduct.getStock() - recordedQuantity;
                    displayFigures(getTotalSales(recordedQuantity, recordedPrice), quantitySoldTextView);
                    displayFigures(remainingStock, quantityLeftTextView);
                } catch (NumberFormatException e) {
                    final String TAG = "Quantity Text changed";
                    displayFigures(0.0, quantitySoldTextView);
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        priceInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                try {
                    recordedPrice = Double.parseDouble(text.toString());
                    displayFigures(getTotalSales(recordedQuantity, recordedPrice), quantitySoldTextView);
                } catch (NumberFormatException e) {
                    final String TAG = "Price text changed";
                    displayFigures(0.0, quantitySoldTextView);
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        customerChip.setOnClickListener(view -> {
            RecordCustomerDialog dialog = new RecordCustomerDialog();
            dialog.show(getFragmentManager(), "Customer Record Dialog");
            dialog.setOnDoneClickListener(this);
        });

        recordButton.setOnClickListener(view -> recordSalesEvent());

    }

    private void setupProductMenu() {
        userId = fbAuth.getCurrentUser().getUid();
        productMenu = new PopupMenu(getActivity(), productInputText);
        products = new ArrayList<>();
        fireStoreHelper.readDocsFromSubCollection(USERS, userId, PRODUCTS)
                .get()
                .addOnCompleteListener(task -> {
                    int index = 0;
                    Product product;
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            product = doc.toObject(Product.class);
                            products.add(product);
                            productMenu.getMenu().add(0, index,
                                    0, Objects.requireNonNull(product).getName());
                            index++;
                        }
                    }
                });
    }

    private double getTotalSales(double stock, double price) {
        return stock * price;
    }

    private <T extends View> void displayFigures(double figure, T display) {
        ((TextView) display).setText(String.valueOf(figure));
    }

}