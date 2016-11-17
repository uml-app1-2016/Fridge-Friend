package edu.uml.cs.jmerrill.fridge_friend;

import android.app.Activity;
import android.content.Intent;
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
                imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "barcode.jpg");
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
                    if (imgFile.exists()) {
                        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
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


}
