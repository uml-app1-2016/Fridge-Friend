package edu.uml.cs.jmerrill.fridge_friend;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private File imgFile;

    private FloatingActionButton fab;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public DBHelper productdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "barcode.jpg");
                imgFile = new File(getFilesDir(), "barcode.jpg");
                Uri tempUri = Uri.fromFile(imgFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        //initialize db
        productdb = new DBHelper(this);

        ListView lvMain = (ListView) findViewById(R.id.lv_main);

        //list of all prods
        ArrayList array_list = productdb.getAllProducts();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        lvMain.setAdapter(arrayAdapter);

    /*
        //list of all prods
        ArrayList array_list = productdb.getAllProducts();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mCategories));
       // mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (true) {//imgFile.getAbsoluteFile().exists()) {
                        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                        //Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        //Bitmap bm = decodeSampledBitmapFromFile(imgFile.getAbsolutePath(), 100, 100);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }

    //Resize picture taken to avoid OutOfMemory error when creating bitmap from image. NOT FUNCTIONAL
    /*public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }*/
}
