package edu.uml.cs.jmerrill.fridge_friend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemAdapter extends ArrayAdapter<UpcItem>{

    public ItemAdapter(Context context, ArrayList<UpcItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Object currentObject = getItem(position);
        Log.d(getClass().getSimpleName(), currentObject.getClass().toString());

        UpcItem currentItem = (UpcItem) currentObject;

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.tv_item_name);
        nameTextView.setText(currentItem.getName());

        TextView expirationDateTextView = (TextView) listItemView.findViewById(R.id.tv_item_expiration_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");
        expirationDateTextView.setText("Exp. Date: " + dateFormat.format(currentItem.getExpDate().getTime()));

        //ImageView thumbnailImageView = (ImageView) listItemView.findViewById(R.id.iv_lv_thumbnail);

        //thumbnailImageView.setImageBitmap();

        return listItemView;
    }
}
