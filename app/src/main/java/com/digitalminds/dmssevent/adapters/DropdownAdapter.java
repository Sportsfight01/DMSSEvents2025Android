package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.digitalminds.dmssevent.R;

public class DropdownAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] items;
    private int selectedIndex;

    public DropdownAdapter(Context context, String[] items, int selectedIndex) {
        super(context, R.layout.item_dropdown, items);
        this.context = context;
        this.items = items;
        this.selectedIndex = selectedIndex;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false);
        TextView tvItem = view.findViewById(R.id.tv_item);
        tvItem.setText(items[position]);

        if (position == selectedIndex) {
            tvItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDarkGrey)); // Highlight color
            tvItem.setTextColor(Color.WHITE);
        }

        return view;
    }
}