package com.digitalminds.dmssevent.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.TeamsTabDoublesGameAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.models.TeamsListModel;

import java.util.ArrayList;


public class TeamsDoublesFragment extends Fragment /*implements TeamsListItemClickCallBack*/ {
    View rootView;
    TeamsTabDoublesGameAdapter teamsListAdapter;
    ListView listViewTeamsList;
    ArrayList<TeamsListModel> teamsListModelArrayList=new ArrayList<TeamsListModel>();
    DmsEventsAppController controller;
    public TeamsDoublesFragment() {
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
        rootView= inflater.inflate(R.layout.fragment_teams, container, false);
        controller=(DmsEventsAppController)getActivity().getApplicationContext();
        listViewTeamsList=(ListView)rootView.findViewById(R.id.listViewTeamsList);
        listViewTeamsList.setEmptyView(rootView.findViewById(R.id.textViewNoResults));
        teamsListModelArrayList=controller.getTeamsDoublesListOfGamesTypeTodayArrayList();
        teamsListAdapter=new TeamsTabDoublesGameAdapter(getActivity(),teamsListModelArrayList);
        listViewTeamsList.setAdapter(teamsListAdapter);
        return rootView;
    }

   /* @Override
    public void teamsItemLick(View view, int position) {
        TeamsListActivity.listItemSelectPosition=position;
        Intent i=new Intent(getActivity(),TeamsListActivity.class);
        startActivity(i);
    }*/
}
