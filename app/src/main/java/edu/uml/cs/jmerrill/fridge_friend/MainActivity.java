package edu.uml.cs.jmerrill.fridge_friend;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Filter;

import static edu.uml.cs.jmerrill.fridge_friend.R.styleable.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private File imgFile;

    private FloatingActionButton fab;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public DBHelper productdb;

    private FilterSortSettings mSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("Settings")) {
            mSettings = (FilterSortSettings) getIntent().getSerializableExtra("Settings");
        } else {
            mSettings = new FilterSortSettings();
        }

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    imgFile = File.createTempFile("barcode", ".jpg", storageDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri tempUri = FileProvider.getUriForFile(getParent(), "edu.uml.cs.jmerrill.fridge_friend.fileprovider", imgFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        //initialize db
        productdb = new DBHelper(this);

        final ListView lvMain = (ListView) findViewById(R.id.lv_main);

        //list of all prods
        //ArrayList array_list = productdb.getAllProducts();

        ArrayList<UpcItem> array_list = null;

        if(productdb.numberOfRows() > 0) {
            array_list = productdb.getAllProducts();
            ItemAdapter adapter = new ItemAdapter(this, array_list);
            lvMain.setAdapter(adapter);
        }

    /*    //To test with all different ItemType options
        ArrayList<UpcItem> array_list = new ArrayList<UpcItem>();
        array_list.add(new UpcItem("Milk", "011111555559", ItemType.DAIRY));
        array_list.add(new UpcItem("Chicken", "012345678900", ItemType.MEAT));
        array_list.add(new UpcItem("Banana", "055555999991", ItemType.PRODUCE));
        array_list.add(new UpcItem("Muffin", "031415920160", ItemType.BAKERY));
        array_list.add(new UpcItem("Tuna", "098765432150", ItemType.PACKAGED));
*/
        ArrayList<UpcItem> filteredList = new ArrayList<UpcItem>();
        for (int i = 0; i < array_list.size(); ++i) {
            if (mSettings.getDairySetting()) {
                if (array_list.get(i).getItemType() == ItemType.DAIRY) {
                    filteredList.add(array_list.get(i));
                }
            }
            if (mSettings.getProduceSetting()) {
                if (array_list.get(i).getItemType() == ItemType.PRODUCE) {
                    filteredList.add(array_list.get(i));
                }
            }
            if (mSettings.getMeatSetting()) {
                if (array_list.get(i).getItemType() == ItemType.MEAT) {
                    filteredList.add(array_list.get(i));
                }
            }
            if (mSettings.getBakerySetting()) {
                if (array_list.get(i).getItemType() == ItemType.BAKERY) {
                    filteredList.add(array_list.get(i));
                }
            }
            if (mSettings.getPackagedSetting()) {
                if (array_list.get(i).getItemType() == ItemType.PACKAGED) {
                    filteredList.add(array_list.get(i));
                }
            }
        }

        switch(mSettings.getSortBySetting()) {
            case DateAdded:
                Collections.sort(filteredList, new Comparator<UpcItem>() {
                    @Override
                    public int compare(UpcItem o1, UpcItem o2) {
                        //return (int) o1.getDateAdded().getTimeInMillis() - (int) o2.getDateAdded().getTimeInMillis();
                        return o1.getDateAdded().compareTo(o2.getDateAdded());
                    }
                });
                break;
            case Name:
                Collections.sort(filteredList, new Comparator<UpcItem>() {
                    @Override
                    public int compare(UpcItem o1, UpcItem o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                break;
            case ExpirationDate:
                Collections.sort(filteredList, new Comparator<UpcItem>() {
                    @Override
                    public int compare(UpcItem o1, UpcItem o2) {
                        //return (int) o1.getExpDate().getTimeInMillis() - (int) o2.getExpDate().getTimeInMillis();
                        return o1.getExpDate().compareTo(o2.getExpDate());
                    }
                });
                break;
            default:
                break;
        }

        ItemAdapter adapter = new ItemAdapter(this, filteredList);
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ItemInfo.class);
                UpcItem itemToPass = (UpcItem) lvMain.getItemAtPosition(position);
                intent.putExtra("UpcItem", (Serializable) itemToPass);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra("Settings", mSettings);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (imgFile.getAbsoluteFile().exists()) {
                        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);

                        // converts captured bitmap to byte array for passing as extra
                        //Bitmap bitmap = (Bitmap) data.getExtras().get("data");


                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        if (bitmap == null) {
                            Log.d("MainActivity", "oh no the bitmap is null");
                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        if (bitmap != null) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 35, stream);
                        }
                        byte[] arr = stream.toByteArray();

                        Log.d("MainActivity", Arrays.toString(arr));

                        intent.putExtra("image", arr);

                        //Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        //Bitmap bm = decodeSampledBitmapFromFile(imgFile.getAbsolutePath(), 100, 100);
                        Log.d("MainActivity", "Entering ResultActivity");
                        startActivity(intent);
                    } else {
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