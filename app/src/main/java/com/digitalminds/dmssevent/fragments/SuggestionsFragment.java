package com.digitalminds.dmssevent.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.BottomListViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuggestionsFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    AppCompatEditText service_type;

    public SuggestionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuggestionsFragment newInstance(String param1, String param2) {
        SuggestionsFragment fragment = new SuggestionsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_suggestions, container, false);
        service_type =(AppCompatEditText)rootView.findViewById(R.id.service_type);

        service_type.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView back_img=(ImageView) getActivity().findViewById(R.id.back_img);
        back_img.setVisibility(View.VISIBLE);
        back_img.setOnClickListener(this);

        /*back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Back:: clicked");
                getFragmentManager().popBackStack();
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        System.out.println("Back:: clicked");
//        getFragmentManager().popBackStack();

        BottomDialogList fragment = new BottomDialogList();
        fragment.show(getActivity().getSupportFragmentManager(), "BottomDialogList");
    }
    public static class BottomDialogList extends BottomSheetDialogFragment {

        BottomListViewAdapter adapterMessage;
        ArrayList<String> listData = new ArrayList<>();


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            System.out.println("BottomDialogList onCreateDialog::");

            return super.onCreateDialog(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet, container, false);
            listData.add("Hardware");
            listData.add("Software");
            listData.add("Service");
            listData.add("Grievance");
            System.out.println("BottomDialogList::");
            ListView listView = view.findViewById(R.id.bottom_listview);

            listView.setAdapter(new BottomListViewAdapter(getActivity(),listData));

            return view;
        }
    }
}