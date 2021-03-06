package com.daveace.salesdiary.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.fragment.BaseFragment;
import com.daveace.salesdiary.interfaces.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
}
