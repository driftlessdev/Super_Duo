package it.jaschke.alexandria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.apache.commons.validator.routines.ISBNValidator;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by Tim on 10/17/2015.
 */
public class ScannerActivity extends ActionBarActivity implements ZBarScannerView.ResultHandler {
    private static final String LOG_TAG = ScannerActivity.class.getSimpleName();
    private ZBarScannerView mScannerView;

    public static final int SCAN_RESULT = 1;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        Log.v(LOG_TAG, rawResult.getBarcodeFormat().getName() + " - " + rawResult.getContents());

        String result;
        if(rawResult.getBarcodeFormat().equals(BarcodeFormat.ISBN10))
        {
            // Not letting the AddBook activity do this since we could scan in an X
            // on some old ISBN-10 formatting for the check digit. Let the validator
            // Do the full conversion
            result = ISBNValidator.getInstance().convertToISBN13(rawResult.getContents());
        }
        else if (rawResult.getBarcodeFormat().equals(BarcodeFormat.ISBN13))
        {
            result = rawResult.getContents();
        }
        else
        {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_TEXT, result);
        setResult(RESULT_OK, intent);
        finish();
    }


}
