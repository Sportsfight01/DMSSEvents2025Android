package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.adapters.CustomExpandableListAdapter;
import com.digitalminds.dmssevent.adapters.ViewPagerAdapterForNominationAwards;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.AdapterCallBack;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.NominationAwardsModel;
import com.digitalminds.dmssevent.models.NominationListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NominationsAwardsActivity extends AppCompatActivity implements AdapterCallBack, WebServiceResponseCallBack {

    ViewPager phaseDetailsViewPager;
    LinearLayout introductionLayout, guideLinesLayout, lineaLyTotalLayout, emptyElement,linearNominationsLeft;
    DmsEventsAppController appController;
    ViewPagerAdapterForNominationAwards nominationsViewPagerAdapter;
    Toolbar toolbar;
    TextView toolbar_title, retryTextView, remainingCountTextView;
    ProgressDialog progressDialog;
    ArrayList<NominationAwardsModel> nominationAwardsModelArrayList = new ArrayList<NominationAwardsModel>();
    ArrayList<NominationListModel> nominationListModelArrayList = new ArrayList<NominationListModel>();
    int nominationCount, RESULT_CODE_FOR_NOMINEE_COUNT = 1;
    ExpandableListView expandableListView;
    CustomExpandableListAdapter customExpandableListAdapter;
    ImageView introductionLayout1;
    TextView spaceTextView;
    Animation startAnimation;
    boolean endOfNomination;
    String lastDateNomination="";
    boolean nominationNotStarted;
    TextView textViewLastDateNomination;
    int inforormalNominationCount;
    TextView tv_milestone_header;

    public static boolean formalAwards = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominations);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        appController = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(NominationsAwardsActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        phaseDetailsViewPager = (ViewPager) findViewById(R.id.phaseDetailsViewPager);
        // Disable clip to padding
        phaseDetailsViewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        phaseDetailsViewPager.setPadding(80, 0, 80, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        phaseDetailsViewPager.setPageMargin(40);
        introductionLayout = (LinearLayout) findViewById(R.id.introductionLayout);
        introductionLayout1 = (ImageView) findViewById(R.id.introductionLayout1);
        emptyElement = (LinearLayout) findViewById(R.id.emptyElementAwards);
        spaceTextView = (TextView) findViewById(R.id.spaceTextView);
        textViewLastDateNomination = (TextView) findViewById(R.id.textViewLastDateNomination);
        lineaLyTotalLayout = (LinearLayout) findViewById(R.id.lineaLyTotalLayout);
        linearNominationsLeft = (LinearLayout) findViewById(R.id.linearNominationsLeft);
        retryTextView = (TextView) findViewById(R.id.retryTextView);
        guideLinesLayout = (LinearLayout) findViewById(R.id.guideLinesLayout);
        remainingCountTextView = (TextView) findViewById(R.id.remainingCountTextView);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        tv_milestone_header = findViewById(R.id.tv_milestone_header);
        lineaLyTotalLayout.setVisibility(View.GONE);
        linearNominationsLeft.setVisibility(View.GONE);
        //startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);

        //setListViewHeightBasedOnChildren(expandableListView);

        if (formalAwards){
            toolbar_title.setText("Formal Award Nominations");
            tv_milestone_header.setText("Formal Awards Introduction");

        }else {
            toolbar_title.setText("Informal Award Nominations");
            tv_milestone_header.setText("Informal Awards Introduction");

        }

        callWebApiForNomiationAwards();
        introductionLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appController.setGuideLines(true);
                Intent i = new Intent(NominationsAwardsActivity.this, GuideLinesActivity.class);
                startActivity(i);
            }
        });
        introductionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appController.setGuideLines(true);

                Intent i = new Intent(NominationsAwardsActivity.this, GuideLinesActivity.class);
                startActivity(i);
            }
        });
        guideLinesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appController.setGuideLines(true);
                Intent i = new Intent(NominationsAwardsActivity.this, GuideLinesActivity.class);
                startActivity(i);
            }
        });
        phaseDetailsViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //setInfoPageChangesParallel(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public static void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void callWebApiForNomiationAwards() {
        if (Utils.isNetworkAvailable(NominationsAwardsActivity.this)) {
            progressDialog.show();
            lineaLyTotalLayout.setVisibility(View.VISIBLE);
            emptyElement.setVisibility(View.GONE);
           String url = ConstantKeys.getNominationAwards + "/" + DmsSharedPreferences.getUserDetails(NominationsAwardsActivity.this).getId();
           // String url = "http://192.168.60.173/DMSSMobileApp.WebApi/api/nomine/getMileStoneAwards/"+DmsSharedPreferences.getUserDetails(NominationsAwardsActivity.this).getId();

            //http://192.168.60.173/DMSSMobileApp.WebApi/api/

            //'http://localhost/DMSSMobileApp.WebApi/api/nomine/getMileStoneAwards/203'
            appController.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForNomiationAwards();
                }
            });
        }
    }

    /**
     * Toolbar widgets on-click actions.
     *
     * @param item A variable of type MenuItem.
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //finishing activity up on click of back arrow button
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void adapterClickedPosition(int position, boolean subChildItem) {
        appController.setSelectedNominationCriteria(nominationAwardsModelArrayList.get(position));
        NominateActivity.selectedNominationPosition = position;
        NominateActivity.selectedNominationCount = nominationCount;
        //  if (nominationCount == 0) {
        // Toast.makeText(this, "You cannot apply more than 2 Nominee's", Toast.LENGTH_SHORT).show();
        // } else {
        if (Utils.isNetworkAvailable(NominationsAwardsActivity.this)) {
            if (DmsSharedPreferences.isIntoductionReadOrNot(NominationsAwardsActivity.this)) {
                if(endOfNomination==false && nominationNotStarted==false){
                    Intent i = new Intent(NominationsAwardsActivity.this, NominateActivity.class);
                    startActivityForResult(i, RESULT_CODE_FOR_NOMINEE_COUNT);
                }else if(endOfNomination==true && nominationNotStarted==false){
                    Toast.makeText(this, "Nominations are Completed..Sorry!", Toast.LENGTH_SHORT).show();
                }else if(endOfNomination==false && nominationNotStarted==true){

                    Toast.makeText(this, "Nominations Coming Soon..!", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Please Read MileStone Introduction and Select Nominee", Toast.LENGTH_SHORT).show();
            }

            // }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_FOR_NOMINEE_COUNT && resultCode == RESULT_OK) {
            String nominatedKey = data.getStringExtra("key");
            if (nominatedKey.equalsIgnoreCase("Nominated")) {
                nominationCount = appController.getNominationCount();
                // callWebApiForNomiationAwards();
               /* if(nominationCount==0){
                    customExpandableListAdapter.notifyDataSetChanged();
                }*/

                remainingCountTextView.setText(String.valueOf(appController.getNominationCount()));
                if (appController.getNominationListModelArrayList().size() > 0) {
                    expandableListView.setVisibility(View.VISIBLE);
                    spaceTextView.setVisibility(View.GONE);
                    introductionLayout.setVisibility(View.GONE);
                    introductionLayout1.setVisibility(View.VISIBLE);
                    customExpandableListAdapter = new CustomExpandableListAdapter(NominationsAwardsActivity.this, appController.getNominationListModelArrayList());
                    expandableListView.setAdapter(customExpandableListAdapter);
                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            expandableListView.startAnimation(startAnimation);
                        }
                    }, 400);
                    expandableListView.clearAnimation();*/
                    customExpandableListAdapter.notifyDataSetChanged();
                } else {
                    expandableListView.setVisibility(View.GONE);
                   // expandableListView.clearAnimation();
                    spaceTextView.setVisibility(View.VISIBLE);
                    introductionLayout.setVisibility(View.VISIBLE);
                    introductionLayout1.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                nominationCount = jsonObject.getInt("FormalNominationCount");
                inforormalNominationCount = jsonObject.getInt("InformalNominationCount");
                endOfNomination = jsonObject.getBoolean("EndOfNomination");
                lastDateNomination = jsonObject.getString("LastDateNomination");
                nominationNotStarted = jsonObject.getBoolean("NominationsNotStarted");

                JSONArray jsonArray = jsonObject.getJSONArray("FormalAwardsList");
                JSONArray informalAwardsjsonArray = jsonObject.getJSONArray("InormalAwardsList");


                JSONArray jsonArrayNominationList = jsonObject.getJSONArray("FormalNominationList");
                JSONArray inforjsonArrayNominationList = jsonObject.getJSONArray("InformalNominationList");


                if (!formalAwards){
                    jsonArray = informalAwardsjsonArray;
                    jsonArrayNominationList = inforjsonArrayNominationList;
                   // nominationCount = inforormalNominationCount;
                    nominationCount = inforormalNominationCount;
                }
                appController.setNomineeCount(nominationCount);

                if (status) {
                    nominationAwardsModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        NominationAwardsModel nominationAwardsModel = new NominationAwardsModel(jsonArray.getJSONObject(i));
                        nominationAwardsModelArrayList.add(nominationAwardsModel);
                    }
                    for (int i = 0; i < jsonArrayNominationList.length(); i++) {
                        NominationListModel nominationAwardsModel = new NominationListModel(jsonArrayNominationList.getJSONObject(i));
                        nominationListModelArrayList.add(nominationAwardsModel);
                    }
                    NominationsAwardsActivity.this.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            textViewLastDateNomination.setText("Last Date: "+lastDateNomination);
                            lineaLyTotalLayout.setVisibility(View.VISIBLE);
                            linearNominationsLeft.setVisibility(View.VISIBLE);
                            if (DmsSharedPreferences.isIntoductionReadOrNot(NominationsAwardsActivity.this)) {
                                introductionLayout.setVisibility(View.GONE);
                                introductionLayout1.setVisibility(View.VISIBLE);
                            }else{
                                introductionLayout.setVisibility(View.VISIBLE);
                                introductionLayout1.setVisibility(View.GONE);
                            }
                            if (nominationsViewPagerAdapter == null) {
                                if (formalAwards){
                                    remainingCountTextView.setText(String.valueOf(nominationCount));
                                    nominationsViewPagerAdapter = new ViewPagerAdapterForNominationAwards(NominationsAwardsActivity.this, nominationAwardsModelArrayList, nominationCount);
                                }else{
                                    remainingCountTextView.setText(String.valueOf(inforormalNominationCount));
                                    nominationsViewPagerAdapter = new ViewPagerAdapterForNominationAwards(NominationsAwardsActivity.this, nominationAwardsModelArrayList, inforormalNominationCount);
                                }


                                phaseDetailsViewPager.setAdapter(nominationsViewPagerAdapter);
                                customExpandableListAdapter = new CustomExpandableListAdapter(NominationsAwardsActivity.this, nominationListModelArrayList);
                                if (nominationListModelArrayList.size() > 0) {
                                    expandableListView.setVisibility(View.VISIBLE);
                                    spaceTextView.setVisibility(View.GONE);
                                    introductionLayout.setVisibility(View.GONE);
                                    introductionLayout1.setVisibility(View.VISIBLE);
                                    expandableListView.setAdapter(customExpandableListAdapter);
                                 /*   new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            expandableListView.startAnimation(startAnimation);
                                        }
                                    }, 100);
                                    expandableListView.clearAnimation();*/
                                    customExpandableListAdapter.notifyDataSetChanged();
                                } else {
                                    expandableListView.setVisibility(View.GONE);
                                    spaceTextView.setVisibility(View.VISIBLE);
                                    //expandableListView.clearAnimation();
                                    introductionLayout.setVisibility(View.VISIBLE);
                                    introductionLayout1.setVisibility(View.GONE);
                                }

                            } else {
                                expandableListView.clearAnimation();
                                nominationsViewPagerAdapter.notifyDataSetChanged();
                                customExpandableListAdapter.notifyDataSetChanged();
                            }
                            //swipeRefreshLayout.setRefreshing(false);
                        }
                    }));


                    //  }
                } else {
                    Utils.showToast(NominationsAwardsActivity.this, "Server Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        progressDialog.cancel();
    }

    @Override
    public void onServiceCallFail(final String error) {
        NominationsAwardsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (error != null) {
                    Utils.showToast(NominationsAwardsActivity.this, error);
                    lineaLyTotalLayout.setVisibility(View.GONE);
                    emptyElement.setVisibility(View.VISIBLE);
                    retryTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callWebApiForNomiationAwards();
                        }
                    });

                } else {
                    Utils.showToast(NominationsAwardsActivity.this, "Network Error");
                    lineaLyTotalLayout.setVisibility(View.GONE);
                    emptyElement.setVisibility(View.VISIBLE);
                    retryTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callWebApiForNomiationAwards();
                        }
                    });
                }
                progressDialog.cancel();
            }
        });
    }
}
