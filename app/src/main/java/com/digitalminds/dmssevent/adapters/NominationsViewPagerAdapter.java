package com.digitalminds.dmssevent.adapters;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.interfaces.AdapterCallBack;
import com.digitalminds.dmssevent.models.NominationAwardsModel;

import java.util.ArrayList;

/**
 * Created by Jaya.Krishna on 27-02-2018.
 */

public class NominationsViewPagerAdapter extends PagerAdapter {
    AdapterCallBack adapterCallBack;
    ArrayList<NominationAwardsModel> nominationAwardsModelArrayList;
    private Context mContext;

    public NominationsViewPagerAdapter(Context context, ArrayList<NominationAwardsModel> nominationAwardsModelArrayList) {
        this.nominationAwardsModelArrayList = nominationAwardsModelArrayList;
        mContext = context;
        adapterCallBack = (AdapterCallBack) context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        NominationAwardsModel tabsModel = nominationAwardsModelArrayList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup rootView;
        rootView = (ViewGroup) inflater.inflate(R.layout.nomination_viewpager_item, collection, false);

        LinearLayout nominationLayout = (LinearLayout) rootView.findViewById(R.id.nominationLayout);
        TextView nominationNameTextView = (TextView) rootView.findViewById(R.id.nominationNameTextView);
        TextView remainingCountTextView = (TextView) rootView.findViewById(R.id.remainingCountTextView);
        nominationNameTextView.setText(nominationAwardsModelArrayList.get(position).getAwardName());
        remainingCountTextView.setText(nominationAwardsModelArrayList.get(position).getReward());
        //nominationNameTextView.setText(tabsModels.get(position).getAwardName());
        nominationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallBack.adapterClickedPosition(position,false);
            }
        });
       // nominationNameTextView.setText(tabsModel.getAwardName());
        collection.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return nominationAwardsModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return nominationAwardsModelArrayList.get(position).getAwardName();
    }

    @Override
    public float getPageWidth(int position) {
        float test = super.getPageWidth(position);
        return test / 1;
    }
}