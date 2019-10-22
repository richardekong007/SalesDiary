package com.daveace.salesdiary.fragment;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.alert.ErrorAlert;
import com.daveace.salesdiary.alert.InformationAlert;
import com.daveace.salesdiary.dialog.RecordCustomerDialog;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.util.LocationUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

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
import static com.daveace.salesdiary.util.StringUtil.clear;
import static com.daveace.salesdiary.util.StringUtil.fieldsAreValid;

public class RecordSalesFragment extends BaseFragment
        implements RecordCustomerDialog.OnDoneClickListener {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.quantityLeft)
    TextView quantityLeftTextView;
    @BindView(R.id.amountSold)
    TextView amountSoldTextView;
    @BindView(R.id.cost)
    TextView costTextView;
    @BindView(R.id.product)
    TextView productInputText;
    @BindView(R.id.productCode)
    TextInputEditText productCodeInputText;
    @BindView(R.id.quantity)
    TextInputEditText quantityInputText;
    @BindView(R.id.salesPrice)
    TextInputEditText priceInputText;
    @BindView(R.id.customerInfoChip)
    Chip customerChip;
    @BindView(R.id.recordButton)
    AppCompatButton recordButton;

    private PopupMenu productMenu;
    private List<Product> products;
    private Product selectedProduct;
    private String userId;
    private String customerId;
    private int selectedProductIndex;
    private double recordedQuantitySold;
    private double recordedPrice;
    private LocationManager locationManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiateLocationService();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationUtil.stopLocationUpdate(locationManager);
    }

    private void initiateLocationService() {
        if (getActivity() != null) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationUtil.requestLocationUpdate(getActivity(), locationManager);
        }
    }

    private void recordSalesEvent() {
        //save sales record and customer details to the database
        setLoading(true);
        if (selectedProduct == null) {
            ErrorAlert.Builder().setContext(getActivity())
                    .setRootView(rootView)
                    .setMessage(getString(R.string.select_product))
                    .build()
                    .show();
            return;
        }

        double quantityLeft = selectedProduct.getStock();
        double totalCost = getTotalCost(recordedQuantitySold, selectedProduct.getCost());
        double totalSales = getTotalSales(recordedQuantitySold, recordedPrice);
        String productId = selectedProduct.getId();

        if (!fieldsAreValid(getActivity(), priceInputText, quantityInputText))
            return;

        if (selectedProduct.getStock() >= recordedQuantitySold && recordedQuantitySold > 0) {
            quantityLeft -= recordedQuantitySold;
        } else {
            ErrorAlert.Builder().setContext(getActivity())
                    .setRootView(rootView)
                    .setMessage(getString(R.string.invalid_sales_quantity))
                    .build()
                    .show();
            return;
        }
        if (customerId == null) {
            ErrorAlert.Builder().setContext(getActivity())
                    .setRootView(rootView)
                    .setMessage(getString(R.string.no_customer_flag))
                    .build()
                    .show();
            return;
        }
        selectedProduct.setStock(quantityLeft);
        SalesEvent salesEvent = SalesEvent.getInstance(productId, userId, customerId,
                totalCost, totalSales,
                recordedQuantitySold, quantityLeft, new Date());
        salesEvent.setLatitude(LocationUtil.getLatitude());
        salesEvent.setLongitude(LocationUtil.getLongitude());

        final DocumentReference productRef = getFireStoreHelper()
                .getSubDocumentReference(USERS, userId, PRODUCTS, productId);
        final DocumentReference salesEventRef = getFireStoreHelper()
                .getSubDocumentReference(USERS, userId, SALESEVENTS, salesEvent.getId());

        FirebaseFirestore
                .getInstance()
                .runTransaction((Transaction.Function<Void>) transaction -> {
                    transaction.set(productRef, selectedProduct);
                    transaction.set(salesEventRef, salesEvent);
                    if (selectedProduct.getStock() < 1.0) {
                        selectedProduct.setAvailability();
                        transaction.set(productRef, selectedProduct);
                        InformationAlert.Builder().setContext(getActivity())
                                .setRootView(rootView)
                                .setMessage(selectedProduct.getName() + getString(R.string.out_of_stock))
                                .build()
                                .show();
                    }
                    return null;
                })
                .addOnSuccessListener(aVoid -> {
                            InformationAlert.Builder().setContext(getActivity())
                                    .setRootView(rootView)
                                    .setMessage(getString(R.string.record_success))
                                    .build()
                                    .show();
                            clear(
                                    productInputText,
                                    productCodeInputText,
                                    quantityInputText,
                                    priceInputText,
                                    amountSoldTextView,
                                    costTextView,
                                    quantityLeftTextView
                            );
                        }
                )
                .addOnFailureListener(e ->
                        ErrorAlert.Builder().setContext(getActivity())
                                .setRootView(rootView)
                                .setMessage(e.getMessage())
                                .build()
                                .show()
                );

        setLoading(false);
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
            displayFigures(getTotalSales(recordedQuantitySold, recordedPrice), amountSoldTextView);
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
                    recordedQuantitySold = Double.parseDouble(text.toString());
                    double remainingStock = selectedProduct.getStock() - recordedQuantitySold;
                    displayFigures(getTotalSales(recordedQuantitySold, recordedPrice), amountSoldTextView);
                    displayFigures(remainingStock, quantityLeftTextView);
                } catch (NumberFormatException e) {
                    final String TAG = "Quantity Text changed";
                    displayFigures(0.0, amountSoldTextView);
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
                    displayFigures(
                            getTotalSales(recordedQuantitySold, recordedPrice),
                            amountSoldTextView
                    );
                } catch (NumberFormatException e) {
                    final String TAG = "Price text changed";
                    displayFigures(0.0, amountSoldTextView);
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        customerChip.setOnClickListener(view -> {
            RecordCustomerDialog dialog = RecordCustomerDialog.newInstance(
                    Objects.requireNonNull(getFragmentManager()),
                    "Customer Record Dialog"
            );
            dialog.setOnDoneClickListener(this);
        });

        recordButton.setOnClickListener(view -> recordSalesEvent());

    }

    private void setupProductMenu() {
        userId = getUserId();
        productMenu = new PopupMenu(Objects.requireNonNull(getActivity())
                , productInputText);
        products = new ArrayList<>();
        getFireStoreHelper().readDocsFromSubCollection(USERS, userId, PRODUCTS)
                .get()
                .addOnCompleteListener(task -> {
                    int index = 0;
                    Product product;
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            product = doc.toObject(Product.class);
                            products.add(product);
                            productMenu.getMenu().add(0, index,
                                    0, Objects.requireNonNull(product).getName());
                            index++;
                        }
                    }
                });
    }

    private double getTotalSales(double quantitySold, double price) {
        return quantitySold * price;
    }

    private double getTotalCost(double quantitySold, double cost) {
        return quantitySold * cost;
    }

    private <T extends View> void displayFigures(double figure, T display) {
        ((TextView) display).setText(String.valueOf(figure));
    }

}