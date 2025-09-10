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
import com.digitalminds.dmssevent.interfaces.TeamsListItemClickCallBack;
import com.digitalminds.dmssevent.models.TeamsListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sandeep.Kumar on 02-02-2018.
 */

public class TeamsListAdapter extends BaseAdapter {
    Context context;
    ArrayList<TeamsListModel> teamsListModelArrayList;
    TeamsListItemClickCallBack teamsListItemClickCallBack;
    String stringDate;

    public TeamsListAdapter(Context context, ArrayList<TeamsListModel> teamsListModelArrayList, TeamsListItemClickCallBack teamsListItemClickCallBack) {
        this.context = context;
        this.teamsListModelArrayList = teamsListModelArrayList;
        this.teamsListItemClickCallBack = teamsListItemClickCallBack;
    }

    @Override
    public int getCount() {
        return teamsListModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return teamsListModelArrayList.hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rootView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.teams_item, null);
        } else {
            rootView = convertView;
        }
        holder.textViewTitleName = (TextView) rootView.findViewById(R.id.textViewTitleName);
        holder.imageViewLogo = (ImageView) rootView.findViewById(R.id.imageViewLogo);
        holder.textViewMatchNo = (TextView) rootView.findViewById(R.id.textViewMatchNo);
        holder.textViewDate = (TextView) rootView.findViewById(R.id.textViewDate);
        holder.textViewTimings = (TextView) rootView.findViewById(R.id.textViewTimings);
        holder.lineaLyForColorViews = (LinearLayout) rootView.findViewById(R.id.lineaLyForColorViews);

        holder.textViewTitleName.setText(teamsListModelArrayList.get(position).getName());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profilepic)
                .error(R.drawable.profilepic);


     //   Glide.with(context).load(ConstantKeys.getImageUrl + teamsListModelArrayList.get(position).getPlayer1Logo()).apply(options).into(holder.imageViewLogo);

        Glide.with(context).load(ConstantKeys.getImageUrl + teamsListModelArrayList.get(position).getLogo()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewLogo);
        /*holder.textViewMatchNo.setText(teamsListModelArrayList.get(position).getMatchNo());
        holder.textViewTimings.setText(teamsListModelArrayList.get(position).getScheduleTime());
       // String color = teamsListModelArrayList.get(position).getColorCode();
        if (teamsListModelArrayList.get(position).getScheduleDate().equalsIgnoreCase("") || teamsListModelArrayList.get(position).getScheduleDate() != null) {
            String[] str_array = teamsListModelArrayList.get(position).getScheduleDate().split("T");
            if (str_array.length > 0) {
                stringDate = str_array[0];
                //final String stringTime = str_array[1];
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
                holder.textViewDate.setText(outputDateFormat.format(tempDate));
            }
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
        for (int i = 0; i < teamsListModelArrayList.size(); i++) {
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
            holder.lineaLyForColorViews.setBackground(gd);
            //paint.setColor(rainbow[i]);
            // Do something with the paint.
        }
      /*  GradientDrawable gd = new GradientDrawable();

        // Specify the shape of drawable
        gd.setShape(GradientDrawable.RECTANGLE);

        // Set the fill color of drawable
        gd.setColor(Color.parseColor(color)); // make the background transparent

        // Create a 2 pixels width red colored border for drawable
        //gd.setStroke(2, Color.RED); // border width and color

        // Make the border rounded
        gd.setCornerRadius(25.0f); // border corner radius

        // Finally, apply the GradientDrawable as TextView background
        holder.lineaLyForColorViews.setBackground(gd);
*/


        //holder.lineaLyForColorViews.setBackgroundColor(Color.parseColor(color));
      /*  if (position % 2 == 1) {
            holder.lineaLyForColorViews.setBackgroundResource(R.drawable.round_corner_yello);
        } else {
            holder.lineaLyForColorViews.setBackgroundResource(R.drawable.round_corner_radius);
        }*/
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //teamsListItemClickCallBack.teamsItemLick(view,position);
            }
        });
        return rootView;
    }

    public class Holder {
        TextView textViewTitleName, textViewMatchNo, textViewDate, textViewTimings;
        LinearLayout lineaLyForColorViews;
        ImageView imageViewLogo;
    }
}
