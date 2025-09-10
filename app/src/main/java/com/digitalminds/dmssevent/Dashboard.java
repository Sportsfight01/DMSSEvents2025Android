package com.digitalminds.dmssevent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.adapters.EventsAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsEventsBaseActivity;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.EventsDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Dashboard.java -
 *
 * @author sandeepkumar
 * @version 1.0
 * @see DmsEventsBaseActivity
 * @since 12-03-2017.
 */
public class Dashboard extends DmsEventsBaseActivity implements WebServiceResponseCallBack,SwipeRefreshLayout.OnRefreshListener {
    ListView albumsListView;
    ArrayList<EventsDetailsModel> arrayListDetailsModels = new ArrayList<EventsDetailsModel>();
    androidx.appcompat.app.ActionBar actionBar;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout emptyElement;
    TextView retryTextView;
    EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventlist);
        actionBarSettings();
        initializeUIElements();

    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(Dashboard.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        albumsListView = (ListView) findViewById(R.id.albumsListView);
        emptyElement=(LinearLayout)findViewById(R.id.emptyElement);
        retryTextView=(TextView)findViewById(R.id.retryTextView);
       // swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        callWebApiForEventDetails();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        callWebApiForEventDetails();
                                    }
                                }
        );

    }

    private void callWebApiForEventDetails() {
        if (Utils.isNetworkAvailable(Dashboard.this)) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            albumsListView.setVisibility(View.VISIBLE);
            String url=ConstantKeys.eventList;
            controller.getWebService().getData(url, this);
        }else{
            swipeRefreshLayout.setRefreshing(false);
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForEventDetails();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

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
        ImageView imageViewLogout = (ImageView) view.findViewById(R.id.imageViewLogout);
        imageViewLogout.setVisibility(View.VISIBLE);

        ImageView imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
        ImageView imageViewNotifications = (ImageView) view.findViewById(R.id.imageViewNotifications);
        imageViewProfile.setVisibility(View.VISIBLE);
        imageViewNotifications.setVisibility(View.VISIBLE);
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        actionBarBackImageView.setVisibility(View.GONE);
        //actionBarHeadingTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        actionBarHeadingTextView.setText("Dmss Events");
        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutDialog();

            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        imageViewNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, FireBaseActivity.class);
                startActivity(i);
            }
        });

    }

    private void LogOutDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
        dialog.setCancelable(false);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure you want to logout ?" );
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                DmsSharedPreferences.saveUserLoggedInStatus(Dashboard.this, false);
                Intent i = new Intent(Dashboard.this, MailValidationActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(Dashboard.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);

                if (status) {

                    arrayListDetailsModels = null;
                    arrayListDetailsModels = new ArrayList<EventsDetailsModel>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        EventsDetailsModel eventsDetailsModel = new EventsDetailsModel(jsonArray.getJSONObject(i));
                        arrayListDetailsModels.add(eventsDetailsModel);

                    }
                    this.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            eventsAdapter = null;
                            eventsAdapter = new EventsAdapter(Dashboard.this, arrayListDetailsModels,controller);
                            albumsListView.setAdapter(eventsAdapter);
                            eventsAdapter.notifyDataSetChanged();
                            //albumsListView.setEmptyView(findViewById(R.id.emptyElement));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }));
                    //  }
                } else {
                    Utils.showToast(Dashboard.this, "Server Error");
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
            Utils.showToast(Dashboard.this, error);
        } else {
            Utils.showToast(Dashboard.this, "Network Error");
        }
        progressDialog.cancel();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        callWebApiForEventDetails();
    }
}
