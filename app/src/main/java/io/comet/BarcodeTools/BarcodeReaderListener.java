package io.comet.BarcodeTools;

import android.util.SparseArray;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

public interface BarcodeReaderListener {
    void onScanned(Barcode barcode);

    void onScannedMultiple(List<Barcode> barcodes);

    void onBitmapScanned(SparseArray<Barcode> sparseArray);

    void onScanError(String errorMessage);

    void onCameraPermissionDenied();
}