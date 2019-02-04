package com.daveace.salesdiary.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.daveace.salesdiary.util.MediaUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;

import static android.app.Activity.RESULT_CANCELED;
import static com.daveace.salesdiary.fragment.ProductCatalogFragment.PRODUCT_BUNDLE;
import static com.daveace.salesdiary.interfaces.Constant.PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.REQUEST_IMAGE_CAPTURE;
import static com.daveace.salesdiary.interfaces.Constant.USERS;
import static com.daveace.salesdiary.util.StringUtil.fieldsAreValid;


public class EditProductFragment extends BaseFragment {


    @BindView(R.id.rootView)
    ScrollView rootView;
    @BindView(R.id.inventory_image)
    ImageView productImageView;
    @BindView(R.id.product)
    TextInputEditText productInput;
    @BindView(R.id.quantity)
    TextInputEditText quantityInput;
    @BindView(R.id.cost)
    TextInputEditText costInput;
    @BindView(R.id.productCode)
    TextInputEditText productCode;
    @BindView(R.id.capture)
    ImageButton captureButton;
    @BindView(R.id.editButton)
    AppCompatButton editButton;
    private Bitmap imageBitmap;

    private String productImagePath;
    private Product product;
    private FirebaseAuth fbAuth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        initView();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_edit_product;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.edit_product);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            MediaUtil.displayImage(getActivity(), productImagePath, productImageView);
        }
        if (resultCode == RESULT_CANCELED) {
            Glide.with(getActivity()).load(imageBitmap).into(productImageView);
            Snackbar.make(rootView, getString(R.string.cam_capture_cancelled), Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void initView() {
        imageBitmap = MediaUtil.createBitmap(productImageView);
        product = getArguments().getParcelable(PRODUCT_BUNDLE);
        if (product != null) {
            productInput.setText(Objects.requireNonNull(product).getName());
            quantityInput.setText(String.valueOf(product.getStock()));
            costInput.setText(String.valueOf(product.getCost()));
            productCode.setText(product.getCode());
            String imagePath = product.getImagePath();
            if (imagePath != null && imagePath.length() > 0) {
                Glide.with(getActivity())
                        .load(imagePath)
                        .into(productImageView);
            }
        }

        captureButton.setOnClickListener(view ->
                productImagePath = MediaUtil.takePhoto(this));
        editButton.setOnClickListener(view -> updateProduct());
    }

    private void updateProduct() {
        if (!fieldsAreValid(getActivity(), productInput, quantityInput, costInput, productCode)) {
            return;
        }
        setLoading(true);
        if (product != null) {
            product.setName(productInput.getText()
                    .toString());
            product.setStock(Double.parseDouble(quantityInput.getText()
                    .toString()));
            product.setCost(Double.parseDouble(costInput.getText()
                    .toString()));
            String userId = fbAuth.getCurrentUser().getUid();
            FireStoreHelper.getInstance()
                    .update(USERS, userId, PRODUCTS, product.getId(), product);
            setLoading(false);
        }
    }
}