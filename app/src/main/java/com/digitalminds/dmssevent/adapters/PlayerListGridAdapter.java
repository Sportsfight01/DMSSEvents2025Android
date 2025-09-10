package com.digitalminds.dmssevent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.digitalminds.dmssevent.interfaces.RemovePlayerInterface;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;

import java.util.ArrayList;

public class PlayerListGridAdapter extends RecyclerView.Adapter<PlayerListGridAdapter.MyViewHolder> {
    Context context;
    ArrayList<SelectedPlayersResultModel> playerDetailsModelArrayList;
    BookingInterface bookingInterface;
    int bookingPositionId;
    DmsEventsAppController appController;
    RemovePlayerInterface removePlayerInterface;
    String gameName;
    String playersList="";
    StringBuffer playersListTest=new StringBuffer();
    int bookingStatus;
    int myBookingOrPending;

    public PlayerListGridAdapter(Context context, String gameName,int bookingPositionId, ArrayList<SelectedPlayersResultModel> playerDetailsModelArrayList, BookingInterface bookingInterface,RemovePlayerInterface removePlayerInterface,int myBookingOrPending) {
        this.context = context;
        this.playerDetailsModelArrayList = playerDetailsModelArrayList;
        this.bookingPositionId = bookingPositionId;
        this.bookingInterface = bookingInterface;
        this.gameName = gameName;
        this.removePlayerInterface = removePlayerInterface;
        this.myBookingOrPending = myBookingOrPending;
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
        if(myBookingOrPending==1) {
            holder.bookingStatusLinearLayout.setVisibility(View.VISIBLE);
            holder.withdrawTextView.setVisibility(View.VISIBLE);
            if (playerDetailsModelArrayList.get(position).getStatus() == 1 && (playerDetailsModelArrayList.get(position).getId() == DmsSharedPreferences.getUserDetails(context).getId())) {
                holder.acceptImageImage.setVisibility(View.VISIBLE);
                holder.withdrawTextView.setVisibility(View.VISIBLE);
                holder.callTextView.setVisibility(View.GONE);
                holder.bookingStatusImageView.setVisibility(View.GONE);
            } else if (playerDetailsModelArrayList.get(position).getStatus() == 2) {
                holder.bookingStatusImageView.setImageResource(R.drawable.icon_reject);
                holder.withdrawTextView.setVisibility(View.GONE);
                holder.callTextView.setVisibility(View.GONE);
                holder.acceptImageImage.setVisibility(View.GONE);
                if (playerDetailsModelArrayList.get(position).getId() != DmsSharedPreferences.getUserDetails(context).getId()) {
                    holder.deletePlayer.setVisibility(View.VISIBLE);
                    bookingStatus = 2;
                } else {
                    holder.deletePlayer.setVisibility(View.GONE);
                }
            } else if (playerDetailsModelArrayList.get(position).getStatus() == 3) {
                holder.withdrawTextView.setVisibility(View.GONE);
                holder.acceptImageImage.setVisibility(View.GONE);
                holder.callTextView.setVisibility(View.GONE);
                holder.bookingStatusImageView.setImageResource(R.drawable.icon_withdrawn);
                if (playerDetailsModelArrayList.get(position).getId() != DmsSharedPreferences.getUserDetails(context).getId()) {
                    holder.deletePlayer.setVisibility(View.VISIBLE);
                    bookingStatus = 3;
                } else {
                    holder.deletePlayer.setVisibility(View.GONE);
                }

            } else if (playerDetailsModelArrayList.get(position).getStatus() == 1 && (playerDetailsModelArrayList.get(position).getId() != DmsSharedPreferences.getUserDetails(context).getId())) {
                holder.withdrawTextView.setVisibility(View.GONE);
                holder.acceptImageImage.setVisibility(View.VISIBLE);
                holder.callTextView.setVisibility(View.GONE);
                holder.bookingStatusImageView.setVisibility(View.GONE);
            } else if (playerDetailsModelArrayList.get(position).getStatus() == 0) {
                holder.acceptImageImage.setVisibility(View.GONE);
                holder.withdrawTextView.setVisibility(View.GONE);
                holder.bookingStatusImageView.setImageResource(R.drawable.icon_wating);
                if (playerDetailsModelArrayList.get(position).getId() != DmsSharedPreferences.getUserDetails(context).getId()) {
                    holder.deletePlayer.setVisibility(View.VISIBLE);
                    bookingStatus = 0;
                } else {
                    holder.deletePlayer.setVisibility(View.GONE);
                }

            } else if (playerDetailsModelArrayList.get(position).getStatus() == 4) {
                holder.acceptImageImage.setVisibility(View.VISIBLE);
                holder.withdrawTextView.setVisibility(View.GONE);
                holder.callTextView.setVisibility(View.GONE);
                holder.bookingStatusImageView.setVisibility(View.GONE);
            }


            holder.bookingStatusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerDetailsModelArrayList.get(position).getStatus() == 2) {
                        Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show();
                    } else if (playerDetailsModelArrayList.get(position).getStatus() == 0) {
                        Toast.makeText(context, "Pending", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holder.bookingStatusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playerDetailsModelArrayList.get(position).getStatus() == 2) {
                        Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show();
                    } else if (playerDetailsModelArrayList.get(position).getStatus() == 0) {
                        Toast.makeText(context, "Pending", Toast.LENGTH_SHORT).show();
                    } else if (playerDetailsModelArrayList.get(position).getStatus() == 3) {
                        Toast.makeText(context, "Withdrawn from the game ", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.profilepic)
                    .error(R.drawable.profilepic);
            Glide.with(context).load(ConstantKeys.getImageUrl + playerDetailsModelArrayList.get(position).getProfilePhoto()).apply(options.circleCropTransform()).into(holder.profileImageView);

            holder.withdrawTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    withdrawDialogBox(position);


                }
            });

            holder.deletePlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialogBox(position, bookingStatus);
                }
            });

            holder.callTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = playerDetailsModelArrayList.get(position).getEmailID();
                    sendMail(email, position);
                }
            });
        }else if(myBookingOrPending==2){
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.profilepic)
                    .error(R.drawable.profilepic);
            Glide.with(context).load(ConstantKeys.getImageUrl + playerDetailsModelArrayList.get(position).getProfilePhoto()).apply(options.circleCropTransform()).into(holder.profileImageView);
            holder.bookingStatusLinearLayout.setVisibility(View.GONE);
            holder.withdrawTextView.setVisibility(View.GONE);
        }
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_game_dialogb);
        TextView dialogDescrpTextView = (TextView) dialog.findViewById(R.id.dialogDescrpTextView);
        TextView dialogTitleTextView = (TextView) dialog.findViewById(R.id.dialogTitleTextView);
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancelTextView);
        TextView oKTextView = (TextView) dialog.findViewById(R.id.oKTextView);
        dialogTitleTextView.setText("Withdraw Game");
        dialogDescrpTextView.setText("After withdrawing, you will not be able re-join the game");
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

    private void deleteDialogBox(int position,int bookingStatus) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_game_dialogb);
        TextView dialogDescrpTextView = (TextView) dialog.findViewById(R.id.dialogDescrpTextView);
        TextView dialogTitleTextView = (TextView) dialog.findViewById(R.id.dialogTitleTextView);
        TextView cancelTextView = (TextView) dialog.findViewById(R.id.cancelTextView);
        TextView oKTextView = (TextView) dialog.findViewById(R.id.oKTextView);
        dialogTitleTextView.setText("Remove Player");
        dialogDescrpTextView.setText("If you remove this player then please add one more player");
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
                int bookingId = playerDetailsModelArrayList.get(position).getId();
                int userId = DmsSharedPreferences.getUserDetails(context).getId();
                removePlayerInterface.removePlayerCallBack(bookingPositionId,bookingId,userId);
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return playerDetailsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView, bookingStatusImageView, acceptImageImage,deletePlayer,callTextView;
        TextView departmentTextView, nameTextView, withdrawTextView;
        LinearLayout bookingStatusLinearLayout,nameLinearLy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = (ImageView) itemView.findViewById(R.id.profileImageView);
            nameLinearLy = (LinearLayout) itemView.findViewById(R.id.nameLinearLy);
            acceptImageImage = (ImageView) itemView.findViewById(R.id.acceptImageImage);
            deletePlayer = (ImageView) itemView.findViewById(R.id.deletePlayer);
            bookingStatusImageView = (ImageView) itemView.findViewById(R.id.bookingStatusImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            withdrawTextView = (TextView) itemView.findViewById(R.id.withdrawTextView);
            departmentTextView = (TextView) itemView.findViewById(R.id.departmentTextView);
            callTextView = (ImageView) itemView.findViewById(R.id.callTextView);
            bookingStatusLinearLayout = (LinearLayout) itemView.findViewById(R.id.bookingStatusLinearLayout);
            int height=appController.getScreenWidth();
            if(height<=1080){
                nameTextView.setTextSize(14);
                withdrawTextView.setTextSize(12);
                /*LinearLayout.LayoutParams allOptionsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                nameLinearLy.setLayoutParams(allOptionsParams);*/
            }else if(height>1080){
                nameTextView.setTextSize(16);
                withdrawTextView.setTextSize(13);
            }



        }
    }
}
