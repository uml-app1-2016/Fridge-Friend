package edu.uml.cs.jmerrill.fridge_friend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;

public class ResultsActivity extends AppCompatActivity {

    File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //image path is Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "barcode.jpg"
        imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "barcode.jpg");

        TextView nameView = (TextView) findViewById(R.id.item_name);
        TextView idView = (TextView) findViewById(R.id.item_id);
        ImageView imageView = (ImageView) findViewById(R.id.imgview);
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.sample1);
        imageView.setImageBitmap(bitmap);

        // create barcode detector
        BarcodeDetector detector =
                new BarcodeDetector.Builder(getApplicationContext())
                    .setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E)
                    .build();

        if (!detector.isOperational()) {
            Log.e("error", "Could not set up the detector");
            return;
        }

        // set up the frame
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        // detect barcode
        Barcode barcode = barcodes.valueAt(0);
        idView.setText(barcode.rawValue);
    }

}
