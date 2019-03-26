package com.daveace.salesdiary.dialog;

import android.os.Bundle;
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
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENTS_RELATED_CUSTOMER;
import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENT_DATE_FORMAT;

public class SalesEventDetailsDialog extends BaseDialog {

    @BindView(R.id.productName)
    TextView productNameTextView;
    @BindView(R.id.productCode)
    TextView productCodeTextView;
    @BindView(R.id.salesDate)
    TextView salesDateTextView;
    @BindView(R.id.price)
    TextView priceTextView;
    @BindView(R.id.quantitySold)
    TextView quantityTextView;
    @BindView(R.id.leftOver)
    TextView leftOverTextView;
    @BindView(R.id.customerName)
    TextView customerNameTextView;
    @BindView(R.id.customerEmail)
    TextView customerEmailTextView;
    @BindView(R.id.productImage)
    ImageView productImageView;
    @BindView(R.id.productCodeIcon)
    ImageView productCodeImageView;
    @BindView(R.id.priceIcon)
    ImageView priceImageView;
    @BindView(R.id.cartIcon)
    ImageView cartImageView;
    @BindView(R.id.stockIcon)
    ImageView stockImageView;
    @BindView(R.id.customerIcon)
    ImageView customerImageView;
    @BindView(R.id.emailIcon)
    ImageView customerEmailImageView;


    public static SalesEventDetailsDialog getInstance(Bundle args) {
        SalesEventDetailsDialog dialog = new SalesEventDetailsDialog();
        if (args != null)
            dialog.setArguments(args);

        return dialog;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        return view;
    }


    @Override
    int getLayout() {
        return R.layout.dialog_sales_event;
    }

    private void initUI() {
        try {
            Bundle bundle = getArguments();
            SalesEvent salesEventsDetail =
                    bundle.getParcelable(SALES_EVENTS_REPORT);
            Customer relatedCustomer =
                    bundle.getParcelable(EVENTS_RELATED_CUSTOMER);
            Product relatedProduct =
                    bundle.getParcelable(EVENT_RELATED_PRODUCT);
            salesDateTextView.setText((new SimpleDateFormat(SALES_EVENT_DATE_FORMAT, Locale.ENGLISH)
                    .format(Objects.requireNonNull(salesEventsDetail)
                            .getDate())));
            productNameTextView.setText(Objects.requireNonNull(relatedProduct)
                    .getName());
            productCodeTextView.setText(Objects.requireNonNull(relatedProduct)
                    .getCode());
            priceTextView.setText(String.valueOf(salesEventsDetail
                    .getPrice()));
            quantityTextView.setText(String.valueOf(salesEventsDetail
                    .getSales()));
            leftOverTextView.setText(String.valueOf(salesEventsDetail
                    .getLeft()));
            customerNameTextView.setText(Objects.requireNonNull(relatedCustomer)
                    .getName());
            customerEmailTextView.setText(Objects.requireNonNull(relatedCustomer
                    .getEmail()));

            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.mipmap.stock, null))
                    .into(productImageView);
            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.mipmap.barcode, null))
                    .into(productCodeImageView);
            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.mipmap.diamond, null))
                    .into(priceImageView);
            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.drawable.ic_shopping_cart_black, null))
                    .into(cartImageView);
            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.mipmap.inventory2, null))
                    .into(stockImageView);
            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.drawable.ic_account_box_black, null))
                    .into(customerImageView);
            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.drawable.ic_email_black, null))
                    .into(customerEmailImageView);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
