package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;

import java.util.ArrayList;


public class BottomListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> commentsListModelArrayList;

    public BottomListViewAdapter(Context context, ArrayList<String> commentsListModelArrayList) {
        this.context = context;
        this.commentsListModelArrayList = commentsListModelArrayList;
    }

    @Override
    public int getCount() {
        return commentsListModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return commentsListModelArrayList.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.bottom_sheet_item, null);
        } else {
            rowView = convertView;
        }
        holder.textViewName = (TextView) rowView.findViewById(R.id.item);
        holder.textViewName.setText(commentsListModelArrayList.get(position));
        return rowView;
    }

    public class Holder {
        TextView textViewName;
    }
}

