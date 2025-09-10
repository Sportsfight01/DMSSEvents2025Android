package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.models.MyBookingModel;

import java.util.ArrayList;

public class ConfirmBookingRecycleViewAdapter extends RecyclerView.Adapter<ConfirmBookingRecycleViewAdapter.MyViewHolder> {
    ArrayList<MyBookingModel> myBookingModelArrayList;
    Context context;
    String[] nameValues;

    public ConfirmBookingRecycleViewAdapter(Context context, String[] nameValues) {
        this.context = context;
        this.nameValues = nameValues;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_booking_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameTextView.setText(nameValues[position]);
        if(position==nameValues.length-1){
            holder.lineTextView.setVisibility(View.GONE);
        }else{
            holder.lineTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return nameValues.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView,departmentTextView,lineTextView;
        ImageView userImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
// get the reference of item view's
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            lineTextView = (TextView) itemView.findViewById(R.id.lineTextView);
            departmentTextView = (TextView) itemView.findViewById(R.id.departmentTextView);
            userImageView = (ImageView) itemView.findViewById(R.id.userImageView);

        }
    }
}
