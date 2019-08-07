package com.daveace.salesdiary.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.util.MediaUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.SPACE;

public class PreviewReportFragment extends BaseFragment {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;

    @BindView(R.id.title)
    Chip titleChip;

    @BindView(R.id.documentRenderer)
    ImageView documentRenderer;

    @BindView(R.id.previous)
    FloatingActionButton previous;

    @BindView(R.id.page)
    TextView page;

    @BindView(R.id.next)
    FloatingActionButton next;

    private Bundle args;

    private int figureIndex;

    private final Map<String, Bitmap> previewFigures = new HashMap<>();

    private String fileName;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = getArguments();
        initUI();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_preview;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.preview_title);
    }

    private void initUI() {
        if (args == null) return;
        addPreviewFigures(args.getString(SummaryFragment.SUMMARY_CHART_TITLE),
                args.getParcelable(SummaryFragment.SUMMARY_CHART));
        addPreviewFigures(args.getString(SummaryFragment.SUMMARY_TABLE_TITLE),
                args.getParcelable(SummaryFragment.SUMMARY_TABLE));
        addPreviewFigures(args.getString(CashFlowFragment.PROFIT_LOSS_CHART_TITLE),
                args.getParcelable(CashFlowFragment.PROFIT_LOSS_CHART));
        addPreviewFigures(args.getString(CashFlowFragment.SALES_COST_CHART_TITLE),
                args.getParcelable(CashFlowFragment.SALES_COST_CHART));
        showFigure(figureIndex);
        next.setOnClickListener(view -> flipForward());
        previous.setOnClickListener(view -> flipBackward());
    }

    private void flipForward() {
        int size = previewFigures.size();
        if (figureIndex < size) {
            showFigure(figureIndex++);
            if (figureIndex > (size - 1)) figureIndex %= size;
        }
    }

    private void flipBackward() {
        int size = previewFigures.size();
        if (figureIndex >= 0) {
            if (figureIndex == 0) figureIndex = size;
            showFigure(--figureIndex);
        }
    }

    private void showFigure(int index) {
        final String pageInfo = "Page";
        String title = String.valueOf(previewFigures.keySet().toArray()[index]);
        titleChip.setText(title);
        MediaUtil.displayImage(documentRenderer, previewFigures.get(title));
        page.setText(String.format("%s%s%s", pageInfo, SPACE, (index + 1)));
    }

    private void addPreviewFigures(String description, Bitmap figure) {
        previewFigures.put(description, figure);
    }
}
