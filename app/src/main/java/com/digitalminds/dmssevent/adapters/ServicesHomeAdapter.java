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
import com.digitalminds.dmssevent.models.ServicesData;

import java.util.ArrayList;

public class ServicesHomeAdapter extends RecyclerView.Adapter<ServicesHomeAdapter.RecyclerViewHolder> {
    public interface ItemClickListener {
        void onItemClick(ServicesData item, int position);
    }
    private ArrayList<ServicesData> courseDataArrayList;
    private Context mcontext;
    private ItemClickListener onItemClickListener;
    public void setItemClickListener(ItemClickListener clickListener) {
        onItemClickListener = clickListener;
    }
    public ServicesHomeAdapter(ArrayList<ServicesData> recyclerDataArrayList, Context mcontext) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_home_adaptercard_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        ServicesData recyclerData = courseDataArrayList.get(position);
        holder.courseTV.setText(recyclerData.getTitle());
        holder.courseIV.setImageResource(recyclerData.getImgid());
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return courseDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView courseTV;
        private ImageView courseIV;
        private LinearLayout llItem;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTV = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourseIV);
            llItem = itemView.findViewById(R.id.ll_item);

          llItem.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  onItemClickListener.onItemClick(courseDataArrayList.get(getAdapterPosition()),getAdapterPosition());
              }
          });
        }
    }
}
