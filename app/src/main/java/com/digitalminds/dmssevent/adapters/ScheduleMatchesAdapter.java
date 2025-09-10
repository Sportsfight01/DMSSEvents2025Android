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
import com.digitalminds.dmssevent.models.ScheduleListGamesModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Sandeep.Kumar on 02-02-2018.
 */

public class ScheduleMatchesAdapter extends BaseAdapter {
    Context context;
    ArrayList<ScheduleListGamesModel> scheduleListGamesModelArrayList;
    Date dt;
    public ScheduleMatchesAdapter(Context context, ArrayList<ScheduleListGamesModel> scheduleListGamesModelArrayList) {
        this.context = context;
        this.scheduleListGamesModelArrayList = scheduleListGamesModelArrayList;
    }

    @Override
    public int getCount() {
        return scheduleListGamesModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return scheduleListGamesModelArrayList.hashCode();
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
        holder.textViewPlayer1=(TextView)rootView.findViewById(R.id.textViewPlayer1);
        holder.textViewPlayer2=(TextView)rootView.findViewById(R.id.textViewPlayer2);
        holder.textViewMatchNumberD=(TextView)rootView.findViewById(R.id.textViewMatchNumberD);
        holder.textViewGameName=(TextView)rootView.findViewById(R.id.textViewGameName);
        holder.textViewGameType=(TextView)rootView.findViewById(R.id.textViewGameType);
        holder.textViewDate=(TextView)rootView.findViewById(R.id.textViewDate);
        holder.textViewTime=(TextView)rootView.findViewById(R.id.textViewTime);
        holder.textViewGameSingles=(TextView)rootView.findViewById(R.id.textViewGameSingles);
        holder.textViewTeamA=(TextView)rootView.findViewById(R.id.textViewTeamA);
        holder.textViewTeamB=(TextView)rootView.findViewById(R.id.textViewTeamB);
        holder.imageViewGamesIconA=(ImageView)rootView.findViewById(R.id.imageViewGamesIconA);
        holder.imageViewGamesIconB=(ImageView)rootView.findViewById(R.id.imageViewGamesIconB);
        holder.imageViewWinner1=(ImageView)rootView.findViewById(R.id.imageViewWinner1);
        holder.imageViewWinner2=(ImageView)rootView.findViewById(R.id.imageViewWinner2);
       // holder.textViewMatchNumber.setText("Match "+scheduleListGamesModelArrayList.get(position).getId());
        holder.textViewMatchNumber.setVisibility(View.GONE);
        holder.textViewTeamA.setText(scheduleListGamesModelArrayList.get(position).getTeamA());
        holder.textViewTeamB.setText(scheduleListGamesModelArrayList.get(position).getTeamB());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);


       // Glide.with(context).load(ConstantKeys.getImageUrl + scheduleListGamesModelArrayList.get(position).getPlayer1Logo()).apply(options).into(holder.imageViewGamesIconA);

       // Glide.with(context).load(ConstantKeys.getImageUrl + scheduleListGamesModelArrayList.get(position).getPlayer2Logo()).apply(options).into(holder.imageViewGamesIconB);


        Glide.with(context).load(ConstantKeys.getImageUrl + scheduleListGamesModelArrayList.get(position).getPlayer1Logo()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewGamesIconA);
        Glide.with(context).load(ConstantKeys.getImageUrl + scheduleListGamesModelArrayList.get(position).getPlayer2Logo()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewGamesIconB);

        String[] str_array = scheduleListGamesModelArrayList.get(position).getScheduleDate().split("T");
        if(str_array.length>0) {
            String stringDate = str_array[0];
            //  final String stringTime = str_array[1];
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
            //String startTime = scheduleListGamesModelArrayList.get(position).getScheduleDate().replace("T"," ");
/*if(startTime.length()>0){


        String startTe = scheduleListGamesModelArrayList.get(position).getScheduleTime();

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
        if(scheduleListGamesModelArrayList.get(position).getWinningPlayer().equalsIgnoreCase(scheduleListGamesModelArrayList.get(position).getPlayer1())){
            holder.imageViewWinner1.setVisibility(View.VISIBLE);
            holder.imageViewWinner2.setVisibility(View.GONE);
        }else if(scheduleListGamesModelArrayList.get(position).getWinningPlayer().equalsIgnoreCase(scheduleListGamesModelArrayList.get(position).getPlayer2())){
            holder.imageViewWinner2.setVisibility(View.VISIBLE);
            holder.imageViewWinner1.setVisibility(View.GONE);
        }else{
            holder.imageViewWinner1.setVisibility(View.GONE);
            holder.imageViewWinner2.setVisibility(View.GONE);
        }
        holder.textViewTime.setText(scheduleListGamesModelArrayList.get(position).getScheduleTime());
        holder.textViewPlayer1.setText(scheduleListGamesModelArrayList.get(position).getPlayer1());
        holder.textViewPlayer2.setText(scheduleListGamesModelArrayList.get(position).getPlayer2());
        holder.textViewGameName.setText(scheduleListGamesModelArrayList.get(position).getGame());
        holder.textViewGameSingles.setText(scheduleListGamesModelArrayList.get(position).getGameType());
        holder.textViewMatchNumberD.setText(scheduleListGamesModelArrayList.get(position).getMatchNo());


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


        for (int i = 0; i < scheduleListGamesModelArrayList.size(); i++) {
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
        if(!scheduleListGamesModelArrayList.get(position).getMatchType().equalsIgnoreCase("")){
            holder.textViewGameType.setText(scheduleListGamesModelArrayList.get(position).getMatchType());
        }else{
            holder.textViewGameType.setVisibility(View.GONE);
        }
        return rootView;
    }
    public class Holder{
        TextView textViewMatchNumber,textViewTitle,textViewTeamA,textViewTeamB,textViewDate,
                textViewPlayer1,textViewPlayer2,textViewMatchNumberD,textViewGameName,textViewTime,textViewGameSingles,textViewGameType;
        LinearLayout lineaLyForColorViews,linearLyForBackground;
        ImageView imageViewGamesIconA,imageViewGamesIconB,imageViewWinner1,imageViewWinner2;

    }
}
