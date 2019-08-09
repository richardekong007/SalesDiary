package com.daveace.salesdiary.fragment;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.alert.ErrorAlert;
import com.daveace.salesdiary.alert.InformationAlert;
import com.daveace.salesdiary.util.MediaUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

import static android.graphics.pdf.PdfDocument.Page;
import static android.graphics.pdf.PdfDocument.PageInfo;
import static com.daveace.salesdiary.interfaces.Constant.PDF_FILE;
import static com.daveace.salesdiary.interfaces.Constant.SPACE;


public class PreviewReportFragment extends BaseFragment {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.title)
    Chip titleChip;
    @BindView(R.id.documentRenderer)
    ImageView figureRenderer;
    @BindView(R.id.previous)
    FloatingActionButton previous;
    @BindView(R.id.page)
    TextView page;
    @BindView(R.id.next)
    FloatingActionButton next;

    private Bundle args;

    private int figureIndex;

    private final Map<String, Bitmap> previewFigures = new HashMap<>();

    private static final String reportFile = "SALES REPORT";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        args = getArguments();
        initUI();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_generate_report, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.printItem:
                generateReport();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        int pages = previewFigures.size();
        String title = String.valueOf(previewFigures.keySet().toArray()[index]);
        titleChip.setText(title);
        MediaUtil.displayImage(figureRenderer, previewFigures.get(title));
        page.setText(String.format("Page%s%s%sof%s%s", SPACE, (index + 1), SPACE, SPACE, pages));
    }

    private void addPreviewFigures(String description, Bitmap figure) {
        previewFigures.put(description, figure);
    }

    private void generateReport() {
        final PdfDocument document = new PdfDocument();
        if (previewFigures.size() < 1) return;
        previewFigures.forEach((title, figure) -> {
            int pageNumber = Collections.singletonList(previewFigures.keySet()).indexOf(title) + 1;
            int width = rootView.getWidth();
            int height = rootView.getHeight();
            int textSize = 60;
            int textWidth;
            PageInfo pageInfo = new PageInfo.Builder(width, height, pageNumber).create();
            Page page = document.startPage(pageInfo);
            Paint textPainter = paintTitle(textSize);
            textWidth = (int) textPainter.measureText(title);
            Paint painter = new Paint();
            page.getCanvas().drawText(title, (width / 2) - (textWidth / 2), 160, textPainter);
            page.getCanvas().drawBitmap(figure, 40, 240, painter);
            document.finishPage(page);
        });
        writePDFToFile(document);
    }

    private void writePDFToFile(PdfDocument document) {
        setLoading(true);
        try {
            document.writeTo(getOutputStream());
            showMessage(getString(R.string.report_generate_msg));
        } catch (Exception e) {
            setLoading(false);
            showErrorMessage(e.getMessage());
        }
        document.close();
        setLoading(false);
    }

    private Paint paintTitle(int textSize) {
        Paint titlePaint = new Paint();
        titlePaint.setColor(getContext().getResources().getColor(R.color.colorPrimary, null));
        titlePaint.setStyle(Paint.Style.FILL);
        titlePaint.setTextSize(textSize);
        return titlePaint;
    }

    private OutputStream getOutputStream() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss", Locale.getDefault());
        String rootDirectory = Objects.requireNonNull(getContext()
                .getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))
                .getAbsolutePath();
        String fileName = rootDirectory + File.separator + reportFile + SPACE + dateFormat.format(new Date()) + PDF_FILE;
        File file = new File(fileName);
        return new FileOutputStream(file);
    }

    private void showErrorMessage(String message) {
        ErrorAlert.Builder().setContext(getActivity())
                .setRootView(rootView).setMessage(message).build().show();
    }

    private void showMessage(String message) {
        InformationAlert.Builder().setContext(getActivity())
                .setRootView(rootView).setMessage(message).build().show();
    }

}
