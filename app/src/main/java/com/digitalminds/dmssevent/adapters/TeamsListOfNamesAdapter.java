package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.models.GetTeamMembersModel;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 02-02-2018.
 */

public class TeamsListOfNamesAdapter extends BaseAdapter {
    Context context;
    ArrayList<GetTeamMembersModel> getTeamMembersModelArrayList;

    public TeamsListOfNamesAdapter(Context context, ArrayList<GetTeamMembersModel> getTeamMembersModelArrayList) {
        this.context = context;
        this.getTeamMembersModelArrayList = getTeamMembersModelArrayList;
    }

    @Override
    public int getCount() {
        return getTeamMembersModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return getTeamMembersModelArrayList.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rootView=convertView;
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(rootView==null){
            rootView=inflater.inflate(R.layout.teamlist_item,null);
        }else{
            rootView=convertView;
        }
        holder.textViewNames=(TextView)rootView.findViewById(R.id.textViewNames);
        holder.textViewNames.setText(getTeamMembersModelArrayList.get(position).getNames());
        return rootView;
    }
    public class Holder{
        TextView textViewNames;
    }
}
