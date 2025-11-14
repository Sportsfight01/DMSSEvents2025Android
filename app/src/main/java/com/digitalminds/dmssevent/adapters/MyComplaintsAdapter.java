package com.digitalminds.dmssevent.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.util.ComplaintsData;

import java.util.ArrayList;
import java.util.List;

public class MyComplaintsAdapter
        extends RecyclerView.Adapter<MyComplaintsAdapter.ItemViewHolder> {

//    private List<ComplaintsData> itemList;
    private OnItemClickListener listener;

    private List<ComplaintsData> fullList;
    private List<ComplaintsData> filteredList;

    public interface OnItemClickListener {
        void onItemClick(ComplaintsData item);
    }

    public MyComplaintsAdapter(List<ComplaintsData> itemList, OnItemClickListener listener) {
//        this.itemList = itemList;
        this.listener = listener;

        this.fullList = new ArrayList<>(itemList);
        this.filteredList = itemList;

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_complaint_type, tv_category_type,tv_su_category_type,tv_status,tv_ticket_id,tv_area,updated_time;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_area = itemView.findViewById(R.id.tv_area);
            tv_category_type = itemView.findViewById(R.id.tv_category_type);
            tv_su_category_type = itemView.findViewById(R.id.tv_su_category_type);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_ticket_id = itemView.findViewById(R.id.tv_ticket_id);
            updated_time = itemView.findViewById(R.id.updated_time);
        }

        public void bind(final ComplaintsData item, final OnItemClickListener listener) {
            tv_area.setText(item.getAreaName());
            tv_category_type.setText(item.getCategoryName());
            tv_su_category_type.setText(item.getSubCategoryName());
            tv_category_type.setText(item.getCategoryName());
            tv_status.setText(item.getCurrentStatus());
            updated_time.setText(item.getLastUpdatedOn());
            tv_ticket_id.setText(item.getComplaintType()+" #"+item.getTicketId());
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actvity_my_complaints_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(filteredList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(fullList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (ComplaintsData item : fullList) {
                if (item.getCategoryName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getSubCategoryName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getComplaintType().toLowerCase().contains(lowerCaseQuery) ||
                        item.getEmployeeName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getAreaName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getCurrentStatus().toLowerCase().contains(lowerCaseQuery) ||
                        item.getTicketId().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}


