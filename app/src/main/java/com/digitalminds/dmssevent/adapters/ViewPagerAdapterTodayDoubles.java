package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.models.TodayDoublesListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Sandeep.Kumar on 31-01-2018.
 */

public class ViewPagerAdapterTodayDoubles extends PagerAdapter {
    private Context mContext;
    ArrayList<TodayDoublesListModel> todayDoubleListOfGamesModelArrayList;
    String stringDate;
    ImageView imageViewWinner1,imageViewWinner2,imageViewWinner3,imageViewWinner4;
    TextView textViewTeamA,textViewTeamB;
    TextView textViewMatchType;

    public ViewPagerAdapterTodayDoubles(Context mContext, ArrayList<TodayDoublesListModel> todayDoubleListOfGamesModelArrayList) {
        this.mContext = mContext;
        this.todayDoubleListOfGamesModelArrayList = todayDoubleListOfGamesModelArrayList;
    }

    @Override
    public int getCount() {
        return todayDoubleListOfGamesModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    /**
     * Depends upon data size, called for each row , Creates each row
     *
     * @param position  A variable of type Integer.
     * @param container A variable of type ViewGroup.
     * @return itemView
     **/
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
       /* custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/lato.ttf");
        custom_fontBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/lato_bold.ttf");
        custom_fontItalic = Typeface.createFromAsset(mContext.getAssets(), "fonts/lato_italic.ttf");
        custom_fontLightItalic = Typeface.createFromAsset(mContext.getAssets(), "fonts/lato_lightitalic.ttf");*/
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_doubles, container, false);
        final TextView textViewPlayer1 = (TextView) itemView.findViewById(R.id.textViewPlayer1);
        final TextView textViewPlayer2 = (TextView) itemView.findViewById(R.id.textViewPlayer2);
        final TextView textViewPlayer3 = (TextView) itemView.findViewById(R.id.textViewPlayer3);
        final TextView textViewPlayer4 = (TextView) itemView.findViewById(R.id.textViewPlayer4);
        final TextView textViewMatchNumber = (TextView) itemView.findViewById(R.id.textViewMatchNumber);
        final TextView textViewGameName = (TextView) itemView.findViewById(R.id.textViewGameName);
        final TextView textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
        final TextView textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
        textViewMatchType = (TextView) itemView.findViewById(R.id.textViewGameType);
        final TextView textViewGameDoubles = (TextView) itemView.findViewById(R.id.textViewGameDoubles);
        final LinearLayout linearLyForBackground = (LinearLayout) itemView.findViewById(R.id.linearLyForBackgroundDoubles);
        final LinearLayout linearLyForBackgroundcolor = (LinearLayout) itemView.findViewById(R.id.linearLyForBackgroundcolor);
        final ImageView imageViewGamesIconA = (ImageView) itemView.findViewById(R.id.imageViewGamesIconA);
        final ImageView imageViewGamesIconB = (ImageView) itemView.findViewById(R.id.imageViewGamesIconB);
        final ImageView imageViewGamesIconC = (ImageView) itemView.findViewById(R.id.imageViewGamesIconC);
        final ImageView imageViewGamesIconD = (ImageView) itemView.findViewById(R.id.imageViewGamesIconD);
        imageViewWinner1=(ImageView)itemView.findViewById(R.id.imageViewWinner1);
        imageViewWinner2=(ImageView)itemView.findViewById(R.id.imageViewWinner2);
        textViewTeamA=(TextView) itemView.findViewById(R.id.textViewTeamA);
        textViewTeamB=(TextView)itemView.findViewById(R.id.textViewTeamB);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);


      //  Glide.with(mContext).load(ConstantKeys.getImageUrl + todayDoubleListOfGamesModelArrayList.get(position).getPlayer1Logo()).apply(options).into(imageViewGamesIconA);
     //   Glide.with(mContext).load(ConstantKeys.getImageUrl + todayDoubleListOfGamesModelArrayList.get(position).getPlayer2Logo()).apply(options).into(imageViewGamesIconB);
       // Glide.with(mContext).load(ConstantKeys.getImageUrl + todayDoubleListOfGamesModelArrayList.get(position).getPlayer3Logo()).apply(options).into(imageViewGamesIconC);
       // Glide.with(mContext).load(ConstantKeys.getImageUrl + todayDoubleListOfGamesModelArrayList.get(position).getPlayer4Logo()).apply(options).into(imageViewGamesIconD);


        Glide.with(mContext).load(ConstantKeys.getImageUrl + todayDoubleListOfGamesModelArrayList.get(position).getPlayer1Logo()).apply(RequestOptions.circleCropTransform()).into(imageViewGamesIconA);
        Glide.with(mContext).load(ConstantKeys.getImageUrl + todayDoubleListOfGamesModelArrayList.get(position).getPlayer2Logo()).apply(RequestOptions.circleCropTransform()).into(imageViewGamesIconB);
        Glide.with(mContext).load(ConstantKeys.getImageUrl + todayDoubleListOfGamesModelArrayList.get(position).getPlayer3Logo()).apply(RequestOptions.circleCropTransform()).into(imageViewGamesIconC);
        Glide.with(mContext).load(ConstantKeys.getImageUrl + todayDoubleListOfGamesModelArrayList.get(position).getPlayer4Logo()).apply(RequestOptions.circleCropTransform()).into(imageViewGamesIconD);
        textViewTeamA.setText(todayDoubleListOfGamesModelArrayList.get(position).getTeamA());
        textViewTeamB.setText(todayDoubleListOfGamesModelArrayList.get(position).getTeamB());

        if(todayDoubleListOfGamesModelArrayList.get(position).getWinningTeam().equalsIgnoreCase(todayDoubleListOfGamesModelArrayList.get(position).getTeamA())){
            imageViewWinner1.setVisibility(View.VISIBLE);
            imageViewWinner2.setVisibility(View.GONE);
        }else if(todayDoubleListOfGamesModelArrayList.get(position).getWinningTeam().equalsIgnoreCase(todayDoubleListOfGamesModelArrayList.get(position).getTeamB())){
            imageViewWinner2.setVisibility(View.VISIBLE);
            imageViewWinner1.setVisibility(View.GONE);
        }else{
            imageViewWinner1.setVisibility(View.GONE);
            imageViewWinner2.setVisibility(View.GONE);
        }



        /*String color = todayDoubleListOfGamesModelArrayList.get(position).getColorCode();
        //linearLyForBackgroundcolor.setBackgroundColor(Color.parseColor(color));


        if (color.length() > 0) {
            // Initialize a new GradientDrawable
            GradientDrawable gd = new GradientDrawable();

            // Specify the shape of drawable
            gd.setShape(GradientDrawable.RECTANGLE);

            // Set the fill color of drawable
            gd.setColor(Color.parseColor(color)); // make the background transparent

            // Create a 2 pixels width red colored border for drawable
            //gd.setStroke(2, Color.RED); // border width and color

            // Make the border rounded
            gd.setCornerRadius(25.0f); // border corner radius

            // Finally, apply the GradientDrawable as TextView background
            linearLyForBackgroundcolor.setBackground(gd);


            //linearLyForBackground.setBackgroundResource(R.drawable.round_corner_yello);
        *//*if (position % 2 == 1) {
            linearLyForBackground.setBackgroundResource(R.drawable.round_corner_yello);
        } else {
            linearLyForBackground.setBackgroundResource(R.drawable.round_corner_radius);
        }*//*
        } else {
            GradientDrawable gd = new GradientDrawable();

            // Specify the shape of drawable
            gd.setShape(GradientDrawable.RECTANGLE);

            // Set the fill color of drawable
            gd.setColor(Color.parseColor("#bdbdbd")); // make the background transparent

            // Create a 2 pixels width red colored border for drawable
            //gd.setStroke(2, Color.RED); // border width and color

            // Make the border rounded
            gd.setCornerRadius(25.0f); // border corner radius

            // Finally, apply the GradientDrawable as TextView background
            linearLyForBackgroundcolor.setBackground(gd);
        }*/

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

/*
        colors.add(String.valueOf(R.color.viewpagergreen)); //eg.Blue
        colors.add(String.valueOf(R.color.viewpagerlightblue)); //eg.Blue
        colors.add(String.valueOf(R.color.viewpagerorange)); //eg.Blue
        colors.add(String.valueOf(R.color.viewpagerpink)); //eg.Blue
        colors.add(String.valueOf(R.color.blueTheme)); //eg.Blue
        colors.add(String.valueOf(R.color.googleRed)); //eg.Blue
        colors.add(String.valueOf(R.color.fbBlue)); //eg.Blue*/
        for (int i = 0; i < todayDoubleListOfGamesModelArrayList.size(); i++) {
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
            linearLyForBackground.setBackground(gd);
            //paint.setColor(rainbow[i]);
            // Do something with the paint.
        }
        if(!todayDoubleListOfGamesModelArrayList.get(position).getMatchType().equalsIgnoreCase("")){
            textViewMatchType.setText(todayDoubleListOfGamesModelArrayList.get(position).getMatchType());
        }else{
            textViewMatchType.setVisibility(View.GONE);
        }
        textViewPlayer1.setText(todayDoubleListOfGamesModelArrayList.get(position).getPlayer1());
        textViewPlayer2.setText(todayDoubleListOfGamesModelArrayList.get(position).getPlayer2());
        textViewPlayer3.setText(todayDoubleListOfGamesModelArrayList.get(position).getPlayer3());
        textViewPlayer4.setText(todayDoubleListOfGamesModelArrayList.get(position).getPlayer4());
        textViewMatchNumber.setText(todayDoubleListOfGamesModelArrayList.get(position).getMatchNo());
        textViewGameName.setText(todayDoubleListOfGamesModelArrayList.get(position).getGame());
        textViewTime.setText(todayDoubleListOfGamesModelArrayList.get(position).getScheduleTime());
        textViewGameDoubles.setText(todayDoubleListOfGamesModelArrayList.get(position).getGameType());

        if (todayDoubleListOfGamesModelArrayList.get(position).getScheduleDate().equalsIgnoreCase("") || todayDoubleListOfGamesModelArrayList.get(position).getScheduleDate() != null) {
            String[] str_array = todayDoubleListOfGamesModelArrayList.get(position).getScheduleDate().split("T");
            if (str_array.length > 0) {
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
                textViewDate.setText(outputDateFormat.format(tempDate));
            }
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
