package com.daveace.salesdiary.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SubCollectionPath;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.interfaces.Constant;
import com.daveace.salesdiary.util.MediaUtil;
import com.daveace.salesdiary.util.StringUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

import static android.app.Activity.RESULT_CANCELED;
import static com.daveace.salesdiary.interfaces.Constant.PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.USERS;
import static com.daveace.salesdiary.util.StringUtil.fieldsAreValid;


public class InventoryFragment extends BaseFragment {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.inventory_image)
    ImageView inventoryImage;
    @BindView(R.id.product)
    TextInputEditText productNameInput;
    @BindView(R.id.quantity)
    TextInputEditText quantityInput;
    @BindView(R.id.cost)
    TextInputEditText costInput;
    @BindView(R.id.productCode)
    TextInputEditText productCode;
    @BindView(R.id.capture)
    AppCompatImageButton captureButton;
    @BindView(R.id.stockUpButton)
    AppCompatButton stockUpButton;
    private String imageFilePath;
    private Bitmap imageBitmap;
    private static final String PRODUCT_PARCELABLE = "PRODUCT_PARCELABLE";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_inventory;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.inventory_title);
    }

    private void initViews() {
        imageBitmap = MediaUtil.createBitmap(inventoryImage);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Product product = bundle.getParcelable(PRODUCT_PARCELABLE);
            productNameInput.setText(Objects.requireNonNull(product).getName());
            quantityInput.setText(String.valueOf(product.getStock()));
            costInput.setText(String.valueOf(product.getCost()));
            productCode.setText(bundle.getString(ScannerFragment.SCANNED_DATA));
            MediaUtil.displayImage(inventoryImage, imageBitmap);
        }

        inventoryImage.setOnClickListener(view -> {
        });
        productCode.setOnClickListener(view -> transferProductDetail());
        captureButton.setOnClickListener(view -> imageFilePath = MediaUtil.takePhoto(this));
        stockUpButton.setOnClickListener(view -> addProduct());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_IMAGE_CAPTURE) {
            MediaUtil.displayImage(getActivity(), imageFilePath, inventoryImage);
        }
        if (resultCode == RESULT_CANCELED) {
            Glide.with(getActivity()).load(imageBitmap).into(inventoryImage);
            Snackbar.make(rootView, getString(R.string.cam_capture_cancelled), Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void addProduct() {
        if (!fieldsAreValid(getActivity(), productNameInput, quantityInput, productCode, costInput)) {
            return;
        }
        setLoading(true);
        String name = productNameInput.getText().toString().trim();
        double quantity = Double.parseDouble(quantityInput.getText().toString().trim());
        double cost = Double.parseDouble(costInput.getText().toString().trim());
        String code = productCode.getText().toString().trim();
        String imgPath = !(TextUtils.isEmpty(imageFilePath)) ? imageFilePath : "";
        Date date = new Date();
        Product product = Product.getInstance(name, quantity, cost, code, imgPath, date);
        String userId = getUserId();
        SubCollectionPath metaData = new SubCollectionPath(USERS, userId, PRODUCTS, product.getId(), product);
        getFireStoreHelper().addDocumentToSubCollection(metaData, rootView);
        setLoading(false);
        StringUtil.clear(productNameInput, quantityInput, productCode, costInput);
    }

    private void transferProductDetail() {
        Bundle bundle = new Bundle();
        if (!fieldsAreValid(getActivity(), productNameInput, quantityInput, costInput))
            return;
        Product tempProduct = Product.getInstance(
                productNameInput.getText().toString(),
                Double.parseDouble(quantityInput.getText().toString()),
                Double.parseDouble(costInput.getText().toString())
        );
        bundle.putParcelable(PRODUCT_PARCELABLE, tempProduct);
        replaceFragment(new ScannerFragment(), true, bundle);
    }

}