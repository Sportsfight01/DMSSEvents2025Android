package com.digitalminds.dmssevent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.adapters.AwardsAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsEventsBaseActivity;
import com.digitalminds.dmssevent.common.TouchImageView;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.AdapterCallBack;
import com.digitalminds.dmssevent.interfaces.AdapterCallForRating;
import com.digitalminds.dmssevent.interfaces.SubAdapterCallBackInterface;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.AwardsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 05-04-2017.
 */
public class AwardsActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack, AdapterCallForRating, AdapterCallBack, SubAdapterCallBackInterface {
    ListView awardsListView;
    androidx.appcompat.app.ActionBar actionBar;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    ArrayList<AwardsModel> awardsModelArrayList = new ArrayList<AwardsModel>();
    AwardsAdapter awardsAdapter;
    LinearLayout emptyElement, awardDescription;
    TextView retryTextView;
    String profilePic = "";
    RelativeLayout awardOverViewLayout;
    ImageView closeImageView, awardImageView;
    boolean largeImageDisplayed = false;
    View viewTouchToExit;
    Toolbar toolbar;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Awards");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(AwardsActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        awardsListView = (ListView) findViewById(R.id.ratingListView);
        emptyElement = (LinearLayout) findViewById(R.id.emptyElement);
        retryTextView = (TextView) findViewById(R.id.retryTextView);
        awardOverViewLayout = (RelativeLayout) findViewById(R.id.awardOverViewLayout);
        viewTouchToExit = (View) findViewById(R.id.viewTouchToExit);
        closeImageView = (ImageView) findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(this);
        viewTouchToExit.setOnClickListener(this);
        awardImageView = (ImageView) findViewById(R.id.awardImageView);
        awardDescription = (LinearLayout) findViewById(R.id.awardDescription);
        callWebApiForEventAwardDetails();


    }

    private void callWebApiForEventAwardDetails() {
        if (Utils.isNetworkAvailable(AwardsActivity.this)) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            String url = ConstantKeys.awards + controller.getSelectedEvent().getId();
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForEventAwardDetails();
                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeImageView:
                awardOverViewLayout.setVisibility(View.GONE);
                largeImageDisplayed = false;
                break;
            case R.id.viewTouchToExit:
                awardOverViewLayout.setVisibility(View.GONE);
                largeImageDisplayed = false;
                break;
        }
    }

    @Override
    public void actionBarSettings() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
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
        ImageView imgReferesh = (ImageView) view.findViewById(R.id.imgReferesh);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        imgReferesh.setVisibility(View.GONE);
        actionBarHeadingTextView.setText("Events Awards");
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                if (status) {
                    awardsModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AwardsModel awardsModel = new AwardsModel(jsonArray.getJSONObject(i));
                        awardsModel.setAwardImage(saveProfilePicUrl(awardsModel.getId()));
                        awardsModelArrayList.add(awardsModel);
                    }
                    this.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (awardsAdapter == null) {
                                awardsAdapter = new AwardsAdapter(AwardsActivity.this, awardsModelArrayList);
                                awardsListView.setAdapter(awardsAdapter);
                                awardsListView.setEmptyView(findViewById(R.id.textViewNoResults));
                            } else {
                                awardsAdapter.notifyDataSetChanged();
                            }
                        }
                    }));


                    //  }
                } else {
                    Utils.showToast(AwardsActivity.this, "Server Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        progressDialog.cancel();
    }

    @Override
    public void onServiceCallFail(final String error) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (error != null) {
                    Utils.showToast(AwardsActivity.this, error);
                } else {
                    Utils.showToast(AwardsActivity.this, "Network Error");
                }
                progressDialog.cancel();
            }
        });

    }


    @Override
    public void adapterClickedPosition(int value, int eventID) {

    }

    public String saveProfilePicUrl(int id) {
        profilePic = "";
        final String imageName = ConstantKeys.fBAwardsAlbumName + "/" + id + "_AwardName.jpg";
        StorageReference childRef = controller.getStorageRef().child(imageName);
        childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                profilePic = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                profilePic = "";
            }
        });
        return profilePic;
    }

    @Override
    public void adapterClickedPosition(int position, boolean subChildItem) {

        String url = awardsModelArrayList.get(position).getAwardImage();
        loadLargeImage(url);

    }

    @Override
    public void onBackPressed() {

        if (largeImageDisplayed) {
            awardOverViewLayout.setVisibility(View.GONE);
            largeImageDisplayed = false;
        } else {
            finish();
        }
    }

    @Override
    public void SubAdapterCallBack(String url) {
        loadLargeImage(url);
    }

    public void loadLargeImage(String url) {
        if (url != null && url.length() > 0) {
            dialogToShowProfilePic(url);
        } else {
            Utils.showToast(AwardsActivity.this, "No Image to display");
        }
    }

    private void dialogToShowProfilePic(String url) {
        //uploadedImageUrl = DmsSharedPreferences.getProfilePicUrl(ProfileActivity.this);
        final Dialog dialogForProfile = new Dialog(AwardsActivity.this);
        dialogForProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForProfile.setContentView(R.layout.profile_dialog);

        dialogForProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        // set the custom dialog components - text, image and button
        TouchImageView touchImageView = (TouchImageView) dialogForProfile.findViewById(R.id.profilePictureOverViewImageView);
        ImageView imageViewClose = (ImageView) dialogForProfile.findViewById(R.id.imageViewClose);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image);


        Glide.with(this).load(url).apply(options).into(touchImageView);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForProfile.cancel();
            }
        });

        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialogForProfile.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogForProfile.show();
        // Apply the newly created layout parameters to the alert dialog window
        dialogForProfile.getWindow().setAttributes(lWindowParams);
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
