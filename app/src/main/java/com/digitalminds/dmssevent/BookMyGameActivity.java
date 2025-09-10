package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.adapters.DaysAndDateRecycleViewAdapter;
import com.digitalminds.dmssevent.adapters.GamesRecycleViewAdapter;
import com.digitalminds.dmssevent.adapters.SelectedPlayersRecycleVAdapter;
import com.digitalminds.dmssevent.adapters.TimingsRecycleViewAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.GridSpacingItemDecoration;
import com.digitalminds.dmssevent.common.Util;
import com.digitalminds.dmssevent.databinding.ActivityBookMyGameBinding;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.interfaces.GameClickListener;
import com.digitalminds.dmssevent.models.BookMyGameModel;
import com.digitalminds.dmssevent.models.BookMyGameResultModel;
import com.digitalminds.dmssevent.models.BookingDaysModel;
import com.digitalminds.dmssevent.models.BookingGamesListModel;
import com.digitalminds.dmssevent.models.ResultModelConfirmation;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;
import com.digitalminds.dmssevent.models.TotalResultBookMyGameModel;
import com.digitalminds.dmssevent.viewmodel.BookMyGameViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class BookMyGameActivity extends AppCompatActivity implements View.OnClickListener, GameClickListener, BookingInterface {

    public static final String TAG = BookMyGameActivity.class.getSimpleName();
    ArrayList<BookingGamesListModel> gamesListModelArrayList;
    ArrayList<BookingDaysModel> daysModelArrayList;
    ArrayList<BookMyGameResultModel> timingsArraylist;
    RecyclerView gamesRecycleView, daysRecycleView, timingsRecycleView, timingsRecycleView1;
    GamesRecycleViewAdapter gamesRecycleViewAdapter;
    DaysAndDateRecycleViewAdapter daysAndDateRecycleViewAdapter;
    TimingsRecycleViewAdapter timingsRecycleViewAdapter, timingsRecycleViewAdapter1;
    ImageView userImageView, addUserImageView, dropDownImageView;
    TextView userNameTextView, addExtraUserTextView;
    Toolbar toolbar;
    TextView toolbar_title_fragments, sendTextView, cancelTextView;
    boolean flag = false;
    boolean isSelected = false;
    BookMyGameViewModel bookMyGameViewModel;
    private ProgressDialog dialog;
    BookMyGameModel bookMyGameModelList;
    ArrayList<TotalResultBookMyGameModel> bookMyGameModelArrayList = new ArrayList<TotalResultBookMyGameModel>();
    LinearLayout yourNameLinearLy, totalLinearLy;
    int selectedGameId = 1, selectedGameDayID;
    String selectedGameName = "", selectedTimings = "";
    String selectedTimingsSelectedDay = "";
    JsonArray jsonArray;
    ArrayList<String> list;
    boolean dataLoadingFlag = false;
    DmsEventsAppController appController;
    ArrayList<SelectedPlayersResultModel> showPlayersResultModelArrayList = new ArrayList<>();
    LinearLayout selectedPlayersLinearLy;
    RecyclerView selectedPlayersRecyleView;
    SelectedPlayersRecycleVAdapter selectedPlayersRecycleVAdapter;
    String message = "";
    int selectedTimeSlot;
    String userID = "";
    boolean firstTimeFlag = true;
    boolean firstTimeFlagToGetDates = true;
    boolean firstTime = false;
    String ids = "";
    LinearLayout timingsLinearLayout;
    RelativeLayout timingsLinearLayout1;
    String BookedDate = "", BookedDay = "", BookedDDMMM = "";
    public static int ToDay;
    LinearLayout ll_choose_partner;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBookMyGameBinding activityBookMyGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_my_game);
        bookMyGameViewModel = ViewModelProviders.of((FragmentActivity) this, (ViewModelProvider.Factory) new BookingViewModelFactory(BookMyGameActivity.this)).get(BookMyGameViewModel.class);
        activityBookMyGameBinding.setBookMyGameViewmodel(bookMyGameViewModel);
        intializeUIElements();
    }

    private void intializeUIElements() {
        appController = (DmsEventsAppController) getApplicationContext();
        userID = Integer.toString(DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getId());
        list = new ArrayList<String>();
        Gson gson = new GsonBuilder().create();
        jsonArray = gson.toJsonTree(list).getAsJsonArray();
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar_title_fragments = (TextView) toolbar.findViewById(R.id.toolbar_title_fragments);
        toolbar_title_fragments.setText("Book My Game");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        totalLinearLy = findViewById(R.id.totalLinearLy);
        totalLinearLy.setVisibility(View.GONE);
        gamesRecycleView = findViewById(R.id.gamesRecycleView);

        addExtraUserTextView = findViewById(R.id.addExtraUserTextView);
        selectedPlayersRecyleView = findViewById(R.id.selectedPlayersRecyleView);
        selectedPlayersLinearLy = findViewById(R.id.selectedPlayersLinearLy);
        daysRecycleView = findViewById(R.id.daysRecycleView);
        timingsRecycleView = findViewById(R.id.timingsRecycleView);
        timingsRecycleView1 = findViewById(R.id.timingsRecycleView1);
        userImageView = findViewById(R.id.userImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        addUserImageView = findViewById(R.id.addUserImageView);
        dropDownImageView = findViewById(R.id.dropDownImageView);
        yourNameLinearLy = findViewById(R.id.yourNameLinearLy);
        sendTextView = findViewById(R.id.sendTextView);
        cancelTextView = findViewById(R.id.cancelTextView);
        timingsLinearLayout = findViewById(R.id.timingsLinearLayout);
        timingsLinearLayout1 = findViewById(R.id.timingsLinearLayout1);
        ll_choose_partner = findViewById(R.id.ll_choose_partner);

        dialog = new ProgressDialog(BookMyGameActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        addUserImageView.setOnClickListener(this);
        dropDownImageView.setOnClickListener(this);
        addExtraUserTextView.setOnClickListener(this);
        sendTextView.setOnClickListener(this);
        cancelTextView.setOnClickListener(this);

        gamesListModelArrayList = new ArrayList<BookingGamesListModel>();
        daysModelArrayList = new ArrayList<BookingDaysModel>();
        timingsArraylist = new ArrayList<BookMyGameResultModel>();
        gamesRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        daysRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bookMyGameModelList = new BookMyGameModel();
        selectedGameDayID = ToDay;
        timingsLinearLayout1.setVisibility(View.GONE);
        if (Util.isNetworkAvailable(BookMyGameActivity.this)) {
            selectedGameDayID = ToDay;
            bookMyGameViewModel.init("getTimings", selectedGameId, selectedGameDayID);
            dialog.setMessage("Loading...");
            dialog.show();
            firstTimeFlagToGetDates = true;
            setTotalBookingTimings();
        }
        String url = DmsSharedPreferences.getProfilePicUrl(BookMyGameActivity.this);
        Glide.with(BookMyGameActivity.this).load(ConstantKeys.getImageUrl + url).apply(RequestOptions.circleCropTransform()).into(userImageView);
        userNameTextView.setText("You");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(this, R.dimen.grid_size);
        selectedPlayersRecyleView.addItemDecoration(itemDecoration);
        selectedPlayersRecyleView.setLayoutManager(gridLayoutManager);

    }

    private void setTotalBookingTimings() {
        bookMyGameViewModel.getTotalGamesDetailsLiveData().observe(BookMyGameActivity.this, bookMyGameModel -> {
            if (bookMyGameModel != null) {
                if (showPlayersResultModelArrayList != null) {
                    showPlayersResultModelArrayList.clear();
                    addSelfUserDetails();
                    appController.setShowPlayersResultModelArrayList(null);
                    if (selectedPlayersRecycleVAdapter != null) {
                        selectedPlayersRecycleVAdapter.notifyDataSetChanged();
                    }

                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    totalLinearLy.setVisibility(View.VISIBLE);
                }

                bookMyGameModelList = bookMyGameModel;
                if (dataLoadingFlag == false) {
                    timingsArraylist.clear();
                    daysModelArrayList.clear();
                    gamesListModelArrayList.clear();
                    TotalResultBookMyGameModel bookMyGameModelList = bookMyGameModel.getResult();
                    bookMyGameModelArrayList.add(bookMyGameModelList);
                    gamesListModelArrayList.addAll(bookMyGameModelList.getGamesList());
                    daysModelArrayList.addAll(bookMyGameModelList.getDays());
                    if (firstTimeFlagToGetDates) {
                        BookedDate = daysModelArrayList.get(selectedGameDayID).getDateString();
                        BookedDay = daysModelArrayList.get(selectedGameDayID).getDayName();
                        BookedDDMMM = daysModelArrayList.get(selectedGameDayID).getDdString() + "-" + daysModelArrayList.get(0).getMMString();
                        firstTimeFlagToGetDates = false;
                    }
                    timingsArraylist.addAll(bookMyGameModelList.getBookingList());
                    if (firstTimeFlag) {
                        selectedGameName = gamesListModelArrayList.get(0).getGameName();
                        firstTimeFlag = false;
                    }
                    if (gamesRecycleViewAdapter == null) {
                        gamesRecycleViewAdapter = new GamesRecycleViewAdapter(BookMyGameActivity.this, gamesListModelArrayList, BookMyGameActivity.this);
                        gamesRecycleView.setAdapter(gamesRecycleViewAdapter);
                    } else {
                        gamesRecycleViewAdapter.notifyDataSetChanged();
                    }

                    if (daysAndDateRecycleViewAdapter == null) {
                        daysAndDateRecycleViewAdapter = new DaysAndDateRecycleViewAdapter(BookMyGameActivity.this, daysModelArrayList, BookMyGameActivity.this, ToDay);
                        daysRecycleView.setAdapter(daysAndDateRecycleViewAdapter);
                    } else {
                        daysAndDateRecycleViewAdapter.notifyDataSetChanged();
                    }

                    if (timingsRecycleViewAdapter == null) {
                        timingsRecycleViewAdapter = new TimingsRecycleViewAdapter(BookMyGameActivity.this, timingsArraylist, BookMyGameActivity.this);
                        timingsRecycleView.setAdapter(timingsRecycleViewAdapter);
                    } else {
                        timingsRecycleViewAdapter.notifyDataSetChanged();
                    }

                    if (timingsRecycleViewAdapter1 == null) {
                        timingsRecycleViewAdapter1 = new TimingsRecycleViewAdapter(BookMyGameActivity.this, timingsArraylist, BookMyGameActivity.this);
                        timingsRecycleView1.setAdapter(timingsRecycleViewAdapter1);
                    } else {
                        timingsRecycleViewAdapter1.notifyDataSetChanged();
                    }

                    setGamesListAdapterList();
                    setTimingsBookingAdapterList();
                    setDateBookingAdapterList();
                } else {
                    timingsArraylist.clear();
                    TotalResultBookMyGameModel bookMyGameModelList = bookMyGameModel.getResult();
                    timingsArraylist.addAll(bookMyGameModelList.getBookingList());
                    if (timingsRecycleViewAdapter == null) {
                        timingsRecycleViewAdapter = new TimingsRecycleViewAdapter(BookMyGameActivity.this, timingsArraylist, BookMyGameActivity.this);
                        timingsRecycleView.setAdapter(timingsRecycleViewAdapter);
                    } else {
                        timingsRecycleViewAdapter.notifyDataSetChanged();
                    }

                    //to hide and show data
                    if (timingsRecycleViewAdapter1 == null) {
                        timingsRecycleViewAdapter1 = new TimingsRecycleViewAdapter(BookMyGameActivity.this, timingsArraylist, BookMyGameActivity.this);
                        timingsRecycleView1.setAdapter(timingsRecycleViewAdapter1);
                    } else {
                        timingsRecycleViewAdapter1.notifyDataSetChanged();
                    }
                    //setTimingsBookingAdapterList();
                }
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(BookMyGameActivity.this, "Internal Server Error,Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setGamesListAdapterList() {

        if (gamesRecycleViewAdapter == null) {
            gamesRecycleViewAdapter = new GamesRecycleViewAdapter(this, gamesListModelArrayList, this);
            gamesRecycleView.setAdapter(gamesRecycleViewAdapter);
        } else {
            gamesRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    private void setDateBookingAdapterList() {

        if (daysAndDateRecycleViewAdapter == null) {
            daysAndDateRecycleViewAdapter = new DaysAndDateRecycleViewAdapter(BookMyGameActivity.this, daysModelArrayList, BookMyGameActivity.this, ToDay);
            daysRecycleView.setAdapter(daysAndDateRecycleViewAdapter);
        } else {
            daysAndDateRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    private void setTimingsBookingAdapterList() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(this, R.dimen.grid_size1);
        timingsRecycleView.addItemDecoration(itemDecoration);
        timingsRecycleView.setLayoutManager(gridLayoutManager);
        if (timingsRecycleViewAdapter == null) {
            timingsRecycleViewAdapter = new TimingsRecycleViewAdapter(BookMyGameActivity.this, timingsArraylist, BookMyGameActivity.this);
            timingsRecycleView.setAdapter(timingsRecycleViewAdapter);
        } else {
            timingsRecycleViewAdapter.notifyDataSetChanged();
        }


        //for hide and show
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 4);
        GridSpacingItemDecoration itemDecoration1 = new GridSpacingItemDecoration(this, R.dimen.grid_size1);
        timingsRecycleView1.addItemDecoration(itemDecoration1);
        timingsRecycleView1.setLayoutManager(gridLayoutManager1);
        if (timingsRecycleViewAdapter1 == null) {
            timingsRecycleViewAdapter1 = new TimingsRecycleViewAdapter(BookMyGameActivity.this, timingsArraylist, BookMyGameActivity.this);

            timingsRecycleView1.setAdapter(timingsRecycleViewAdapter1);
        } else {
            timingsRecycleViewAdapter1.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addUserImageView:
                if (Util.isNetworkAvailable(BookMyGameActivity.this)) {
                    SelectPlayerActivity.GameName = selectedGameName;
                    SelectPlayerActivity.jsonElements = "";
                    appController.setShowPlayersResultModelArrayList(null);
                    Intent intent = new Intent(BookMyGameActivity.this, SelectPlayerActivity.class);
                    startActivityForResult(intent, 2);
                }
                break;
            case R.id.dropDownImageView:
                if (!flag) {
                   /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            0);
                    params.weight=2.2f;
                    //params.height= ViewGroup.LayoutParams.MATCH_PARENT;
                    timingsLinearLayout.setLayoutParams(params);*/


                    timingsLinearLayout.setVisibility(View.GONE);
                    timingsLinearLayout1.setVisibility(View.VISIBLE);
                    dropDownImageView.setImageDrawable(getResources().getDrawable(R.drawable.up_icon));
                    flag = true;
                } else if (flag) {
                    /*LinearLayout.LayoutParams params = new
                            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    // Set the height by params
                    params.height = 350;
                    timingsLinearLayout.setLayoutParams(params);*/
                    timingsLinearLayout1.setVisibility(View.GONE);
                    timingsLinearLayout.setVisibility(View.VISIBLE);
                    dropDownImageView.setImageDrawable(getResources().getDrawable(R.drawable.downn_arrow));
                    flag = false;
                }

                break;
            case R.id.addExtraUserTextView:
                if (Util.isNetworkAvailable(BookMyGameActivity.this)) {
                    SelectPlayerActivity.GameName = selectedGameName;
                  /*  if(showPlayersResultModelArrayList!=null){
                        showPlayersResultModelArrayList.remove(0);
                    }*/
                    for (int i = 0; i < showPlayersResultModelArrayList.size(); i++) {
                        if (firstTime) {
                            ids = ids + "," + String.valueOf(showPlayersResultModelArrayList.get(i).getId());
                        } else {
                            firstTime = true;
                            ids = String.valueOf(showPlayersResultModelArrayList.get(i).getId());
                        }
                    }
                    SelectPlayerActivity.jsonElements = ids;
                    SelectPlayerActivity.callActivity = 2;
                    if (showPlayersResultModelArrayList != null) {
                        if (showPlayersResultModelArrayList.get(0).getId() == DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getId()) {
                            showPlayersResultModelArrayList.remove(0);
                        }
                    }
                    appController.setShowPlayersResultModelArrayList(showPlayersResultModelArrayList);
                    Intent intent = new Intent(BookMyGameActivity.this, SelectPlayerActivity.class);
                    startActivityForResult(intent, 2);
                }

                break;
            case R.id.sendTextView:
                if (selectedTimings != null && selectedTimings.length() > 0) {
                    if (showPlayersResultModelArrayList != null) {
                        if (showPlayersResultModelArrayList.size() == 2 || showPlayersResultModelArrayList.size() == 4) {
                            bookMyGameViewModel.initBooking(userID, "callBookMyGameService", getJsonDataForBooking());
                            dialog.setMessage("Loading...");
                            dialog.show();
                            getBookingResultStatus();
                        } else {
                            Toast.makeText(BookMyGameActivity.this, "Please select players", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BookMyGameActivity.this, "Please select players", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BookMyGameActivity.this, "Please select slot timings", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cancelTextView:

                Intent intent = new Intent();
                intent.putExtra("BookingConfirmed", "Cancel");
                setResult(5, intent);
                finish();
                break;

            /*case R.id.mySwipeRefreshLayout:
                mySwipeRefreshLayout.setRefreshing(true);
                refreshData();
                break;*/
        }
    }

    public Boolean compareTwoDates(String date) {

       /* @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(valid_until);
        if (new Date().after(strDate)) {
            catalog_outdated = 1;
        }*/


        return true;
    }

    private void getBookingResultStatus() {
        bookMyGameViewModel.getFinalBookingResponseData().observe(BookMyGameActivity.this, bookingStatusResonse -> {
            if (bookingStatusResonse != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                boolean status = bookingStatusResonse.getStatus();
                ResultModelConfirmation result = bookingStatusResonse.getTotalResult();

                if (status == true && result.getGameBookingStatus().equalsIgnoreCase("true")) {
                    Intent intent = new Intent();
                    intent.putExtra("BookingConfirmed", "PlayersAdded");
                    setResult(5, intent);
                    Toast.makeText(BookMyGameActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else if (status == true && result.getGameBookingStatus().equalsIgnoreCase("false")) {
                    Toast.makeText(BookMyGameActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(BookMyGameActivity.this, "Internal Server Error,Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JsonObject getJsonDataForBooking() {
        int selectedGameValue = selectedGameId;
        int selectedDayIdValue = selectedGameDayID;
        int selectedTiming = selectedTimeSlot;
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("Resource", selectedGameValue);
            jsonObject.addProperty("Day", selectedDayIdValue);
            jsonObject.addProperty("Slot", selectedTiming);
            jsonObject.addProperty("BookedDate", BookedDate);
            jsonObject.addProperty("BookedDay", BookedDay);
            jsonObject.addProperty("BookedDDMMM", BookedDDMMM);
            if (showPlayersResultModelArrayList.size() == 2) {
                jsonObject.addProperty("Player1", showPlayersResultModelArrayList.get(0).getId());
                jsonObject.addProperty("Player2", showPlayersResultModelArrayList.get(1).getId());
            } else if (showPlayersResultModelArrayList.size() == 4) {
                jsonObject.addProperty("Player1", showPlayersResultModelArrayList.get(0).getId());
                jsonObject.addProperty("Player2", showPlayersResultModelArrayList.get(1).getId());
                jsonObject.addProperty("Player3", showPlayersResultModelArrayList.get(2).getId());
                jsonObject.addProperty("Player4", showPlayersResultModelArrayList.get(3).getId());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
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
    public void onClick(int position, String clickedFrom) {
        if (clickedFrom.equalsIgnoreCase("Games")) {
            dataLoadingFlag = true;
            if (bookMyGameModelList != null && bookMyGameViewModel.getTotalGamesDetailsLiveData().hasObservers()) {
                bookMyGameViewModel.getTotalGamesDetailsLiveData().removeObservers(this);
            }
            selectedGameName = gamesListModelArrayList.get(position).getGameName();
            selectedGameId = gamesListModelArrayList.get(position).getID();
            bookMyGameViewModel.init("getTimings", selectedGameId, selectedGameDayID);
            dialog.setMessage("Loading...");
            dialog.show();
            setTotalBookingTimings();

        } else if (clickedFrom.equalsIgnoreCase("Day")) {
            //Removing previous selected timings

            //Default selected position
           // selectedGameName = gamesListModelArrayList.get(0).getGameName();


            if (ToDay == selectedGameDayID) {
                selectedTimings = selectedTimingsSelectedDay;
            } else {
                selectedTimings = "";
            }
            dataLoadingFlag = true;
            if (showPlayersResultModelArrayList != null) {
                showPlayersResultModelArrayList.clear();
            }
            if (bookMyGameModelList != null && bookMyGameViewModel.getTotalGamesDetailsLiveData().hasObservers()) {
                bookMyGameViewModel.getTotalGamesDetailsLiveData().removeObservers(this);
            }

            selectedGameDayID = daysModelArrayList.get(position).getDayNo();
            BookedDate = daysModelArrayList.get(position).getDateString();
            BookedDay = daysModelArrayList.get(position).getDayName();
            BookedDDMMM = daysModelArrayList.get(position).getDdString() + "-" + daysModelArrayList.get(position).getMMString();
            bookMyGameViewModel.init("getTimings", selectedGameId, selectedGameDayID);
            dialog.setMessage("Loading...");
            dialog.show();

            //HERE SHOWING CHOOSE PARTNER
            if (ToDay <= selectedGameDayID) {
                ll_choose_partner.setVisibility(View.VISIBLE);
            } else {
                ll_choose_partner.setVisibility(View.GONE);
            }


            setTotalBookingTimings();
        } else if (clickedFrom.equalsIgnoreCase("Timings")) {
            String bookingSlot = timingsArraylist.get(position).getBookedSlot();
            String arg[] = bookingSlot.split("-");
            String time = arg[0];
            selectedTimings = time;
            selectedTimingsSelectedDay = time;
            selectedTimeSlot = timingsArraylist.get(position).getBookingDTO().getSlot();
            /*if(firstTimeFlagToGetDates){
                BookedDate = daysModelArrayList.get(0).getDateString();
                BookedDay = daysModelArrayList.get(0).getDayName();
                BookedDDMMM = daysModelArrayList.get(0).getDdString()+"-"+daysModelArrayList.get(0).getMMString();
                firstTimeFlagToGetDates=false;
            }*/

        } else if (clickedFrom.equalsIgnoreCase("deleteUser")) {
            // Adding Logged User 1st index position
            SelectedPlayersResultModel selectedPlayersResultModel = new SelectedPlayersResultModel();
            String departName = DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getDepartment();
            String displayName = DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getDisplayName();
            String imageUrl = DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getProfileImageURL();
            selectedPlayersResultModel.setDeptName(departName);
            selectedPlayersResultModel.setDisplayName(displayName);
            selectedPlayersResultModel.setProfilePhoto(imageUrl);
            selectedPlayersResultModel.setId(DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getId());
            if (showPlayersResultModelArrayList == null) {
                showPlayersResultModelArrayList = new ArrayList<>();
            }
            if (showPlayersResultModelArrayList.size() <= 1) {
                showPlayersResultModelArrayList.add(0, selectedPlayersResultModel);
            }

            if (position > 0) {
                showPlayersResultModelArrayList.remove(position);
            }

            if (selectedPlayersRecycleVAdapter != null) {
                selectedPlayersRecycleVAdapter.notifyDataSetChanged();
            }
            appController.setShowPlayersResultModelArrayList(showPlayersResultModelArrayList);
            if (showPlayersResultModelArrayList.size() == 2) {
                addExtraUserTextView.setVisibility(View.VISIBLE);
                yourNameLinearLy.setVisibility(View.GONE);
                selectedPlayersLinearLy.setVisibility(View.VISIBLE);
                addExtraUserTextView.setText("Optional: Add 2 More Players");
            } else if (showPlayersResultModelArrayList.size() == 1) {
                yourNameLinearLy.setVisibility(View.VISIBLE);
                selectedPlayersLinearLy.setVisibility(View.GONE);
                addExtraUserTextView.setVisibility(View.GONE);
            } else if (showPlayersResultModelArrayList.size() == 3) {
                addExtraUserTextView.setVisibility(View.VISIBLE);
                addExtraUserTextView.setText("Add 1 More Player");
                yourNameLinearLy.setVisibility(View.GONE);
                selectedPlayersLinearLy.setVisibility(View.VISIBLE);
            }


        }

    }

    @Override
    public void onClickItem(int position, int bookingPositionId, String call, String bookingId, String bookingStatus) {
        int selectedGameValue = selectedGameId;
        int selectedDayIdValue = selectedGameDayID;
        String selectedTimeIdValue = selectedTimings;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (message != null) {
                message = data.getStringExtra("MESSAGE");
                if (message.equalsIgnoreCase("PlayersAdded")) {
                    showPlayersResultModelArrayList = appController.getShowPlayersResultModelArrayList();
                    setSelectedPlayersAdapter();
                    // dropDownCall();

                } else if (message.equalsIgnoreCase("Cancel")) {

                    showPlayersResultModelArrayList = appController.getShowPlayersResultModelArrayList();
                    setSelectedPlayersAdapter();

                }
            }
        }
    }

    private void dropDownCall() {
        if (!flag) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    0);
            params.weight = 2.2f;
            //params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            timingsLinearLayout.setLayoutParams(params);
            dropDownImageView.setImageDrawable(getResources().getDrawable(R.drawable.up_icon));
            flag = true;
        } else if (flag) {
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // Set the height by params
            params.height = 350;
            timingsLinearLayout.setLayoutParams(params);
            dropDownImageView.setImageDrawable(getResources().getDrawable(R.drawable.downn_arrow));
            flag = false;
        }
    }


    private void addSelfUserDetails() {
        addExtraUserTextView.setVisibility(View.GONE);
        yourNameLinearLy.setVisibility(View.VISIBLE);
    }

    private void setSelectedPlayersAdapter() {
        /*if (selectedPlayersRecycleVAdapter == null) {*/
        SelectedPlayersResultModel selectedPlayersResultModel = new SelectedPlayersResultModel();
        String departName = DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getDepartment();
        String displayName = DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getDisplayName();
        String imageUrl = DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getProfileImageURL();
        selectedPlayersResultModel.setDeptName(departName);
        selectedPlayersResultModel.setDisplayName(displayName);
        selectedPlayersResultModel.setProfilePhoto(imageUrl);
        selectedPlayersResultModel.setId(DmsSharedPreferences.getUserDetails(BookMyGameActivity.this).getId());
        if (showPlayersResultModelArrayList == null)
            showPlayersResultModelArrayList = new ArrayList<>();


        showPlayersResultModelArrayList.add(0, selectedPlayersResultModel);
        if (showPlayersResultModelArrayList.size() == 2) {
            addExtraUserTextView.setVisibility(View.VISIBLE);
            yourNameLinearLy.setVisibility(View.GONE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
            addExtraUserTextView.setText("Add 2 More Players");
        } else if (showPlayersResultModelArrayList.size() == 4) {
            addExtraUserTextView.setVisibility(View.GONE);
            yourNameLinearLy.setVisibility(View.GONE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
        }
        selectedPlayersRecycleVAdapter = null;
        selectedPlayersRecycleVAdapter = new SelectedPlayersRecycleVAdapter(BookMyGameActivity.this, showPlayersResultModelArrayList, BookMyGameActivity.this);
        selectedPlayersRecyleView.setAdapter(selectedPlayersRecycleVAdapter);
        /*} else {
            selectedPlayersRecycleVAdapter.notifyDataSetChanged();
        }*/
    }


    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("BookingConfirmed", "Cancel");
        setResult(5, intent);
        finish();
    }
}
