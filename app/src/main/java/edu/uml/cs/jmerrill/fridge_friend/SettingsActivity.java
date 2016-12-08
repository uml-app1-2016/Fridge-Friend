package edu.uml.cs.jmerrill.fridge_friend;

import android.content.DialogInterface;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.query.Filter;

public class SettingsActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox cbFilterDairy = (CheckBox) findViewById(R.id.cb_filter_dairy);
        CheckBox cbFilterProduce = (CheckBox) findViewById(R.id.cb_filter_produce);
        CheckBox cbFilterMeat = (CheckBox) findViewById(R.id.cb_filter_meat);
        CheckBox cbFilterBakery = (CheckBox) findViewById(R.id.cb_filter_bakery);
        CheckBox cbFilterPackaged = (CheckBox) findViewById(R.id.cb_filter_packaged);
        RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radio_group_sort_by);

        FilterSortSettings initialSettings;
        if (getIntent().hasExtra("Settings")) {
            initialSettings = (FilterSortSettings) getIntent().getSerializableExtra("Settings");
        } else {
            initialSettings = new FilterSortSettings();
        }

        cbFilterDairy.setChecked(initialSettings.getDairySetting());
        cbFilterProduce.setChecked(initialSettings.getProduceSetting());
        cbFilterMeat.setChecked(initialSettings.getMeatSetting());
        cbFilterBakery.setChecked(initialSettings.getBakerySetting());
        cbFilterPackaged.setChecked(initialSettings.getPackagedSetting());
        switch (initialSettings.getSortBySetting()) {
            case DateAdded:
                rgSortBy.check(R.id.radio_sort_date_added);
                break;
            case Name:
                rgSortBy.check(R.id.radio_sort_name);
                break;
            case ExpirationDate:
                rgSortBy.check(R.id.radio_sort_expiration_date);
                break;
            default:
                break;
        }

        SortBy initialSortSetting;
        switch(rgSortBy.getCheckedRadioButtonId()) {
            case R.id.radio_sort_name:
                initialSortSetting = SortBy.Name;
                break;
            case R.id.radio_sort_date_added:
                initialSortSetting = SortBy.DateAdded;
                break;
            case R.id.radio_sort_expiration_date:
                initialSortSetting = SortBy.ExpirationDate;
                break;
            default:
                initialSortSetting = SortBy.DateAdded;
        }

        final FilterSortSettings settings =
                new FilterSortSettings(cbFilterDairy.isChecked(), cbFilterProduce.isChecked(),
                        cbFilterMeat.isChecked(), cbFilterBakery.isChecked(),
                        cbFilterPackaged.isChecked(), initialSortSetting);

        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_sort_name:
                        settings.setSortBySetting(SortBy.Name);
                        break;
                    case R.id.radio_sort_date_added:
                        settings.setSortBySetting(SortBy.DateAdded);
                        break;
                    case R.id.radio_sort_expiration_date:
                        settings.setSortBySetting(SortBy.ExpirationDate);
                        break;
                }
            }
        });

        cbFilterDairy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setDairySetting(isChecked);
            }
        });


        cbFilterProduce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setProduceSetting(isChecked);
            }
        });


        cbFilterMeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setMeatSetting(isChecked);
            }
        });


        cbFilterBakery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setBakerySetting(isChecked);
            }
        });


        cbFilterPackaged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setPackagedSetting(isChecked);
            }
        });

        Button btnDefaultSettings = (Button) findViewById(R.id.btn_default_settings);
        btnDefaultSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Reset to default settings?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        intent.putExtra("Settings", new FilterSortSettings());
                        Toast.makeText(getApplicationContext(), "Reset to default settings.", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button btnSaveSettings = (Button) findViewById(R.id.btn_save_settings);
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Save settings?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        intent.putExtra("Settings", settings);
                        Toast.makeText(getApplicationContext(), "Settings saved.", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button btnClearDatabase = (Button) findViewById(R.id.btn_clear_database);
        btnClearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Delete all items?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete all items
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "All items have been deleted.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Settings Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
