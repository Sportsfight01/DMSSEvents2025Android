package com.digitalminds.dmssevent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.adapters.PollRecycleViewAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsEventsBaseActivity;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.TouchImageView;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.AdapterCallForRating;
import com.digitalminds.dmssevent.interfaces.SubAdapterCallBackInterface;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.EventRatingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class RatingActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack, AdapterCallForRating, SwipeRefreshLayout.OnRefreshListener, SubAdapterCallBackInterface {
    //ListView ratingListView;
    RecyclerView recycler_view;
    androidx.appcompat.app.ActionBar actionBar;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    ArrayList<EventRatingModel> eventRatingModelArrayList = new ArrayList<EventRatingModel>();
    int apiCall = 0;
    //RatingAdapter ratingAdapter;
    PollRecycleViewAdapter ratingAdapter;
    Timer timer = new Timer();
    //private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout emptyElement;
    TextView retryTextView, emptyTextView;
    RelativeLayout ratingOverViewLayout;
    ImageView closeRatingImageView, imgReferesh;
    TouchImageView ratingImageView;
    boolean imageVisible = false;
    Toolbar toolbar;
    TextView toolbar_title, textViewNoResults;
    private RecyclerView.LayoutManager layoutManager;
    EventRatingModel eventsRatingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        //actionBarSettings();
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imgReferesh = (ImageView) toolbar.findViewById(R.id.imgReferesh);
        toolbar_title.setText("Audience Poll");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(RatingActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        // ratingListView = (ListView) findViewById(R.id.ratingListView);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        emptyElement = (LinearLayout) findViewById(R.id.emptyElement);
        retryTextView = (TextView) findViewById(R.id.retryTextView);
        textViewNoResults = (TextView) findViewById(R.id.textViewNoResults);
        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        ratingOverViewLayout = (RelativeLayout) findViewById(R.id.ratingOverViewLayout);
        closeRatingImageView = (ImageView) findViewById(R.id.closeRatingImageView);
        closeRatingImageView.setOnClickListener(this);
        ratingImageView = (TouchImageView) findViewById(R.id.ratingImageView);
        //ratingListView.setEmptyView(findViewById(R.id.emptyElement));
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        //swipeRefreshLayout.setOnRefreshListener(this);
        callWebApiForEventRatingDetails();
        retryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebApiForEventRatingDetails();
            }
        });
        /*swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        callWebApiForEventRatingDetails();
                                    }
                                }
        );*/
        imgReferesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebApiForEventRatingDetails();
            }
        });

    }


    private void callWebApiForEventRatingDetails() {
        if (Utils.isNetworkAvailable(RatingActivity.this)) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            apiCall = 1;
            String url = ConstantKeys.getPerformances + DmsSharedPreferences.getUserDetails(RatingActivity.this).getId() + "/" + controller.getSelectedEvent().getId();
            controller.getWebService().getData(url, this);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            progressDialog.cancel();
            emptyTextView.setText("No Internet available..Please");
            retryTextView.setText("Retry");
            emptyElement.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View view) {
        closeRatingImageView.setVisibility(View.GONE);
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
        imgReferesh.setVisibility(View.VISIBLE);
        //actionBarHeadingTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        actionBarHeadingTextView.setText("Audience Poll");
        imgReferesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                callWebApiForEventRatingDetails();
            }
        });
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String getProfilePic(String EmpID,String DisplayName) {

        final String[] profilePic = new String[1];
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://dmssevents-606bf.appspot.com");
        final String imageName = "Profile_pics/" + EmpID + "_" + DisplayName + ".jpg";
        StorageReference childRef = storageRef.child(imageName);
        childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                profilePic[0] = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                profilePic[0] = "";
            }
        });
        return profilePic[0];
    }


    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                if (status) {
                    if (apiCall == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                        eventRatingModelArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventsRatingModel = new EventRatingModel(jsonArray.getJSONObject(i));
                            eventRatingModelArrayList.add(eventsRatingModel);
                            controller.setEventRatingModel(eventsRatingModel);
                        }
                        int size = eventRatingModelArrayList.size();
                        this.runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (ratingAdapter == null) {
                                    textViewNoResults.setVisibility(View.GONE);
                                    // ratingAdapter = new RatingAdapter(RatingActivity.this, eventRatingModelArrayList);

                                    /*ArrayList<ParticipantsModel> participantList = new ArrayList<>();

                                    for (int i = 0; i < eventRatingModelArrayList.size(); i++) {

                                        for (int j = 0; j < eventRatingModelArrayList.get(i).getArrayParticipantsModels().size(); j++) {
                                            ParticipantsModel participantsModel = eventRatingModelArrayList.get(i).getArrayParticipantsModels().get(j);
                                            String profileImageUrl =  getProfilePic(participantsModel.getEmpID(),participantsModel.getDisplayName());
                                            participantsModel.setProfilePic(profileImageUrl);
                                            participantList.add(participantsModel);

                                        }
                                        eventRatingModelArrayList.get(i).getArrayParticipantsModels().clear();
                                        eventRatingModelArrayList.get(i).setArrayParticipantsModels(participantList);
                                    }*/



                                    ratingAdapter = new PollRecycleViewAdapter(RatingActivity.this, eventRatingModelArrayList);
                                    recycler_view.setAdapter(ratingAdapter);
                                    if (eventRatingModelArrayList.isEmpty()) {
                                        textViewNoResults.setVisibility(View.VISIBLE);
                                    } else {
                                        textViewNoResults.setVisibility(View.GONE);
                                    }
                                    //recycler_view.setEmptyView(findViewById(R.id.textViewNoResults));

                                } else {
                                    ratingAdapter.notifyDataSetChanged();
                                    if (eventRatingModelArrayList.isEmpty()) {
                                        textViewNoResults.setVisibility(View.VISIBLE);
                                    } else {
                                        textViewNoResults.setVisibility(View.GONE);
                                    }
                                }
                                //swipeRefreshLayout.setRefreshing(false);
                            }
                        }));

                    } else if (apiCall == 2) {
                        final String message = jsonObject.getString(ConstantKeys.messageKey);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RatingActivity.this, message, Toast.LENGTH_SHORT).show();
                                callWebApiForEventRatingDetails();
                                //swipeRefreshLayout.setRefreshing(false);
                            }
                        });

                    }
                    //  }
                } else {
                    Utils.showToast(RatingActivity.this, "Server Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        progressDialog.cancel();
    }

    @Override
    public void onServiceCallFail(String error) {
        if (error != null) {
            Utils.showToast(RatingActivity.this, error);
        } else {
            Utils.showToast(RatingActivity.this, "Network Error");
        }
        progressDialog.cancel();
        //swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void adapterClickedPosition(int value, int id) {
        apiCall = 2;
        progressDialog.show();
        callWebApiForRating(value, id);

    }

    private void callWebApiForRating(int ratingValue, int id) {
        if (Utils.isNetworkAvailable(RatingActivity.this)) {
            String url = ConstantKeys.rating;
            controller.getWebService().postData(url, getJsonDataForRating(ratingValue, id), this);
        } else {
            progressDialog.cancel();
        }
    }

    private String getJsonDataForRating(int ratingValue, int id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RatedBy", DmsSharedPreferences.getUserDetails(RatingActivity.this).getId());
            jsonObject.put("TPEventId", id);
            jsonObject.put("Rate", ratingValue);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject.toString();
    }


    @Override
    public void onRefresh() {
        callWebApiForEventRatingDetails();
    }

    @Override
    public void SubAdapterCallBack(String url) {
        if (url != null && url.length() > 0) {
            /*ratingOverViewLayout.setVisibility(View.VISIBLE);
            ratingImageView.bringToFront();
            closeRatingImageView.bringToFront();
            imageVisible = true;
            //String mainUrl=url.replaceAll(" ","%20");
            Glide.with(RatingActivity.this).load(url).into(ratingImageView);*/
            dialogToShowProfilePic(url);
            // Picasso.with(RatingActivity.this).load(url).into(ratingImageView);
        } else {
            Utils.showToast(RatingActivity.this, "No Image to display");
        }
    }

    private void dialogToShowProfilePic(String url) {
        //uploadedImageUrl = DmsSharedPreferences.getProfilePicUrl(ProfileActivity.this);
        final Dialog dialogForProfile = new Dialog(RatingActivity.this);
        dialogForProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForProfile.setContentView(R.layout.profile_dialog);

        dialogForProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        // set the custom dialog components - text, image and button
        TouchImageView touchImageView = (TouchImageView) dialogForProfile.findViewById(R.id.profilePictureOverViewImageView);
        ImageView imageViewClose = (ImageView) dialogForProfile.findViewById(R.id.imageViewClose);
        //Picasso.with(getActivity()).load(ConstantKeys.getImageUrl + uploadedImageUrl).into(touchImageView);
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
    public void onBackPressed() {
        if (imageVisible) {
            ratingOverViewLayout.setVisibility(View.GONE);
            imageVisible = false;
        } else {
            finish();
        }
    }

    public void nodata() {
        emptyElement.setVisibility(View.VISIBLE);
        emptyTextView.setText("Please wait for completion of team performance");
        retryTextView.setText("Refresh to see Ratings");
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
