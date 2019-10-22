package com.daveace.salesdiary.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.daveace.salesdiary.util.MediaUtil;

import java.util.ArrayList;

import static android.widget.LinearLayout.LayoutParams;

public class PreviewAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Bitmap> images;

    public PreviewAdapter(Context ctx) {
        this.context = ctx;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Bitmap bitmap = images.get(position);
        LinearLayout layout = new LinearLayout(context);
        ImageView imageView = new ImageView(context);
        setLayoutParameters(layout);
        setLayoutParameters(imageView);
        MediaUtil.displayImage(imageView,bitmap);
        layout.addView(imageView);
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }

    private void setLayoutParameters(View view) {
        view.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));
    }
}
