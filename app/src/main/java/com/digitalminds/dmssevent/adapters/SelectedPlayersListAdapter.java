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
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.interfaces.SelectedPlayersInterface;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;
import com.google.gson.JsonArray;

import java.util.ArrayList;

public class SelectedPlayersListAdapter extends RecyclerView.Adapter<SelectedPlayersListAdapter.MyViewHolder> {
    Context context;
    ArrayList<SelectedPlayersResultModel> selectedPlayersResultModelArrayList;
    BookingInterface bookingInterface;
    String selectedElement;
    private int lastSelectedPosition = -1;
    SelectedPlayersInterface selectedPlayersInterface;
    String text="";
    JsonArray jsonArray;
    ArrayList<String> list;
    ArrayList<SelectedPlayersResultModel> finalArrayList=new ArrayList<SelectedPlayersResultModel>();;
    SelectedPlayersResultModel finalSelectedModel=new SelectedPlayersResultModel();
    int callActivity;
    DmsEventsAppController appController;
    SelectedPlayersResultModel selectedModelLTemp;



    public SelectedPlayersListAdapter(Context context, int callActivity, ArrayList<SelectedPlayersResultModel> selectedPlayersResultModelArrayList,
                                      BookingInterface bookingInterface,
                                      SelectedPlayersInterface selectedPlayersInterface, DmsEventsAppController appController) {
        this.context = context;
        this.selectedPlayersResultModelArrayList = selectedPlayersResultModelArrayList;
        this.bookingInterface = bookingInterface;
        this.callActivity = callActivity;
        this.selectedPlayersInterface = selectedPlayersInterface;
        this.appController = appController;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_player_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SelectedPlayersResultModel selectedPlayersResultModels = selectedPlayersResultModelArrayList.get(position);
        holder.userNameTextView.setText(selectedPlayersResultModelArrayList.get(position).getDisplayName());
        holder.deptNameTextView.setText(selectedPlayersResultModelArrayList.get(position).getDeptName());
        String url= ConstantKeys.getImageUrl +selectedPlayersResultModelArrayList.get(position).getProfilePhoto();
        Glide.with(context).load(url).apply(RequestOptions.circleCropTransform()).into(holder.userImageView);
        if(callActivity==2){
            /*holder.selectImageView.setImageResource(() ? R.drawable.icon_filter_selected : R.drawable.icon_filter_unselected));*/
        }
        holder.selectImageView.setImageResource(selectedPlayersResultModelArrayList.get(position).isSelected() ? R.drawable.icon_filter_selected : R.drawable.icon_filter_unselected);
        holder.selectImageViewLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=selectedPlayersResultModelArrayList.get(position).getId();
                int idUser= DmsSharedPreferences.getUserDetails(context).getId();
                if(selectedId== idUser){
                    Toast.makeText(context,"You Can't Select Your Name",Toast.LENGTH_SHORT).show();
                }else{
                    selectedPlayersInterface.onClick(position,null,null);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedPlayersResultModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, deptNameTextView;
        ImageView userImageView,selectImageView;
        LinearLayout selectImageViewLy;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            userNameTextView = (TextView) itemView.findViewById(R.id.userNameTextView);
            deptNameTextView = (TextView) itemView.findViewById(R.id.deptNameTextView);
            userImageView = (ImageView) itemView.findViewById(R.id.userImageView);
            selectImageView = (ImageView) itemView.findViewById(R.id.selectImageView);
            selectImageViewLy = (LinearLayout) itemView.findViewById(R.id.selectImageViewLy);

        }
    }
}
