package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.models.PendingBookimgsModel;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;

import java.util.ArrayList;

public class PendingBookingsRecycleViewAdapter extends RecyclerView.Adapter<PendingBookingsRecycleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<PendingBookimgsModel> pendingBookimgsModelArrayList;
    BookNoficPlayerListAdapter playerListGridAdapter;
    ArrayList<SelectedPlayersResultModel> playerDetailsModelArrayList;
    BookingInterface bookingInterface;
    int bookingPositionId;


    public PendingBookingsRecycleViewAdapter(Context context, ArrayList<PendingBookimgsModel> pendingBookimgsModelArrayList,BookingInterface bookingInterface) {
        this.context = context;
        this.pendingBookimgsModelArrayList = pendingBookimgsModelArrayList;
        this.bookingInterface=bookingInterface;
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
        holder.infoImageView.setVisibility(View.GONE);
        holder.addImageView.setVisibility(View.GONE);
        holder.gameNameTextView.setText(pendingBookimgsModelArrayList.get(position).getBookingDTO().getGame());
        holder.gameDateTextView.setText(pendingBookimgsModelArrayList.get(position).getBookingDTO().getBookedDay()+" "+
                pendingBookimgsModelArrayList.get(position).getBookingDTO().getBookedDDMMM());
        holder.gameSlotTextView.setText(pendingBookimgsModelArrayList.get(position).getBookedSlot());
        holder.recyclerViewGrid.setLayoutManager(new GridLayoutManager(context, 1));
        playerDetailsModelArrayList=new ArrayList<SelectedPlayersResultModel>();
        playerDetailsModelArrayList.addAll(pendingBookimgsModelArrayList.get(position).getPlayersDetails());
        int bookingPosition=pendingBookimgsModelArrayList.get(position).getBookingDTO().getBookingID();
        String gameName=pendingBookimgsModelArrayList.get(position).getBookingDTO().getGame();
        playerListGridAdapter=new BookNoficPlayerListAdapter(context,bookingPosition,gameName,playerDetailsModelArrayList,bookingInterface);
        holder.recyclerViewGrid.setAdapter(playerListGridAdapter);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);
        Glide.with(context).load(ConstantKeys.getAllImagesUrl + pendingBookimgsModelArrayList.get(position).getResourceForBooking().getResourceIcon()).apply(options.circleCropTransform()).into(holder.gameImageView);

       String userID = Integer.toString(DmsSharedPreferences.getUserDetails(context).getId());
        holder.acceptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String bookingPlayerId=Integer.toString(playerDetailsModelArrayList.get(position).getId());
                int bookingPositionId=pendingBookimgsModelArrayList.get(position).getBookingDTO().getBookingID();
                String bookingStatus=Integer.toString(1);
                bookingInterface.onClickItem(position,bookingPositionId,"accept",userID,bookingStatus);
            }
        });
        holder.rejectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String bookingIds=Integer.toString(playerDetailsModelArrayList.get(position).getId());
                int bookingPositionId=pendingBookimgsModelArrayList.get(position).getBookingDTO().getBookingID();
                String bookingStatus=Integer.toString(2);
                bookingInterface.onClickItem(position,bookingPositionId,"reject",userID,bookingStatus);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingBookimgsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView gameDateTextView, gameNameTextView, dateNumberTextView, dayTextView,rejectTextView,acceptTextView,gameSlotTextView;// init the item view's
        ImageView gameImageView,infoImageView;
        ImageView addImageView;
        int width, height;
        RecyclerView recyclerViewGrid;

        public MyViewHolder(View itemView) {
            super(itemView);
// get the reference of item view's
            gameDateTextView = (TextView) itemView.findViewById(R.id.gameDateTextView);
            gameSlotTextView = (TextView) itemView.findViewById(R.id.gameSlotTextView);
            acceptTextView = (TextView) itemView.findViewById(R.id.acceptTextView);
            rejectTextView = (TextView) itemView.findViewById(R.id.rejectTextView);
            gameNameTextView = (TextView) itemView.findViewById(R.id.gameNameTextView);
            dateNumberTextView = (TextView) itemView.findViewById(R.id.dateNumberTextView);
            dayTextView = (TextView) itemView.findViewById(R.id.dayTextView);
            gameImageView = (ImageView) itemView.findViewById(R.id.gameImageView);
            infoImageView = (ImageView) itemView.findViewById(R.id.infoImageView);
            addImageView = (ImageView) itemView.findViewById(R.id.addImageView);
            recyclerViewGrid = (RecyclerView) itemView.findViewById(R.id.recyclerViewGrid);
        }
    }
}
