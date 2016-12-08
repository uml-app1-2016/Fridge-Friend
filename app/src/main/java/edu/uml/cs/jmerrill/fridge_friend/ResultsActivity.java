package edu.uml.cs.jmerrill.fridge_friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
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

import java.io.File;
import java.text.SimpleDateFormat;

public class ResultsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<UpcItem> {

    File imgFile;
    DBHelper productdb;
    public UpcItem upcItem;


    private static final String LOG_TAG = ResultsActivity.class.getSimpleName();
    private static final int UPC_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ResultsActivity", "Entered ResultsActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

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
        productdb = new DBHelper(this);
        Button btnAddItem = (Button) findViewById(R.id.btn_add_item);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productdb.insertProduct(upcItem);
                //productdb.deleteProduct(upcItem);
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<UpcItem> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "yup the loader's getting made");

        imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "barcode.jpg");


        if(getIntent().getExtras() == null) {
            Log.d(LOG_TAG, "It's null oh no");
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

        // set up the frame
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        getBaseContext().deleteFile("barcode.jpg");

        String barcode_id;
        if (barcodes.size() != 0) {
            // detect barcode
            Barcode barcode = barcodes.valueAt(0);

            if (barcode.rawValue.charAt(0) == '0') {
                Log.d(LOG_TAG, "helllooooooo");
                barcode_id = barcode.rawValue.substring(1);
                //barcode_id = barcode.rawValue;
            } else {
                barcode_id = barcode.rawValue;
            }

        } else {
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
        ImageView imageView = (ImageView) findViewById(R.id.imgview);

        if(data != null) {
            upcItem = data;
            Log.d(LOG_TAG, upcItem.getId());
        }

        upcItem.applyItemType();

        imageView.setImageDrawable(upcItem.getThumbnail());

        TextView nameView = (TextView) findViewById(R.id.item_name);
        TextView idView = (TextView) findViewById(R.id.item_id);
        TextView typeView = (TextView) findViewById(R.id.item_type);
        TextView expDateView = (TextView) findViewById(R.id.item_exp_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");

        nameView.setText(upcItem.getName());
        idView.setText(upcItem.getId());
        typeView.setText(upcItem.getItemType().toString());
        //expDateView.setText(dateFormat.format(upcItem.getExpDate().getTime()));



    }

    @Override
    public void onLoaderReset(Loader<UpcItem> loader) {
    }
}
