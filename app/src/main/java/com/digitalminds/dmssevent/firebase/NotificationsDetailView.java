package com.digitalminds.dmssevent.firebase;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.FragmentsMainActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.ExpandableTextView;
import com.digitalminds.dmssevent.common.Utils;

import java.util.List;

public class NotificationsDetailView extends AppCompatActivity{
    public static String imageUrl;
    public static String titleValue;
    public static String messageValue;

    ImageView image;

    //RelativeLayout messageView;
    Toolbar toolbar;
    TextView textViewTitle;
    TextView toolbar_title;
    //TextView textViewDescription;
    ExpandableTextView textViewDescription;



    DmsEventsAppController controller;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staticpage);
        controller=(DmsEventsAppController) getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("News Feed");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        intilizeUIElements();
    }

    private void intilizeUIElements() {
        image=(ImageView)findViewById(R.id.image);
        //messageView=(RelativeLayout)findViewById(R.id.messageView);
        textViewTitle=(TextView)findViewById(R.id.textViewTitle);
        textViewDescription=(ExpandableTextView) findViewById(R.id.textViewDescriptionNotif);
        textViewDescription.setAnimationDuration(750L);
        // set interpolators for both expanding and collapsing animations
        textViewDescription.setInterpolator(new OvershootInterpolator());
// or set them separately
        textViewDescription.setExpandInterpolator(new OvershootInterpolator());
        textViewDescription.setCollapseInterpolator(new OvershootInterpolator());
        /*SpannableString content = new SpannableString(headingValue);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);*/
        if(imageUrl!=null&&imageUrl.length()>0){
            image.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.no_imag_aval);



            Glide.with(this).load(imageUrl).apply(options).into(image);
        }else {
            image.setVisibility(View.GONE);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUrl.length()>0){
                    Utils.dialogToShowWallpaper(NotificationsDetailView.this,imageUrl);
                }
            }
        });

      /*  if (bmp != null) {
            image.setImageBitmap(bmp);
            image.setVisibility(View.VISIBLE);
        } else {
            textViewTitle.setText(titleValue);
            textViewDescription.setText(messageValue);
            image.setVisibility(View.GONE);
        }*/

        textViewTitle.setText(titleValue);
        textViewDescription.setText(messageValue);
        textViewDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (textViewDescription.isExpanded()) {
                    textViewDescription.collapse();
                } else {
                    textViewDescription.expand();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                if(isAppAlreadyOpen())
                {
                    controller.setNotificationCalled(true);
                    finish();
                }else{
                    Intent in =new Intent(NotificationsDetailView.this, FragmentsMainActivity.class);
                    controller.setNotificationCalled(true);
                    startActivity(in);
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
   /* @Override
    public void onClick(View view) {
        if(view.getId()==back.getId())
        {
            if(isAppAlreadyOpen())
            {
                finish();
            }else{
                Intent in =new Intent(NotificationsDetailView.this, FragmentsMainActivity.class);
                controller.setNotificationCalled(true);
                startActivity(in);
                finish();
            }
        }
    }*/

    public boolean isAppAlreadyOpen() {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if (taskList.get(0).numActivities >1 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        image=null;
        titleValue=null;
        super.onDestroy();
    }
}