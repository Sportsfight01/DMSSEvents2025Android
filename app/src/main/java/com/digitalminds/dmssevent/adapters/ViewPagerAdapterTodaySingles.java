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
import com.digitalminds.dmssevent.models.TodaySinglesListModel;

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

public class ViewPagerAdapterTodaySingles extends PagerAdapter {
    private Context mContext;
    ArrayList<TodaySinglesListModel> todayListModelArrayList;
    String stringDate;
    TextView textViewMatchNumber, textViewGameName, textViewDate, textViewTime, textViewGameSingles,textViewMatchType;
    ImageView imageViewWinner1,imageViewWinner2;

    public ViewPagerAdapterTodaySingles(Context mContext, ArrayList<TodaySinglesListModel> todayListModelArrayList) {
        this.mContext = mContext;
        this.todayListModelArrayList = todayListModelArrayList;
    }

    @Override
    public int getCount() {
        return todayListModelArrayList.size();
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
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_games, container, false);
        final TextView textViewPlayer1 = (TextView) itemView.findViewById(R.id.textViewPlayer1);
        final TextView textViewPlayer2 = (TextView) itemView.findViewById(R.id.textViewPlayer2);
        textViewMatchNumber = (TextView) itemView.findViewById(R.id.textViewMatchNumber);
        textViewMatchType = (TextView) itemView.findViewById(R.id.textViewMatchType);
        textViewGameName = (TextView) itemView.findViewById(R.id.textViewGameName);
        textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
        textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
        imageViewWinner1=(ImageView)itemView.findViewById(R.id.imageViewWinner1);
        imageViewWinner2=(ImageView)itemView.findViewById(R.id.imageViewWinner2);
        textViewGameSingles = (TextView) itemView.findViewById(R.id.textViewGameSingles);
        final LinearLayout linearLyForBackground = (LinearLayout) itemView.findViewById(R.id.linearLyForBackground);
        final ImageView imageViewGamesIconA = (ImageView) itemView.findViewById(R.id.imageViewGamesIconA);
        final ImageView imageViewGamesIconB = (ImageView) itemView.findViewById(R.id.imageViewGamesIconB);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);

       // Glide.with(mContext).load(ConstantKeys.getImageUrl+ todayListModelArrayList.get(position).getPlayer1Logo()).apply(options).into(imageViewGamesIconA);
      // Glide.with(mContext).load(ConstantKeys.getImageUrl+ todayListModelArrayList.get(position).getPlayer2Logo()).apply(options).into(imageViewGamesIconB);
        String winningPlayer=todayListModelArrayList.get(position).getWinningPlayer();
        Glide.with(mContext).load(ConstantKeys.getImageUrl + todayListModelArrayList.get(position).getPlayer1Logo()).apply(RequestOptions.circleCropTransform()).into(imageViewGamesIconA);
        Glide.with(mContext).load(ConstantKeys.getImageUrl + todayListModelArrayList.get(position).getPlayer2Logo()).apply(RequestOptions.circleCropTransform()).into(imageViewGamesIconB);
        if(todayListModelArrayList.get(position).getWinningPlayer().equalsIgnoreCase(todayListModelArrayList.get(position).getPlayer1())){
            imageViewWinner1.setVisibility(View.VISIBLE);
            imageViewWinner2.setVisibility(View.GONE);
        }else if(!winningPlayer.equalsIgnoreCase("")){
            if(todayListModelArrayList.get(position).getWinningPlayer().equalsIgnoreCase(todayListModelArrayList.get(position).getPlayer2())) {
                imageViewWinner2.setVisibility(View.VISIBLE);
                imageViewWinner1.setVisibility(View.GONE);
            }
        }else {
            imageViewWinner1.setVisibility(View.GONE);
            imageViewWinner2.setVisibility(View.GONE);
        }

       /* Picasso.with(mContext).load(ConstantKeys.getImageUrl+ todayListModelArrayList.get(position).getPlayer1Logo()).transform(new CircleTransform()).into(imageViewGamesIconA);
        Picasso.with(mContext).load(ConstantKeys.getImageUrl+ todayListModelArrayList.get(position).getPlayer2Logo()).transform(new CircleTransform()).into(imageViewGamesIconB);*/

        /*String color = todayListModelArrayList.get(position).getColorCode();
        //linearLyForBackground.setBackgroundColor(Color.parseColor(color));
        if (color.length() > 0) {
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
            linearLyForBackground.setBackground(gd);
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
            linearLyForBackground.setBackground(gd);
        }*/
        /*if (position % 2 == 1) {
            linearLyForBackground.setBackgroundResource(R.drawable.round_corner_yello);
        } else {
            linearLyForBackground.setBackgroundResource(R.drawable.round_corner_radius);
        }*/
        List<String> colors = new ArrayList<>();

     /*   colors.add(String.valueOf(R.color.viewpagergreen)); //eg.Blue
        colors.add(String.valueOf(R.color.viewpagerlightblue)); //eg.Blue
        colors.add(String.valueOf(R.color.viewpagerorange)); //eg.Blue
        colors.add(String.valueOf(R.color.viewpagerpink)); //eg.Blue
        colors.add(String.valueOf(R.color.blueTheme)); //eg.Blue
        colors.add(String.valueOf(R.color.googleRed)); //eg.Blue
        colors.add(String.valueOf(R.color.fbBlue)); //eg.Blue*/
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


        for (int i = 0; i < todayListModelArrayList.size(); i++) {
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




        textViewPlayer1.setText(todayListModelArrayList.get(position).getPlayer1());
        textViewPlayer2.setText(todayListModelArrayList.get(position).getPlayer2());
        if(!todayListModelArrayList.get(position).getMatchType().equalsIgnoreCase("")){
            textViewMatchType.setText(todayListModelArrayList.get(position).getMatchType());
        }else{
            textViewMatchType.setVisibility(View.GONE);
        }

        textViewMatchNumber.setText(todayListModelArrayList.get(position).getMatchNo());
        textViewGameName.setText(todayListModelArrayList.get(position).getGame());
        textViewTime.setText(todayListModelArrayList.get(position).getScheduleTime());
        textViewGameSingles.setText(todayListModelArrayList.get(position).getGameType());
        if (todayListModelArrayList.get(position).getScheduleDate().equalsIgnoreCase("") || todayListModelArrayList.get(position).getScheduleDate() != null) {
            String[] str_array = todayListModelArrayList.get(position).getScheduleDate().split("T");
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
