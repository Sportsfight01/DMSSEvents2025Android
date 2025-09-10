package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.fragments.ResultsDoublesFragment;
import com.digitalminds.dmssevent.fragments.ScheduleDoublesFragment;
import com.digitalminds.dmssevent.fragments.TeamsDoublesFragment;
import com.digitalminds.dmssevent.fragments.TodayDoublesMatchesFragment;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.ScheduleDoublesListModel;
import com.digitalminds.dmssevent.models.TeamsListModel;
import com.digitalminds.dmssevent.models.TodayDoublesListModel;
import com.digitalminds.dmssevent.models.TodaySinglesListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DoublesGamesTabsActivity extends AppCompatActivity implements View.OnClickListener, WebServiceResponseCallBack {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView toolbar_title_games;
    DmsEventsAppController dmsEventsAppController;
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    ArrayList<TodayDoublesListModel> todayListModelArraySinglesList = new ArrayList<TodayDoublesListModel>();
    ArrayList<TeamsListModel> teamsListModelArrayList = new ArrayList<TeamsListModel>();
    ArrayList<ScheduleDoublesListModel> scheduleListGamesModelArrayList = new ArrayList<ScheduleDoublesListModel>();
    ArrayList<TodayDoublesListModel> resultListGamesModelArrayList = new ArrayList<TodayDoublesListModel>();
    //ArrayList<TodayDoublesListModel> todayDoublesListOfGamesModelArrayList = new ArrayList<TodayDoublesListModel>();
    String title="";
    TodaySinglesListModel todaySinglesListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tabs);
        dmsEventsAppController = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(DoublesGamesTabsActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar_games);
        toolbar_title_games = (TextView) toolbar.findViewById(R.id.toolbar_title_games);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title=dmsEventsAppController.getGetAllGamesModelArrayList().get(dmsEventsAppController.getGamesItemSelectedPosition()).getName();
        toolbar_title_games.setText(title);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        callWebApiForSportsData();
    }

    private void callWebApiForSportsData() {
        if (Utils.isNetworkAvailable(DoublesGamesTabsActivity.this)) {
            //callWebApiType = 1;
            progressDialog.show();
            //emptyElement.setVisibility(View.GONE);
            int gameId=dmsEventsAppController.getGetAllGamesModelArrayList().get(dmsEventsAppController.getGamesItemSelectedPosition()).getId();
            String url = ConstantKeys.getGameDetailsByID + gameId;
            //String url = "http://192.168.100.92:1010/api/events/eventawards";
            dmsEventsAppController.getWebService().getData(url, this);
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TodayDoublesMatchesFragment(), "Today");
        adapter.addFragment(new TeamsDoublesFragment(), "Teams");
        adapter.addFragment(new ScheduleDoublesFragment(), "Schedule");
        adapter.addFragment(new ResultsDoublesFragment(), "Results");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                // Intent i = new Intent(SinglesGamesTabsActivity.this, FragmentsMainActivity.class);
                //startActivity(i);
                // finish();
                break;
        }
    }

    @Override
    public void onServiceCallSuccess(String result) {

        if (result != null) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                if (status) {
                    JSONObject jsonObjectResult = jsonObject.getJSONObject(ConstantKeys.resultKey);
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("DoubleList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFinal=jsonArray.getJSONObject(i);
                        if(!jsonObjectFinal.isNull("TodayList")){
                            final JSONArray jsonArrayTodayList = jsonObjectFinal.getJSONArray("TodayList");
                            for (int m = 0; m < jsonArrayTodayList.length(); m++) {
                                TodayDoublesListModel todayDoublesListModel = new TodayDoublesListModel(jsonArrayTodayList.getJSONObject(m));
                                todayListModelArraySinglesList.add(todayDoublesListModel);
                            }
                            dmsEventsAppController.setTodayDoublesListOfGamesTypeTodayArrayList(todayListModelArraySinglesList);
                        }
                        if(!jsonObjectFinal.isNull("TeamsList")){
                            final JSONArray jsonArrayTeamList = jsonObjectFinal.getJSONArray("TeamsList");
                            for (int j = 0; j < jsonArrayTeamList.length(); j++) {
                                TeamsListModel teamsListModel = new TeamsListModel(jsonArrayTeamList.getJSONObject(j));
                                teamsListModelArrayList.add(teamsListModel);
                            }
                            dmsEventsAppController.setTeamsDoublesListOfGamesTypeTodayArrayList(teamsListModelArrayList);
                        }
                        if(!jsonObjectFinal.isNull("ScheduleList")){
                            final JSONArray jsonArrayScheduleList = jsonObjectFinal.getJSONArray("ScheduleList");
                            for (int k = 0; k < jsonArrayScheduleList.length(); k++) {
                                ScheduleDoublesListModel scheduleListGamesModel = new ScheduleDoublesListModel(jsonArrayScheduleList.getJSONObject(k));
                                scheduleListGamesModelArrayList.add(scheduleListGamesModel);
                            }
                            dmsEventsAppController.setTodayDoublesListOfGamesTypeScheduleArrayList(scheduleListGamesModelArrayList);
                        }
                        if(!jsonObjectFinal.isNull("ResultList")){
                            final JSONArray jsonArrayResultList = jsonObjectFinal.getJSONArray("ResultList");
                            for (int l = 0; l < jsonArrayResultList.length(); l++) {
                                TodayDoublesListModel totalResultesultListModel = new TodayDoublesListModel(jsonArrayResultList.getJSONObject(l));
                                resultListGamesModelArrayList.add(totalResultesultListModel);
                            }
                            dmsEventsAppController.setTodayDoublesListOfGamesTypeResultArrayList(resultListGamesModelArrayList);
                        }
                    }
                    //do stuff
                    this.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.setVisibility(View.VISIBLE);
                            setupViewPager(viewPager);
                            tabLayout.setupWithViewPager(viewPager);
                        }
                    }));


                    //  }
                } else {
                    Utils.showToast(DoublesGamesTabsActivity.this, "Server Error");
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
                    Utils.showToast(DoublesGamesTabsActivity.this, error);
                } else {
                    Utils.showToast(DoublesGamesTabsActivity.this, "Network Error");
                }
                progressDialog.cancel();
            }
        });


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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
