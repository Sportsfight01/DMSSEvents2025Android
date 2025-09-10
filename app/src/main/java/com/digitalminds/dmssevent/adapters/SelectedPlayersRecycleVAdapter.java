package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.interfaces.GameClickListener;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;

import java.util.ArrayList;

public class SelectedPlayersRecycleVAdapter extends RecyclerView.Adapter<SelectedPlayersRecycleVAdapter.MyViewHolder> {
    ArrayList<SelectedPlayersResultModel> showPlayersResultModelArrayList;
    Context context;
    GameClickListener gameClickListener;

    public SelectedPlayersRecycleVAdapter(Context context, ArrayList<SelectedPlayersResultModel> showPlayersResultModelArrayList, GameClickListener gameClickListener) {
        this.context = context;
        this.showPlayersResultModelArrayList = showPlayersResultModelArrayList;
        this.gameClickListener = gameClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_player_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.nameTextView.setText(showPlayersResultModelArrayList.get(position).getDisplayName());
        holder.departmentTextView.setText(showPlayersResultModelArrayList.get(position).getDeptName());
        String url = showPlayersResultModelArrayList.get(position).getProfilePhoto();
        Glide.with(context).load(ConstantKeys.getImageUrl +url).apply(RequestOptions.circleCropTransform()).into(holder.profileImageView);
        if(showPlayersResultModelArrayList.get(position).getId()==DmsSharedPreferences.getUserDetails(context).getId()){
            holder.deleteUserImageView.setVisibility(View.GONE);
        }else{
            holder.deleteUserImageView.setVisibility(View.VISIBLE);
        }
        holder.deleteUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClickListener.onClick(position,"deleteUser");
            }
        });

    }

    @Override
    public int getItemCount() {
        return showPlayersResultModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView,departmentTextView;
        ImageView profileImageView,deleteUserImageView;


        public MyViewHolder(View itemView) {
            super(itemView);
// get the reference of item view's
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            profileImageView = (ImageView) itemView.findViewById(R.id.profileImageView);
            deleteUserImageView = (ImageView) itemView.findViewById(R.id.deleteUserImageView);
            departmentTextView = (TextView) itemView.findViewById(R.id.departmentTextView);

        }
    }
}
