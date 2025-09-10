package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.models.NominationCriteria;

import java.util.ArrayList;

/**
 * Created by Jaya.Krishna on 27-02-2018.
 */

public class NominationsSpinnerAdapter extends BaseAdapter {
    private ArrayList<NominationCriteria> myPlaceJobDetailses;
    Context context;

    public NominationsSpinnerAdapter(Context context, ArrayList<NominationCriteria> myPlaceJobDetailses) {
        super();
        this.myPlaceJobDetailses = myPlaceJobDetailses;
        this.context = context;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return myPlaceJobDetailses.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return myPlaceJobDetailses.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return myPlaceJobDetailses.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        NominationCriteria myPlaceJobDetails = myPlaceJobDetailses.get(position);
        LayoutInflater lif = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /***
         * Checking if View is null if not We will display View directly.
         */
        if (convertView == null) {
            /****** Inflate contacts_list_item.xml file for each row ( Defined below ) *******/
            view = lif.inflate(R.layout.nominations_spinner_item, null);

        } else {
            view = convertView;
        }
        TextView nominationsSpinnerTextView = (TextView) view.findViewById(R.id.nominationsSpinnerTextView);
        nominationsSpinnerTextView.setText(myPlaceJobDetails.getTitle());

        return view;
    }

}