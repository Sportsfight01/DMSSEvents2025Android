package com.digitalminds.dmssevent.adapters;

import android.content.Context;
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
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;

import java.util.ArrayList;

public class BookedPlayersDetailsAdapter extends RecyclerView.Adapter<BookedPlayersDetailsAdapter.MyViewHolder> {
    Context context;
    ArrayList<SelectedPlayersResultModel> playerDetailsModels;

    public BookedPlayersDetailsAdapter(Context context, ArrayList<SelectedPlayersResultModel> playerDetailsModels) {
        this.context = context;
        this.playerDetailsModels = playerDetailsModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameTextView.setText(playerDetailsModels.get(position).getDisplayName());
        holder.departmentTextView.setText(playerDetailsModels.get(position).getDeptName());
        holder.withdrawTextView.setVisibility(View.GONE);
        holder.callTextView.setVisibility(View.GONE);
        if (playerDetailsModels.get(position).getStatus() == 1) {
            holder.acceptImageImage.setVisibility(View.VISIBLE);
            holder.bookingStatusImageView.setVisibility(View.GONE);
        } else if (playerDetailsModels.get(position).getStatus() == 2) {
            holder.bookingStatusImageView.setImageResource(R.drawable.icon_reject);
        } else if (playerDetailsModels.get(position).getStatus()== 3) {
            holder.bookingStatusImageView.setImageResource(R.drawable.icon_withdrawn);
           // holder.bookingStatusImageView.setVisibility(View.GONE);
        } else if (playerDetailsModels.get(position).getStatus()== 0) {
            holder.acceptImageImage.setVisibility(View.GONE);
            holder.bookingStatusImageView.setImageResource(R.drawable.icon_wating);
        }

        holder.bookingStatusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerDetailsModels.get(position).getStatus() == 2){
                    Toast.makeText(context,"Rejected",Toast.LENGTH_SHORT).show();
                }else if (playerDetailsModels.get(position).getStatus() == 0){
                    Toast.makeText(context,"Pending",Toast.LENGTH_SHORT).show();
                }

            }
        });

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);
        Glide.with(context).load(ConstantKeys.getImageUrl + playerDetailsModels.get(position).
                getProfilePhoto()).apply(options.circleCropTransform()).into(holder.profileImageView);


    }





    @Override
    public int getItemCount() {
        return playerDetailsModels.size();
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
