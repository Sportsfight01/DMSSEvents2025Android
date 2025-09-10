package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.interfaces.GameClickListener;
import com.digitalminds.dmssevent.models.BookingDaysModel;

import java.util.ArrayList;

public class DaysAndDateRecycleViewAdapter extends RecyclerView.Adapter<DaysAndDateRecycleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<BookingDaysModel> bookMyGameModelArrayList;
    int row_index = -1;
    int lastClickedPosition=-1;
    int ToDay;
    boolean firstTimeLoad=true;
    GameClickListener gameClickListener;

    public DaysAndDateRecycleViewAdapter(Context context, ArrayList<BookingDaysModel> bookMyGameModelArrayList, GameClickListener gameClickListener,int ToDay) {
        this.context = context;
        this.bookMyGameModelArrayList = bookMyGameModelArrayList;
        this.gameClickListener = gameClickListener;
        this.ToDay = ToDay;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.days_n_date_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.dayLinearLyForWidth.getLayoutParams();
// Changes the height and width to the specified *pixels*
        int newWidth=params.width=width-80;
        params.width = newWidth/5;
        holder.dayLinearLyForWidth.setLayoutParams(params);
        holder.monDayTextView.setText(bookMyGameModelArrayList.get(position).getDayName());
        holder.mon_date_dayTextView.setText(bookMyGameModelArrayList.get(position).getDdString());
        bookMyGameModelArrayList.get(ToDay).setSelected(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookMyGameModelArrayList.size() > 0) {
                   /* if (position != 0) {
                        firstTimeLoad = false;
                    }*/
                    row_index = position;
                    notifyDataSetChanged();
                    if (lastClickedPosition != position) {
                        gameClickListener.onClick(position,"Day");
                    }
                    // imageViewPagerDialog(selectedPosition);
                }
            }
        });


        if (row_index == position) {
            lastClickedPosition = row_index;
            holder.monDayLinearLayout.setBackgroundResource(R.drawable.gradient_corner_radius_book);
            holder.mon_date_dayTextView.setTextColor(Color.rgb(255, 255, 255));
            holder.monDayTextView.setTextColor(Color.rgb(255, 255, 255));
        } else {
            if (firstTimeLoad == true&& bookMyGameModelArrayList.get(position).isSelected()) {
                holder.monDayLinearLayout.setBackgroundResource(R.drawable.gradient_corner_radius_book);
                holder.mon_date_dayTextView.setTextColor(Color.rgb(255, 255, 255));
                holder.monDayTextView.setTextColor(Color.rgb(255, 255, 255));
                firstTimeLoad=false;

            } else {
               holder.monDayLinearLayout.setBackgroundResource(R.drawable.days_corner_grey);
                //holder.dayLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.greyLight));
                holder.mon_date_dayTextView.setTextColor(Color.BLACK);
                holder.monDayTextView.setTextColor(Color.BLACK);
            }
        }

    }

    @Override
    public int getItemCount() {
        return bookMyGameModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mon_date_dayTextView;
        LinearLayout monDayLinearLayout,dayLinearLyForWidth;
        TextView monDayTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            monDayTextView = (TextView) itemView.findViewById(R.id.monDayTextView);
            mon_date_dayTextView = (TextView) itemView.findViewById(R.id.mon_date_dayTextView);
            monDayLinearLayout=(LinearLayout)itemView.findViewById(R.id.monDayLinearLayout);
            dayLinearLyForWidth=(LinearLayout)itemView.findViewById(R.id.dayLinearLyForWidth);
        }
    }
}
