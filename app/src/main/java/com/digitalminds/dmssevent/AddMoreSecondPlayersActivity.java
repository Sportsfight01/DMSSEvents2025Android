package com.digitalminds.dmssevent;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.adapters.DepartListAdapter;
import com.digitalminds.dmssevent.adapters.PrefferedPlayersAdapter;
import com.digitalminds.dmssevent.adapters.SelectedPlayersListAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.GridSpacingItemDecoration;
import com.digitalminds.dmssevent.common.Util;
import com.digitalminds.dmssevent.databinding.ActivityAddMorePlayersBinding;
import com.digitalminds.dmssevent.interfaces.BookingInterface;
import com.digitalminds.dmssevent.interfaces.GameClickListener;
import com.digitalminds.dmssevent.interfaces.ListItemClickListener;
import com.digitalminds.dmssevent.interfaces.SelectedPlayersInterface;
import com.digitalminds.dmssevent.models.AddedPlayersModel;
import com.digitalminds.dmssevent.models.DepartmentSearchModel;
import com.digitalminds.dmssevent.models.RecentPlayersModel;
import com.digitalminds.dmssevent.models.ResultOFDepartment;
import com.digitalminds.dmssevent.models.SelectedPlayersResultModel;
import com.digitalminds.dmssevent.viewmodel.AddMoreViewModel;
import com.digitalminds.dmssevent.viewmodel.AddMoreViewModelFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddMoreSecondPlayersActivity extends AppCompatActivity implements View.OnClickListener, BookingInterface, SelectedPlayersInterface, GameClickListener, ListItemClickListener {
    RecyclerView selectedPlayersRecycleView;
    RecyclerView horizontalRecycleView;
    EditText searchEditText;
    ImageView searchImageView,departmentImageView;
    AddMoreViewModel addMoreViewModel;
    private ProgressDialog dialog;
    ArrayList<SelectedPlayersResultModel> selectedPlayersResultModelArrayList = new ArrayList<SelectedPlayersResultModel>();
    ArrayList<DepartmentSearchModel> departmentSearchModelArrayList = new ArrayList<DepartmentSearchModel>();
    SelectedPlayersListAdapter selectedPlayersListAdapter;
    DmsEventsAppController appController;
    int canSendBackCount;
    JSONArray jsonArrayTemp = new JSONArray();
    List<Integer> myListArray = new ArrayList<Integer>();
    PrefferedPlayersAdapter prefferedPlayersAdapter;
    Toolbar toolbar;
    TextView toolbar_title_fragments, selectBtTextView, cancelBtTextView;
    ArrayList<SelectedPlayersResultModel> tempSelectedIOne = new ArrayList<SelectedPlayersResultModel>();
    public static String GameNames;
    String ids = "";
    boolean firstTime = false;
    String userID = "";
    Dialog dialogDepartment;
    DepartListAdapter departListAdapter;
    RecyclerView recycleViewAddSpecies;
    int departID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddMorePlayersBinding activityAddMorePlayersBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_more_players);
        addMoreViewModel = ViewModelProviders.of((FragmentActivity) this, (ViewModelProvider.Factory) new AddMoreViewModelFactory(AddMoreSecondPlayersActivity.this)).get(AddMoreViewModel.class);
        activityAddMorePlayersBinding.setAddMorePlayerViewModel(addMoreViewModel);
        intializeUIElements();
    }

    private void intializeUIElements() {
        appController = (DmsEventsAppController) getApplicationContext();
        userID = Integer.toString(DmsSharedPreferences.getUserDetails(AddMoreSecondPlayersActivity.this).getId());
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar_title_fragments = (TextView) toolbar.findViewById(R.id.toolbar_title_fragments);
        toolbar_title_fragments.setText("Preferred Players");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        selectedPlayersRecycleView = findViewById(R.id.selectedPlayersRecycleView);
        horizontalRecycleView = findViewById(R.id.horizontalRecycleView);
        searchEditText = findViewById(R.id.searchEditText);
        departmentImageView = findViewById(R.id.departmentImageView);
        searchImageView = findViewById(R.id.searchImageView);
        selectBtTextView = findViewById(R.id.selectBtTextView);
        cancelBtTextView = findViewById(R.id.cancelBtTextView);
        searchImageView.setOnClickListener(this);
        selectBtTextView.setOnClickListener(this);
        cancelBtTextView.setOnClickListener(this);
        departmentImageView.setOnClickListener(this);
        dialog = new ProgressDialog(AddMoreSecondPlayersActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        horizontalRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(this, R.dimen.grid_size);
        selectedPlayersRecycleView.addItemDecoration(itemDecoration);
        selectedPlayersRecycleView.setLayoutManager(gridLayoutManager);
        getDepartmentList();
    }

    private void getDepartmentList() {
        if(Util.isNetworkAvailable(this)){
            addMoreViewModel.init("getDepartment");
            dialog.setMessage("Loading...");
            dialog.show();
            setDepartmentDetails();
        }
    }

    private void setDepartmentDetails() {
        addMoreViewModel.getTotalDetartments().observe(AddMoreSecondPlayersActivity.this, new Observer<ResultOFDepartment>() {
            @Override
            public void onChanged(ResultOFDepartment resultOFDepartment) {
                if (resultOFDepartment != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    departmentSearchModelArrayList.clear();
                    List<DepartmentSearchModel> departmentSearchModels = resultOFDepartment.getTotalResult();
                    departmentSearchModelArrayList.addAll(departmentSearchModels);
                    appController.setDepartmentSearchModelArrayList(departmentSearchModelArrayList);

                }
            }
        });

    }

    private void setAddMorePlayerDetails() {
        addMoreViewModel.getTotalSelectedPlayers().observe(AddMoreSecondPlayersActivity.this, new Observer<RecentPlayersModel>() {
            @Override
            public void onChanged(RecentPlayersModel recentPlayersModel) {
                if (recentPlayersModel != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    selectedPlayersResultModelArrayList.clear();
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

                    if (selectedPlayersListAdapter == null) {
                        selectedPlayersListAdapter = new SelectedPlayersListAdapter(AddMoreSecondPlayersActivity.this,
                                3, selectedPlayersResultModelArrayList, AddMoreSecondPlayersActivity.this,
                                AddMoreSecondPlayersActivity.this, appController);

                        selectedPlayersRecycleView.setAdapter(selectedPlayersListAdapter);
                    } else {
                        selectedPlayersListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchImageView:
                String searchKey = searchEditText.getText().toString().trim();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                if (searchKey.length() > 0) {
                    addMoreViewModel.init("searchPlayer", searchKey);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    setAddMorePlayerDetails();
                } else {
                    Toast.makeText(AddMoreSecondPlayersActivity.this, "Please enter name or depart", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.selectBtTextView:
                if (tempSelectedIOne.size() == 0) {
                    jsonArrayTemp = null;
                }
                jsonArrayTemp = new JSONArray();
                for (int i = 0; i < tempSelectedIOne.size(); i++) {
                    jsonArrayTemp.put(tempSelectedIOne.get(i).getId());
                    if (firstTime) {
                        ids = ids + "," + String.valueOf(tempSelectedIOne.get(i).getId());
                    } else {
                        firstTime = true;
                        ids = String.valueOf(tempSelectedIOne.get(i).getId());
                    }


                    myListArray.add(tempSelectedIOne.get(i).getId());
                }
                if (jsonArrayTemp != null && jsonArrayTemp.length() > 0) {
                    if (GameNames != null) {

                        addMoreViewModel.init(userID, "selectedplayerAdd", GameNames, getJsonObjet1(ids));
                        dialog.setMessage("Loading...");
                        dialog.show();
                        getAddedPlayersList();
                    }

                   // Toast.makeText(AddMorePlayersActivity.this, jsonArrayTemp.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMoreSecondPlayersActivity.this, "Please Select Atleast One Player", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.cancelBtTextView:
                finish();
                break;
            case R.id.departmentImageView:

                openDialogDepartmentList(appController.getDepartmentSearchModelArrayList(),"Department List");
                break;

        }
    }

    public void openDialogDepartmentList(ArrayList<DepartmentSearchModel> arrayList, String title) {

        dialogDepartment = new Dialog(this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDepartment.setCancelable(false);
        dialogDepartment.setContentView(R.layout.dialog_add_depart);
        ImageView imageViewClose = (ImageView) dialogDepartment.findViewById(R.id.imageViewClose);
        TextView titleTextView = (TextView) dialogDepartment.findViewById(R.id.titleTextView);
        titleTextView.setText(title);
        recycleViewAddSpecies = (RecyclerView) dialogDepartment.findViewById(R.id.recycleViewAddSpecies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(this, R.dimen.grid_size1);
        recycleViewAddSpecies.addItemDecoration(itemDecoration);
        recycleViewAddSpecies.setLayoutManager(gridLayoutManager);

        //dialog.dismiss();
        departListAdapter = new DepartListAdapter(this, arrayList, AddMoreSecondPlayersActivity.this);
        recycleViewAddSpecies.setAdapter(departListAdapter);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDepartment.dismiss();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogDepartment.getWindow().getAttributes());
        lp.width = width - 10;
        lp.height = height / 2;
        //lp.gravity = Gravity.CENTER;

        dialogDepartment.getWindow().setAttributes(lp);


        dialogDepartment.show();

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

    private void getAddedPlayersList() {
        addMoreViewModel.getAddedTotalSelectedPlayers().observe(AddMoreSecondPlayersActivity.this, new Observer<AddedPlayersModel>() {
            @Override
            public void onChanged(AddedPlayersModel addedPlayersModel) {
                if (addedPlayersModel != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    List<SelectedPlayersResultModel> playersResultModels = addedPlayersModel.getTotalResult();
                    String status = addedPlayersModel.getStatus();
                    if (status.equalsIgnoreCase("true")) {
                        Toast.makeText(AddMoreSecondPlayersActivity.this, "Players Added Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", "PlayersAdded");
                        setResult(1, intent);
                        finish();
                    } else {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(AddMoreSecondPlayersActivity.this, "Network Error,Please add players again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClickItem(int position,int bookingPositionId, String call, String bookingId, String bookingStatus) {

    }

    @Override
    public void onClick(int position, JsonArray jsonElements, ArrayList<SelectedPlayersResultModel> selectedPlayersResult9ModelArrayList) {
        boolean selected = selectedPlayersResultModelArrayList.get(position).isSelected();
        if (!selected) {
            selectedPlayersResultModelArrayList.get(position).setSelected(true);
            tempSelectedIOne.add(selectedPlayersResultModelArrayList.get(position));
        } else {
            selectedPlayersResultModelArrayList.get(position).setSelected(false);
            int deselectedPosition = -1;
            for (int i = 0; i < tempSelectedIOne.size(); i++) {
                int id = tempSelectedIOne.get(i).getId();
                int selectedID = selectedPlayersResultModelArrayList.get(position).getId();
                if (id == selectedID) {
                    deselectedPosition = i;
                    break;
                }
            }
            if (deselectedPosition != -1) {
                tempSelectedIOne.remove(deselectedPosition);
            }
        }
        selectedPlayersListAdapter.notifyDataSetChanged();
        if (prefferedPlayersAdapter == null) {
            prefferedPlayersAdapter = new PrefferedPlayersAdapter(AddMoreSecondPlayersActivity.this, 3, tempSelectedIOne, AddMoreSecondPlayersActivity.this);
            horizontalRecycleView.setAdapter(prefferedPlayersAdapter);
        } else {
            prefferedPlayersAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(int position, String clickedFrom) {
        if (tempSelectedIOne != null) {
            ArrayList<Integer> selectedId = new ArrayList<Integer>();
            ArrayList<SelectedPlayersResultModel> selectedPlayersResultModelAyLTemp = tempSelectedIOne;
            if (selectedPlayersResultModelAyLTemp != null && selectedPlayersResultModelAyLTemp.size() > 0) {
                for (int i = 0; i < selectedPlayersResultModelAyLTemp.size(); i++) {
                    selectedId.add(selectedPlayersResultModelAyLTemp.get(i).getId());
                }
                int selectedPlayerID = selectedId.get(position);
                tempSelectedIOne.remove(position);
                prefferedPlayersAdapter.notifyDataSetChanged();
                if (tempSelectedIOne.size() == 0) {
                    tempSelectedIOne.clear();
                }
                int rotation = 0;
                for (int i = 0; i < selectedPlayersResultModelArrayList.size(); i++) {
                    if (selectedPlayerID == selectedPlayersResultModelArrayList.get(i).getId()) {
                        selectedPlayersResultModelArrayList.get(i).setSelected(false);
                        selectedPlayersListAdapter.notifyDataSetChanged();
                        prefferedPlayersAdapter.notifyDataSetChanged();
                        //jsonArrayTemp.add(selectedPlayersResultModelArrayList.get(i).getId());
                        rotation++;
                        if (rotation == selectedId.size()) {
                            canSendBackCount = rotation;
                            break;
                        }
                    }
                }
            }

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
    public void onClickRecycleItem(int position, String name, String imageUrl) {
        String depart=appController.getDepartmentSearchModelArrayList().get(position).getDeptName();
        departID=appController.getDepartmentSearchModelArrayList().get(position).getId();
        dialogDepartment.dismiss();
        getPlayersByDepartment(departID);


    }

    private void getPlayersByDepartment(int departID) {
        if (Util.isNetworkAvailable(this)) {
            addMoreViewModel.init("getPlayersByDepartment", departID);
            dialog.setMessage("Loading...");
            dialog.show();
            setAddMorePlayerDetails();
        }
    }
}
