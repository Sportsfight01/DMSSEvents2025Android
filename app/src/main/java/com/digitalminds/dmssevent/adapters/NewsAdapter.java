package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.ExpandableTextView;
import com.digitalminds.dmssevent.common.RoundedCornersTransformation;
import com.digitalminds.dmssevent.interfaces.AdaterClickCallBackForFavourite;
import com.digitalminds.dmssevent.interfaces.AdaterClickCallBackForPosition;
import com.digitalminds.dmssevent.interfaces.SubAdapterCallBackInterface;
import com.digitalminds.dmssevent.interfaces.TeamsListItemClickCallBack;
import com.digitalminds.dmssevent.models.GetAllNewsFeedModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Sandeep.Kumar on 30-01-2018.
 */

public class NewsAdapter extends BaseAdapter {
    Context context;
    ArrayList<GetAllNewsFeedModel> allNewsFeedModelArrayList;
    public static int sCorner = 0;
    public static int sMargin = 0;
    AdaterClickCallBackForPosition adaterClickCallBackForPosition;
    AdaterClickCallBackForFavourite adaterClickCallBackForFavourite;
    SubAdapterCallBackInterface subAdapterCallBackInterface;
    TeamsListItemClickCallBack teamsListItemClickCallBack;
    String imageUrl;

    public NewsAdapter(Context context, ArrayList<GetAllNewsFeedModel> allNewsFeedModelArrayList,
                       AdaterClickCallBackForPosition adaterClickCallBackForPosition,AdaterClickCallBackForFavourite adaterClickCallBackForFavourite,
                       SubAdapterCallBackInterface subAdapterCallBackInterface) {
        this.context = context;
        this.allNewsFeedModelArrayList = allNewsFeedModelArrayList;
        this.adaterClickCallBackForPosition = adaterClickCallBackForPosition;
        this.adaterClickCallBackForFavourite = adaterClickCallBackForFavourite;
        this.subAdapterCallBackInterface = subAdapterCallBackInterface;
    }

    @Override
    public int getCount() {
        return allNewsFeedModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return allNewsFeedModelArrayList.hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.news_adapter, null);
        } else {
            rowView = convertView;
        }


        /**********Declaration of id's***********/
        holder.textViewName = (TextView) rowView.findViewById(R.id.textViewName);
        holder.textViewGap = (TextView) rowView.findViewById(R.id.textViewGap);
        holder.relativeTotalItem = (RelativeLayout) rowView.findViewById(R.id.relativeTotalItem);
        holder.imageViewMewFeedImage = (ImageView) rowView.findViewById(R.id.imageViewMewFeedImage);
        holder.linearLyLike = (LinearLayout) rowView.findViewById(R.id.linearLyLike);
        holder.profileImageView = (ImageView) rowView.findViewById(R.id.profileImageView);
        holder.imageViewLike = (ImageView) rowView.findViewById(R.id.imageViewLike);
        holder.imageViewComments = (ImageView) rowView.findViewById(R.id.imageViewComments);
        holder.progressBar = (ProgressBar) rowView.findViewById(R.id.progress);
        holder.progressGif = (GifImageView) rowView.findViewById(R.id.progressGif);
        holder.textViewDescription = (ExpandableTextView) rowView.findViewById(R.id.textViewDescriptionNewFeed);
        holder.textViewDescription.setAnimationDuration(750L);

        // set interpolators for both expanding and collapsing animations
        holder.textViewDescription.setInterpolator(new OvershootInterpolator());

// or set them separately
        holder.textViewDescription.setExpandInterpolator(new OvershootInterpolator());
        holder.textViewDescription.setCollapseInterpolator(new OvershootInterpolator());
        //holder.textViewDescription = (JustifyTextView) rowView.findViewById(R.id.textViewDescription);
        holder.textViewLikesCount = (TextView) rowView.findViewById(R.id.textViewLikesCount);
        holder.textViewComments = (TextView) rowView.findViewById(R.id.textViewComments);
        holder.textViewDate = (TextView) rowView.findViewById(R.id.textViewDate);
        if (allNewsFeedModelArrayList.get(position).getProfileImageUrl() != null) {
            imageUrl = ConstantKeys.getImageUrl + allNewsFeedModelArrayList.get(position).getProfileImageUrl();
        }
        //imageUrl = imageUrl.replaceAll(" ", "%20");
        Glide.with(context).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(holder.profileImageView);
        //Picasso.with(context).load(imageUrl).transform(new CircleTransform()).into(holder.profileImageView);
        /*Glide.with(context).load
                ("http://192.168.100.92:1010/docs/images/"+allNewsFeedModelArrayList.get(position).getFBImageUrl()).
                apply(RequestOptions.bitmapTransform
                (new RoundedCornersTransformation(context, sCorner, sMargin))).into(holder.imageViewMewFeedImage);*/
        if(allNewsFeedModelArrayList.get(position).getFBImageUrl().length()>0||(!allNewsFeedModelArrayList.get(position).getFBImageUrl().equalsIgnoreCase(""))){
            holder.imageViewMewFeedImage.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
            holder.progressGif.setVisibility(View.VISIBLE);
            String newFeedUrl=ConstantKeys.getAllImagesNewsUrl + allNewsFeedModelArrayList.get(position).getFBImageUrl();
            //newFeedUrl = newFeedUrl.replaceAll(" ", "%20");
            RequestOptions options = new RequestOptions()
                    .centerCrop()
//                    .placeholder(R.drawable.places_ic_search)
                    .error(R.drawable.milestone_25);
        Glide.with(context)
                .load(newFeedUrl)
                .apply(options)
               /// .apply(RequestOptions.bitmapTransform
               // (new RoundedCornersTransformation(context, sCorner, sMargin)))     /*.override(800,300))*/
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //Glide.with(context).load(ConstantKeys.getAllImagesNewsUrl + allNewsFeedModelArrayList.get(position).getFBImageUrl()).apply(RequestOptions.bitmapTransform
                             //   (new RoundedCornersTransformation(context, sCorner, sMargin))).into(holder.imageViewMewFeedImage);
                        holder.progressBar.setVisibility(View.GONE);
                        holder.progressGif.setVisibility(View.GONE);
                        //holder.imageViewMewFeedImage.setBackgroundResource(R.drawable.lightgrey_gradientcolor);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.progressGif.setVisibility(View.GONE);
                        //holder.imageViewMewFeedImage.setBackgroundResource(R.drawable.lightgrey_gradientcolor);
                        return false;
                    }
                })
                .into(holder.imageViewMewFeedImage);
        }else{
            holder.imageViewMewFeedImage.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.progressGif.setVisibility(View.GONE);
            Glide.with(context)
                    .load(R.drawable.rsz_bag_event_eighteen)
                    .apply(RequestOptions.bitmapTransform
                            (new RoundedCornersTransformation(context, sCorner, sMargin)));
        }
        if (allNewsFeedModelArrayList.get(position).isFavourite()) {
            holder.imageViewLike.setImageResource(R.drawable.icon_like);
        } else {
            holder.imageViewLike.setImageResource(R.drawable.icon_unlike);
        }
        if (allNewsFeedModelArrayList.get(position).getCommentsCount()>0) {
            holder.imageViewComments.setImageResource(R.drawable.icon_select_comment);
        } else {
            holder.imageViewComments.setImageResource(R.drawable.icon_unselect_comment);
        }
        /*Bitmap mbitmap = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.milestone)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 100, 100, mpaint);// Round Image Corner 100 100 100 100
        holder.imageViewMewFeedImage .setImageBitmap(imageRounded);*/
       /* if (allNewsFeedModelArrayList.size() > 0) {
            if (allNewsFeedModelArrayList.get(position).getProfileImage()!="") {
                String url=allNewsFeedModelArrayList.get(position).getProfileImage();
                Picasso.with(context).load(url).error(R.drawable.profilepic).transform(new CircleTransform()).into(holder.profileImageView);
            }
        }*/
        // String namesValue = names[position];
        holder.textViewName.setText(allNewsFeedModelArrayList.get(position).getEmployeeName());
        if(allNewsFeedModelArrayList.get(position).getTitle().trim().length()>0){
            holder.textViewGap.setVisibility(View.GONE);
            holder.textViewDescription.setVisibility(View.VISIBLE);
            holder.textViewDescription.setText(allNewsFeedModelArrayList.get(position).getTitle());
            holder.textViewDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (holder.textViewDescription.isExpanded()) {
                        holder.textViewDescription.collapse();
                    } else {
                        holder.textViewDescription.expand();
                    }
                }
            });
        }else{
            holder.textViewDescription.setVisibility(View.GONE);
            holder.textViewGap.setVisibility(View.VISIBLE);
        }

        if (allNewsFeedModelArrayList.get(position).getLikesCount() > 1) {
            holder.textViewLikesCount.setText(String.valueOf(allNewsFeedModelArrayList.get(position).getLikesCount() + " Likes"));
        } else {
            holder.textViewLikesCount.setText(String.valueOf(allNewsFeedModelArrayList.get(position).getLikesCount() + " Like"));
        }
        if (allNewsFeedModelArrayList.get(position).getCommentsCount() > 1) {
            holder.textViewComments.setText(String.valueOf(allNewsFeedModelArrayList.get(position).getCommentsCount() + " Comments"));
        } else {
            holder.textViewComments.setText(String.valueOf(allNewsFeedModelArrayList.get(position).getCommentsCount() + " Comment"));
        }

        if (allNewsFeedModelArrayList.get(position).getCreatedOn() != "" || allNewsFeedModelArrayList.get(position).getCreatedOn() != null) {
            String[] str_array = allNewsFeedModelArrayList.get(position).getCreatedOn().split("T");
            String stringDate = str_array[0];
            final String stringTime = str_array[1];

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date tempDate = null;
            try {
                tempDate = simpleDateFormat.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            holder.textViewDate.setText(outputDateFormat.format(tempDate));
        } else {
            holder.textViewDate.setText("-");
        }
        holder.textViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newsID=allNewsFeedModelArrayList.get(position).getId();
                String name=allNewsFeedModelArrayList.get(position).getEmployeeName();
                adaterClickCallBackForPosition.itemClick(view, position,newsID,name);
            }
        });


        holder.linearLyLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newsID=allNewsFeedModelArrayList.get(position).getId();
                int likesCount=allNewsFeedModelArrayList.get(position).getLikesCount();
                adaterClickCallBackForFavourite.itemClickFavourite(view, position,allNewsFeedModelArrayList.get(position).isFavourite(),newsID,likesCount);
            }
        });
        holder.imageViewMewFeedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subAdapterCallBackInterface.SubAdapterCallBack(ConstantKeys.getAllImagesNewsUrl + allNewsFeedModelArrayList.get(position).getFBImageUrl());
            }
        });
        holder.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subAdapterCallBackInterface.SubAdapterCallBack( ConstantKeys.getImageUrl+allNewsFeedModelArrayList.get(position).getProfileImageUrl());
            }
        });
        return rowView;
    }


    /**********Holder class for intialising variables************/
    public class Holder {
        TextView textViewName, textViewLikesCount, textViewComments, textViewDate,textViewGap;
        ImageView imageViewMewFeedImage, profileImageView,imageViewLike,imageViewComments;
        ProgressBar progressBar;
        GifImageView progressGif;
        LinearLayout linearLyLike;
        RelativeLayout relativeTotalItem;
        ExpandableTextView textViewDescription;
        //JustifyTextView textViewDescription;
    }
}
