package edu.uml.cs.jmerrill.fridge_friend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class ItemInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Intent thisIntent = getIntent();
        final UpcItem upcItem = (UpcItem) getIntent().getSerializableExtra("upcItem");

        TextView tvItemName = (TextView) findViewById(R.id.tv_item_info_name);
        tvItemName.setText(upcItem.getName());

        TextView tvDateAdded = (TextView) findViewById(R.id.tv_item_info_date_added);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");
        tvDateAdded.setText("Date Added: " + dateFormat.format(upcItem.getDateAdded().getTime());

        TextView tvItemType = (TextView) findViewById(R.id.tv_item_info_type);
        tvItemType.setText("Item Type: " + upcItem.getItemType().toString());

        TextView tvExpirationDate = (TextView) findViewById(R.id.tv_item_info_expiration_date);
        tvExpirationDate.setText("Exp. Date: " + dateFormat.format(upcItem.getExpDate().getTime()));

        Button btnRemoveItem = (Button) findViewById(R.id.btn_remove_item);
        btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
