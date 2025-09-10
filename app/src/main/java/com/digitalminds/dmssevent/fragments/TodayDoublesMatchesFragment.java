package com.digitalminds.dmssevent.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.MyGamesDoublesAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.TodayDoublesListModel;

import java.util.ArrayList;


public class TodayDoublesMatchesFragment extends Fragment implements WebServiceResponseCallBack {
    View rootView;
    ListView listViewMatches;
    MyGamesDoublesAdapter myGamesDoublesAdapter;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    LinearLayout emptyElement, awardDescription;
    TextView retryTextView;
    ArrayList<TodayDoublesListModel> todayListModelArrayDoublesList =new ArrayList<TodayDoublesListModel>();

    public TodayDoublesMatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_todays_match, container, false);
        controller=(DmsEventsAppController)getActivity().getApplicationContext();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        emptyElement = (LinearLayout) rootView.findViewById(R.id.emptyElement);
        retryTextView = (TextView) rootView.findViewById(R.id.retryTextView);
        listViewMatches = (ListView) rootView.findViewById(R.id.listViewMatches);
        listViewMatches.setEmptyView(rootView.findViewById(R.id.textViewNoResults));
        todayListModelArrayDoublesList =controller.getTodayDoublesListOfGamesTypeTodayArrayList();
        myGamesDoublesAdapter=new MyGamesDoublesAdapter(getActivity(), todayListModelArrayDoublesList);
        listViewMatches.setAdapter(myGamesDoublesAdapter);
        return rootView;
    }

    @Override
    public void onServiceCallSuccess(String result) {

    }

    @Override
    public void onServiceCallFail(String error) {

    }
}
