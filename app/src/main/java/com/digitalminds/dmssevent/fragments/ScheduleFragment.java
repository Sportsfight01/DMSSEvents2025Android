package com.digitalminds.dmssevent.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.ScheduleMatchesAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.models.ScheduleListGamesModel;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment {
    View rootView;
    ScheduleMatchesAdapter scheduleMatchesAdapter;
    ListView listViewScheduleList;
    ArrayList<ScheduleListGamesModel> scheduleListGamesModelArrayList=new ArrayList<ScheduleListGamesModel>();
    DmsEventsAppController controller;
    public ScheduleFragment() {
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
        rootView= inflater.inflate(R.layout.fragment_schedule, container, false);

        controller=(DmsEventsAppController)getActivity().getApplicationContext();
        listViewScheduleList=(ListView)rootView.findViewById(R.id.listViewScheduleList);
        listViewScheduleList.setEmptyView(rootView.findViewById(R.id.textViewNoResults));
        scheduleListGamesModelArrayList=controller.getScheduleListGamesModelArrayList();
        scheduleMatchesAdapter=new ScheduleMatchesAdapter(getActivity(),scheduleListGamesModelArrayList);
        listViewScheduleList.setAdapter(scheduleMatchesAdapter);
        return rootView;
    }

}
