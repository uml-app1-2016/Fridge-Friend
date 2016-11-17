package edu.uml.cs.jmerrill.fridge_friend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item);

        DBHelper productdb ;

        ImageView ivSample = (ImageView) findViewById(R.id.img_sample);
        Bitmap bmSample = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.sample1);
        ivSample.setImageBitmap(bmSample);

        ImageButton btnCamera = (ImageButton) findViewById(R.id.btn_take_picture);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

       //when barcode is scanned, extract name, type, shelflife, and upc from scanner and save as temp vals. pass these temps to function below to add to db.
        //productdb.insertProduct(scannedname, scannedtype, scannedshelflife, scannedupc))
    }
}
