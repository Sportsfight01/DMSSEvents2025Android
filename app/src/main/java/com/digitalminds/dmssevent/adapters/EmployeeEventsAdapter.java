package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.interfaces.SubAdapterCallBackInterface;
import com.digitalminds.dmssevent.models.ParticipantsModel;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class EmployeeEventsAdapter extends BaseAdapter {
    ArrayList<ParticipantsModel> eventParticipationModelArrayList;
    Context context;
    SubAdapterCallBackInterface subAdapterCallBackInterface;
    String imageUrl = "";

    public EmployeeEventsAdapter(ArrayList<ParticipantsModel> eventParticipationModelArrayList, Context context) {
        this.eventParticipationModelArrayList = eventParticipationModelArrayList;
        this.context = context;
        subAdapterCallBackInterface = (SubAdapterCallBackInterface) context;
    }

    @Override
    public int getCount() {
        return eventParticipationModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return eventParticipationModelArrayList.hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.ratinglist_item, null);
        } else {
            rowView = convertView;
        }

        if (position % 2 != 0) {
            rowView.setBackgroundResource(R.color.greyLight);
        }
        holder.employeeNmae = (TextView) rowView.findViewById(R.id.employeeNmae);
        holder.employeeDepartment = (TextView) rowView.findViewById(R.id.employeeDepartment);
        holder.poleProfileImageView = (ImageView) rowView.findViewById(R.id.poleProfileImageView);
        holder.poleProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subAdapterCallBackInterface.SubAdapterCallBack(eventParticipationModelArrayList.get(position).getProfilePic());
            }
        });
        if (eventParticipationModelArrayList.size() > 0) {

            /*imageUrl = eventParticipationModelArrayList.get(position).getProfilePic();
            // if(eventParticipationModelArrayList.get(position).getProfilePic() .length() >0){
            if ((imageUrl != null) || (!imageUrl.equalsIgnoreCase(""))) {
                //Picasso.with(context).load(eventParticipationModelArrayList.get(position).getProfilePic()).error(R.drawable.profilepic).transform(new CircleTransform()).into(holder.poleProfileImageView);
                Glide.with(context).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(holder.poleProfileImageView);
            }*/
            String employName = eventParticipationModelArrayList.get(position).getDisplayName() + " - " + eventParticipationModelArrayList.get(position).getEmpID();
            String departaName = eventParticipationModelArrayList.get(position).getDeptName();
            holder.employeeNmae.setText(employName);
            holder.employeeDepartment.setText(departaName);

        }
        return rowView;
    }

    public class Holder {
        TextView employeeNmae, employeeDepartment;
        ImageView poleProfileImageView;


    }
}
