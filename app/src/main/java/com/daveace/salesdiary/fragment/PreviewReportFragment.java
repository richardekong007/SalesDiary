package com.daveace.salesdiary.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.daveace.salesdiary.Adapter.PreviewAdapter;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.interfaces.BackIconActionBarMarker;
import com.daveace.salesdiary.util.MediaUtil;
import com.daveace.salesdiary.util.PdfDocumentUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;

public class PreviewReportFragment extends BaseFragment implements BackIconActionBarMarker {

    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.preview_item_title)
    Chip itemTitle;
    @BindView(R.id.preview_pager)
    ViewPager previewPager;
    @BindView(R.id.back_button)
    FloatingActionButton backButton;
    @BindView(R.id.page_chip)
    Chip pageChip;
    @BindView(R.id.forward_button)
    FloatingActionButton forwardButton;

    private ArrayList<Bitmap> images;
    private ArrayList<String> titles;
    private LinkedHashMap<String, String> documents;

    @Override
    public int getLayout() {
        return R.layout.fragment_preview2;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.preview_title);
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
        inflater.inflate(R.menu.menu_generate_report, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.printItem) {
            setLoading(true);
            PdfDocumentUtil.generateReport(
                    getContext(),
                    rootView,
                    documents
            );
            setLoading(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        int firstPage = 1;
        documents = getImageStrings();
        loadPreviewItems(Objects.requireNonNull(documents));
        itemTitle.setText(titles.get(0));
        pageChip.setText(String.format(
                Locale.ENGLISH,
                getString(R.string.page_number_pattern),
                firstPage,
                images.size()
        ));
        PreviewAdapter adapter = new PreviewAdapter(getContext());
        adapter.setImages(images);
        previewPager.setAdapter(adapter);
        detectSlide(previewPager);
        detectForwardClick(forwardButton);
        detectBackClick(backButton);
    }

    private void loadPreviewItems(LinkedHashMap<String, String> maps) {
        images = new ArrayList<>();
        titles = new ArrayList<>();
        maps.entrySet().forEach(entry -> {
            String title = entry.getKey();
            Bitmap bitmapImage = MediaUtil.bytesToBitmap(MediaUtil.decodeBase64(entry.getValue()));
            titles.add(title);
            images.add(bitmapImage);
        });
    }


    private void detectSlide(ViewPager pager) {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int lastPage = images.size();
                int currentPage = position + 1;
                itemTitle.setText(titles.get(position));
                pageChip.setText(String.format(
                        Locale.ENGLISH,
                        getString(R.string.page_number_pattern),
                        currentPage,
                        lastPage)
                );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void detectForwardClick(View view) {
        view.setOnClickListener(theView -> {
            int nextItem = previewPager.getCurrentItem() + 1;
            if (nextItem < images.size()) {
                previewPager.setCurrentItem(nextItem);
            }
        });
    }

    private void detectBackClick(View view) {
        view.setOnClickListener(theView -> {
            int prevItem = previewPager.getCurrentItem() - 1;
            if (prevItem >= 0) {
                previewPager.setCurrentItem(prevItem);
            }
        });
    }

}
