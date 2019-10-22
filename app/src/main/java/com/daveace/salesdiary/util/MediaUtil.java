package com.daveace.salesdiary.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.fragment.BaseFragment;
import com.daveace.salesdiary.interfaces.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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

    public static Bitmap createBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private final static AtomicReference<Bitmap> atomicBitmapReference = new AtomicReference<>();

    public static Bitmap createBitmap(View view) {

        ((AppCompatActivity) view.getContext()).runOnUiThread(() -> {
            final ViewTreeObserver obs = view.getViewTreeObserver();
            obs.addOnGlobalLayoutListener(() -> {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(() -> {
                });
                atomicBitmapReference.set(Bitmap.createBitmap(
                        view.getRootView().getWidth(),
                        view.getRootView().getHeight(),
                        Bitmap.Config.ARGB_8888)
                );
                Canvas canvas = new Canvas(atomicBitmapReference.get());
                view.draw(canvas);
            });
        });
        return atomicBitmapReference.get();
    }

    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        return stream.toByteArray();
    }

    public static Bitmap bytesToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static String encodeBytes (byte [] bytes){
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static byte [] decodeBase64(String base64){
        return Base64.decode(base64, Base64.DEFAULT);
    }

}
