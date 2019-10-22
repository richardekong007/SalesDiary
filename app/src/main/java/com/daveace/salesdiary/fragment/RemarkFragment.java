package com.daveace.salesdiary.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.Product;
import com.daveace.salesdiary.entity.SalesEvent;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.interfaces.Interpretable;
import com.daveace.salesdiary.model.SalesSummaryFigureDatum;

import java.util.ArrayList;

import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.EVENT_RELATED_PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.SALES_EVENTS_REPORTS;

public class RemarkFragment extends BaseFragment implements BackIconActionBarMarker, Interpretable {


    @BindView(R.id.remark_text)
    TextView remarkTextView;

    @SuppressLint("StaticFieldLeak")
    private static RemarkFragment instance = null;

    public static RemarkFragment getInstance(Bundle args) {
        if (instance == null) {
            instance = new RemarkFragment();
        }
        instance.setArguments(args);
        return instance;
    }

    public FragmentTransaction add(@NonNull FragmentTransaction ft, int resId) {
        if (!instance.isAdded()) {
            ft.add(resId, instance);
        }
        return ft;
    }

    @Override
    public int getLayout() {
        return R.layout.child_fragment_remark;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.interpretation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        Bundle args = getArguments();
        if (args == null) return;
        ArrayList<SalesEvent> events = args.getParcelableArrayList(SALES_EVENTS_REPORTS);
        ArrayList<Product> products = args.getParcelableArrayList(EVENT_RELATED_PRODUCTS);
        ArrayList<SalesSummaryFigureDatum> data = getSalesSummaryFigureData(events, products);
        String remark = getRemark(getContext(), data);
        remarkTextView.setText(remark);
    }
}