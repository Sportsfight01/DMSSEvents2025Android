package com.digitalminds.dmssevent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.GridSpacingItemDecoration;
import com.digitalminds.dmssevent.interfaces.AddPlayerCallBack;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.interfaces.RemovePlayerInterface;
import com.digitalminds.dmssevent.models.MyBookingsModel;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;

import java.util.ArrayList;

public class MyBookingsRecycleViewAdapter extends RecyclerView.Adapter<MyBookingsRecycleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<MyBookingsModel> myBookingsModelArrayList;
    PlayerListGridAdapter playerListGridAdapter;
    ArrayList<SelectedPlayersResultModel> playerDetailsModelArrayList;
    BookingInterface bookingInterface;
    AddPlayerCallBack addPlayerCallBack;
    RemovePlayerInterface removePlayerInterface;
    int rotation = 0;
    int myBookingOrPending;

    public MyBookingsRecycleViewAdapter(Context context, ArrayList<MyBookingsModel> myBookingsModelArrayList, BookingInterface bookingInterface, RemovePlayerInterface removePlayerInterface, AddPlayerCallBack addPlayerCallBack, int myBookingOrPending) {
        this.context = context;
        this.myBookingsModelArrayList = myBookingsModelArrayList;
        this.bookingInterface = bookingInterface;
        this.removePlayerInterface = removePlayerInterface;
        this.addPlayerCallBack = addPlayerCallBack;
        this.myBookingOrPending = myBookingOrPending;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bookings_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.acceptLinearLayout.setVisibility(View.GONE);
        holder.gameNameTextView.setText(myBookingsModelArrayList.get(position).getBookingDTO().getGame());
        holder.gameDateTextView.setText(myBookingsModelArrayList.get(position).getBookingDTO().getBookedDay() + " " +
                myBookingsModelArrayList.get(position).getBookingDTO().getBookedDDMMM());
        holder.gameSlotTextView.setText(myBookingsModelArrayList.get(position).getBookedSlot());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(context, R.dimen.grid_size);
        // holder.recyclerViewGrid.addItemDecoration(itemDecoration);
        holder.recyclerViewGrid.setLayoutManager(gridLayoutManager);
        // holder.recyclerViewGrid.setLayoutManager(new GridLayoutManager(context, 1));
        playerDetailsModelArrayList = new ArrayList<SelectedPlayersResultModel>();
        playerDetailsModelArrayList.addAll(myBookingsModelArrayList.get(position).getPlayersDetails());
        int bookingPositionId = myBookingsModelArrayList.get(position).getBookingDTO().getBookingID();
        String gameName = myBookingsModelArrayList.get(position).getBookingDTO().getGame();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);
        Glide.with(context).load(ConstantKeys.getAllImagesUrl + myBookingsModelArrayList.get(position).getResourceForBooking().getResourceIcon()).apply(options.circleCropTransform()).into(holder.gameImageView);
        if (myBookingOrPending == 1) {
            if (playerDetailsModelArrayList != null) {
                rotation=0;
                for(int i=0;i<playerDetailsModelArrayList.size();i++){
                    if (playerDetailsModelArrayList.get(i).getStatus() == 1) {
                        rotation++;
                    }
                }
                        if(rotation==3){
                            holder.addImageView.setVisibility(View.GONE);
                        }else{
                            holder.addImageView.setVisibility(View.VISIBLE);
                        }

            }


        } else if (myBookingOrPending == 2) {
            holder.addImageView.setVisibility(View.GONE);
        }
        playerListGridAdapter = new PlayerListGridAdapter(context, gameName, bookingPositionId, playerDetailsModelArrayList, bookingInterface, removePlayerInterface, myBookingOrPending);
        holder.recyclerViewGrid.setAdapter(playerListGridAdapter);
        holder.infoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerDetailsDialog(position);
            }
        });
        holder.addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayerCallBack.addMorePlayerClick(position, bookingPositionId, gameName);
            }
        });


    }

    private void openPlayerDetailsDialog(int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_booking_dialog);
        TextView dialogTitleTextView = (TextView) dialog.findViewById(R.id.dialogTitleTextView);
        TextView bookingStatusTextView = (TextView) dialog.findViewById(R.id.bookingStatusTextView);
        TextView reasonTextView = (TextView) dialog.findViewById(R.id.reasonTextView);
        TextView oKTextView = (TextView) dialog.findViewById(R.id.oKTextView);
        if (myBookingsModelArrayList.get(position).getSlotStatus() == 1) {
            bookingStatusTextView.setText("Pending");
            reasonTextView.setText(myBookingsModelArrayList.get(position).getSlotStatusReason());
        } else if (myBookingsModelArrayList.get(position).getSlotStatus() == 2) {
            bookingStatusTextView.setText("Booking Confirmed");
            reasonTextView.setText(myBookingsModelArrayList.get(position).getSlotStatusReason());
        } else if (myBookingsModelArrayList.get(position).getSlotStatus() == 0) {
            bookingStatusTextView.setText("Not Booked");
            reasonTextView.setText(myBookingsModelArrayList.get(position).getSlotStatusReason());
        } else if (myBookingsModelArrayList.get(position).getSlotStatus() == 3) {
            bookingStatusTextView.setText("Conflict");
            reasonTextView.setText(myBookingsModelArrayList.get(position).getSlotStatusReason());
        } else if (myBookingsModelArrayList.get(position).getSlotStatus() == 4) {
            bookingStatusTextView.setText("Booking Confirmed");
            reasonTextView.setText(myBookingsModelArrayList.get(position).getSlotStatusReason());
        }

        oKTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return myBookingsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView gameDateTextView, gameNameTextView, dateNumberTextView, dayTextView, gameSlotTextView;// init the item view's
        ImageView gameImageView, infoImageView, addImageView;
        int width, height;
        RecyclerView recyclerViewGrid;
        LinearLayout acceptLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            gameSlotTextView = (TextView) itemView.findViewById(R.id.gameSlotTextView);
            gameDateTextView = (TextView) itemView.findViewById(R.id.gameDateTextView);
            gameNameTextView = (TextView) itemView.findViewById(R.id.gameNameTextView);
            dateNumberTextView = (TextView) itemView.findViewById(R.id.dateNumberTextView);
            dayTextView = (TextView) itemView.findViewById(R.id.dayTextView);
            gameImageView = (ImageView) itemView.findViewById(R.id.gameImageView);
            infoImageView = (ImageView) itemView.findViewById(R.id.infoImageView);
            addImageView = (ImageView) itemView.findViewById(R.id.addImageView);
            recyclerViewGrid = (RecyclerView) itemView.findViewById(R.id.recyclerViewGrid);
            acceptLinearLayout = (LinearLayout) itemView.findViewById(R.id.acceptLinearLayout);

        }
    }
}
