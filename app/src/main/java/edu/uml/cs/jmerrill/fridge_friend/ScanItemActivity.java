package edu.uml.cs.jmerrill.fridge_friend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScanItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item);

         DBHelper productdb ;

       //when barcode is scanned, extract name, type, shelflife, and upc from scanner and save as temp vals. pass these temps to function below to add to db.
        //productdb.insertProduct(scannedname, scannedtype, scannedshelflife, scannedupc))
    }
}
