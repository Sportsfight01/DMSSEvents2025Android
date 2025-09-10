package com.digitalminds.dmssevent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.GridSpacingItemDecoration;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.interfaces.GameClickListener;
import com.digitalminds.dmssevent.models.BookMyGameResultModel;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;

import java.util.ArrayList;

public class TimingsRecycleViewAdapter extends RecyclerView.Adapter<TimingsRecycleViewAdapter.MyViewHolder> {
    ArrayList<BookMyGameResultModel> bookMyGameModelArrayList;
    Context context;
    BookingInterface bookingInterface;
    GameClickListener gameClickListener;
    int setClickableFlag=0;
    int row_index = -1;
    int lastClickedPosition;
    boolean firstTimeLoad=true;
    boolean firstTime=false;
    BookedPlayersDetailsAdapter bookedPlayersDetailsAdapter;
    RecyclerView playersListRecycleView;


    public TimingsRecycleViewAdapter(Context context, ArrayList<BookMyGameResultModel> bookMyGameModelArrayList, GameClickListener gameClickListener) {
        this.context = context;
        this.bookMyGameModelArrayList = bookMyGameModelArrayList;
        this.gameClickListener = gameClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timings_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String bookingSlot=bookMyGameModelArrayList.get(position).getSlot();
        holder.timingsTextView.setText(bookingSlot);
            if (bookMyGameModelArrayList.get(position).getSlotStatus() == 1) {
                holder.timeLinearLayout.setBackgroundResource(R.drawable.pending_bookingborder);
                holder.timingsTextView.setTextColor(context.getResources().getColor(R.color.dmsOrange));
                bookMyGameModelArrayList.get(position).setTimeSlotPending(true);
            } else if (bookMyGameModelArrayList.get(position).getSlotStatus() >= 2) {
                holder.timeLinearLayout.setBackgroundResource(R.drawable.booked_game_border);
                holder.timingsTextView.setTextColor(context.getResources().getColor(R.color.googleRed));
                bookMyGameModelArrayList.get(position).setTimeSlotBookedSuccess(true);
            } else if (bookMyGameModelArrayList.get(position).getSlotStatus() == 0) {
                holder.timeLinearLayout.setBackgroundResource(R.drawable.time_border_grey);
                holder.timingsTextView.setTextColor(Color.BLACK);
                bookMyGameModelArrayList.get(position).setTimeSlotAvailable(true);
        }else if (bookMyGameModelArrayList.get(position).getSlotStatus() == -1) {
                holder.timeLinearLayout.setBackgroundResource(R.drawable.pink_round_radius);
                holder.timingsTextView.setTextColor(context.getResources().getColor(R.color.white));
                bookMyGameModelArrayList.get(position).setTimeSlotOver(true);

            }


        holder.timeClickLinearLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookMyGameModelArrayList.size() > 0) {
                    if (bookMyGameModelArrayList.get(position).isTimeSlotAvailable()) {
                        if (position != 0) {
                            firstTimeLoad = false;
                        }
                        row_index = position;
                        notifyDataSetChanged();
                        if (lastClickedPosition != position || firstTimeLoad == true) {
                            gameClickListener.onClick(position, "Timings");
                        }
                    }else if(bookMyGameModelArrayList.get(position).isTimeSlotBookedSuccess()){
                        openPlayerDetailsDialog(position);
                    }else if(bookMyGameModelArrayList.get(position).isTimeSlotPending()){
                        openPlayerDetailsDialog(position);
                    }else if(bookMyGameModelArrayList.get(position).isTimeSlotOver()){
                        Toast.makeText(context,"You cannot book this slot",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (bookMyGameModelArrayList.get(position).isTimeSlotAvailable()){
            if (row_index == position) {
            lastClickedPosition = row_index;
            holder.timeLinearLayout.setBackgroundResource(R.drawable.timer_border_green);
            holder.timingsTextView.setTextColor(Color.rgb(255, 255, 255));
        } else {
            if (firstTimeLoad == true && bookMyGameModelArrayList.get(position).isSelected()) {
                holder.timeLinearLayout.setBackgroundResource(R.drawable.timer_border_green);
                holder.timingsTextView.setTextColor(Color.rgb(255, 255, 255));
                firstTimeLoad=false;

              } else {
            holder.timeLinearLayout.setBackgroundResource(R.drawable.time_border_grey);
            holder.timingsTextView.setTextColor(Color.BLACK);
             }
        }
        }
    }

    private void openPlayerDetailsDialog(int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.player_details_dialog);
        TextView dialogTitleTextView = (TextView) dialog.findViewById(R.id.dialogTitleTextView);
        playersListRecycleView = (RecyclerView) dialog.findViewById(R.id.playersListRecycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(context, R.dimen.grid_size);
        playersListRecycleView.addItemDecoration(itemDecoration);
        playersListRecycleView.setLayoutManager(gridLayoutManager);
        RecyclerView playersListRecycleView = (RecyclerView) dialog.findViewById(R.id.playersListRecycleView);
        TextView oKTextView = (TextView) dialog.findViewById(R.id.oKTextView);
        dialogTitleTextView.setText("Players List");

        ArrayList<SelectedPlayersResultModel> playerDetailsModels=new ArrayList<SelectedPlayersResultModel>();
        playerDetailsModels.addAll(bookMyGameModelArrayList.get(position).getPlayersDetails());
        if (playerDetailsModels != null) {
            bookedPlayersDetailsAdapter=new BookedPlayersDetailsAdapter(context,playerDetailsModels);
            playersListRecycleView.setAdapter(bookedPlayersDetailsAdapter);
        }
        oKTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void availableSlotBooking() {


    }

    @Override
    public int getItemCount() {
        return bookMyGameModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView timingsTextView;
        LinearLayout timeLinearLayout,timeClickLinearLy;


        public MyViewHolder(View itemView) {
            super(itemView);
// get the reference of item view's

            timingsTextView = (TextView) itemView.findViewById(R.id.timingsTextView);
            timeLinearLayout = (LinearLayout) itemView.findViewById(R.id.timeLinearLayout);
            timeClickLinearLy = (LinearLayout) itemView.findViewById(R.id.timeClickLinearLy);


        }
    }
}
