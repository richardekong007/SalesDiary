//package com.daveace.salesdiary.dialog;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.daveace.salesdiary.R;
//import com.daveace.salesdiary.entity.Customer;
//import com.daveace.salesdiary.entity.Product;
//import com.daveace.salesdiary.entity.SalesEvent;
//import com.daveace.salesdiary.util.LocationUtil;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.text.SimpleDateFormat;
//import java.util.Locale;
//import java.util.Objects;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import butterknife.BindView;
//
//import static com.daveace.salesdiary.interfaces.Constant.BEARING;
//import static com.daveace.salesdiary.interfaces.Constant.EVENTS_RELATED_CUSTOMER;
//import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCT;
//import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORT;
//import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENT_DATE_FORMAT;
//import static com.daveace.salesdiary.interfaces.Constant.TILT_ANGLE;
//import static com.daveace.salesdiary.interfaces.Constant.ZOOM_LEVEL;
//
//public class SalesEventDetailsDialog extends BaseDialog implements OnMapReadyCallback {
//
//    @BindView(R.id.productName)
//    TextView productNameTextView;
//    @BindView(R.id.productCode)
//    TextView productCodeTextView;
//    @BindView(R.id.salesDate)
//    TextView salesDateTextView;
//    @BindView(R.id.price)
//    TextView priceTextView;
//    @BindView(R.id.quantitySold)
//    TextView quantityTextView;
//    @BindView(R.id.leftOver)
//    TextView leftOverTextView;
//    @BindView(R.id.customerName)
//    TextView customerNameTextView;
//    @BindView(R.id.customerEmail)
//    TextView customerEmailTextView;
//    @BindView(R.id.place_text)
//    TextView placeTextView;
//    @BindView(R.id.productImage)
//    ImageView productImageView;
//    @BindView(R.id.productCodeIcon)
//    ImageView productCodeImageView;
//    @BindView(R.id.priceIcon)
//    ImageView priceImageView;
//    @BindView(R.id.cartIcon)
//    ImageView cartImageView;
//    @BindView(R.id.stockIcon)
//    ImageView stockImageView;
//    @BindView(R.id.customerIcon)
//    ImageView customerImageView;
//    @BindView(R.id.emailIcon)
//    ImageView customerEmailImageView;
//    @BindView(R.id.locationIcon)
//    ImageView locationImageView;
//
//    public static SalesEventDetailsDialog getInstance(Bundle args) {
//        SalesEventDetailsDialog dialog = new SalesEventDetailsDialog();
//        if (args != null)
//            dialog.setArguments(args);
//
//        return dialog;
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = super.onCreateView(inflater, container, savedInstanceState);
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map_fragment);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
//        initUI();
//        return view;
//    }
//
//
//    @Override
//    int getLayout() {
//        return R.layout.dialog_sales_event;
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        GoogleApiAvailability googleApiAvailability =
//                GoogleApiAvailability.getInstance();
//        int status = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
//
//        switch (status) {
//            case ConnectionResult.SERVICE_MISSING:
//            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
//            case ConnectionResult.SERVICE_DISABLED:
//                googleApiAvailability.showErrorNotification(getActivity(), status);
//                break;
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        LatLng coordinate;
//        try {
//            SalesEvent salesEvent = getArguments().getParcelable(SALES_EVENTS_REPORT);
//            Double lat = Objects.requireNonNull(salesEvent).getLatitude();
//            Double lng = salesEvent.getLongitude();
//            coordinate = new LatLng(lat, lng);
//            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            CameraPosition cameraPosition = CameraPosition.builder()
//                    .target(coordinate)
//                    .zoom(ZOOM_LEVEL)
//                    .bearing(BEARING)
//                    .tilt(TILT_ANGLE)
//                    .build();
//            googleMap.moveCamera(CameraUpdateFactory.
//                    newCameraPosition(cameraPosition));
//            googleMap.addMarker(new MarkerOptions().position(coordinate));
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void initUI() {
//
//        try {
//            Bundle bundle = getArguments();
//            SalesEvent salesEventsDetail =
//                    bundle.getParcelable(SALES_EVENTS_REPORT);
//            Customer relatedCustomer =
//                    bundle.getParcelable(EVENTS_RELATED_CUSTOMER);
//            Product relatedProduct =
//                    bundle.getParcelable(EVENT_RELATED_PRODUCT);
//            salesDateTextView.setText((new SimpleDateFormat(SALES_EVENT_DATE_FORMAT, Locale.ENGLISH)
//                    .format(Objects.requireNonNull(salesEventsDetail)
//                            .getDate())));
//
//            placeTextView.setText(getPlace(salesEventsDetail));
//
//            productNameTextView.setText(Objects.requireNonNull(relatedProduct)
//                    .getName());
//            productCodeTextView.setText(Objects.requireNonNull(relatedProduct)
//                    .getCode());
//            priceTextView.setText(String.valueOf(salesEventsDetail
//                    .getSalesPrice()));
//            quantityTextView.setText(String.valueOf(salesEventsDetail
//                    .getSales()));
//            leftOverTextView.setText(String.valueOf(salesEventsDetail
//                    .getLeft()));
//            customerNameTextView.setText(Objects.requireNonNull(relatedCustomer)
//                    .getName());
//            customerEmailTextView.setText(Objects.requireNonNull(relatedCustomer
//                    .getEmail()));
//
//            Glide.with(getActivity()).load(getActivity()
//                    .getResources().getDrawable(R.mipmap.stock, null))
//                    .into(productImageView);
//            Glide.with(getActivity()).load(getActivity()
//                    .getResources().getDrawable(R.mipmap.barcode, null))
//                    .into(productCodeImageView);
//            Glide.with(getActivity()).load(getActivity()
//                    .getResources().getDrawable(R.mipmap.diamond, null))
//                    .into(priceImageView);
//            Glide.with(getActivity()).load(getActivity()
//                    .getResources().getDrawable(R.drawable.ic_shopping_cart_black, null))
//                    .into(cartImageView);
//            Glide.with(getActivity()).load(getActivity()
//                    .getResources().getDrawable(R.mipmap.inventory2, null))
//                    .into(stockImageView);
//            Glide.with(getActivity()).load(getActivity()
//                    .getResources().getDrawable(R.drawable.ic_account_box_black, null))
//                    .into(customerImageView);
//            Glide.with(getActivity()).load(getActivity()
//                    .getResources().getDrawable(R.drawable.ic_email_black, null))
//                    .into(customerEmailImageView);
//            Glide.with(getActivity()).load(getActivity()
//                    .getResources().getDrawable(R.drawable.ic_location_black, null))
//                    .into(locationImageView);
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getPlace(SalesEvent salesEventsDetail) {
//        String place = "";
//        LatLng location = new LatLng(salesEventsDetail.getLatitude(),
//                salesEventsDetail.getLatitude());
//        if (location.latitude > 0 && location.longitude > 0) {
//            place = LocationUtil.getPlaceFromLocation(getActivity(),
//                    location);
//        }
//        return place;
//    }
//
//}
