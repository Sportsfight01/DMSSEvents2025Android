package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.models.TodaySinglesListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Sandeep.Kumar on 02-02-2018.
 */

public class MyGamesSingleAdapter extends BaseAdapter {
    Context context;
    ArrayList<TodaySinglesListModel> todayListModelArraySinglesList;
    int matchValue;
    public MyGamesSingleAdapter(Context context, ArrayList<TodaySinglesListModel> todayListModelArraySinglesList) {
        this.context = context;
        this.todayListModelArraySinglesList = todayListModelArraySinglesList;
    }

    @Override
    public int getCount() {
        return todayListModelArraySinglesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return todayListModelArraySinglesList.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rootView=convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            rootView=inflater.inflate(R.layout.mygames_singleadapter,null);
        }else {
            rootView=convertView;
        }
        holder.textViewMatchNumber=(TextView)rootView.findViewById(R.id.textViewMatchNumber);
        holder.lineaLyForColorViews=(LinearLayout) rootView.findViewById(R.id.lineaLyForColorViews);
        holder.linearLyForBackground=(LinearLayout) rootView.findViewById(R.id.linearLyForBackground);
        holder.textViewTitle=(TextView)rootView.findViewById(R.id.textViewTitle);
        holder.textViewGameType = (TextView) rootView.findViewById(R.id.textViewGameType);
        holder.textViewDate=(TextView)rootView.findViewById(R.id.textViewDate);
        holder.textViewTeamA=(TextView)rootView.findViewById(R.id.textViewTeamA);
        holder.textViewTeamB=(TextView)rootView.findViewById(R.id.textViewTeamB);
        holder.textViewPlayer1=(TextView)rootView.findViewById(R.id.textViewPlayer1);
        holder.textViewPlayer2=(TextView)rootView.findViewById(R.id.textViewPlayer2);
        holder.textViewMatchNumberD=(TextView)rootView.findViewById(R.id.textViewMatchNumberD);
        holder.textViewGameName=(TextView)rootView.findViewById(R.id.textViewGameName);
        holder.textViewDate=(TextView)rootView.findViewById(R.id.textViewDate);
        holder.textViewTime=(TextView)rootView.findViewById(R.id.textViewTime);
        holder.textViewGameSingles=(TextView)rootView.findViewById(R.id.textViewGameSingles);
        holder.textViewTeamA=(TextView)rootView.findViewById(R.id.textViewTeamA);
        holder.textViewTeamB=(TextView)rootView.findViewById(R.id.textViewTeamB);
        holder.imageViewGamesIconA=(ImageView)rootView.findViewById(R.id.imageViewGamesIconA);
        holder.imageViewGamesIconB=(ImageView)rootView.findViewById(R.id.imageViewGamesIconB);
        holder.imageViewWinner1=(ImageView)rootView.findViewById(R.id.imageViewWinner1);
        holder.imageViewWinner2=(ImageView)rootView.findViewById(R.id.imageViewWinner2);
       /* for(int i = 0; i< todayListModelArraySinglesList.size(); i++){
            matchValue++;
            holder.textViewMatchNumber.setText("Match "+String.valueOf(i+1));
        }*/


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);


       // Glide.with(context).load(ConstantKeys.getImageUrl + todayListModelArraySinglesList.get(position).getPlayer1Logo()).apply(options).into(holder.imageViewGamesIconA);
       // Glide.with(context).load(ConstantKeys.getImageUrl + todayListModelArraySinglesList.get(position).getPlayer2Logo()).apply(options).into(holder.imageViewGamesIconB);


        Glide.with(context).load(ConstantKeys.getImageUrl + todayListModelArraySinglesList.get(position).getPlayer1Logo()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewGamesIconA);
        Glide.with(context).load(ConstantKeys.getImageUrl + todayListModelArraySinglesList.get(position).getPlayer2Logo()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewGamesIconB);

        /*Picasso.with(context).load(ConstantKeys.getImageUrl+ todayListModelArraySinglesList.get(position).getPlayer1Logo()).transform(new CircleTransform()).into(holder.imageViewGamesIconA);
        Picasso.with(context).load(ConstantKeys.getImageUrl+ todayListModelArraySinglesList.get(position).getPlayer2Logo()).transform(new CircleTransform()).into(holder.imageViewGamesIconB);*/
        String[] str_array = todayListModelArraySinglesList.get(position).getScheduleDate().split("T");
        if(str_array.length>0) {

            String stringDate = str_array[0];
            //final String stringTime = str_array[1];
            // String removeSeconds=stringTime.substring(stringTime.length()-4);
            // String removeSeconds = stringTime.substring(0, Math.min(stringTime.length(), 5));
            //  String second = stringTime.substring(Math.max(0, stringTime.length() - 4), stringTime.length());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date tempDate = null;
            try {
                tempDate = simpleDateFormat.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");


            //holder.textViewTitle.setText(sdfs.format(dt));
            //String startTime = todayListModelArraySinglesList.get(position).getScheduleDate().replace("T"," ");
/*if(startTime.length()>0){


        String startTe = todayListModelArraySinglesList.get(position).getScheduleTime();

        StringTokenizer tk = new StringTokenizer(startTime);
        String date = tk.nextToken();
        String time = tk.nextToken();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        try {
            dt = sdf.parse(time);
            System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(dt!=null){
            holder.textViewTitle.setText(sdfs.format(dt));
        }
}*/
            holder.textViewDate.setText(outputDateFormat.format(tempDate));
        }
        holder.textViewTime.setText(todayListModelArraySinglesList.get(position).getScheduleTime());
        if(!todayListModelArraySinglesList.get(position).getMatchType().equalsIgnoreCase("")){
            holder.textViewGameType.setText(todayListModelArraySinglesList.get(position).getMatchType());
        }else{
            holder.textViewGameType.setVisibility(View.GONE);
        }
        holder.textViewPlayer1.setText(todayListModelArraySinglesList.get(position).getPlayer1());
        holder.textViewPlayer2.setText(todayListModelArraySinglesList.get(position).getPlayer2());
        holder.textViewGameName.setText(todayListModelArraySinglesList.get(position).getGame());
        holder.textViewGameSingles.setText(todayListModelArraySinglesList.get(position).getGameType());
        holder.textViewMatchNumberD.setText(todayListModelArraySinglesList.get(position).getMatchNo());

        if(todayListModelArraySinglesList.get(position).getWinningPlayer().equalsIgnoreCase(todayListModelArraySinglesList.get(position).getPlayer1())){
            holder.imageViewWinner1.setVisibility(View.VISIBLE);
            holder.imageViewWinner2.setVisibility(View.GONE);
        }else if(todayListModelArraySinglesList.get(position).getWinningPlayer().equalsIgnoreCase(todayListModelArraySinglesList.get(position).getPlayer2())){
            holder.imageViewWinner2.setVisibility(View.VISIBLE);
            holder.imageViewWinner1.setVisibility(View.GONE);
        }else{
            holder.imageViewWinner1.setVisibility(View.GONE);
            holder.imageViewWinner2.setVisibility(View.GONE);
        }
        if(!todayListModelArraySinglesList.get(position).getMatchType().equalsIgnoreCase("")){
            holder.textViewGameType.setText(todayListModelArraySinglesList.get(position).getMatchType());
        }else{
            holder.textViewGameType.setVisibility(View.GONE);
        }


       // String color = todayListModelArraySinglesList.get(position).getColorCode();
        List<String> colors = new ArrayList<>();

        //eg.Blue
        colors.add("#89a7d5"); // ...
        colors.add("#d34836");
        colors.add("#9f3179");
        colors.add("#5C65AB"); // ...
        colors.add("#2dbe78");
        colors.add("#f26d7d"); // ...
        colors.add("#edce23");
        colors.add("#c69c6d");
        colors.add("#f26c4f");
        colors.add("#fbaf5d");
        colors.add("#cb8bb4");
        for (int i = 0; i < todayListModelArraySinglesList.size(); i++) {
            Random random = new Random();
            int generatedRandomNum = random.nextInt(colors.size());
            GradientDrawable gd = new GradientDrawable();

            // Specify the shape of drawable
            gd.setShape(GradientDrawable.RECTANGLE);
            // iew.setBackgroundColor(Color.parseColor(colors.get(generatedRandomNum)));
            // Set the fill color of drawable
            gd.setColor((Color.parseColor(colors.get(generatedRandomNum))));

            // Create a 2 pixels width red colored border for drawable
            //gd.setStroke(2, Color.RED); // border width and color

            // Make the border rounded
            gd.setCornerRadius(25.0f); // border corner radius

            // Finally, apply the GradientDrawable as TextView background
            holder.linearLyForBackground.setBackground(gd);
            //paint.setColor(rainbow[i]);
            // Do something with the paint.
        }

        return rootView;
    }
    public class Holder{
        TextView textViewMatchNumber,textViewTitle,textViewTeamA,textViewTeamB,
                textViewDate,textViewPlayer1,textViewPlayer2,textViewMatchNumberD,textViewGameName,textViewTime,textViewGameSingles,textViewGameType;
        LinearLayout lineaLyForColorViews,linearLyForBackground;
        ImageView imageViewGamesIconA,imageViewGamesIconB,imageViewWinner1,imageViewWinner2;


    }
}
