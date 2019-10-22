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

import java.util.Objects;

import butterknife.BindView;

public class SummarizedSalesFiguresFragment extends BaseFragment implements BackIconActionBarMarker {

    @BindView(R.id.table_title)
    Chip tableTitle;
    @BindView(R.id.table_layout)
    CardView tableLayout;
    @BindView(R.id.interpret)
    AppCompatButton interpretButton;

    private Bundle args;


    @Override
    public int getLayout() {
        return R.layout.fragment_summarized_sales_figures;
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
        if (args == null) return;
        final String tableTitleText = Objects.requireNonNull(getContext())
                .getString(R.string.chart_summary);
        tableTitle.setText(tableTitleText);
        SummaryTableFragment tableFragment = SummaryTableFragment.getInstance(args);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        tableFragment.add(ft,R.id.table_layout);
        interpretButton.setOnClickListener(view -> {
            setLoading(true);
            new Handler().post(() -> {
                byte[] tableInBytes = extractTableInBytes();
                getImageStrings().put(
                        tableTitleText,
                        MediaUtil.encodeBytes(tableInBytes)
                );
                replaceFragment(new SummaryInterpretationFragment(), true, args);
            });
            setLoading(false);
        });
    }

    private byte[] extractTableInBytes() {
        int width = tableLayout.getWidth();
        int height = tableLayout.getHeight();
        Bitmap tableInBitmap = MediaUtil.createBitmap(tableLayout, width, height);
        return MediaUtil.toByteArray(tableInBitmap);
    }

}
