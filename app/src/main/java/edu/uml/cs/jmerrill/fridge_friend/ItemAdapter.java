package edu.uml.cs.jmerrill.fridge_friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sam on 12/3/2016.
 */

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


        UpcItem currentItem = (UpcItem) getItem(position);

            TextView nameTextView = (TextView) listItemView.findViewById(R.id.tv_item_name);
            nameTextView.setText(currentItem.getName());

            TextView shelfLifeTextView = (TextView) listItemView.findViewById(R.id.tv_item_shelf_life);
            nameTextView.setText(currentItem.getExpDate().toString());

        return listItemView;
    }
}
