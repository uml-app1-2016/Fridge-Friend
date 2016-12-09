package edu.uml.cs.jmerrill.fridge_friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.text.SimpleDateFormat;

public class ResultsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<UpcItem> {

    // helper for the local database
    DBHelper productdb;

    //the resulting UpcItem
    public UpcItem upcItem;

    // Tag for debug logs
    private static final String LOG_TAG = ResultsActivity.class.getSimpleName();
    private static final int UPC_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // initialize network connection and start loader
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(UPC_LOADER_ID, null, this).forceLoad();
        } else {
            Log.e(LOG_TAG, "Could not connect");
        }

        productdb = new DBHelper(this);

        // AddItem button to insert newly-created UpcItem into the database
        Button btnAddItem = (Button) findViewById(R.id.btn_results_add_item);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productdb.insertProduct(upcItem);
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<UpcItem> onCreateLoader(int id, Bundle args) {

        // the image file is stored as a byte array as an extra from MainActivity
        if(getIntent().getExtras() == null) {
            Log.d(LOG_TAG, "No image file present");
        }

        Log.d(LOG_TAG, getIntent().getExtras().toString());

        byte[] img = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

        // create barcode detector
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E | Barcode.EAN_13 | Barcode.ISBN | Barcode.EAN_8)
                        .build();

        if (!detector.isOperational()) {
            Log.e("error", "Could not set up the detector");
            return null;
        }

        // set up the frame on the image and parse the barcode
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        // we no longer need the cached image, so get rid of it for the next run
        getBaseContext().deleteFile("barcode.jpg");

        // create Upc string
        String barcode_id;
        if (barcodes.size() != 0) {
            // the Barcode API can fetch multiple barcodes in one image
            // we only require the first one detected
            Barcode barcode = barcodes.valueAt(0);

            // remove the leading 0 to allow searching with the UpcDatabase
            if (barcode.rawValue.charAt(0) == '0') {
                barcode_id = barcode.rawValue.substring(1);
            } else {
                barcode_id = barcode.rawValue;
            }

        } else {
            // repeat the image capture process if no barcode was parsed
            Log.e(LOG_TAG, "Bitmap did not return barcode");
            barcode_id = "00000000000";

            Toast.makeText(getApplicationContext(), "Bad barcode image. Try again.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        Log.d(LOG_TAG, barcode_id);
        return new UpcItemLoader(this, barcode_id);
    }

    @Override
    public void onLoadFinished(Loader<UpcItem> loader, UpcItem data) {
        ImageView imageView = (ImageView) findViewById(R.id.iv_results_thumbnail);

        if(data != null) {
            upcItem = data;
            Log.d(LOG_TAG, upcItem.getId());
        }

        // now that UpcItem has been initialized, it is possible to apply an ItemType
        upcItem.applyItemType();

        // display thumbnail image
        Bitmap thumbnailBitmap = BitmapFactory.decodeByteArray(upcItem.getThumbnail(), 0, upcItem.getThumbnail().length);
        imageView.setImageBitmap(thumbnailBitmap);

        // text fields in UI
        TextView nameView = (TextView) findViewById(R.id.tv_results_item_name);
        TextView idView = (TextView) findViewById(R.id.tv_results_item_id);
        TextView typeView = (TextView) findViewById(R.id.tv_results_item_type);
        TextView expDateView = (TextView) findViewById(R.id.tv_results_item_expiration_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");

        nameView.setText(upcItem.getName());
        idView.setText(upcItem.getId());
        typeView.setText(upcItem.getItemType().toString());
        expDateView.setText(dateFormat.format(upcItem.getExpDate().getTime()));
    }

    @Override
    public void onLoaderReset(Loader<UpcItem> loader) {
    }
}
