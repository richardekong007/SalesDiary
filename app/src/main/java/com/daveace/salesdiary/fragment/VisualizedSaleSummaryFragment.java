package com.daveace.salesdiary.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.util.MediaUtil;
import com.google.android.material.chip.Chip;

import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.REPORT_TYPE;

public class VisualizedSaleSummaryFragment extends BaseFragment implements BackIconActionBarMarker {

    @BindView(R.id.chart_title)
    Chip chartTitle;
    @BindView(R.id.chart_layout)
    CardView chartLayout;
    @BindView(R.id.summarize)
    AppCompatButton summarizeButton;

    private Bundle args;

    @Override
    public int getLayout() {
        return R.layout.fragment_visualized_sale_summary;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.sales_summary);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initUI();
        return view;
    }

    private void initUI() {
        args = getArguments();
        assert args != null;
        final String chartTitleText = args.getString(REPORT_TYPE);
        chartTitle.setText(chartTitleText);
        SummaryChartFragment chartFragment = SummaryChartFragment.getInstance(args);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        chartFragment.add(ft,R.id.chart_layout);
        summarizeButton.setOnClickListener(view -> {
            setLoading(true);
            new Handler().post(() -> {
                byte[] chartInBytes = getChartInBytes();
                getImageStrings().put(
                        chartTitleText,
                        MediaUtil.encodeBytes(chartInBytes)
                );
                replaceFragment(new SummarizedSalesFiguresFragment(), true, args);
            });
            setLoading(false);
        });
    }

    private byte[] getChartInBytes() {
        int width = chartLayout.getWidth();
        int height = chartLayout.getHeight();
        Bitmap chartInBitmap = MediaUtil.createBitmap(chartLayout, width, height);
        return MediaUtil.toByteArray(chartInBitmap);
    }

}
