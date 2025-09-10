package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.AwardsActivity;
import com.digitalminds.dmssevent.EventAlbumsList;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.RatingActivity;
import com.digitalminds.dmssevent.ScheduleActivity;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.CountDownTimerView;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.models.EventsDetailsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sandeep.kumar on 13-03-2017.
 */
public class EventsAdapter extends BaseAdapter {
    Context context;
    ArrayList<EventsDetailsModel> eventsDetailsModels;
    private Handler handler;
    private Runnable runnable;
    DmsEventsAppController controller;
    String stringDate;
    Holder holder;

    public EventsAdapter(Context context, ArrayList<EventsDetailsModel> eventsDetailsModels, DmsEventsAppController controller) {
        this.context = context;
        this.eventsDetailsModels = eventsDetailsModels;
        this.controller = controller;
    }

    @Override
    public int getCount() {
        return eventsDetailsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return eventsDetailsModels.hashCode();
    }
    public static boolean isNetworkAvailable1(Context act) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            Toast.makeText(act,"No Internet Connection",Toast.LENGTH_SHORT).show();

            return false;
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.events_list_item, null);
        } else {
            rowView = convertView;
        }
        holder.lineaLyListItem = (LinearLayout) rowView.findViewById(R.id.lineaLyListItem);
        holder.imgViewListItem = (ImageView) rowView.findViewById(R.id.imgViewListItem);
        holder.txtViewDate = (TextView) rowView.findViewById(R.id.txtViewDate);
        //holder.textViewDaysToGo = (TextView) rowView.findViewById(R.id.textViewDaysToGo);
        //holder.textViewHours = (TextView) rowView.findViewById(R.id.textViewHours);
        //holder.textViewMinutes = (TextView) rowView.findViewById(R.id.textViewMinutes);
        //holder.textViewSeconds = (TextView) rowView.findViewById(R.id.textViewSeconds);
        //holder.eventEdtTextView = (TextView) rowView.findViewById(R.id.eventEdtTextView);
        holder.buttonGallery = (Button) rowView.findViewById(R.id.buttonGallery);
        holder.buttonScheduler = (Button) rowView.findViewById(R.id.buttonScheduler);
        holder.buttonRating = (Button) rowView.findViewById(R.id.buttonRating);
        holder.buttonAwards = (Button) rowView.findViewById(R.id.buttonAwards);
        holder.timerLinearLayout = (LinearLayout) rowView.findViewById(R.id.timerLinearLayout);
        holder.linearLayoutDays = (LinearLayout) rowView.findViewById(R.id.linearLayoutDays);
        holder.daysTextView = (TextView) rowView.findViewById(R.id.daysTextView);
        holder.hoursTextView = (TextView) rowView.findViewById(R.id.hoursTextView);
        holder.counterview = (CountDownTimerView) rowView.findViewById(R.id.counterview);
        if (eventsDetailsModels.get(position).getLaunchDate().equalsIgnoreCase("") || eventsDetailsModels.get(position).getLaunchDate() != null) {
            String[] str_array = eventsDetailsModels.get(position).getLaunchDate().split("T");
            stringDate = str_array[0];
            final String stringTime = str_array[1];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date tempDate = null;

            Calendar calendar = Calendar.getInstance();
            String presentDate = simpleDateFormat.format(calendar.getTime());



            try {
                tempDate = simpleDateFormat.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date1 = tempDate;
            Date currentDate = new Date();
            if (date1.before(currentDate)) {
                holder.timerLinearLayout.setVisibility(View.GONE);
                holder.txtViewDate.setText("Event Completed on " + outputDateFormat.format(tempDate));
            }else if(stringDate.equals(presentDate)){
                holder.timerLinearLayout.setVisibility(View.GONE);
                holder.txtViewDate.setText("Event Started " + outputDateFormat.format(tempDate));
            }else {
                //holder.counterview.setEndTime("2018-02-19" + " " + "11:30:33");//yyyy-MM-dd HH:mm:ss
                holder.counterview.setEndTime(stringDate + " " + stringTime);
                holder.timerLinearLayout.setVisibility(View.VISIBLE);
                holder.txtViewDate.setText(outputDateFormat.format(tempDate));
            }
            //holder.txtViewDate.setText(outputDateFormat.format(tempDate));
        } else {
            holder.txtViewDate.setText("-");
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.icon_loadingview)
                .error(R.drawable.no_imag_aval);
        String url= ConstantKeys.getAllImagesNewsUrl+eventsDetailsModels.get(position).getCoverImageURL();
        Glide.with(context).load(url).apply(options).into(holder.imgViewListItem);
        /*if(position==2){
            holder.imgViewListItem.setBackgroundResource(R.drawable.banner);
        }else if(position==1){

            holder.imgViewListItem.setBackgroundResource(R.drawable.rsz_bag_event_eighteen);
        }else if(position==0){
            holder.imgViewListItem.setBackgroundResource(R.drawable.milestone2019);
        }*/

        holder.imgViewListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable1(context)) {
                   // if(position==1) {
                    //    Intent intent = new Intent(context, EventBanners.class);
                   //     context.startActivity(intent);
                  //  }else{
                      //  Toast.makeText(context,"No Banners Yet..!",Toast.LENGTH_SHORT).show();
                   // }
                }
                // startGalleryActivity();
            }
        });
        holder.buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setSelectedEvent(eventsDetailsModels.get(position));
                EventAlbumsList.selectedEventPosition=position;
                if (isNetworkAvailable1(context)) {
                    Intent intent = new Intent(context, EventAlbumsList.class);
                    context.startActivity(intent);
                }
                // startGalleryActivity();
            }
        });

        holder.buttonScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setSelectedEvent(eventsDetailsModels.get(position));
                if (isNetworkAvailable1(context)) {
                    Intent i = new Intent(context, ScheduleActivity.class);
                    context.startActivity(i);
                }
            }
        });
        holder.buttonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setSelectedEvent(eventsDetailsModels.get(position));
                if (isNetworkAvailable1(context)) {
                    Intent i = new Intent(context, RatingActivity.class);
                    context.startActivity(i);
                }
            }
        });
        holder.buttonAwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setSelectedEvent(eventsDetailsModels.get(position));
                if (isNetworkAvailable1(context)) {
                    Intent i = new Intent(context, AwardsActivity.class);
                    context.startActivity(i);
                }
            }
        });
        return rowView;
    }

   /* public void countDownStart() {
        try {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 1000);
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        // Here Set your Event Date
                        Date eventDate = dateFormat.parse(stringDate);
                        Date currentDate = new Date();
                        if (!currentDate.after(eventDate)) {
                            long diff = eventDate.getTime()
                                    - currentDate.getTime();
                            long days = diff / (24 * 60 * 60 * 1000);
                            diff -= days * (24 * 60 * 60 * 1000);
                            long hours = diff / (60 * 60 * 1000);
                            diff -= hours * (60 * 60 * 1000);
                            long minutes = diff / (60 * 1000);
                            diff -= minutes * (60 * 1000);
                            long seconds = diff / 1000;
                        *//*if(days==0){
                            linearLayoutDays.setVisibility(View.GONE);
                        }else{
                            textViewDaysToGo.setText(""+ String.format("%02d", days));
                        }
                        if(hours==0){
                            linearLayoutHours.setVisibility(View.GONE);
                        }else{
                            textViewHours.setText("" + String.format("%02d", hours));
                        }
                        if(days==0&&hours==0&&minutes==0){
                            linearLayoutMinutes.setVisibility(View.GONE);
                        }else{
                            textViewMinutes.setText("" + String.format("%02d", minutes));
                        }*//*
                            if (days < 02) {
                                holder.daysTextView.setText("Day");
                            } else {
                                holder.daysTextView.setText("Days");
                            }
                            if (hours < 02) {
                                holder.hoursTextView.setText("Hour");
                            } else {
                                holder.hoursTextView.setText("Hours");
                            }
                            holder.textViewDaysToGo.setText("" + String.format("%02d", days));
                            holder.textViewHours.setText("" + String.format("%02d", hours));
                            holder.textViewMinutes.setText("" + String.format("%02d", minutes));
                            holder.textViewSeconds.setText("" + String.format("%02d", seconds));
                        } else {
                     *//*   linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.GONE);
                        tvEvent.setText("Android Event Start");*//*
                            holder.timerLinearLayout.setVisibility(View.GONE);
                            holder.txtViewDate.setVisibility(View.VISIBLE);
                            holder.txtViewDate.setText("Event Started!");
                            handler.removeCallbacks(runnable);
                            // handler.removeMessages(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/


    public class Holder {
        TextView txtViewDate, textViewDaysToGo, textViewHours, textViewMinutes, textViewSeconds, eventEdtTextView, hoursTextView, daysTextView;
        LinearLayout lineaLyListItem, timerLinearLayout, linearLayoutDays, linearLayoutHours, linearLayoutMinutes;
        ImageView imgViewListItem;
        Button buttonGallery, buttonRating, buttonScheduler, buttonAwards;
        CountDownTimerView counterview;

    }
}