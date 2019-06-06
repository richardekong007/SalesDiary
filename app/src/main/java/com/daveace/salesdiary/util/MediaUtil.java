package com.daveace.salesdiary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.fragment.BaseFragment;
import com.daveace.salesdiary.interfaces.Constant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

public class MediaUtil {

    private static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File fileSystem = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(imageFileName,
                Constant.IMAGE_FILE_TYPE,
                fileSystem);
    }

    public static String takePhoto(BaseFragment fragment) {
        Context context = fragment.getContext();
        Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageFilePath = null;
        if (captureImage.resolveActivity(Objects.requireNonNull(context).getPackageManager()) != null) {

            File imageFile = null;
            try {
                imageFile = createImageFile(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(context, context.getString(R.string.authority), imageFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                imageFilePath = imageFile.getAbsolutePath();
                fragment.startActivityForResult(captureImage, Constant.REQUEST_IMAGE_CAPTURE);

            }
        }
        return imageFilePath;
    }

    public static void displayImage(Context context, String imageFilePath, ImageView imageView) {
        File imageFile = new File(imageFilePath);
        if (imageFile.exists()) {
            Glide.with(context).load(imageFile).into(imageView);
        }
    }

    public static void displayImage(ImageView imageView, Bitmap bitmap) {
        Glide.with(imageView.getContext()).load(bitmap).into(imageView);
    }

    public static <T extends ImageView> Bitmap createBitmap(T source) {
        return ((BitmapDrawable) source.getDrawable()).getBitmap();
    }

    public static Bitmap createBitmap(View source, Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y/3;
        source.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        source.layout(0, 0, source.getMeasuredWidth(), source.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        Drawable background = source.getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        source.layout(source.getLeft(), source.getTop(), source.getRight(), source.getBottom());
        source.draw(canvas);
        return bitmap;
    }
}
