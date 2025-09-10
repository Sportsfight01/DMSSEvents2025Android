package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.models.DepartmentListModel;
import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 01-03-2018.
 */

public class CustomSpinnerAdapter extends BaseAdapter {
Context context;
    ArrayList<DepartmentListModel> departmentListModelArrayList;

    public CustomSpinnerAdapter(Context context, ArrayList<DepartmentListModel> departmentListModelArrayList) {
        this.context = context;
        this.departmentListModelArrayList = departmentListModelArrayList;
    }

    @Override
    public int getCount() {
        return departmentListModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return departmentListModelArrayList.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
       Holder holder = new Holder();
        View rowView = convertView;
        DepartmentListModel departmentListModel = departmentListModelArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.custom_spinner_item, null);
        } else {
            rowView = convertView;
        }
        holder.textViewSpinner=(TextView)rowView.findViewById(R.id.textViewSpinner);
        holder.textViewSpinner.setText(departmentListModel.getDeptName());
        return rowView;
    }
    public class Holder{
        TextView textViewSpinner;
    }
}
