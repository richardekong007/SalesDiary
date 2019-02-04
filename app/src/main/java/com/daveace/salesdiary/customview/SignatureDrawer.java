package com.daveace.salesdiary.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.interfaces.Constant;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.print.PrintHelper;

public class SignatureDrawer extends View {

    private static final int TOUCH_TOLERANCE = 10;
    private static final int COLOR = Color.BLACK;
    private static final int STROKE_WIDTH = 8;
    private static final Paint.Style PAINT_STYLE = Paint.Style.STROKE;
    private static final Paint.Cap STROKE_CAP = Paint.Cap.ROUND;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private final Paint paintScreen;
    private final Paint paintLine;
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, Path> pathMap = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, Point> previousPointMap = new HashMap<>();
    private OnTouchEndListener onTouchEndListener;


    public SignatureDrawer(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        paintScreen = new Paint();
        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(COLOR);
        paintLine.setStyle(PAINT_STYLE);
        paintLine.setStrokeWidth(STROKE_WIDTH);
        paintLine.setStrokeCap(STROKE_CAP);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    public void clear() {
        pathMap.clear();
        previousPointMap.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);
        for (Integer key : pathMap.keySet())
            canvas.drawPath(pathMap.get(key), paintLine);
        if (onTouchEndListener != null) {
            onTouchEndListener.onEnd(true);
        }
    }

    private void drawToBitmap() {
        bitmapCanvas.drawBitmap(bitmap, 0, 0, paintScreen);
        for (Integer key : pathMap.keySet())
            bitmapCanvas.drawPath(pathMap.get(key), paintLine);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();

        //determine if touch started or ended or fingers are moving
        if (action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted(event.getX(actionIndex), event.getY(actionIndex),
                    event.getPointerId(actionIndex));
        } else if (action == MotionEvent.ACTION_POINTER_UP ||
                action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded(event.getPointerId(actionIndex));
        } else {
            touchMoved(event);
        }
        invalidate();
        return true;
    }

    private void touchStarted(float x, float y, int lineId) {
        Path path;
        Point point;
        if (!pathMap.containsKey(lineId)) {
            path = new Path();
            point = new Point();
            pathMap.put(lineId, path);
            previousPointMap.put(lineId, point);
        } else {
            path = pathMap.get(lineId);
            point = previousPointMap.get(lineId);
        }
        path.moveTo(x, y);
        point.x = (int) x;
        point.y = (int) y;
    }

    private void touchMoved(MotionEvent event) {
        onTouchEndListener.onEnd(false);
        for (int i = 0; i < event.getPointerCount(); i++) {
            int pointerId = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerId);

            if (pathMap.containsKey(pointerId)) {
                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);
                Path path = pathMap.get(pointerId);
                Point point = previousPointMap.get(pointerId);
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);
                //determine the calculated distance are significant enough
                if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                    //move the path to the new location
                    path.quadTo(point.x, point.y, (newX + point.x) / 2,
                            (newY + point.y) / 2);
                    //store the new coordinates
                    point.x = (int) newX;
                    point.y = (int) newY;
                }
            }
        }
    }

    private void touchEnded(int lineId) {
        Path path = pathMap.get(lineId);
        bitmapCanvas.drawPath(path, paintLine);
        path.reset();
        onTouchEndListener.onEnd(true);
    }

    public String save() {
        drawToBitmap();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss", Locale.getDefault());
        String imageName = dateFormat.format(new Date());
        File imageFile = new File(
                Objects.requireNonNull(
                        getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                        .getAbsolutePath()
                        + File.separator
                        + imageName
                        + Constant.IMAGE_FILE_TYPE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile.getAbsolutePath();
    }

    public void print() {
        if (PrintHelper.systemSupportsPrint()) {
            PrintHelper printHelper = new PrintHelper(getContext());
            drawToBitmap();
            printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            printHelper.printBitmap("Signature", bitmap);
        } else {
            Snackbar.make(getRootView(), getContext().getString(R.string.print_support_error), Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void setOnTouchEndListener(OnTouchEndListener onTouchEndListener) {
        this.onTouchEndListener = onTouchEndListener;
    }

    public interface OnTouchEndListener {
        void onEnd(boolean ended);
    }

}
