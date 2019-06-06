package com.daveace.salesdiary.fragment;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.ImageView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.alert.ErrorAlert;
import com.daveace.salesdiary.interfaces.Constant;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

public class PreviewReportFragment extends BaseFragment {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;

    @BindView(R.id.documentRenderer)
    ImageView documentRenderer;

    @BindView(R.id.previous)
    FloatingActionButton previous;

    @BindView(R.id.next)
    FloatingActionButton next;

    private Bundle args;

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
        try {
            createPDFDocument();
            renderDocument();
        } catch (IOException e) {
            ErrorAlert.Builder()
                    .setContext(getActivity())
                    .setRootView(rootView)
                    .setMessage(e.getMessage())
                    .build()
                    .show();
            documentRenderer.setImageBitmap(args.getParcelable(SummaryFragment.SUMMARY_CHART));
        }
    }

    private PdfDocument createPDFDocument() throws IOException {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                .Builder(100, 100, 1)
                .create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Bitmap bitmap = args.getParcelable(CashFlowFragment.PROFIT_LOSS_CHART);
        page.getCanvas().drawBitmap(Objects.requireNonNull(bitmap),0,0,null);
        document.finishPage(page);
        document.writeTo(getOutputStream());
        document.close();
        return document;
    }

    private void renderDocument() throws IOException {
        File pdfDocument = new File(fileName);
        ParcelFileDescriptor fileDescriptor =
                ParcelFileDescriptor.open(pdfDocument, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer renderer = new PdfRenderer(fileDescriptor);
        PdfRenderer.Page renderPage = renderer.openPage(0);
        int pageWidth = renderPage.getWidth();
        int pageHeight = renderPage.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888);
        renderPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        documentRenderer.setImageBitmap(bitmap);
        renderPage.close();
        fileDescriptor.close();

    }

    private OutputStream getOutputStream() throws IOException {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "dd-mm-yyyy HH:mm:ss",
                        Locale.getDefault());
        fileName = dateFormat.format(new Date());
        File documentFile =
                new File(Objects
                        .requireNonNull(getContext()
                                .getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                        .getAbsolutePath()
                        + File.separator
                        + fileName
                        + Constant.PDF_FILE);
        return new FileOutputStream(documentFile);
    }
}
