package com.digitalminds.dmssevent.viewmodel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.AddMoreSecondPlayersActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.SecondPlayersListAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.databinding.ActivitySelectplayerBinding;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.interfaces.SelectedPlayersInterface;
import com.digitalminds.dmssevent.models.RecentPlayersModel;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddExtraPlayerActivity extends AppCompatActivity implements View.OnClickListener, BookingInterface, SelectedPlayersInterface {
    LinearLayout addMorePlayersLinearLayout, recentPlayersLinearLy;
    TextView recentPlayersTextView, selectedPlayerTextView, cancelTextView, selectTextView;
    RecyclerView recentPlayersRecycleView;
    SelectPlayerViewModel selectPlayerViewModel;
    public static String GameName;
    public static String jsonElements;
    private ProgressDialog dialog;
    ArrayList<SelectedPlayersResultModel> selectedPlayersResultModelArrayList = new ArrayList<SelectedPlayersResultModel>();
    SecondPlayersListAdapter secondPlayersListAdapter;
    String selectedPlayersArray = "";
    boolean evenNumberPlayers = false;
    ArrayList<SelectedPlayersResultModel> showPlayersResultModelArrayList;
    DmsEventsAppController appController;
    public static int callActivity;
    JsonArray jsonArray;
    JsonArray jsonArrayTemp = new JsonArray();
    ArrayList<SelectedPlayersResultModel> finalArrayList = new ArrayList<SelectedPlayersResultModel>();
    int canSendBackCount;
    String userID = "";
    TextView gameNameTextView;
    Toolbar toolbar;
    TextView toolbar_title_fragments;
    LinearLayout preferredPlayersLineaLy,emptyLinearLy,selectOrCancelLinearLy;
    TextView recentPlayersTextView1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySelectplayerBinding activitySelectplayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_selectplayer);
        selectPlayerViewModel = ViewModelProviders.of(this, new SelectPlayerViewModelFactory(this)).get(SelectPlayerViewModel.class);
        activitySelectplayerBinding.setSelectplayerViewmodel(selectPlayerViewModel);
        intializeUIElements();

    }

    private void intializeUIElements() {
        appController = (DmsEventsAppController) getApplicationContext();
        userID = Integer.toString(DmsSharedPreferences.getUserDetails(AddExtraPlayerActivity.this).getId());
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar_title_fragments = (TextView) toolbar.findViewById(R.id.toolbar_title_fragments);
        toolbar_title_fragments.setText("Preferred Players");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addMorePlayersLinearLayout = findViewById(R.id.addMorePlayersLinearLayout);
        gameNameTextView = findViewById(R.id.gameNameTextView);
        addMorePlayersLinearLayout.setOnClickListener(this);
        recentPlayersLinearLy = findViewById(R.id.recentPlayersLinearLy);
        recentPlayersTextView = findViewById(R.id.recentPlayersTextView);
        selectedPlayerTextView = findViewById(R.id.selectedPlayerTextView);
        cancelTextView = findViewById(R.id.cancelTextView);
        recentPlayersTextView1 = findViewById(R.id.recentPlayersTextView1);
        cancelTextView.setOnClickListener(this);
        selectTextView = findViewById(R.id.selectTextView);
        preferredPlayersLineaLy = findViewById(R.id.preferredPlayersLineaLy);
        emptyLinearLy = findViewById(R.id.emptyLinearLy);
        selectOrCancelLinearLy = findViewById(R.id.selectOrCancelLinearLy);
        selectTextView.setOnClickListener(this);
        recentPlayersRecycleView = findViewById(R.id.recentPlayersRecycleView);
        LinearLayoutManager llm = new LinearLayoutManager(AddExtraPlayerActivity.this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recentPlayersRecycleView.setLayoutManager(llm);

        //gamesRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dialog = new ProgressDialog(AddExtraPlayerActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (GameName != null) {
            gameNameTextView.setText(GameName);
            selectPlayerViewModel.init(userID, "selectedplayer", GameName, getJsonObjet1(jsonElements));
            dialog.setMessage("Loading...");
            dialog.show();
            getTotalPlayersList();
        }


    }


    private JsonObject getJsonObjet1(String ids) {
        JsonObject jsonObjecta = new JsonObject();

        try {
            jsonObjecta.addProperty("cdgamemateids", ids);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObjecta;

    }

    private void getTotalPlayersList() {
        selectPlayerViewModel.getTotalSelectedPlayers().observe(AddExtraPlayerActivity.this, new Observer<RecentPlayersModel>() {
            @Override
            public void onChanged(RecentPlayersModel recentPlayersModel) {
                if (recentPlayersModel != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    List<SelectedPlayersResultModel> playersResultModels = recentPlayersModel.getTotalResult();
                    selectedPlayersResultModelArrayList.addAll(playersResultModels);

                    ArrayList<Integer> selectedId = new ArrayList<Integer>();
                    ArrayList<SelectedPlayersResultModel> selectedPlayersResultModelAyLTemp = appController.getFirstPlayersArrayList();
                    if (selectedPlayersResultModelAyLTemp != null && selectedPlayersResultModelAyLTemp.size() > 0) {
                        for (int i = 0; i < selectedPlayersResultModelAyLTemp.size(); i++) {
                            selectedId.add(selectedPlayersResultModelAyLTemp.get(i).getId());
                        }

                        int rotation = 0;
                        Iterator<SelectedPlayersResultModel> playersIterator = selectedPlayersResultModelArrayList.iterator();// Create an iterator
                        while (playersIterator.hasNext()) {// As long as there are elements in the list
                            SelectedPlayersResultModel nextModel = playersIterator.next();// Get the next element
                            if (selectedId.contains(nextModel.getId())) {
                                playersIterator.remove();
                                rotation++;
                                if (rotation == selectedId.size()) {
                                    canSendBackCount = rotation;
                                    //break;
                                }
                            }
                        }

                    }


                    if (secondPlayersListAdapter == null) {
                        secondPlayersListAdapter = new SecondPlayersListAdapter(AddExtraPlayerActivity.this,
                                callActivity, selectedPlayersResultModelArrayList, AddExtraPlayerActivity.this,
                                AddExtraPlayerActivity.this, appController);
                        if(selectedPlayersResultModelArrayList.size()==0){
                            emptyLinearLy.setVisibility(View.VISIBLE);
                            recentPlayersTextView1.setText("No preferred players for " +GameName);
                            recentPlayersLinearLy.setVisibility(View.GONE);
                            preferredPlayersLineaLy.setVisibility(View.GONE);
                            selectOrCancelLinearLy.setVisibility(View.GONE);
                        }else{
                            emptyLinearLy.setVisibility(View.GONE);
                            recentPlayersLinearLy.setVisibility(View.VISIBLE);
                            preferredPlayersLineaLy.setVisibility(View.VISIBLE);
                            selectOrCancelLinearLy.setVisibility(View.VISIBLE);
                        }
                        //gamesRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                        recentPlayersRecycleView.setAdapter(secondPlayersListAdapter);
                    } else {
                        if(selectedPlayersResultModelArrayList.size()==0){
                            emptyLinearLy.setVisibility(View.VISIBLE);
                            recentPlayersTextView1.setText("No preferred players for " +GameName);
                            recentPlayersLinearLy.setVisibility(View.GONE);
                            preferredPlayersLineaLy.setVisibility(View.GONE);
                            selectOrCancelLinearLy.setVisibility(View.GONE);
                        }else{
                            emptyLinearLy.setVisibility(View.GONE);
                            recentPlayersLinearLy.setVisibility(View.VISIBLE);
                            preferredPlayersLineaLy.setVisibility(View.VISIBLE);
                            selectOrCancelLinearLy.setVisibility(View.VISIBLE);
                        }
                        secondPlayersListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addMorePlayersLinearLayout:
                if (Utils.isNetworkAvailable(AddExtraPlayerActivity.this)) {
                    Intent i = new Intent(AddExtraPlayerActivity.this, AddMoreSecondPlayersActivity.class);
                    AddMoreSecondPlayersActivity.GameNames = GameName;
                    startActivityForResult(i, 1);
                }

                break;
            case R.id.cancelTextView:
                Intent intent3 = new Intent();
                intent3.putExtra("MESSAGE", "Cancel");
                setResult(2, intent3);
                finish();
                break;
            case R.id.selectTextView:
                ArrayList<SelectedPlayersResultModel> selectedPlayer = new ArrayList<SelectedPlayersResultModel>();
                jsonArray = new JsonArray();
                for (int i = 0; i < selectedPlayersResultModelArrayList.size(); i++) {
                    if (selectedPlayersResultModelArrayList.get(i).isSelected()) {
                        selectedPlayer.add(selectedPlayersResultModelArrayList.get(i));
                        jsonArray.add(selectedPlayersResultModelArrayList.get(i).getId());
                    }
                }
                selectedPlayersArray = jsonArray.toString();
                if (evenNumberPlayers == true && jsonArray.size() == 2 && canSendBackCount == 4) {
                    appController.setSecondPlayersArrayList(selectedPlayer);
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "PlayersAdded");
                    setResult(2, intent);
                    finish();
                } else if (evenNumberPlayers == false && jsonArray.size() == 1 && canSendBackCount == 2) {
                    appController.setSecondPlayersArrayList(selectedPlayer);
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "PlayersAdded");
                    setResult(2, intent);
                    finish();

                } else if (evenNumberPlayers == true && jsonArray.size() == 3 && canSendBackCount == 4) {
                    appController.setSecondPlayersArrayList(selectedPlayer);
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "PlayersAdded");
                    setResult(2, intent);
                    finish();
                } else if (evenNumberPlayers == false && jsonArray.size() == 4 && canSendBackCount > 4) {
                    Toast.makeText(AddExtraPlayerActivity.this, "You can't select more than 4 players", Toast.LENGTH_SHORT).show();
                } else if (jsonArray.size() == 0 && canSendBackCount > 1) {

                    Toast.makeText(AddExtraPlayerActivity.this, "Please select player", Toast.LENGTH_SHORT).show();
                } else if (jsonArray.size() == 1 && canSendBackCount == 3) {
                    Toast.makeText(AddExtraPlayerActivity.this, "Please select one more player", Toast.LENGTH_SHORT).show();
                } else if (evenNumberPlayers == true && jsonArray.size() == 1 && canSendBackCount == 4) {
                    appController.setSecondPlayersArrayList(selectedPlayer);
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "PlayersAdded");
                    setResult(2, intent);
                    finish();
                }else if(evenNumberPlayers == false && jsonArray.size() == 2 && canSendBackCount == 3){
                    Toast.makeText(AddExtraPlayerActivity.this, "Please select 1 more player", Toast.LENGTH_SHORT).show();
                }else if(evenNumberPlayers == true && jsonArray.size() == 1 && canSendBackCount == 2){
                    appController.setSecondPlayersArrayList(selectedPlayer);
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "PlayersAdded");
                    setResult(2, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onClickItem(int position, int bookingPositionId, String call, String bookingId, String bookingStatus) {
    }

    @Override
    public void onClick(int position, JsonArray jsonEleyments, ArrayList<SelectedPlayersResultModel> selectedPlayersResultM9odelArrayList) {
        boolean selected = selectedPlayersResultModelArrayList.get(position).isSelected();
        if (!selected) {
            selectedPlayersResultModelArrayList.get(position).setSelected(true);
            canSendBackCount++;
        } else {
            selectedPlayersResultModelArrayList.get(position).setSelected(false);
            canSendBackCount--;
        }
        if (canSendBackCount == 1) {
            evenNumberPlayers = true;
            selectTextView.setAlpha((float) 1);
        } else if (canSendBackCount == 2) {
            evenNumberPlayers = true;
            selectTextView.setAlpha((float) 1);
        } else if (canSendBackCount == 3) {
            evenNumberPlayers = false;
            selectTextView.setAlpha((float) 0.5);
        } else if (canSendBackCount == 4) {
            evenNumberPlayers = true;
            selectTextView.setAlpha((float) 1);
        } else if (canSendBackCount == 0) {
            evenNumberPlayers = false;
            selectTextView.setAlpha((float) 0.5);
        } else if (canSendBackCount > 4) {
            evenNumberPlayers = false;
            selectTextView.setAlpha((float) 0.5);
        }
        secondPlayersListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "back");
        setResult(2, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            selectedPlayersResultModelArrayList.clear();
            canSendBackCount = 0;
            selectPlayerViewModel.init(userID, "selectedplayer", GameName, getJsonObjet1(jsonElements));
            dialog.setMessage("Loading...");
            dialog.show();
            getTotalPlayersList();
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
}
