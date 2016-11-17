package edu.uml.cs.jmerrill.fridge_friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;

public class ResultsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<UpcItem> {

    File imgFile;
    public UpcItem upcItem;

    private static final String LOG_TAG = ResultsActivity.class.getSimpleName();
    private static final int UPC_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //image path is Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "barcode.jpg"
        imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "barcode.jpg");

        // load data from network
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(UPC_LOADER_ID, null, this).forceLoad();
        } else {
            Log.e(LOG_TAG, "Could not connect");
        }

        // use button to add upcItem to database
        // TODO: error handling for misread barcodes
    }

    @Override
    public Loader<UpcItem> onCreateLoader(int id, Bundle args) {
        ImageView imageView = (ImageView) findViewById(R.id.imgview);

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.sample5);
        imageView.setImageBitmap(bitmap);

        // create barcode detector
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E)
                        .build();

        if (!detector.isOperational()) {
            Log.e("error", "Could not set up the detector");
            return null;
        }

        // set up the frame
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        // detect barcode
        Barcode barcode = barcodes.valueAt(0);
        String barcode_id = barcode.rawValue;

        return new UpcItemLoader(this, barcode_id);
    }

    @Override
    public void onLoadFinished(Loader<UpcItem> loader, UpcItem data) {

        if(data != null) {
            upcItem = data;
            Log.d(LOG_TAG, upcItem.getId());
        }

        TextView nameView = (TextView) findViewById(R.id.item_name);
        TextView idView = (TextView) findViewById(R.id.item_id);

        nameView.setText(upcItem.getName());
        idView.setText(upcItem.getId());
    }

    @Override
    public void onLoaderReset(Loader<UpcItem> loader) {
    }
}
