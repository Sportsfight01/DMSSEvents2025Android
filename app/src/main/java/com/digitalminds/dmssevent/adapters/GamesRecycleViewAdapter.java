package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.interfaces.GameClickListener;
import com.digitalminds.dmssevent.models.BookingGamesListModel;

import java.util.ArrayList;

public class GamesRecycleViewAdapter extends RecyclerView.Adapter<GamesRecycleViewAdapter.MyViewHolder> {
    ArrayList<BookingGamesListModel> gamesListModelArrayList;
    Context context;
    GameClickListener gameClickListener;
    int row_index = -1;
    int lastClickedPosition;
    boolean firstTimeLoad=true;


    public GamesRecycleViewAdapter(Context context, ArrayList<BookingGamesListModel> gamesListModelArrayList, GameClickListener gameClickListener) {
        this.context = context;
        this.gamesListModelArrayList = gamesListModelArrayList;
        this.gameClickListener = gameClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.games_recycleview_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.gameNameTextView.setText(gamesListModelArrayList.get(position).getGameName());
        String url=gamesListModelArrayList.get(position).getResourceIcon();
        Glide.with(context).load(ConstantKeys.getAllImagesUrl + url).apply(RequestOptions.circleCropTransform()).into(holder.gameImageView);
        gamesListModelArrayList.get(0).setSelected(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gamesListModelArrayList.size() > 0) {
                    if (position != 0) {
                        firstTimeLoad = false;
                    }
                    row_index = position;
                    notifyDataSetChanged();
                    if (lastClickedPosition != position) {
                        gameClickListener.onClick(position,"Games");
                    }
                    // imageViewPagerDialog(selectedPosition);
                }
            }
        });


        if (row_index == position) {
            lastClickedPosition = row_index;
            holder.gameTypeLinearLayout.setBackgroundResource(R.drawable.green_extra_round_corners);
            holder.gameNameTextView.setTextColor(Color.rgb(255, 255, 255));
        } else {
            if (firstTimeLoad && gamesListModelArrayList.get(position).isSelected()) {
                holder.gameTypeLinearLayout.setBackgroundResource(R.drawable.green_extra_round_corners);
                holder.gameNameTextView.setTextColor(Color.rgb(255, 255, 255));
                firstTimeLoad=false;

            } else {
                holder.gameTypeLinearLayout.setBackgroundResource(R.drawable.ex_corner_radius_grey);
                holder.gameNameTextView.setTextColor(Color.BLACK);
            }
        }

    }


    @Override
    public int getItemCount() {
        return gamesListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView gameNameTextView;
        ImageView gameImageView;
        int width, height;
        LinearLayout gameTypeLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
// get the reference of item view's
            gameNameTextView = (TextView) itemView.findViewById(R.id.gameNameTextView);
            gameImageView = (ImageView) itemView.findViewById(R.id.gameImageView);
            gameTypeLinearLayout = (LinearLayout) itemView.findViewById(R.id.gameTypeLinearLayout);
        }
    }
}
