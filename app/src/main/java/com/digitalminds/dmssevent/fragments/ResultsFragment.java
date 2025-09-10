package com.digitalminds.dmssevent.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.ResultsMatchesAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.models.ResultListGamesModel;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 02-02-2018.
 */

public class ResultsFragment extends Fragment {
    View rootView;
    ResultsMatchesAdapter resultMatchesAdapter;
    ListView listViewScheduleList;
    ArrayList<ResultListGamesModel> scheduleListGamesModelArrayList=new ArrayList<ResultListGamesModel>();
    DmsEventsAppController controller;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_schedule, container, false);

        controller=(DmsEventsAppController)getActivity().getApplicationContext();
        listViewScheduleList=(ListView)rootView.findViewById(R.id.listViewScheduleList);
        listViewScheduleList.setEmptyView(rootView.findViewById(R.id.textViewNoResults));
        scheduleListGamesModelArrayList=controller.getMainResultListGamesModelArrayList();
        resultMatchesAdapter=new ResultsMatchesAdapter(getActivity(),scheduleListGamesModelArrayList);
        listViewScheduleList.setAdapter(resultMatchesAdapter);
        return rootView;
    }

}

