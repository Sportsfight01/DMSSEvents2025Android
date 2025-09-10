package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.digitalminds.dmssevent.adapters.TeamsListOfNamesAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.GetTeamMembersModel;
import com.digitalminds.dmssevent.models.TeamsListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 02-02-2018.
 */

public class TeamsListActivity extends AppCompatActivity implements WebServiceResponseCallBack{
    ListView listViewTeamList;
   // String[] titles = {"sandeep", "Madhu", "JK", "Ashish", "Naveen", "Satya", "Mohan", "Paneeth"};
    TeamsListOfNamesAdapter teamsListOfNamesAdapter;
    Toolbar toolbar;
    ArrayList<TeamsListModel> teamsListModelArrayList=new ArrayList<TeamsListModel>();
    TextView toolbar_title_fragments;
    public static int listItemSelectPosition;
    int gameID;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    ArrayList<GetTeamMembersModel> getTeamMembersModelArrayList=new ArrayList<GetTeamMembersModel>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_list);
        controller=(DmsEventsAppController)getApplicationContext();
        progressDialog = new ProgressDialog(TeamsListActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        teamsListModelArrayList=controller.getTeamsListModelArrayList();
        gameID=teamsListModelArrayList.get(listItemSelectPosition).getPlayerId();
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar_title_fragments=(TextView)toolbar.findViewById(R.id.toolbar_title_fragments);
        toolbar_title_fragments.setText(teamsListModelArrayList.get(listItemSelectPosition).getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        callWebApiToGetTeamList();
        listViewTeamList=(ListView)findViewById(R.id.listViewTeamList);
        listViewTeamList.setEmptyView(findViewById(R.id.textViewNoResults));
    }

    private void callWebApiToGetTeamList() {
        if (Utils.isNetworkAvailable(TeamsListActivity.this)) {
            progressDialog.show();
            //emptyElement.setVisibility(View.GONE);
            String url= ConstantKeys.getTeamMembers+gameID;
            //String url = "http://192.168.100.92:1010/api/events/eventawards";
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();

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
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);

                if (status) {
                    JSONObject jsonObjectResult = jsonObject.getJSONObject(ConstantKeys.resultKey);
                    JSONArray jsonArrayResult=jsonObjectResult.getJSONArray("MembersList");
                    String teamName=jsonObjectResult.getString("TeamName");
                    getTeamMembersModelArrayList.clear();
                    for (int i = 0; i < jsonArrayResult.length(); i++) {
                        GetTeamMembersModel getTeamMembersModel = new GetTeamMembersModel(jsonArrayResult.getJSONObject(i),teamName);
                        getTeamMembersModelArrayList.add(getTeamMembersModel);
                        this.runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (teamsListOfNamesAdapter == null) {
                                    teamsListOfNamesAdapter = new TeamsListOfNamesAdapter(TeamsListActivity.this, getTeamMembersModelArrayList);
                                    listViewTeamList.setAdapter(teamsListOfNamesAdapter);

                                } else {
                                    teamsListOfNamesAdapter.notifyDataSetChanged();
                                }
                                //swipeRefreshLayout.setRefreshing(false);
                            }
                        }));

                    }
                    //  }
                } else {
                    Utils.showToast(TeamsListActivity.this, "Server Error");
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
                    Utils.showToast(TeamsListActivity.this, error);
                } else {
                    Utils.showToast(TeamsListActivity.this, "Network Error");
                }
                progressDialog.cancel();
            }
        });
    }
}
