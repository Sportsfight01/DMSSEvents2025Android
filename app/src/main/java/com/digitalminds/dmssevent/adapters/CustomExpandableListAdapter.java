package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.models.NominationListModel;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 05-03-2018.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    ArrayList<NominationListModel> nominationListModelArrayList;

    public CustomExpandableListAdapter(Context context, ArrayList<NominationListModel> nominationListModelArrayList) {
        this.context = context;
        this.nominationListModelArrayList = nominationListModelArrayList;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.nominationListModelArrayList.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        String listText;
       // final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if(nominationListModelArrayList.get(expandedListPosition).getAwardName().equalsIgnoreCase("Business Unit of the Year")){
            listText=nominationListModelArrayList.get(expandedListPosition).getAwardName()+" - "+nominationListModelArrayList.get(expandedListPosition).getDeptName();
        }else{
            listText=nominationListModelArrayList.get(expandedListPosition).getAwardName()+" - "+nominationListModelArrayList.get(expandedListPosition).getEmployeeName();
        }

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_expandable, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(listText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.nominationListModelArrayList.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.nominationListModelArrayList.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
       // String listTitle = (String) getGroup(listPosition);
        String listTitle = "Applied Nomination List";
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_expandable1, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem1);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
