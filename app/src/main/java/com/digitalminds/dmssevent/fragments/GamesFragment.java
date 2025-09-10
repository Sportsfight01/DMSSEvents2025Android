package com.digitalminds.dmssevent.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.digitalminds.dmssevent.DoublesGamesTabsActivity;
import com.digitalminds.dmssevent.GamesListActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.SinglesGamesTabsActivity;
import com.digitalminds.dmssevent.adapters.GamesGridItemsAdapter;
import com.digitalminds.dmssevent.adapters.ViewPagerAdapterTodaySingles;
import com.digitalminds.dmssevent.adapters.ViewPagerAdapterTodayDoubles;
import com.digitalminds.dmssevent.circleindicator.CirclePageIndicator;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.GridItemCallBack;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.GetAllGamesModel;
import com.digitalminds.dmssevent.models.TodayDoublesListModel;
import com.digitalminds.dmssevent.models.TodaySinglesListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 29-01-2018.
 */

public class GamesFragment extends Fragment implements GridItemCallBack,WebServiceResponseCallBack {
    View rootView,viewLine;
   // AutoScrollViewPager autoScrollViewPager1, autoScrollViewPagerResult;
    CirclePageIndicator mIndicator;
    ViewPagerAdapterTodaySingles viewPagerAdapterTodaySingles;
    ViewPagerAdapterTodayDoubles viewPagerAdapterTodayDoubles;
    GamesGridItemsAdapter gamesGridItemsAdapter;
    TextView textViewMore,textViewResultData,textViewTodayData,retryTextView;
    GridView gridViewGamesList;
    ViewGroup.LayoutParams layoutParams;
    DmsEventsAppController controller;
    private Toolbar toolbar;
    ViewPager autoScrollViewPager,autoScrollViewPagerResult;
    ProgressDialog progressDialog;
    ArrayList<GetAllGamesModel> getAllGamesModelArrayList=new ArrayList<GetAllGamesModel>();
    ArrayList<TodaySinglesListModel> todayListModelArraySinglesList =new ArrayList<TodaySinglesListModel>();
    ArrayList<TodayDoublesListModel> todayDoublesListOfGamesModelArrayList =new ArrayList<TodayDoublesListModel>();
    LinearLayout linearLyTotalViews,emptyElement;
    TextView textViewMatchResults,textViewTodayMatch;
    ScrollView scrollViewGames;
    public static GamesFragment newInstance() {
        GamesFragment fragment = new GamesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_games, container, false);
        controller = (DmsEventsAppController) getActivity().getApplicationContext();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        autoScrollViewPager = (ViewPager) rootView.findViewById(R.id.autoScrollViewPager);
        autoScrollViewPagerResult = (ViewPager) rootView.findViewById(R.id.autoScrollViewPagerResult);
        autoScrollViewPager.setClipToPadding(false);
        autoScrollViewPagerResult.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        autoScrollViewPager.setPadding(80, 0, 80, 0);
        autoScrollViewPagerResult.setPadding(80, 0, 80, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        autoScrollViewPager.setPageMargin(40);
        autoScrollViewPagerResult.setPageMargin(40);
        textViewTodayData = (TextView) rootView.findViewById(R.id.textViewTodayData);
        scrollViewGames = (ScrollView) rootView.findViewById(R.id.scrollViewGames);
        linearLyTotalViews = (LinearLayout) rootView.findViewById(R.id.linearLyTotalViews);
        emptyElement = (LinearLayout) rootView.findViewById(R.id.emptyElement);
        textViewResultData = (TextView) rootView.findViewById(R.id.textViewResultData);
        textViewTodayMatch = (TextView) rootView.findViewById(R.id.textViewTodayMatch);
        textViewMatchResults = (TextView) rootView.findViewById(R.id.textViewMatchResults);
        viewLine = (View) rootView.findViewById(R.id.viewLine);
        mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        gridViewGamesList = (GridView) rootView.findViewById(R.id.gridViewGamesList);
        textViewMore = (TextView) rootView.findViewById(R.id.textViewMore);
        retryTextView = (TextView) rootView.findViewById(R.id.retryTextView);
        scrollViewGames.setVisibility(View.GONE);
        callWebApiToGetGamesList();
        textViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(getActivity())) {
                    Intent i = new Intent(getActivity(), GamesListActivity.class);
                    startActivity(i);
                }

            }
        });

        //mIndicator.setViewPager(autoScrollViewPager);
        return rootView;
    }
    private void callWebApiToGetGamesList() {
        if (Utils.isNetworkAvailable(getActivity())) {
            //callWebApiType = 1;
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            linearLyTotalViews.setVisibility(View.VISIBLE);
            String url = ConstantKeys.getAllGames;
            //String url = "http://192.168.100.92:1010/api/events/eventawards";
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            linearLyTotalViews.setVisibility(View.GONE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiToGetGamesList();
                }
            });
        }
    }
    @Override
    public void onItemClick(View view, int position,String gameType) {
        controller.setGamesItemSelectedPosition(position);
        if (Utils.isNetworkAvailable(getActivity())) {
            if(gameType.equalsIgnoreCase("Single")){
                Intent i = new Intent(getActivity(), SinglesGamesTabsActivity.class);
                startActivity(i);
            }else if(gameType.equalsIgnoreCase("Double")){
                Intent i = new Intent(getActivity(), DoublesGamesTabsActivity.class);
                startActivity(i);
            }

        }
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                if (status) {
                    getAllGamesModelArrayList.clear();
                    todayListModelArraySinglesList.clear();
                    todayDoublesListOfGamesModelArrayList.clear();
                    JSONObject jsonObjectResult = jsonObject.getJSONObject(ConstantKeys.resultKey);
                    JSONArray jsonArrayGamesList = jsonObjectResult.getJSONArray("GamesList");
                    JSONArray jsonArrayTodayList = jsonObjectResult.getJSONArray("TodaySingleList");
                    JSONArray jsonArrayTodayDoubleList = jsonObjectResult.getJSONArray("TodayDoubleList");
                    for (int i = 0; i < jsonArrayGamesList.length(); i++) {
                        GetAllGamesModel getAllGamesModel = new GetAllGamesModel(jsonArrayGamesList.getJSONObject(i));
                        getAllGamesModelArrayList.add(getAllGamesModel);
                    }

                    for (int i = 0; i < jsonArrayTodayList.length(); i++) {
                        TodaySinglesListModel todaySinglesListModel = new TodaySinglesListModel(jsonArrayTodayList.getJSONObject(i));
                        todayListModelArraySinglesList.add(todaySinglesListModel);
                    }

                    for (int i = 0; i < jsonArrayTodayDoubleList.length(); i++) {
                        TodayDoublesListModel todayDoublesListModel = new TodayDoublesListModel(jsonArrayTodayDoubleList.getJSONObject(i));
                        todayDoublesListOfGamesModelArrayList.add(todayDoublesListModel);
                    }
                    controller.setGetAllGamesModelArrayList(getAllGamesModelArrayList);
                    if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollViewGames.setVisibility(View.VISIBLE);
                            if (gamesGridItemsAdapter == null) {
                                if (getAllGamesModelArrayList.size() > 4) {
                                    textViewMore.setVisibility(View.VISIBLE);
                                } else {
                                    textViewMore.setVisibility(View.GONE);
                                }
                                gamesGridItemsAdapter = new GamesGridItemsAdapter(getActivity(), getAllGamesModelArrayList, GamesFragment.this);
                                gridViewGamesList.setAdapter(gamesGridItemsAdapter);
                                if (todayListModelArraySinglesList.size() > 0) {
                                    autoScrollViewPager.setVisibility(View.VISIBLE);
                                    textViewTodayData.setVisibility(View.GONE);
                                    viewPagerAdapterTodaySingles = new ViewPagerAdapterTodaySingles(getActivity(), todayListModelArraySinglesList);
                                    autoScrollViewPager.setAdapter(viewPagerAdapterTodaySingles);
                                } else {
                                    autoScrollViewPager.setVisibility(View.GONE);
                                    textViewTodayData.setVisibility(View.VISIBLE);
                                }
                                if (todayDoublesListOfGamesModelArrayList.size() > 0) {
                                    autoScrollViewPagerResult.setVisibility(View.VISIBLE);
                                    textViewResultData.setVisibility(View.GONE);
                                    viewPagerAdapterTodayDoubles = new ViewPagerAdapterTodayDoubles(getActivity(), todayDoublesListOfGamesModelArrayList);
                                    autoScrollViewPagerResult.setAdapter(viewPagerAdapterTodayDoubles);
                                    textViewTodayMatch.setVisibility(View.VISIBLE);
                                    textViewMatchResults.setVisibility(View.VISIBLE);
                                    //viewLine.setVisibility(View.VISIBLE);
                                } else {
                                    autoScrollViewPagerResult.setVisibility(View.GONE);
                                    textViewResultData.setVisibility(View.VISIBLE);
                                }


                            } else {
                                gamesGridItemsAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
                } else {
                    Utils.showToast(getActivity(), "Server Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        progressDialog.cancel();
    }

    @Override
    public void onServiceCallFail(final String error) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error != null) {
                        Utils.showToast(getActivity(), error);
                        emptyElement.setVisibility(View.VISIBLE);
                        linearLyTotalViews.setVisibility(View.GONE);
                        retryTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callWebApiToGetGamesList();
                            }
                        });
                    } else {
                        Utils.showToast(getActivity(), "Network Error");
                        emptyElement.setVisibility(View.VISIBLE);
                        linearLyTotalViews.setVisibility(View.GONE);
                        retryTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callWebApiToGetGamesList();
                            }
                        });
                    }
                    progressDialog.cancel();
                }
            });
        }
    }
}
