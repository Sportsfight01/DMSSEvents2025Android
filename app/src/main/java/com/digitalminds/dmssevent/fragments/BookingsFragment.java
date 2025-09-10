package com.digitalminds.dmssevent.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.AddPlayerFromBookingFragmentsActv;
import com.digitalminds.dmssevent.BookMyGameActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.MyBookingsRecycleViewAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.GridSpacingItemDecoration;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.AddPlayerCallBack;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.interfaces.RemovePlayerInterface;
import com.digitalminds.dmssevent.models.BookingStatusResonse;
import com.digitalminds.dmssevent.models.MyBookingsModel;
import com.digitalminds.dmssevent.models.PendingBookimgsModel;
import com.digitalminds.dmssevent.models.RemovePlayerModel;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;
import com.digitalminds.dmssevent.models.TotalBookingGamesResonse;
import com.digitalminds.dmssevent.viewmodel.TotalBookingViewModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

//J sandeeep Kumar
public class BookingsFragment extends Fragment implements View.OnClickListener, BookingInterface, RemovePlayerInterface, AddPlayerCallBack {
    View rootView;
    DmsEventsAppController appController;
    ProgressBar progressBar;
    TextView percentageTextView, bookMyGameTextView, pendingBkngTextView, myBkngTextView, currentWeekStatusTextView, balanceMinutesTextView, myNotifCountTextView;
    MyBookingsRecycleViewAdapter myBookingsRecycleViewAdapter, myPendingBookingsRecycleViewAdapter;
    RecyclerView myBookingsRecycleView, myPendingRecycleView;
    TotalBookingViewModel totalBookingViewModel;
    private ProgressDialog dialog;
    TotalBookingGamesResonse totalBookingGamesResonse;
    ArrayList<PendingBookimgsModel> pendingBookimgsModelArrayList = new ArrayList<PendingBookimgsModel>();
    ArrayList<MyBookingsModel> myBookingsModelArrayList = new ArrayList<MyBookingsModel>();
    ArrayList<MyBookingsModel> myPendingModelArrayList = new ArrayList<MyBookingsModel>();
    RelativeLayout myInvitationsRelativieLy;
    Toolbar toolbar;
    String message = "test";
    String userID = "";
    LinearLayout emptyElement;
    ScrollView scrollView;
    TextView retryTextView;
    String totalMinutes = "";
    String bookedGamesMinutes = "";
    int ToDay;
    ArrayList<SelectedPlayersResultModel> playerDetailsModelArrayList;
    ArrayList<SelectedPlayersResultModel> playerDetailsModelArrayListA;
    boolean deletePlayerFirst = false;
    TextView myPendingBTextView;
    String showToastMessage = "";
    //SwipeRefreshLayout mySwipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_bookings, parent, false);
        totalBookingViewModel = ViewModelProviders.of(getActivity()).get(TotalBookingViewModel.class);
        intializeUIElements();
        return rootView;
    }

    private void intializeUIElements() {
        userID = Integer.toString(DmsSharedPreferences.getUserDetails(getActivity()).getId());
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        myInvitationsRelativieLy = (RelativeLayout) toolbar.findViewById(R.id.myInvitationsRelativieLy);
        myNotifCountTextView = (TextView) toolbar.findViewById(R.id.myNotifCountTextView);
        myNotifCountTextView.setVisibility(View.GONE);
        appController = (DmsEventsAppController) getActivity().getApplicationContext();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        bookMyGameTextView = (TextView) rootView.findViewById(R.id.bookMyGameTextView);
        currentWeekStatusTextView = (TextView) rootView.findViewById(R.id.currentWeekStatusTextView);
        balanceMinutesTextView = (TextView) rootView.findViewById(R.id.balanceMinutesTextView);
        pendingBkngTextView = (TextView) rootView.findViewById(R.id.pendingBkngTextView);
        myBkngTextView = (TextView) rootView.findViewById(R.id.myBkngTextView);
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        emptyElement = (LinearLayout) rootView.findViewById(R.id.emptyElement);
        retryTextView = (TextView) rootView.findViewById(R.id.retryTextView);
        myPendingBTextView = (TextView) rootView.findViewById(R.id.myPendingBTextView);
        //  mySwipeRefreshLayout =(SwipeRefreshLayout)rootView. findViewById(R.id.mySwipeRefreshLayout);
        bookMyGameTextView.setOnClickListener(this);
        retryTextView.setOnClickListener(this);
        myBookingsRecycleView = (RecyclerView) rootView.findViewById(R.id.myBookingsRecycleView);
        myPendingRecycleView = (RecyclerView) rootView.findViewById(R.id.myPendingRecycleView);
        percentageTextView = rootView.findViewById(R.id.percentageTextView);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(getActivity(), R.dimen.grid_size);
        myBookingsRecycleView.addItemDecoration(itemDecoration);
        myBookingsRecycleView.setLayoutManager(gridLayoutManager);
        myBookingsRecycleView.setNestedScrollingEnabled(false);


        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        GridSpacingItemDecoration itemDecoration1 = new GridSpacingItemDecoration(getActivity(), R.dimen.grid_size);
        myPendingRecycleView.addItemDecoration(itemDecoration1);
        myPendingRecycleView.setLayoutManager(gridLayoutManager1);
        myPendingRecycleView.setNestedScrollingEnabled(false);


        totalBookingGamesResonse = new TotalBookingGamesResonse();
        if (Utils.isNetworkAvailable(getActivity())) {
            emptyElement.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            getTotalResponseData();
        } else {
            emptyElement.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
        /*mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (Utils.isNetworkAvailable(getActivity())) {
                            emptyElement.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            getTotalResponseData();
                        } else {
                            emptyElement.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.GONE);
                        }
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );*/
    }

    private void getTotalResponseData() {
        totalBookingViewModel.init(userID);
        dialog.setMessage("Loading...");
        dialog.show();
        // totalBookingViewModel.getTotalGamesData().removeObservers(this);
        totalBookingViewModel.getTotalGamesData().observe(getActivity(), new Observer<TotalBookingGamesResonse>() {
            @Override
            public void onChanged(TotalBookingGamesResonse totalBookingResponse) {
                if (totalBookingResponse != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    pendingBookimgsModelArrayList.clear();
                    pendingBookimgsModelArrayList.clear();
                    myBookingsModelArrayList.clear();
                    myPendingModelArrayList.clear();
                    totalBookingGamesResonse = totalBookingResponse;
                    ToDay = totalBookingResponse.getTotalResult().getToDay();
                    List<PendingBookimgsModel> pendingBookimgsModelList = totalBookingResponse.getTotalResult().getPendingBookings();
                    List<MyBookingsModel> myBookingsModelList = totalBookingResponse.getTotalResult().getMyBookings();
                    List<MyBookingsModel> myPendingBooking = totalBookingResponse.getTotalResult().getMyPendingBookings();
                    pendingBookimgsModelArrayList.addAll(pendingBookimgsModelList);
                    appController.setPendingBookimgsModelArrayList(pendingBookimgsModelArrayList);
                    myBookingsModelArrayList.addAll(myBookingsModelList);
                    myPendingModelArrayList.addAll(myPendingBooking);
                    appController.setPlayersStatusForAddButton(myBookingsModelArrayList);
                    totalMinutes = Integer.toString(totalBookingResponse.getTotalResult().getTotalMinutes());
                    bookedGamesMinutes = Integer.toString(totalBookingResponse.getTotalResult().getBookedGamesMinutes());
                    double playedMin = (double) totalBookingResponse.getTotalResult().getBookedGamesMinutes();
                    double totalMin = (double) totalBookingResponse.getTotalResult().getTotalMinutes();
                    double percentagePlayed = (playedMin / totalMin);
                    double totalPercentage = percentagePlayed * 100;
                    int value = (int) Math.round(totalPercentage);
                    String totalPercentageText = String.valueOf(value);
                    String weekStatus = Integer.toString(totalBookingResponse.getTotalResult().getBookedGamesCount());
                    balanceMinutesTextView.setText(bookedGamesMinutes + "/" + totalMinutes + " min");
                    percentageTextView.setText(totalPercentageText + "%");
                    currentWeekStatusTextView.setText(weekStatus + " Games booked in this week");
                    int showProgressValue = (int) totalPercentage;
                    progressBar.setProgress(showProgressValue);

                    if (Integer.parseInt(bookedGamesMinutes) >= Integer.parseInt(totalMinutes)) {
                        bookMyGameTextView.setAlpha(0.5f);
                    } else {
                        bookMyGameTextView.setAlpha(1);
                    }
                    if (myBookingsModelList.size() != 0) {
                        myBkngTextView.setVisibility(View.VISIBLE);

                    } else {
                        myBkngTextView.setVisibility(View.INVISIBLE);
                    }
                    if (myPendingBooking.size() != 0) {
                        myPendingBTextView.setVisibility(View.VISIBLE);

                    } else {
                        myPendingBTextView.setVisibility(View.INVISIBLE);
                    }

                    if (pendingBookimgsModelList.size() != 0) {
                        myInvitationsRelativieLy.setVisibility(View.VISIBLE);
                        myNotifCountTextView.setVisibility(View.VISIBLE);
                        myNotifCountTextView.setText(Integer.toString(pendingBookimgsModelList.size()));
                    } else {
                        myNotifCountTextView.setVisibility(View.GONE);
                    }
                    if (myBookingsRecycleViewAdapter == null) {
                        myBookingsRecycleViewAdapter = new MyBookingsRecycleViewAdapter(getActivity(), myBookingsModelArrayList, BookingsFragment.this, BookingsFragment.this, BookingsFragment.this, 1);
                        myBookingsRecycleView.setAdapter(myBookingsRecycleViewAdapter);
                    } else {
                        myBookingsRecycleViewAdapter.notifyDataSetChanged();
                    }


                    if (myPendingBookingsRecycleViewAdapter == null) {
                        myPendingBookingsRecycleViewAdapter = new MyBookingsRecycleViewAdapter(getActivity(), myPendingModelArrayList, BookingsFragment.this, BookingsFragment.this, BookingsFragment.this, 2);
                        myPendingRecycleView.setAdapter(myPendingBookingsRecycleViewAdapter);
                    } else {
                        myPendingBookingsRecycleViewAdapter.notifyDataSetChanged();
                    }


                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Internal Server Error,Please Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });
        setMyBookingAdapterList();
        setMyPendingBookingAdapterList();
    }

    private void setMyPendingBookingAdapterList() {
        if (myPendingBookingsRecycleViewAdapter == null) {
            myPendingBookingsRecycleViewAdapter = new MyBookingsRecycleViewAdapter(getActivity(), myPendingModelArrayList, BookingsFragment.this, BookingsFragment.this, BookingsFragment.this, 2);
            myPendingRecycleView.setAdapter(myPendingBookingsRecycleViewAdapter);
        } else {
            myPendingBookingsRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    private void setMyBookingAdapterList() {
        if (myBookingsRecycleViewAdapter == null) {
            myBookingsRecycleViewAdapter = new MyBookingsRecycleViewAdapter(getActivity(), myBookingsModelArrayList, BookingsFragment.this, BookingsFragment.this, BookingsFragment.this, 1);
            myBookingsRecycleView.setAdapter(myBookingsRecycleViewAdapter);
            appController.setCallingFromPendingNotif(false);
        } else {
            myBookingsRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    public static BookingsFragment newInstance() {
        BookingsFragment fragment = new BookingsFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bookMyGameTextView:
                if (Utils.isNetworkAvailable(getActivity())) {
                    if (myBookingsModelArrayList != null) {
                        if (Integer.parseInt(bookedGamesMinutes) >= Integer.parseInt(totalMinutes)) {
                            Toast.makeText(getActivity(), "You have completed bookings for this week.Thank You", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(getActivity(), BookMyGameActivity.class);
                            appController.setShowPlayersResultModelArrayList(null);
                            BookMyGameActivity.ToDay = ToDay;
                            startActivityForResult(i, 5);
                        }
                    } else {
                        Intent i = new Intent(getActivity(), BookMyGameActivity.class);
                        appController.setShowPlayersResultModelArrayList(null);
                        BookMyGameActivity.ToDay = ToDay;
                        startActivityForResult(i, 5);
                    }
                }
                break;
            case R.id.retryTextView:
                if (Utils.isNetworkAvailable(getActivity())) {
                    emptyElement.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    getTotalResponseData();
                } else {
                    emptyElement.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (message != null) {
                message = data.getStringExtra("BookingConfirmed");
                if (message.equalsIgnoreCase("PlayersAdded")) {
                    getTotalResponseData();
                } else if (message.equalsIgnoreCase("Cancel")) {
                    getTotalResponseData();
                }
            }
        }

    }

    @Override
    public void onClickItem(int position, int bookingPositionId, String call, String bookingId, String bookingStatus) {
        dialog.setMessage("Loading...");
        dialog.show();
        totalBookingViewModel.initWithDrawUser(userID, "withdraw", bookingPositionId, bookingStatus);
        updateDeletedUserDataData();


    }

    private void updateDeletedUserDataData() {
        totalBookingViewModel = ViewModelProviders.of(getActivity()).get(TotalBookingViewModel.class);
        totalBookingViewModel.getWithdrawResponseData().observe(getActivity(), new Observer<BookingStatusResonse>() {
            @Override
            public void onChanged(BookingStatusResonse bookingStatusResonse) {
                if (bookingStatusResonse != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    String message = bookingStatusResonse.getTotalResult().getMessage();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    if (Utils.isNetworkAvailable(getActivity())) {
                        emptyElement.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        getTotalResponseData();
                    } else {
                        emptyElement.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getTotalResponseData();

    }

    @Override
    public void removePlayerCallBack(int gameId, int playerId, int userId) {

        dialog.setMessage("Loading...");
        dialog.show();
        totalBookingViewModel.initRemoveUser(getJsonRemovePlayer(gameId, playerId, userId));
        //totalBookingViewModel.initRemoveUser(gameId, playerId, userId);
        updateRemovePlayerDetails();
    }

    private JsonObject getJsonRemovePlayer(int gameId, int playerId, int userId) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("BookingId", gameId);
            jsonObject.addProperty("RemovePersonId", playerId);
            jsonObject.addProperty("MyId", userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return jsonObject;
    }

    private void updateRemovePlayerDetails() {
        totalBookingViewModel = ViewModelProviders.of(getActivity()).get(TotalBookingViewModel.class);
        totalBookingViewModel.getRemovePlayerResponseData().observe(getActivity(), new Observer<RemovePlayerModel>() {
            @Override
            public void onChanged(RemovePlayerModel bookingStatusResonse) {
                if (bookingStatusResonse != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    String message = bookingStatusResonse.getTotalResult();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    if (Utils.isNetworkAvailable(getActivity())) {
                        emptyElement.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        getTotalResponseData();
                    } else {
                        emptyElement.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    @Override
    public void addMorePlayerClick(int position, int bookingIdPosition, String gameName) {
        DisplayToast(position);
        boolean selected = appController.isAddPlayerOrNot();
        if (selected) {
            Intent i = new Intent(getActivity(), AddPlayerFromBookingFragmentsActv.class);
            i.putExtra("clickedPosition", position);
            i.putExtra("bookingPosition", bookingIdPosition);
            i.putExtra("gameName", gameName);
            i.putExtra("selectedGameId", myBookingsModelArrayList.get(position).getBookingDTO().getResource());
            i.putExtra("selectedGameDayID", myBookingsModelArrayList.get(position).getBookingDTO().getDay());
            i.putExtra("selectedTiming", myBookingsModelArrayList.get(position).getBookingDTO().getSlot());
            i.putExtra("BookedDate", myBookingsModelArrayList.get(position).getBookingDTO().getBookedDate());
            i.putExtra("BookedDay", myBookingsModelArrayList.get(position).getBookingDTO().getBookedDay());
            i.putExtra("BookedDDMMM", myBookingsModelArrayList.get(position).getBookingDTO().getBookedDDMMM());
            i.putExtra("selectedGameName", myBookingsModelArrayList.get(position).getBookingDTO().getGame());
            i.putExtra("Player1", myBookingsModelArrayList.get(position).getBookingDTO().getPlayer1());
            i.putExtra("Player2", myBookingsModelArrayList.get(position).getBookingDTO().getPlayer2());
            i.putExtra("Player3", myBookingsModelArrayList.get(position).getBookingDTO().getPlayer3());
            i.putExtra("Player4", myBookingsModelArrayList.get(position).getBookingDTO().getPlayer4());
            playerDetailsModelArrayList = new ArrayList<SelectedPlayersResultModel>();
            playerDetailsModelArrayList.addAll(myBookingsModelArrayList.get(position).getPlayersDetails());
            appController.setFirstPlayersArrayList(playerDetailsModelArrayList);
            startActivityForResult(i, 5);
            deletePlayerFirst = false;
        } else {
            Toast.makeText(getActivity(), showToastMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void updatePlayersStatus(int position) {
        playerDetailsModelArrayListA = new ArrayList<SelectedPlayersResultModel>();
        playerDetailsModelArrayListA.addAll(appController.getPlayersStatusForAddButton().get(position).getPlayersDetails());
        for (int i = 0; i < playerDetailsModelArrayListA.size(); i++) {
            if (playerDetailsModelArrayListA.get(i).getStatus() == 2 || (playerDetailsModelArrayListA.get(i).getStatus() == 3)) {
                playerDetailsModelArrayListA.get(i).setAddButton(false);
                showToastMessage = "Please delete rejected/withdrawn player and add new players";
            } else if (playerDetailsModelArrayListA.get(i).getStatus() == 1) {
                if (playerDetailsModelArrayListA.size() == 4) {
                    playerDetailsModelArrayListA.get(i).setAddButton(false);
                    showToastMessage = "Your Game is Confirmed,you cannot add player now!";
                } else {
                    playerDetailsModelArrayListA.get(i).setAddButton(true);
                }
            } else if (playerDetailsModelArrayListA.get(i).getStatus() == 0) {
                playerDetailsModelArrayListA.get(i).setAddButton(true);
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private void DisplayToast(int position){
    playerDetailsModelArrayListA = new ArrayList<SelectedPlayersResultModel>();
        playerDetailsModelArrayListA.addAll(appController.getPlayersStatusForAddButton().get(position).getPlayersDetails());

        int rejectOrWithdranCount = 0;
        int confirmCount = 0;
        int pendingCount = 0;
       for (int i =0;i < playerDetailsModelArrayListA.size();i++)
       {
           if(playerDetailsModelArrayListA.get(i).getStatus() ==2 ||playerDetailsModelArrayListA.get(i).getStatus() ==3 ) {
               rejectOrWithdranCount++;
           }else if (playerDetailsModelArrayListA.get(i).getStatus()==1 ||playerDetailsModelArrayListA.get(i).getStatus()==4)
           {
               confirmCount++;
           }else if(playerDetailsModelArrayListA.get(i).getStatus()==0){
               if(playerDetailsModelArrayListA.size()==4){
                   showToastMessage = "Maximum 4 players can be added";
                   playerDetailsModelArrayListA.get(position).setAddButton(false);
                   pendingCount++;
               }
           }
       }
       if(rejectOrWithdranCount >0)
       {
           showToastMessage = "Please delete rejected/withdrawn player and add new players";
           //playerDetailsModelArrayListA.get(position).setAddButton(false);
           appController.setAddPlayerOrNot(false);
           // Do not Allow Action of Add button
       }else if(confirmCount<=2 && playerDetailsModelArrayListA.size()<4)
       {
           appController.setAddPlayerOrNot(true);
           //playerDetailsModelArrayListA.get(position).setAddButton(true);
           //unhide Add Button
       }
       if (confirmCount ==4) {
          // playerDetailsModelArrayListA.get(position).setAddButton(false);
           appController.setAddPlayerOrNot(false);
           showToastMessage = "All Players are confirmed,If any player is withdrawn then you can add another player.";
           //Hide Add button
       }else if(confirmCount==3&&playerDetailsModelArrayListA.size()==3){
           //playerDetailsModelArrayListA.get(position).setAddButton(true);
           appController.setAddPlayerOrNot(true);
       }
       if(myPendingBookingsRecycleViewAdapter!=null){
           myPendingBookingsRecycleViewAdapter.notifyDataSetChanged();
       }

    }
}
