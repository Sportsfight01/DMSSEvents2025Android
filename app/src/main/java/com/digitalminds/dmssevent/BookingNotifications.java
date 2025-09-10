package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.digitalminds.dmssevent.adapters.PendingBookingsRecycleViewAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.GridSpacingItemDecoration;
import com.digitalminds.dmssevent.common.Util;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.models.BookingStatusResonse;
import com.digitalminds.dmssevent.models.MyBookingsModel;
import com.digitalminds.dmssevent.models.PendingBookimgsModel;
import com.digitalminds.dmssevent.models.TotalBookingGamesResonse;
import com.digitalminds.dmssevent.viewmodel.BookingsNotificationsViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookingNotifications extends AppCompatActivity implements BookingInterface {
    PendingBookingsRecycleViewAdapter pendingBookingsAdapter;
    RecyclerView pendingBookingsRecycleView;
    BookingsNotificationsViewModel notificationsViewModel;
    DmsEventsAppController appController;
    private ProgressDialog dialog;
    TotalBookingGamesResonse totalBookingGamesResonse;
    ArrayList<PendingBookimgsModel> pendingBookimgsModelArrayList = new ArrayList<PendingBookimgsModel>();
    ArrayList<MyBookingsModel> myBookingsModelArrayList = new ArrayList<MyBookingsModel>();
    Toolbar toolbar;
    TextView toolbar_title_fragments;
    String userID = "";
    LinearLayout bookingLinearLy, noBookingsLinearLy;
    SwipeRefreshLayout mySwipeRefreshLayout;

    int showDialog=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_notifications);
        notificationsViewModel = ViewModelProviders.of(BookingNotifications.this).get(BookingsNotificationsViewModel.class);
        intializeUIElements();
    }

    private void intializeUIElements() {
        userID = Integer.toString(DmsSharedPreferences.getUserDetails(BookingNotifications.this).getId());
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar_title_fragments = (TextView) toolbar.findViewById(R.id.toolbar_title_fragments);
        toolbar_title_fragments.setText("Invitations");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        appController = (DmsEventsAppController) getApplicationContext();
        dialog = new ProgressDialog(BookingNotifications.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        pendingBookingsRecycleView = findViewById(R.id.pendingBookingsRecycleView);
        bookingLinearLy = findViewById(R.id.bookingLinearLy);
        noBookingsLinearLy = findViewById(R.id.noBookingsLinearLy);
        mySwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(BookingNotifications.this, 1);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(BookingNotifications.this, R.dimen.grid_size);
        pendingBookingsRecycleView.addItemDecoration(itemDecoration);
        pendingBookingsRecycleView.setLayoutManager(gridLayoutManager);
        totalBookingGamesResonse = new TotalBookingGamesResonse();
        if(Util.isNetworkAvailable(BookingNotifications.this)){
            notificationsViewModel.init(userID, "getTotalBookings", 0, "");
            dialog.setMessage("Loading...");
            dialog.show();
            setTotalBookingData();
        }
         mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(Util.isNetworkAvailable(BookingNotifications.this)){
                            notificationsViewModel.init(userID, "getTotalBookings", 0, "");
                            dialog.setMessage("Loading...");
                            dialog.show();
                            setTotalBookingData();
                        }
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

    }

    private void updateInvitationData() {
        notificationsViewModel = ViewModelProviders.of(BookingNotifications.this).get(BookingsNotificationsViewModel.class);
        notificationsViewModel.getInvitationResponseData().observe(BookingNotifications.this, new Observer<BookingStatusResonse>() {
            @Override
            public void onChanged(BookingStatusResonse bookingStatusResonse) {
                if (bookingStatusResonse != null) {

                    String message=bookingStatusResonse.getTotalResult().getMessage();

                    notificationsViewModel.init(userID, "getTotalBookings", 0, "");
                    /*if (dialog.isShowing()) {
                        dialog.dismiss();
                    }*/
                    Toast.makeText(BookingNotifications.this, message, Toast.LENGTH_SHORT).show();
                    showDialog=1;
                    setTotalBookingData();

                }else {

                }
            }
        });
    }

    private void setTotalBookingData() {
        notificationsViewModel.getTotalGamesData().observe(BookingNotifications.this, new Observer<TotalBookingGamesResonse>() {
            @Override
            public void onChanged(TotalBookingGamesResonse totalBookingResponse) {
                if (totalBookingResponse != null) {
                    if (showDialog==0&&dialog.isShowing()) {
                        dialog.dismiss();
                    }else if(showDialog==1&&dialog.isShowing()){
                        dialog.show();
                    }

                    pendingBookimgsModelArrayList.clear();
                    totalBookingGamesResonse = totalBookingResponse;
                    List<PendingBookimgsModel> pendingBookimgsModelList = totalBookingResponse.getTotalResult().getPendingBookings();
                    List<MyBookingsModel> myBookingsModelList = totalBookingResponse.getTotalResult().getMyBookings();
                    pendingBookimgsModelArrayList.addAll(pendingBookimgsModelList);
                    myBookingsModelArrayList.addAll(myBookingsModelList);
                    if (pendingBookimgsModelList.size() != 0) {
                        bookingLinearLy.setVisibility(View.VISIBLE);
                        noBookingsLinearLy.setVisibility(View.GONE);
                    } else {
                        bookingLinearLy.setVisibility(View.GONE);
                        noBookingsLinearLy.setVisibility(View.VISIBLE);
                    }

                    if (pendingBookingsAdapter == null) {
                        pendingBookingsAdapter = new PendingBookingsRecycleViewAdapter(BookingNotifications.this, pendingBookimgsModelArrayList, BookingNotifications.this);
                        pendingBookingsRecycleView.setAdapter(pendingBookingsAdapter);
                    } else {
                        pendingBookingsAdapter.notifyDataSetChanged();
                    }
                        if(showDialog==1&&dialog.isShowing()){
                            dialog.dismiss();
                            showDialog=0;
                        }

                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    bookingLinearLy.setVisibility(View.GONE);
                    Toast.makeText(BookingNotifications.this,"Network error,Please try after some time",Toast.LENGTH_SHORT).show();
                    //noBookingsLinearLy.setVisibility(View.VISIBLE);
                }

            }
        });
       // setPendingBookingAdapterList();
    }

    private void setPendingBookingAdapterList() {

        if (pendingBookingsAdapter == null) {
            pendingBookingsAdapter = new PendingBookingsRecycleViewAdapter(BookingNotifications.this, pendingBookimgsModelArrayList, BookingNotifications.this);
            pendingBookingsRecycleView.setAdapter(pendingBookingsAdapter);
        } else {
            pendingBookingsAdapter.notifyDataSetChanged();
        }
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


    @Override
    public void onClickItem(int position,int bookingPositionId, String call, String bookingPlayerId, String bookingStatus) {
        dialog.setMessage("Loading...");
        dialog.show();
        notificationsViewModel.init(userID, "getInvitations", bookingPositionId, bookingStatus);
        if(pendingBookimgsModelArrayList!=null){
            pendingBookimgsModelArrayList.clear();
        }
        updateInvitationData();

    }
}
