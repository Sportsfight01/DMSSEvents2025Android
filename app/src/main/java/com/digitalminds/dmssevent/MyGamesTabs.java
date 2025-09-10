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
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.fragments.MyGameDoublesFragment;
import com.digitalminds.dmssevent.fragments.MyGameSinglesFragment;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.TodayDoublesListModel;
import com.digitalminds.dmssevent.models.TodaySinglesListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyGamesTabs extends AppCompatActivity implements View.OnClickListener, WebServiceResponseCallBack {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView toolbar_title_games;
    DmsEventsAppController dmsEventsAppController;
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    ArrayList<TodaySinglesListModel> myGamesModelArraySinglesList = new ArrayList<TodaySinglesListModel>();
    ArrayList<TodayDoublesListModel> tmyGamesModelArrayDoublesList = new ArrayList<TodayDoublesListModel>();
    //ArrayList<TeamsListModel> teamsListModelArrayList = new ArrayList<TeamsListModel>();
    //ArrayList<ScheduleListGamesModel> scheduleListGamesModelArrayList = new ArrayList<ScheduleListGamesModel>();
   // ArrayList<ScheduleListGamesModel> resultListGamesModelArrayList = new ArrayList<ScheduleListGamesModel>();
    //ArrayList<TodayDoublesListModel> todayDoublesListOfGamesModelArrayList = new ArrayList<TodayDoublesListModel>();
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tabs);
        dmsEventsAppController = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(MyGamesTabs.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar_games);
        toolbar_title_games = (TextView) toolbar.findViewById(R.id.toolbar_title_games);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //title=dmsEventsAppController.getGetAllGamesModelArrayList().get(dmsEventsAppController.getGamesItemSelectedPosition()).getName();
        toolbar_title_games.setText("My Games");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        callWebApiForSportsData();
    }

    private void callWebApiForSportsData() {
        if (Utils.isNetworkAvailable(MyGamesTabs.this)) {
            //callWebApiType = 1;
            progressDialog.show();
            //emptyElement.setVisibility(View.GONE);
           // int gameId=dmsEventsAppController.getGetAllGamesModelArrayList().get(dmsEventsAppController.getGamesItemSelectedPosition()).getId();
            String url = ConstantKeys.getAllGamesByEmpId + DmsSharedPreferences.getUserDetails(MyGamesTabs.this).getId();
            //String url = ConstantKeys.getAllGamesByEmpId + 81;
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
        adapter.addFragment(new MyGameSinglesFragment(), "Singles");
        adapter.addFragment(new MyGameDoublesFragment(), "Doubles");
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
                    JSONArray jsonArraySingleList = jsonObjectResult.getJSONArray("SingleList");
                    JSONArray jsonArrayDoubleList = jsonObjectResult.getJSONArray("DoubleList");
                    if(jsonArraySingleList!=null){
                    for (int i = 0; i < jsonArraySingleList.length(); i++) {
                        JSONObject jsonObjectFinalSingleList=jsonArraySingleList.getJSONObject(i);
                        TodaySinglesListModel todaySinglesListModel = new TodaySinglesListModel(jsonObjectFinalSingleList);
                        myGamesModelArraySinglesList.add(todaySinglesListModel);
                    }
                            dmsEventsAppController.setMyGamesModelArraySinglesList(myGamesModelArraySinglesList);
                    }
                    if(jsonArrayDoubleList!=null){
                        for (int i = 0; i < jsonArrayDoubleList.length(); i++) {
                            JSONObject jsonObjectFinalDoubleList=jsonArrayDoubleList.getJSONObject(i);
                            TodayDoublesListModel todayDoublesListModel = new TodayDoublesListModel(jsonObjectFinalDoubleList);
                            tmyGamesModelArrayDoublesList.add(todayDoublesListModel);
                        }
                        dmsEventsAppController.setMyGamesModelArrayDoublesList(tmyGamesModelArrayDoublesList);
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
                    Utils.showToast(MyGamesTabs.this, "Server Error");
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
                    Utils.showToast(MyGamesTabs.this, error);
                } else {
                    Utils.showToast(MyGamesTabs.this, "Network Error");
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
