package com.digitalminds.dmssevent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.interfaces.AdapterCallForRating;
import com.digitalminds.dmssevent.models.EventRatingModel;
import com.digitalminds.dmssevent.models.ParticipantsModel;

import java.util.ArrayList;

public class PollRecycleViewAdapter extends RecyclerView.Adapter<PollRecycleViewAdapter.MyViewHolder> {

    Context context;
    androidx.appcompat.app.ActionBar actionBar;
    ArrayList<EventRatingModel> eventRatingModelArrayList;
    AdapterCallForRating adapterCallBack;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEventHeading, textViewRate, textViewVotes, textViewAvgRating, descTextView;
        ListView listViewEmployees;
        LinearLayout linearLayoutVotes, linearLayoutAvgRating;

        public MyViewHolder(View view) {
            super(view);
            textViewEventHeading = (TextView) view.findViewById(R.id.textViewEventHeading);
            textViewVotes = (TextView) view.findViewById(R.id.textViewVotes);
            textViewAvgRating = (TextView) view.findViewById(R.id.textViewAvgRating);
            textViewRate = (TextView) view.findViewById(R.id.textViewRate);
            descTextView = (TextView) view.findViewById(R.id.descTextView);
            listViewEmployees = (ListView) view.findViewById(R.id.listViewEmployees);
            linearLayoutVotes = (LinearLayout) view.findViewById(R.id.linearLayoutVotes);
            linearLayoutAvgRating = (LinearLayout) view.findViewById(R.id.linearLayoutAvgRating);
        }
    }


    public PollRecycleViewAdapter(Context context, ArrayList<EventRatingModel> eventRatingModelArrayList) {
        this.eventRatingModelArrayList = eventRatingModelArrayList;
        this.context = context;
        adapterCallBack = (AdapterCallForRating) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rating_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textViewEventHeading.setText(eventRatingModelArrayList.get(position).getTPEventName());
        holder.descTextView.setText(eventRatingModelArrayList.get(position).getTPEventDescription());

        /***********if condition to check wheather the particular event is rated or not.
         If the event is rated the votes and avg rating with done button will be visible to user or else we are not displaying to user*************/
        if (eventRatingModelArrayList.get(position).isRated()) {
            holder.textViewRate.setText("Done");
            holder.textViewRate.setBackgroundResource(R.drawable.green_round_corner_bg);
            holder.linearLayoutVotes.setVisibility(View.VISIBLE);
            holder.linearLayoutAvgRating.setVisibility(View.VISIBLE);
            holder.textViewVotes.setText(Integer.toString(eventRatingModelArrayList.get(position).getTotalVote()));
            holder.textViewAvgRating.setText(Integer.toString(eventRatingModelArrayList.get(position).getRateAvarage()) + "%");
        } else if (!eventRatingModelArrayList.get(position).isRated()) {
            holder.textViewRate.setText("Rate");
            holder.textViewRate.setBackgroundResource(R.drawable.orange_round_corner_bg);
            holder.linearLayoutVotes.setVisibility(View.GONE);
            holder.linearLayoutAvgRating.setVisibility(View.GONE);
        }
        holder.textViewRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventRatingModelArrayList.get(position).isRated() == false) {
                    ratingDialog(position);
                } else if (eventRatingModelArrayList.get(position).isRated() == true) {
                    Toast.makeText(context, "You have already rated this event", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ArrayList<ParticipantsModel> participantsModels = eventRatingModelArrayList.get(position).getArrayParticipantsModels();


        holder.listViewEmployees.setAdapter(new EmployeeEventsAdapter(participantsModels, context));
        setListViewHeightBasedOnItems(holder.listViewEmployees);

        /*******This if condition is for we are adding listview inside listview so in normal
         * case this will not work for this setNestedScrollingEnabled method will work if we make that true*********/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.listViewEmployees.setNestedScrollingEnabled(true);

        }
    }

    @Override
    public int getItemCount() {
        return eventRatingModelArrayList.size();
    }



    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    /*****
     * Dialog box for submitting rating and we are calling when the particular event is not
     * rating then we are displaying this dialog box or else we sre displaying a toast
     ***********/
    private void ratingDialog(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_dialog);
        TextView textViewHeading = (TextView) dialog.findViewById(R.id.textViewHeading);
        TextView textViewSubmit = (TextView) dialog.findViewById(R.id.textViewSubmit);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.textViewCancel);
        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        ratingBar.setStepSize(1);

        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating1 = String.valueOf(ratingBar.getRating());
                int rating = (int) ratingBar.getRating();
                if (rating > 0) {
                    adapterCallBack.adapterClickedPosition(rating, eventRatingModelArrayList.get(position).getId());
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Kindly rate the event before submitting", Toast.LENGTH_SHORT).show();
                    dialog.show();
                }

            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
