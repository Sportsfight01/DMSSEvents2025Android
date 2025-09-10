package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import com.digitalminds.dmssevent.adapters.SelectedPlayersAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.GridSpacingItemDecoration;
import com.digitalminds.dmssevent.common.Util;
//import com.dmss.dmssevent.databinding.SelectedPlayersActivityBindingImpl;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.interfaces.GameClickListener;
import com.digitalminds.dmssevent.models.BookMyGameModel;
import com.digitalminds.dmssevent.models.ConfirmBookingResponse;
import com.digitalminds.dmssevent.models.ResultModelConfirmation;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;
import com.digitalminds.dmssevent.viewmodel.AddExtraPlayerActivity;
import com.digitalminds.dmssevent.viewmodel.BookMyGameViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class AddPlayerFromBookingFragmentsActv extends AppCompatActivity implements View.OnClickListener, GameClickListener, BookingInterface {
    ImageView userImageView;
    TextView userNameTextView, addExtraUserTextView;
    Toolbar toolbar;
    TextView toolbar_title_fragments, sendTextView, cancelTextView;
    BookMyGameViewModel bookMyGameViewModel;
    private ProgressDialog dialog;
    BookMyGameModel bookMyGameModelList;
    LinearLayout totalLinearLy;
    int selectedGameId, selectedGameDayID;
    String selectedGameName = "";
    JsonArray jsonArray;
    ArrayList<String> list;
    DmsEventsAppController appController;
    ArrayList<SelectedPlayersResultModel> firstPlayersResultModelArrayList;
    ArrayList<SelectedPlayersResultModel> secondResultModelArrayList;
    LinearLayout selectedPlayersLinearLy, secondSelectedPlayersLinearLy;
    RecyclerView playersListRecycleView, secondPlayersListRecycleView1;
    SelectedPlayersAdapter selectedPlayersRecycleVAdapter;
    SelectedPlayersAdapter secondPlayersRecycleVAdapter;
    String message = "";
    int selectedTimeSlot;
    String userID = "";
    boolean firstTime = false;
    String ids = "";
    LinearLayout timingsLinearLayout;
    String BookedDate = "", BookedDay = "", BookedDDMMM = "";
    public static int ToDay;
    String reverseDate;
    Integer player1, player2, player3, player4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // SelectedPlayersActivityBindingImpl selectedPlayersActivityBinding=DataBindingUtil.setContentView(this, R.layout.selected_players_activity);
        //ActivityBookMyGameBinding activityBookMyGameBinding = DataBindingUtil.setContentView(this, R.layout.selected_players_activity);
        //bookMyGameViewModel = ViewModelProviders.of(this, new BookingViewModelFactory(AddPlayerFromBookingFragmentsActv.this)).get(BookMyGameViewModel.class);
        // selectedPlayersActivityBinding.setBookMyGameViewmodel(bookMyGameViewModel);
        setContentView(R.layout.selected_players_activity);
        bookMyGameViewModel = ViewModelProviders.of(this).get(BookMyGameViewModel.class);
        intializeUIElements();
    }

    private void intializeUIElements() {
        appController = (DmsEventsAppController) getApplicationContext();
        userID = Integer.toString(DmsSharedPreferences.getUserDetails(AddPlayerFromBookingFragmentsActv.this).getId());
        list = new ArrayList<String>();
        Gson gson = new GsonBuilder().create();
        jsonArray = gson.toJsonTree(list).getAsJsonArray();
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar_title_fragments = (TextView) toolbar.findViewById(R.id.toolbar_title_fragments);
        toolbar_title_fragments.setText("Add Players");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        totalLinearLy = findViewById(R.id.totalLinearLy);
        addExtraUserTextView = findViewById(R.id.addExtraUserTextView);
        playersListRecycleView = findViewById(R.id.playersListRecycleView1);
        secondPlayersListRecycleView1 = findViewById(R.id.secondPlayersListRecycleView1);
        selectedPlayersLinearLy = findViewById(R.id.selectedPlayersLinearLy);
        secondSelectedPlayersLinearLy = findViewById(R.id.secondSelectedPlayersLinearLy);
        //daysRecycleView = findViewById(R.id.daysRecycleView);
        // timingsRecycleView = findViewById(R.id.timingsRecycleView);
        userImageView = findViewById(R.id.userImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        sendTextView = findViewById(R.id.sendTextView);
        cancelTextView = findViewById(R.id.cancelTextView);
        timingsLinearLayout = findViewById(R.id.timingsLinearLayout);
        dialog = new ProgressDialog(AddPlayerFromBookingFragmentsActv.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        addExtraUserTextView.setOnClickListener(this);
        sendTextView.setOnClickListener(this);
        cancelTextView.setOnClickListener(this);
        secondResultModelArrayList = new ArrayList<SelectedPlayersResultModel>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(this, R.dimen.grid_size);
        playersListRecycleView.addItemDecoration(itemDecoration);
        playersListRecycleView.setLayoutManager(gridLayoutManager);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 1);
        GridSpacingItemDecoration itemDecoration1 = new GridSpacingItemDecoration(this, R.dimen.grid_size);
        secondPlayersListRecycleView1.addItemDecoration(itemDecoration1);
        secondPlayersListRecycleView1.setLayoutManager(gridLayoutManager1);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Integer user_id = extras.getInt("selectedGameId");
        Integer gameId = extras.getInt("selectedGameDayID");
        Integer slotTime = extras.getInt("selectedTiming");
        String booked_date = extras.getString("BookedDate");

        try{
            StringTokenizer tokens = new StringTokenizer(booked_date, "T");
            String first = tokens.nextToken();// this will contain "Fruit"
            String second = tokens.nextToken();

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            try {
                reverseDate = sdf2.format(sdf1.parse(first));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }catch (Exception ex){

        }


        String booked_day = extras.getString("BookedDay");
        String booked_ddmmyy = extras.getString("BookedDDMMM");
        selectedGameId = user_id;
        selectedGameDayID = gameId;
        selectedTimeSlot = slotTime;
        BookedDate = reverseDate;
        BookedDay = booked_day;
        BookedDDMMM = booked_ddmmyy;
        player1 = extras.getInt("Player1");
        player2 = extras.getInt("Player2");
        player3 = extras.getInt("Player3");
        player4 = extras.getInt("Player4");


        selectedGameName = extras.getString("selectedGameName");
        firstPlayersResultModelArrayList = new ArrayList<SelectedPlayersResultModel>();
        if (appController.getFirstPlayersArrayList() != null) {
            firstPlayersResultModelArrayList.addAll(appController.getFirstPlayersArrayList());
            setSecondPlayersAdapter();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addUserImageView:
                /*if (Util.isNetworkAvailable(AddPlayerFromBookingFragmentsActv.this)) {
                    SelectPlayerActivity.GameName = selectedGameName;
                    SelectPlayerActivity.jsonElements = "";
                    appController.setShowPlayersFromAddPlayerActArrayList(null);
                    Intent intent = new Intent(AddPlayerFromBookingFragmentsActv.this, SelectPlayerActivity.class);
                    startActivityForResult(intent, 2);
                }*/
                break;
            case R.id.addExtraUserTextView:
                if (Util.isNetworkAvailable(AddPlayerFromBookingFragmentsActv.this)) {
                    AddExtraPlayerActivity.GameName = selectedGameName;
                    for (int i = 0; i < firstPlayersResultModelArrayList.size(); i++) {
                        if (firstTime) {
                            ids = ids + "," + String.valueOf(firstPlayersResultModelArrayList.get(i).getId());
                        } else {
                            firstTime = true;
                            ids = String.valueOf(firstPlayersResultModelArrayList.get(i).getId());
                        }
                    }
                    AddExtraPlayerActivity.jsonElements = ids;
                    AddExtraPlayerActivity.callActivity = 2;
                    Intent intent = new Intent(AddPlayerFromBookingFragmentsActv.this, AddExtraPlayerActivity.class);
                    startActivityForResult(intent, 2);
                }

                break;
            case R.id.sendTextView:
                if (firstPlayersResultModelArrayList != null) {
                    if (firstPlayersResultModelArrayList.size() == 2 && secondResultModelArrayList.size() == 2) {
                        bookMyGameViewModel = ViewModelProviders.of(this).get(BookMyGameViewModel.class);
                        bookMyGameViewModel.initBooking(userID, "callBookMyGameService", getJsonDataForBooking());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        getBookingResultStatus();

                    } else if (firstPlayersResultModelArrayList.size() == 2 && secondResultModelArrayList.size() == 1) {
                        Toast.makeText(AddPlayerFromBookingFragmentsActv.this, "Please Add 1 more player", Toast.LENGTH_SHORT).show();
                    } else if (firstPlayersResultModelArrayList.size() == 1 && secondResultModelArrayList.size() == 2) {
                        Toast.makeText(AddPlayerFromBookingFragmentsActv.this, "Please Add 1 more player", Toast.LENGTH_SHORT).show();
                    } else if (firstPlayersResultModelArrayList.size() == 1 && secondResultModelArrayList.size() == 3) {
                        bookMyGameViewModel = ViewModelProviders.of(this).get(BookMyGameViewModel.class);
                        bookMyGameViewModel.initBooking(userID, "callBookMyGameService", getJsonDataForBooking());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        getBookingResultStatus();
                    } else if (firstPlayersResultModelArrayList.size() == 1 && secondResultModelArrayList.size() == 1) {
                        bookMyGameViewModel = ViewModelProviders.of(this).get(BookMyGameViewModel.class);
                        bookMyGameViewModel.initBooking(userID, "callBookMyGameService", getJsonDataForBooking());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        getBookingResultStatus();
                    } else if (firstPlayersResultModelArrayList.size() == 3 && secondResultModelArrayList.size() == 1) {
                        bookMyGameViewModel = ViewModelProviders.of(this).get(BookMyGameViewModel.class);
                        bookMyGameViewModel.initBooking(userID, "callBookMyGameService", getJsonDataForBooking());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        getBookingResultStatus();
                    }else if(firstPlayersResultModelArrayList.size() == 3 && secondResultModelArrayList.size() == 0){
                        Toast.makeText(AddPlayerFromBookingFragmentsActv.this, "Please Add 1 more player", Toast.LENGTH_SHORT).show();
                    }else if(firstPlayersResultModelArrayList.size() == 2 && secondResultModelArrayList.size() == 0){
                        Toast.makeText(AddPlayerFromBookingFragmentsActv.this, "Please Add Players or Press Cancel Button", Toast.LENGTH_SHORT).show();
                    }else if(firstPlayersResultModelArrayList.size() == 1 && secondResultModelArrayList.size() == 0){
                        Toast.makeText(AddPlayerFromBookingFragmentsActv.this, "Please Add player", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddPlayerFromBookingFragmentsActv.this, "Please select players", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cancelTextView:
                Intent intent = new Intent();
                intent.putExtra("BookingConfirmed", "Cancel");
                setResult(5, intent);
                finish();
                break;
        }
    }

    private void getBookingResultStatus() {
        bookMyGameViewModel.getFinalBookingResponseData().observe(AddPlayerFromBookingFragmentsActv.this, new Observer<ConfirmBookingResponse>() {
            @Override
            public void onChanged(ConfirmBookingResponse bookingStatusResonse) {
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
                        Toast.makeText(AddPlayerFromBookingFragmentsActv.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (status == true && result.getGameBookingStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(AddPlayerFromBookingFragmentsActv.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(AddPlayerFromBookingFragmentsActv.this, "Internal Server Error,Please Try Again", Toast.LENGTH_SHORT).show();
                }
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
            //jsonObject.addProperty("BookedDate", BookedDate);
           // jsonObject.addProperty("BookedDay", BookedDay);
           // jsonObject.addProperty("BookedDDMMM", BookedDDMMM);
            if (firstPlayersResultModelArrayList.size() == 2 && secondResultModelArrayList.size() == 2) {
                if (player1 == 0 && player2 == 0) {
                    jsonObject.addProperty("Player1", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player2", secondResultModelArrayList.get(1).getId());

                } else if (player2 == 0 && player3 == 0) {
                    jsonObject.addProperty("Player2", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player3", secondResultModelArrayList.get(1).getId());
                } else if (player3 == 0 && player1 == 0) {
                    jsonObject.addProperty("Player3", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player1", secondResultModelArrayList.get(1).getId());
                } else if (player1 == 0 && player4 == 0) {
                    jsonObject.addProperty("Player1", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player4", secondResultModelArrayList.get(1).getId());
                }else if(player3 == 0 && player4 == 0){
                    jsonObject.addProperty("Player3", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player4", secondResultModelArrayList.get(1).getId());
                }
                // jsonObject.addProperty("Player1", firstPlayersResultModelArrayList.get(0).getId());
                //  jsonObject.addProperty("Player2", firstPlayersResultModelArrayList.get(1).getId());
            } else if (firstPlayersResultModelArrayList.size() == 1 && secondResultModelArrayList.size() == 1) {
                if (player1 == 0) {
                    jsonObject.addProperty("Player1", secondResultModelArrayList.get(0).getId());

                } else if (player2 == 0) {
                    jsonObject.addProperty("Player2", secondResultModelArrayList.get(0).getId());
                } else if (player3 == 0) {
                    jsonObject.addProperty("Player3", secondResultModelArrayList.get(0).getId());
                } else if (player4 == 0) {
                    jsonObject.addProperty("Player4", secondResultModelArrayList.get(0).getId());
                }
            } else if (firstPlayersResultModelArrayList.size() == 3 && secondResultModelArrayList.size() == 1) {
                if (player1 == 0) {
                    jsonObject.addProperty("Player1", secondResultModelArrayList.get(0).getId());
                } else if (player2 == 0) {
                    jsonObject.addProperty("Player2", secondResultModelArrayList.get(0).getId());
                } else if (player3 == 0) {
                    jsonObject.addProperty("Player3", secondResultModelArrayList.get(0).getId());
                } else if (player4 == 0) {
                    jsonObject.addProperty("Player4", secondResultModelArrayList.get(0).getId());
                }

            } else if (firstPlayersResultModelArrayList.size() == 1 && secondResultModelArrayList.size() == 3) {
                if (player1 == 0 && player2 == 0 && player3 == 0) {
                    jsonObject.addProperty("Player1", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player2", secondResultModelArrayList.get(1).getId());
                    jsonObject.addProperty("Player3", secondResultModelArrayList.get(2).getId());
                } else if (player2 == 0 && player3 == 0 && player4 == 0) {
                    jsonObject.addProperty("Player2", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player3", secondResultModelArrayList.get(1).getId());
                    jsonObject.addProperty("Player4", secondResultModelArrayList.get(2).getId());
                } else if (player3 == 0 && player4 == 0 && player1 == 0) {
                    jsonObject.addProperty("Player3", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player4", secondResultModelArrayList.get(1).getId());
                    jsonObject.addProperty("Player1", secondResultModelArrayList.get(2).getId());
                } else if (player1 == 0 && player2 == 0 && player4 == 0) {
                    jsonObject.addProperty("Player1", secondResultModelArrayList.get(0).getId());
                    jsonObject.addProperty("Player2", secondResultModelArrayList.get(1).getId());
                    jsonObject.addProperty("Player4", secondResultModelArrayList.get(2).getId());
                }

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
                Intent intent = new Intent();
                intent.putExtra("BookingConfirmed", "Cancel");
                setResult(5, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position, String clickedFrom) {
        if (clickedFrom.equalsIgnoreCase("deleteUser")) {
            secondResultModelArrayList.remove(position);
            if (secondPlayersRecycleVAdapter != null) {
                secondPlayersRecycleVAdapter.notifyDataSetChanged();
            }
            appController.setSecondPlayersArrayList(firstPlayersResultModelArrayList);
            setSecondPlayersAdapter();
        }

    }

    @Override
    public void onClickItem(int position, int bookingPositionId, String call, String bookingId, String bookingStatus) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (message != null) {
                message = data.getStringExtra("MESSAGE");
                if (message.equalsIgnoreCase("PlayersAdded")) {

                    secondResultModelArrayList.addAll(appController.getSecondPlayersArrayList());
                    //firstPlayersResultModelArrayList.addAll(secondResultModelArrayList);
                    setSecondPlayersAdapter();

                } else if (message.equalsIgnoreCase("Cancel")) {

                    firstPlayersResultModelArrayList = appController.getSecondPlayersArrayList();
                    setSecondPlayersAdapter();
                }
            }
        }
    }

    private void setSecondPlayersAdapter() {
        if (secondResultModelArrayList.size() == 0 && ((firstPlayersResultModelArrayList.size() == 1))) {
            addExtraUserTextView.setVisibility(View.VISIBLE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
            addExtraUserTextView.setText("Add 1 or 3 More Players");
        } else if (secondResultModelArrayList.size() == 2 && firstPlayersResultModelArrayList.size() == 2) {
            addExtraUserTextView.setVisibility(View.GONE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
        } else if (secondResultModelArrayList.size() == 1 && firstPlayersResultModelArrayList.size() == 2) {
            addExtraUserTextView.setVisibility(View.VISIBLE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
            addExtraUserTextView.setText("Add 1 More Player");
        } else if (secondResultModelArrayList.size() == 0 && firstPlayersResultModelArrayList.size() == 2) {
            addExtraUserTextView.setVisibility(View.VISIBLE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
            addExtraUserTextView.setText("Add 2 More Players");
        } else if (secondResultModelArrayList.size() == 0 && ((firstPlayersResultModelArrayList.size() == 3))) {
            addExtraUserTextView.setVisibility(View.VISIBLE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
            addExtraUserTextView.setText("Add 1 More Player");
        }else if(secondResultModelArrayList.size() == 1 && (firstPlayersResultModelArrayList.size() == 3)){
            addExtraUserTextView.setVisibility(View.GONE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
        }else if(secondResultModelArrayList.size() == 3 && (firstPlayersResultModelArrayList.size() == 1)){
            addExtraUserTextView.setVisibility(View.GONE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
        }else if (secondResultModelArrayList.size() == 1 && (firstPlayersResultModelArrayList.size() == 1)){
            addExtraUserTextView.setVisibility(View.VISIBLE);
            selectedPlayersLinearLy.setVisibility(View.VISIBLE);
            addExtraUserTextView.setText("Optional: Add 2 More Players");
        }

        selectedPlayersRecycleVAdapter = null;
        selectedPlayersRecycleVAdapter = new SelectedPlayersAdapter(AddPlayerFromBookingFragmentsActv.this, firstPlayersResultModelArrayList, AddPlayerFromBookingFragmentsActv.this, 1);
        playersListRecycleView.setAdapter(selectedPlayersRecycleVAdapter);

        secondPlayersRecycleVAdapter = null;
        if (secondResultModelArrayList != null) {
            secondSelectedPlayersLinearLy.setVisibility(View.VISIBLE);
        }

        secondPlayersRecycleVAdapter = new SelectedPlayersAdapter(AddPlayerFromBookingFragmentsActv.this, secondResultModelArrayList, AddPlayerFromBookingFragmentsActv.this, 2);
        secondPlayersListRecycleView1.setAdapter(secondPlayersRecycleVAdapter);
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
