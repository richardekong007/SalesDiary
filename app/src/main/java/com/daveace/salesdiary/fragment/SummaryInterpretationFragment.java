package com.daveace.salesdiary.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.util.MediaUtil;
import com.google.android.material.chip.Chip;

import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.REPORT_TYPE;
import static com.daveace.salesdiary.interfaces.Constant.SPACE;

public class SummaryInterpretationFragment extends BaseFragment
        implements BackIconActionBarMarker {

    @BindView(R.id.title_chip)
    Chip titleChip;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    private byte[] summaryInterpretationInByte;

    private final static int INTERPRETATION_HOLDER_ID = R.id.interpretation_holder;
    private final static int REMARK_HOLDER_ID = R.id.remark_holder;
    private final static int PROFITABILITY_STATUS_HOLDER_ID = R.id.profitability_status_holder;
    private static final String SUMMARY_INTERPRETATION_TITLE = "Summary Interpretations";

    @Override
    public int getLayout() {
        return R.layout.fragment_summary_interpretation;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.interpretation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_report_preview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.previewItem) {
            getImageStrings().put(
                    SUMMARY_INTERPRETATION_TITLE,
                    MediaUtil.encodeBytes(summaryInterpretationInByte)
            );
            replaceFragment(new PreviewReportFragment(), true, null);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        Bundle args = getArguments();
        assert args != null;
        String titleText = args.getString(REPORT_TYPE)
                + SPACE + getString(R.string.summary_interpretation);
        titleChip.setText(titleText);
        addFragments(args);
        new Handler().post(() ->
                summaryInterpretationInByte = getViewInByte()
        );
    }

    private void addFragments(Bundle bundle) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        SummarizedInterpretationFragment interpretationFragment = SummarizedInterpretationFragment
                .getInstance(bundle);

        RemarkFragment remarkFragment = RemarkFragment.getInstance(bundle);

        ProfitabilityStatusFragment statusFragment = ProfitabilityStatusFragment
                .getInstance(bundle);

        interpretationFragment.add(ft,INTERPRETATION_HOLDER_ID);
        remarkFragment.add(ft,REMARK_HOLDER_ID);
        statusFragment.add(ft,PROFITABILITY_STATUS_HOLDER_ID);
        ft.commitAllowingStateLoss();
    }

    private byte[] getViewInByte() {
        int width = rootView.getWidth();
        int height = rootView.getHeight();
        Bitmap bitmap = MediaUtil.createBitmap(rootView, width, height);
        return MediaUtil.toByteArray(bitmap);
    }

}
