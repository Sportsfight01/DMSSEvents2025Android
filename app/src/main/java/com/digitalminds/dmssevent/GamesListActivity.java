package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalminds.dmssevent.adapters.GamesGridTotalItemsAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.GridItemCallBack;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.GetAllGamesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 31-01-2018.
 */

public class GamesListActivity extends AppCompatActivity implements GridItemCallBack, WebServiceResponseCallBack,View.OnClickListener {
    GridView gridViewGames;
    GamesGridTotalItemsAdapter gamesGridItemsAdapter;
    ProgressDialog progressDialog;
    ActionBar actionBar;
    Toolbar toolbar;
    TextView toolbar_title_GamesList;
    DmsEventsAppController controller;
    ImageView imageViewMyGames;
    ArrayList<GetAllGamesModel> getAllGamesModelArrayList = new ArrayList<GetAllGamesModel>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameslist);
        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(GamesListActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title_GamesList = (TextView) toolbar.findViewById(R.id.toolbar_title_GamesList);
        imageViewMyGames = (ImageView) toolbar.findViewById(R.id.imageViewMyGames);
        imageViewMyGames.setOnClickListener(this);
        toolbar_title_GamesList.setText("All Games");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        gridViewGames = (GridView) findViewById(R.id.gridViewGames);
        callWebApiToGetGamesList();

    }

    private void callWebApiToGetGamesList() {
        if (Utils.isNetworkAvailable(GamesListActivity.this)) {
            //callWebApiType = 1;
            progressDialog.show();
            //emptyElement.setVisibility(View.GONE);
            String url = ConstantKeys.getAllGames;
            //String url = "http://192.168.100.92:1010/api/events/eventawards";
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            /*emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForGetAllNewsFeed();
                }
            });*/
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
    public void onItemClick(View view, int position, String gameType) {
        controller.setGamesItemSelectedPosition(position);
        if (gameType.equalsIgnoreCase("Single")) {
            Intent i = new Intent(GamesListActivity.this, SinglesGamesTabsActivity.class);
            startActivity(i);
        } else if (gameType.equalsIgnoreCase("Double")) {
            Intent i = new Intent(GamesListActivity.this, DoublesGamesTabsActivity.class);
            startActivity(i);
        }

    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                if (status) {
                    JSONObject jsonResult = jsonObject.getJSONObject(ConstantKeys.resultKey);
                    JSONArray jsonArray = jsonResult.getJSONArray("GamesList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        GetAllGamesModel getAllGamesModel = new GetAllGamesModel(jsonArray.getJSONObject(i));
                        getAllGamesModelArrayList.add(getAllGamesModel);
                    }
                    controller.setGetAllGamesModelArrayList(getAllGamesModelArrayList);
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gamesGridItemsAdapter = new GamesGridTotalItemsAdapter(GamesListActivity.this, getAllGamesModelArrayList, GamesListActivity.this);
                            gridViewGames.setAdapter(gamesGridItemsAdapter);
                        }
                    });
                } else {
                    Utils.showToast(GamesListActivity.this, "Server Error");
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
                    Utils.showToast(GamesListActivity.this, error);
                } else {
                    Utils.showToast(GamesListActivity.this, "Network Error");
                }
                progressDialog.cancel();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewMyGames:
                if (Utils.isNetworkAvailable(GamesListActivity.this)) {
                    Intent i=new Intent(this,MyGamesTabs.class);
                    startActivity(i);
                }
                break;
        }
    }
}
