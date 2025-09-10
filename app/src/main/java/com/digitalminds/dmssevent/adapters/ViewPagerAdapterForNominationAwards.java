package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.interfaces.AdapterCallBack;
import com.digitalminds.dmssevent.models.NominationAwardsModel;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 31-01-2018.
 */

public class ViewPagerAdapterForNominationAwards extends PagerAdapter {
    AdapterCallBack adapterCallBack;
    ArrayList<NominationAwardsModel> nominationAwardsModelArrayList;
    private Context mContext;
   ProgressBar progressBar;
    int nominationCount;
    public ViewPagerAdapterForNominationAwards(Context mContext, ArrayList<NominationAwardsModel> nominationAwardsModelArrayList,int nominationCount) {
        this.mContext = mContext;
        this.nominationCount = nominationCount;
        this.nominationAwardsModelArrayList = nominationAwardsModelArrayList;
        adapterCallBack=(AdapterCallBack)mContext;
    }

    @Override
    public int getCount() {
        return nominationAwardsModelArrayList.size();
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
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.nomination_viewpager_item, container, false);
        LinearLayout nominationLayout = (LinearLayout) itemView.findViewById(R.id.nominationLayout);
        TextView nominationNameTextView = (TextView) itemView.findViewById(R.id.nominationNameTextView);
        TextView textViewNomineate = (TextView) itemView.findViewById(R.id.textViewNomineate);
        TextView textViewRewards = (TextView) itemView.findViewById(R.id.textViewRewards);
        ImageView imageViewAwardPhoto = (ImageView) itemView.findViewById(R.id.imageViewAwardPhoto);
        final ImageView imageViewAwardPhotoBag = (ImageView) itemView.findViewById(R.id.imageViewAwardPhotoBag);
        TextView remainingCountTextView = (TextView) itemView.findViewById(R.id.remainingCountTextView);
        nominationNameTextView.setText(nominationAwardsModelArrayList.get(position).getAwardName());
        textViewRewards.setText(nominationAwardsModelArrayList.get(position).getReward());
        if(nominationCount==0){
            textViewNomineate.setAlpha(0.5f);
        }else{
            textViewNomineate.setAlpha(1f);

        }


       //Glide.with(mContext).load(ConstantKeys.getAllImagesUrl + nominationAwardsModelArrayList.get(position).getIconURL()).into(imageViewAwardPhoto);

        Glide.with(mContext)
                .load(ConstantKeys.getAllImagesUrl + nominationAwardsModelArrayList.get(position).getIconURL())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageViewAwardPhotoBag.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageViewAwardPhotoBag.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageViewAwardPhoto);
       /* Glide.with(mContext)
                .load(ConstantKeys.getAllImagesUrl + nominationAwardsModelArrayList.get(position).getIconURL())
                .error(R.drawable.ERROR_IMAGE_NAME)
                .into(imageViewAwardPhoto);*/

        //nominationNameTextView.setText(tabsModels.get(position).getAwardName());
        nominationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallBack.adapterClickedPosition(position,false);
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
