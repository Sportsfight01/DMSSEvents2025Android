package com.digitalminds.dmssevent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.models.PlayerDetailModel;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;

import java.util.ArrayList;

public class BookNoficPlayerListAdapter extends RecyclerView.Adapter<BookNoficPlayerListAdapter.MyViewHolder> {
    ArrayList<PlayerDetailModel> playerDetailModelArrayList;
    Context context;
    ArrayList<SelectedPlayersResultModel> playerDetailsModelArrayList;
    BookingInterface bookingInterface;
    int bookingPositionId;
    DmsEventsAppController appController;
    String gameName;
    String playersList="";
    StringBuffer playersListTest=new StringBuffer();

    public BookNoficPlayerListAdapter(Context context, int bookingPositionId,String gameName, ArrayList<SelectedPlayersResultModel> playerDetailsModelArrayList, BookingInterface bookingInterface) {
        this.context = context;
        this.playerDetailsModelArrayList = playerDetailsModelArrayList;
        this.bookingPositionId = bookingPositionId;
        this.bookingInterface = bookingInterface;
        this.gameName = gameName;
        appController=(DmsEventsAppController)context.getApplicationContext();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_grid_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameTextView.setText(playerDetailsModelArrayList.get(position).getDisplayName());
        holder.departmentTextView.setText(playerDetailsModelArrayList.get(position).getDeptName());
        holder.withdrawTextView.setVisibility(View.GONE);
        if(playerDetailsModelArrayList.get(position).getId()==DmsSharedPreferences.getUserDetails(context).getId()){
            holder.callTextView.setVisibility(View.GONE);
        }else{
            holder.callTextView.setVisibility(View.GONE);
        }

        if (playerDetailsModelArrayList.get(position).getStatus() == 1) {
            holder.acceptImageImage.setVisibility(View.VISIBLE);
            holder.bookingStatusImageView.setVisibility(View.GONE);
        } else if (playerDetailsModelArrayList.get(position).getStatus() == 2) {
            holder.bookingStatusImageView.setImageResource(R.drawable.icon_reject);
        } else if (playerDetailsModelArrayList.get(position).getStatus() == 3) {
            holder.bookingStatusImageView.setVisibility(View.VISIBLE);
            holder.bookingStatusImageView.setImageResource(R.drawable.icon_withdrawn);

        } else if (playerDetailsModelArrayList.get(position).getStatus() == 0) {
            holder.acceptImageImage.setVisibility(View.GONE);
            holder.bookingStatusImageView.setImageResource(R.drawable.icon_wating);
        }

        holder.bookingStatusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerDetailsModelArrayList.get(position).getStatus() == 2){
                    Toast.makeText(context,"Rejected",Toast.LENGTH_SHORT).show();
                }else if (playerDetailsModelArrayList.get(position).getStatus() == 0){
                    Toast.makeText(context,"Pending",Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.callTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=playerDetailsModelArrayList.get(position).getEmailID();
                sendMail(email,position);
            }
        });
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);
        Glide.with(context).load(ConstantKeys.getImageUrl + playerDetailsModelArrayList.get(position).getProfilePhoto()).apply(options.circleCropTransform()).into(holder.profileImageView);


    }

    private void sendMail(String email,int position) {
            for(int i=0;i<playerDetailsModelArrayList.size();i++){
                if(playerDetailsModelArrayList.get(position).getId()!=DmsSharedPreferences.getUserDetails(context).getId()) {
                    playersList = playerDetailsModelArrayList.get(i).getDisplayName() ;
                    playersListTest.append(playersList+",");
                }
            }

        String message="Your "+ gameName+ " with this players "+ "("+playersListTest+")" +" is in pending state,Please accept or reject your invitation from the app so that other player can utilize the time";
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_CC, "sandeep.kumar@digitalminds.solutions");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, gameName+" Game Invitation");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

           // Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void withdrawDialogBox(int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_game_dialogb);
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancelTextView);
        TextView oKTextView = (TextView) dialog.findViewById(R.id.oKTextView);
        // if button is clicked, close the custom dialog
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        oKTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String bookingIds = Integer.toString(playerDetailsModelArrayList.get(position).getId());
                String bookingStatus = Integer.toString(3);
                bookingInterface.onClickItem(position, bookingPositionId, "withdraw", bookingIds, bookingStatus);
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return playerDetailsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView, bookingStatusImageView, acceptImageImage,callTextView;
        TextView departmentTextView, nameTextView, withdrawTextView;
        LinearLayout bookingStatusLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = (ImageView) itemView.findViewById(R.id.profileImageView);
            acceptImageImage = (ImageView) itemView.findViewById(R.id.acceptImageImage);
            bookingStatusImageView = (ImageView) itemView.findViewById(R.id.bookingStatusImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            withdrawTextView = (TextView) itemView.findViewById(R.id.withdrawTextView);
            departmentTextView = (TextView) itemView.findViewById(R.id.departmentTextView);
            callTextView = (ImageView) itemView.findViewById(R.id.callTextView);
            bookingStatusLinearLayout = (LinearLayout) itemView.findViewById(R.id.bookingStatusLinearLayout);
        }
    }
}
