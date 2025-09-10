package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalminds.dmssevent.adapters.BannerGalleryViewPagerAdapter;
import com.digitalminds.dmssevent.adapters.GalleryRecycleViewAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsEventsBaseActivity;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.interfaces.AdapterCallBack;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.PhotoDetailsModel;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar J on 30-03-2017.
 */
public class BannerGalleryActivity extends DmsEventsBaseActivity implements AdapterCallBack, WebServiceResponseCallBack {
    public static int bannerPosition;
    RecyclerView recyclerView;
    ViewPager imageViewPager;
    DmsEventsAppController controller;
    GalleryRecycleViewAdapter galleryRecycleViewAdapter;
    BannerGalleryViewPagerAdapter galleryViewPagerAdapter;
    androidx.appcompat.app.ActionBar actionBar;
    Context thisContext;
    int selectedPosition = 0;
    ProgressDialog progressDialog;
    Context context;
    public static ArrayList<PhotoDetailsModel> photoDetailsModels = new ArrayList<PhotoDetailsModel>();
    Toolbar toolbar;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_gallery);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Events Gallery");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initializeUIElements();
    }

    @Override
    public void adapterClickedPosition(int position, boolean subChildItem) {
        imageViewPager.setCurrentItem(position, false);
        selectedPosition = position;
    }

    @Override
    public void initializeUIElements() {
        selectedPosition = bannerPosition;
        context = BannerGalleryActivity.this;
        controller = (DmsEventsAppController) getApplicationContext();
        thisContext = BannerGalleryActivity.this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_viewBanner);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisContext, LinearLayoutManager.HORIZONTAL, false));
        imageViewPager = (ViewPager) findViewById(R.id.imageViewPagerBanner);
        galleryViewPagerAdapter = new BannerGalleryViewPagerAdapter(photoDetailsModels, thisContext);
        galleryRecycleViewAdapter = new GalleryRecycleViewAdapter(photoDetailsModels, thisContext, selectedPosition, DmsSharedPreferences.getUserDetails(BannerGalleryActivity.this).getId());
        recyclerView.setAdapter(galleryRecycleViewAdapter);
        imageViewPager.setAdapter(galleryViewPagerAdapter);
        imageViewPager.setCurrentItem(selectedPosition, false);
        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                galleryRecycleViewAdapter.setSelectedPosition(position);
                recyclerView.smoothScrollToPosition(position);
                selectedPosition = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void actionBarSettings() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_action_bar, null);
        actionBar.setCustomView(view, new androidx.appcompat.app.ActionBar.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        actionBar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM | androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME);
        TextView actionBarHeadingTextView = (TextView) view.findViewById(R.id.actionBarHeadingTextView);
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        ImageView imgCreateFolder = (ImageView) view.findViewById(R.id.imgCreateFolder);
        imgCreateFolder.setVisibility(View.GONE);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        actionBarHeadingTextView.setText("Events Gallery");
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onServiceCallSuccess(String result) {

    }

    @Override
    public void onServiceCallFail(String error) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
