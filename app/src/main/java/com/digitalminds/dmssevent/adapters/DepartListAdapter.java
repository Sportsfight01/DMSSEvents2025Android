package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.interfaces.ListItemClickListener;
import com.digitalminds.dmssevent.models.DepartmentSearchModel;

import java.util.ArrayList;

public class DepartListAdapter extends RecyclerView.Adapter<DepartListAdapter.MyViewHolder> {
    Context context;
    ArrayList<DepartmentSearchModel> departmentSearchModelArrayList;
    ListItemClickListener listItemClickListener;
    String url="";
    String name="";

    public DepartListAdapter(Context context, ArrayList<DepartmentSearchModel> departmentSearchModelArrayList, ListItemClickListener listItemClickListener) {
        this.context = context;
        this.departmentSearchModelArrayList = departmentSearchModelArrayList;
        this.listItemClickListener=listItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_depart_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
         name=departmentSearchModelArrayList.get(position).getDeptName();
         holder.nameTextView.setText(name);
        holder.listItemLineaLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemClickListener.onClickRecycleItem(position,name,url);
                holder.imageViewSelect.setImageResource(R.drawable.icon_filter_selected);
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentSearchModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView,imageViewSelect;
        TextView nameTextView;
        LinearLayout listItemLineaLy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = (ImageView) itemView.findViewById(R.id.profileImageView);
            imageViewSelect = (ImageView) itemView.findViewById(R.id.imageViewSelect);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            listItemLineaLy = (LinearLayout) itemView.findViewById(R.id.listItemLineaLy);

        }
    }
}
