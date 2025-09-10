package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalminds.dmssevent.NominateActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.models.EmployeeListBasedOnDepartModel;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 01-03-2018.
 */

public class CustomSpinnerEmploeeAdapter extends BaseAdapter {
Context context;
    ArrayList<EmployeeListBasedOnDepartModel> employeeListBasedOnDepartModels;

    public CustomSpinnerEmploeeAdapter(Context context, ArrayList<EmployeeListBasedOnDepartModel> employeeListBasedOnDepartModels) {
        this.context = context;
        this.employeeListBasedOnDepartModels = employeeListBasedOnDepartModels;
    }

    @Override
    public int getCount() {
        return employeeListBasedOnDepartModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return employeeListBasedOnDepartModels.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
       Holder holder = new Holder();
        View rowView = convertView;
        EmployeeListBasedOnDepartModel employeeListBasedOnDepartModel = employeeListBasedOnDepartModels.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.custom_spinner_item, null);
        } else {
            rowView = convertView;
        }
        holder.textViewSpinner=(TextView)rowView.findViewById(R.id.textViewSpinner);
        if(employeeListBasedOnDepartModel.getDisplayName().equalsIgnoreCase("Select Employee")){
            holder.textViewSpinner.setText(employeeListBasedOnDepartModel.getDisplayName());
        }else{

            if (NominateActivity.isDmssLeader && employeeListBasedOnDepartModel.getLeaderTitle() != null && !TextUtils.isEmpty(employeeListBasedOnDepartModel.getLeaderTitle())){
                holder.textViewSpinner.setText(employeeListBasedOnDepartModel.getDisplayName()+" - "+employeeListBasedOnDepartModel.getLeaderTitle());
            }else {
                holder.textViewSpinner.setText(employeeListBasedOnDepartModel.getDisplayName()+" - "+employeeListBasedOnDepartModel.getEmpID());
            }
           /* if (employeeListBasedOnDepartModel.getLeaderTitle() != null && !TextUtils.isEmpty(employeeListBasedOnDepartModel.getLeaderTitle())){
                holder.textViewSpinner.setText(employeeListBasedOnDepartModel.getDisplayName()+" - "+employeeListBasedOnDepartModel.getLeaderTitle());
            }else {
                holder.textViewSpinner.setText(employeeListBasedOnDepartModel.getDisplayName()+" - "+employeeListBasedOnDepartModel.getEmpID());
            }*/

        }

        return rowView;
    }
    public class Holder{
        TextView textViewSpinner;
        LinearLayout spinnerDropDownHeight;
    }
}
