package com.daveace.salesdiary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.bumptech.glide.Glide;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.customview.CustomExpandableCardView;
import com.daveace.salesdiary.entity.Customer;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.BEARING;
import static com.daveace.salesdiary.interfaces.Constant.EVENTS_RELATED_CUSTOMER;
import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENT_DATE_FORMAT;
import static com.daveace.salesdiary.interfaces.Constant.TILT_ANGLE;
import static com.daveace.salesdiary.interfaces.Constant.ZOOM_LEVEL;

public class SalesEventDetailsFragment extends BaseFragment implements OnMapReadyCallback,
        BackIconActionBarMarker {

    @BindView(R.id.productName)
    TextView productNameTextView;
    @BindView(R.id.productCode)
    TextView productCodeTextView;
    @BindView(R.id.salesDate)
    TextView salesDateTextView;
    @BindView(R.id.salesPrice)
    TextView priceTextView;
    @BindView(R.id.quantitySold)
    TextView quantityTextView;
    @BindView(R.id.leftOver)
    TextView leftOverTextView;
    @BindView(R.id.customerName)
    TextView customerNameTextView;
    @BindView(R.id.customerEmail)
    TextView customerEmailTextView;
    @BindView(R.id.customerCompany)
    TextView customerCompanyTextView;
    @BindView(R.id.customerPhone)
    TextView customerPhoneTextView;
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
    @BindView(R.id.customerCompanyIcon)
    ImageView customerCompanyImageView;
    @BindView(R.id.customerPhoneIcon)
    ImageView customerPhoneImageView;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.location_detail_card)
    CustomExpandableCardView locationDetailContainer;
    @BindView(R.id.product_detail_card)
    CustomExpandableCardView productDetailContainer;
    @BindView(R.id.customer_detail_card)
    CustomExpandableCardView customerDetailContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initUI(savedInstanceState);
        return view;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_sales_event_diary_details;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.saies_event_details);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        GoogleApiAvailability googleApiAvailability =
                GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());

        switch (status) {
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:
                googleApiAvailability.showErrorNotification(getActivity(), status);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (map != null)
            map.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng coordinate;

        SalesEvent salesEvent = getArguments().getParcelable(SALES_EVENTS_REPORT);
        Double lat = Objects.requireNonNull(salesEvent).getLatitude();
        Double lng = salesEvent.getLongitude();
        coordinate = new LatLng(lat, lng);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(coordinate)
                .zoom(ZOOM_LEVEL)
                .bearing(BEARING)
                .tilt(TILT_ANGLE)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.
                newCameraPosition(cameraPosition));
        googleMap.addMarker(new MarkerOptions().position(coordinate));

    }

    private void initUI(Bundle savedInstanceState) {
        map.onCreate(savedInstanceState);
        if (map != null) {
            map.getMapAsync(this);
        }
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
                    .getSalesPrice()));
            quantityTextView.setText(String.valueOf(salesEventsDetail
                    .getSales()));
            leftOverTextView.setText(String.valueOf(salesEventsDetail
                    .getLeft()));
            customerNameTextView.setText(Objects.requireNonNull(relatedCustomer)
                    .getName());
            customerEmailTextView.setText(Objects.requireNonNull(relatedCustomer
                    .getEmail()));
            customerCompanyTextView.setText(Objects.requireNonNull(relatedCustomer)
                    .getCompany());
            customerPhoneTextView.setText(Objects.requireNonNull(relatedCustomer)
                    .getPhone());

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
            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.drawable.ic_company_black, null))
                    .into(customerCompanyImageView);
            Glide.with(getActivity()).load(getActivity()
                    .getResources().getDrawable(R.drawable.ic_phone_black, null))
                    .into(customerPhoneImageView);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

}
