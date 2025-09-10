package com.digitalminds.dmssevent.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.EventsAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.EventsDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 30-01-2018.
 */

public class EventsFragment extends Fragment implements WebServiceResponseCallBack,SwipeRefreshLayout.OnRefreshListener {
    View rootView;
    ListView albumsListView;
    ArrayList<EventsDetailsModel> arrayListDetailsModels = new ArrayList<EventsDetailsModel>();
    androidx.appcompat.app.ActionBar actionBar;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    //private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout emptyElement,header_main_page_clist1;
    TextView retryTextView;
    EventsAdapter eventsAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
       // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        rootView = inflater.inflate(R.layout.activity_eventlist, parent, false);
        controller = (DmsEventsAppController) getActivity().getApplicationContext();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        albumsListView = (ListView) rootView.findViewById(R.id.albumsListView);
        emptyElement=(LinearLayout)rootView.findViewById(R.id.emptyElement);
        retryTextView=(TextView)rootView.findViewById(R.id.retryTextView);
        header_main_page_clist1=(LinearLayout)rootView.findViewById(R.id.header_main_page_clist1);

        callWebApiForEventDetails();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callWebApiForEventDetails();
                mSwipeRefreshLayout.setRefreshing(false);
                // Configure the refreshing colors
                mSwipeRefreshLayout.setColorSchemeResources(R.color.fbBlue);

            }
        });

        return rootView;
    }
    private void callWebApiForEventDetails() {
        if (Utils.isNetworkAvailable(getActivity())) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            header_main_page_clist1.setVisibility(View.VISIBLE);
            String url= ConstantKeys.eventList;
            controller.getWebService().getData(url, this);
        }else{
            //swipeRefreshLayout.setRefreshing(false);
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            header_main_page_clist1.setVisibility(View.GONE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForEventDetails();
                }
            });
        }
    }


    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);

                if (status) {
                    arrayListDetailsModels=null;
                    arrayListDetailsModels = new ArrayList<EventsDetailsModel>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        EventsDetailsModel eventsDetailsModel = new EventsDetailsModel(jsonArray.getJSONObject(i));
                        arrayListDetailsModels.add(eventsDetailsModel);

                    }
                    if(getActivity()!=null){
                    getActivity().runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(eventsAdapter == null) {
                                eventsAdapter = new EventsAdapter(getActivity(), arrayListDetailsModels, controller);
                                albumsListView.setAdapter(eventsAdapter);
                                eventsAdapter.notifyDataSetChanged();
                            }else{
                                eventsAdapter.notifyDataSetChanged();
                            }
                        }
                    }));
                      }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        progressDialog.cancel();

    }

    @Override
    public void onServiceCallFail(final String error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (error != null) {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }
        });

    }

    @Override
    public void onRefresh() {
        callWebApiForEventDetails();
    }
}

