package com.daveace.salesdiary.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.alert.InformationAlert;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;

public class ScannerFragment extends BaseFragment {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.scannedCode)
    TextView scannedValue;
    @BindView(R.id.done)
    FloatingActionButton done;
    private CameraSource cam;
    private static final int REQ_CAM_PERMISSION = 100;
    private static final String CAM_PERMISSION = Manifest.permission.CAMERA;
    private String intentData;
    static final String SCANNED_DATA = "SCANNED_DATA";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        done.setOnClickListener(doneButton -> {
            Bundle bundle;
            if (getArguments() != null && !(TextUtils.isEmpty(intentData))) {
                bundle = getArguments();
                bundle.putString(SCANNED_DATA, intentData);
                replaceFragment(new InventoryFragment(), false, bundle);
            } else {
                removeFragment(this);
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_scanner;
    }

    @Override
    public CharSequence getTitle() {
        return getString(R.string.scanner_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupScanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cam != null) {
            cam.release();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupScanner() {
        InformationAlert.Builder()
                .setContext(getActivity())
                .setRootView(rootView)
                .setMessage(getString(R.string.scanner_start_message))
                .build()
                .show();

        BarcodeDetector detector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cam = new CameraSource.Builder(Objects.requireNonNull(getActivity()), detector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), CAM_PERMISSION)
                            == (PackageManager.PERMISSION_GRANTED))
                        cam.start(surfaceView.getHolder());
                    else
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{CAM_PERMISSION}, REQ_CAM_PERMISSION);
                } catch (IOException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cam.stop();
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                InformationAlert.Builder()
                        .setContext(getActivity())
                        .setRootView(rootView)
                        .setMessage(getString(R.string.scanner_stop_message))
                        .build()
                        .show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCodes = detections.getDetectedItems();
                if (barCodes.size() > 0) {
                    scannedValue.post(() -> {
                        scannedValue.removeCallbacks(null);
                        intentData = barCodes.valueAt(0).displayValue;
                        scannedValue.setText(intentData);
                    });
                }
            }
        });
    }
}
