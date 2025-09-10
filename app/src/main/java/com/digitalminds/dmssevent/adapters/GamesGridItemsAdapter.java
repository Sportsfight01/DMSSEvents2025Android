package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.interfaces.GridItemCallBack;
import com.digitalminds.dmssevent.models.GetAllGamesModel;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 31-01-2018.
 */

public class GamesGridItemsAdapter extends BaseAdapter {
    Context context;
    ArrayList<GetAllGamesModel> getAllGamesModelArrayList;
    GridItemCallBack gridItemCallBack;

    public GamesGridItemsAdapter(Context context, ArrayList<GetAllGamesModel> getAllGamesModelArrayList, GridItemCallBack gridItemCallBack) {
        this.context = context;
        this.getAllGamesModelArrayList = getAllGamesModelArrayList;
        this.gridItemCallBack = gridItemCallBack;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return getAllGamesModelArrayList.hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rowView=convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.game_gridview_item, null);
        } else {
            rowView = convertView;
        }
        /**********Declaration of id's***********/
        holder.textViewGameName = (TextView) rowView.findViewById(R.id.textViewGameName);
        holder.imageViewGamesIcon = (ImageView) rowView.findViewById(R.id.imageViewGamesIcon);
        holder.textViewGameName.setText(getAllGamesModelArrayList.get(position).getName());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.milestone_25)
                .error(R.drawable.milestone_25);
        Glide.with(context).load(ConstantKeys.getAllImagesUrl+ getAllGamesModelArrayList.get(position).getIconURL()).apply(options).into(holder.imageViewGamesIcon);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridItemCallBack.onItemClick(view,position,getAllGamesModelArrayList.get(position).getGameType());
            }
        });
        return rowView;
    }

    public class Holder{
        TextView textViewGameName;
        ImageView imageViewGamesIcon;
    }
}
