package com.digitalminds.dmssevent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.TrackStatusAdapter;
import com.digitalminds.dmssevent.models.TrackStatus;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrakStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrakStatusFragment extends Fragment implements TrackStatusAdapter.ItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TrackStatusAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrakStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrakStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrakStatusFragment newInstance(String param1, String param2) {
        TrakStatusFragment fragment = new TrakStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_trak_status, container, false);


        ArrayList<TrackStatus> trackStatusData = new ArrayList<>();

        trackStatusData.add(new TrackStatus("26/06/2024 9:23 Am","IT","VPN Issue","My global protect vpn is not working,tried multple ways still it's not working"));
        trackStatusData.add(new TrackStatus("26/06/2024 9:23 Am","IT","VPN Issue","My global protect vpn is not working,tried multple ways still it's not working"));
        trackStatusData.add(new TrackStatus("26/06/2024 9:23 Am","IT","VPN Issue","My global protect vpn is not working,tried multple ways still it's not working"));
        trackStatusData.add(new TrackStatus("26/06/2024 9:23 Am","IT","VPN Issue","My global protect vpn is not working,tried multple ways still it's not working"));
        trackStatusData.add(new TrackStatus("26/06/2024 9:23 Am","IT","VPN Issue","My global protect vpn is not working,tried multple ways still it's not working"));
        trackStatusData.add(new TrackStatus("26/06/2024 9:23 Am","IT","VPN Issue","My global protect vpn is not working,tried multple ways still it's not working"));


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_trackStatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TrackStatusAdapter(getActivity(), trackStatusData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);



        return view;
    }
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "You clicked  on row number " + position, Toast.LENGTH_SHORT).show();
    }
}