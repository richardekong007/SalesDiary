package com.daveace.salesdiary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.alert.BaseAlert;
import com.daveace.salesdiary.alert.ErrorAlert;
import com.daveace.salesdiary.alert.InformationAlert;
import com.daveace.salesdiary.interfaces.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

import static android.graphics.pdf.PdfDocument.Page;
import static android.graphics.pdf.PdfDocument.PageInfo;
import static com.daveace.salesdiary.interfaces.Constant.SPACE;

public class PdfDocumentUtil {

    private static final int TEXT_SIZE = 60;
    private static final String PDF_FILE_NAME = "SALES REPORT";
    private static final String FILE_NAMING_PATTERN = "dd-mm-yyyy HH:mm:ss";
    private static final double SCALE_FACTOR = 1.2;


    public static void generateReport(Context ctx, View rootView, LinkedHashMap<String,String> documents) {
        final PdfDocument pdfDocument = new PdfDocument();
        if (documents.size() < 1) return;
        documents.entrySet().forEach(entry -> {
            String title = entry.getKey();
            byte [] imageBytes = MediaUtil.decodeBase64(entry.getValue());
            Bitmap imageBitmap = MediaUtil.bytesToBitmap(imageBytes);
            DisplayMetrics displayMetrics = getDisplay(ctx);
            int pageNumber = Collections.singletonList(documents.keySet()).indexOf(title) + 1;
            int width = (int) (displayMetrics.widthPixels * SCALE_FACTOR);
            int height = (int) (displayMetrics.heightPixels * SCALE_FACTOR);
            PageInfo pageInfo = new PageInfo.Builder(width, height, pageNumber).create();
            Page page = pdfDocument.startPage(pageInfo);
            Paint textPaint = paintTitle(ctx);
            int textWidth = (int) textPaint.measureText(title);
            page.getCanvas().drawText(
                    title,
                    (width >> 1) - (textWidth >> 1),
                    160,
                    textPaint
            );
            page.getCanvas().drawBitmap(
                    imageBitmap,
                    (int) (width * 0.1),
                    (int) (height * 0.1),
                    new Paint()
            );
            pdfDocument.finishPage(page);
        });
        writePDFToFile(ctx, pdfDocument, rootView);
    }


    private static void writePDFToFile(
            Context ctx,
            PdfDocument document,
            View rootView) {
        String message;
        try {
            document.writeTo(createStream(ctx));
            message = ctx.getString(R.string.report_generate_msg);
            showMessage(
                    ctx,
                    rootView,
                    InformationAlert.Builder(),
                    message
            );
        } catch (Exception e) {
            message = e.getMessage();
            showMessage(
                    ctx,
                    rootView,
                    ErrorAlert.Builder(),
                    message
            );
        }
        document.close();
    }

    private static OutputStream createStream(Context ctx) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                FILE_NAMING_PATTERN,
                Locale.getDefault()
        );
        String rootDirectory = Objects.requireNonNull(Objects.requireNonNull(ctx)
                .getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).getAbsolutePath();
        String fileName = rootDirectory
                + File.separator
                + PDF_FILE_NAME
                + SPACE
                + dateFormat.format(new Date())
                + Constant.PDF_FILE_EXT;
        File file = new File(fileName);
        return new FileOutputStream(file);
    }

    private static Paint paintTitle(Context ctx) {
        Paint titlePaint = new Paint();
        titlePaint.setColor(
                Objects.requireNonNull(
                        ctx)
                        .getResources()
                        .getColor(R.color.colorPrimary, null)
        );
        titlePaint.setStyle(Paint.Style.FILL);
        titlePaint.setTextSize(TEXT_SIZE);
        return titlePaint;
    }

    private static void showMessage(Context ctx,
                                    View rootView,
                                    BaseAlert.Builder alertBuilder,
                                    String message) {
        alertBuilder
                .setContext(ctx)
                .setRootView(rootView)
                .setMessage(message)
                .build()
                .show();
    }

    private static DisplayMetrics getDisplay(Context ctx) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((AppCompatActivity) ctx)
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);
        return metrics;
    }
}

