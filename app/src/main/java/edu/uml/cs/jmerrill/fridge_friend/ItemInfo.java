package edu.uml.cs.jmerrill.fridge_friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class ItemInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        final UpcItem upcItem = (UpcItem) getIntent().getSerializableExtra("UpcItem");

        TextView tvItemName = (TextView) findViewById(R.id.tv_item_info_name);
        tvItemName.setText(upcItem.getName());

        TextView tvDateAdded = (TextView) findViewById(R.id.tv_item_info_date_added);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");
        tvDateAdded.setText(dateFormat.format(upcItem.getDateAdded().getTime()));

        TextView tvItemType = (TextView) findViewById(R.id.tv_item_info_type);
        tvItemType.setText(upcItem.getItemType().toString());

        TextView tvExpirationDate = (TextView) findViewById(R.id.tv_item_info_expiration_date);
        tvExpirationDate.setText(dateFormat.format(upcItem.getExpDate().getTime()));

        TextView tvUpcCode = (TextView) findViewById(R.id.tv_item_info_upc_code);
        tvUpcCode.setText(upcItem.getId());

        ImageView imgThumbnail = (ImageView) findViewById(R.id.img_item_info_thumbnail);
        Bitmap thumbnail =
                BitmapFactory.decodeByteArray(upcItem.getThumbnail(), 0, upcItem.getThumbnail().length);

        imgThumbnail.setImageBitmap(thumbnail);

        Button btnRemoveItem = (Button) findViewById(R.id.btn_item_info_remove_item);
        btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemInfo.this);
                builder.setTitle("Delete this item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete item
                        //dbhelper.deleteProduct(upcItem);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Item has been deleted.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ItemInfo.this, MainActivity.class));
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

    }

}
