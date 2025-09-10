package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.models.CommentsListModel;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 13-02-2018.
 */

public class CommentListAdapter extends BaseAdapter {
    Context context;
    ArrayList<CommentsListModel> commentsListModelArrayList;

    public CommentListAdapter(Context context, ArrayList<CommentsListModel> commentsListModelArrayList) {
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
            rowView = inflater.inflate(R.layout.commnet_list_item, null);
        } else {
            rowView = convertView;
        }
        holder.textViewName = (TextView) rowView.findViewById(R.id.textViewName);
        holder.textViewCommentDescr = (TextView) rowView.findViewById(R.id.textViewCommentDescr);
        holder.textViewCommentDescr.setText("Commented as : "+commentsListModelArrayList.get(position).getComment());
        holder.textViewName.setText(commentsListModelArrayList.get(position).getEmployeeName());
        return rowView;
    }

    public class Holder {
        TextView textViewName, textViewCommentDescr;
    }
}
