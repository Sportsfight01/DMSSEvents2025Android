package com.digitalminds.dmssevent.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.MailValidationActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.BottomListViewAdapter;
import com.digitalminds.dmssevent.adapters.ServicesHomeAdapter;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.MovableFloatingActionButton;
import com.digitalminds.dmssevent.models.ServicesData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 29-01-2018.
 */

public class ServicesHomeFragment extends Fragment  {
    View rootView;
    MovableFloatingActionButton fab;
    TextView textViewProfile, textViewNotifications, textViewLogout,toolbar_title,textViewNominations;
    Toolbar toolbar;
    DmsEventsAppController appController;
    private RecyclerView recyclerView;
    private ArrayList<ServicesData> recyclerDataArrayList;
    public static ServicesHomeFragment newInstance() {
        ServicesHomeFragment fragment = new ServicesHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        rootView = inflater.inflate(R.layout.fragment_services_home, parent, false);
        recyclerView=rootView.findViewById(R.id.idCourseRV);

        // created new array list..
        recyclerDataArrayList=new ArrayList<>();

        // added data to array list
        recyclerDataArrayList.add(new ServicesData("IT",R.drawable.profilepic));
        recyclerDataArrayList.add(new ServicesData("HR",R.drawable.profilepic));
        recyclerDataArrayList.add(new ServicesData("Accounts",R.drawable.profilepic));
        recyclerDataArrayList.add(new ServicesData("Admin",R.drawable.profilepic));
        recyclerDataArrayList.add(new ServicesData("Track Status",R.drawable.profilepic));
        recyclerDataArrayList.add(new ServicesData("Suggestions/Feedback",R.drawable.profilepic));

        // added data from arraylist to adapter class.
        ServicesHomeAdapter adapter=new ServicesHomeAdapter(recyclerDataArrayList,getActivity());

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(new ServicesHomeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ServicesData item, int position) {
                System.out.println("ServicesData::"+item);
                if(item.getTitle().equalsIgnoreCase("IT")||item.getTitle().equalsIgnoreCase("HR")||
                        item.getTitle().equalsIgnoreCase("Accounts")||item.getTitle().equalsIgnoreCase("Admin")){
                    ServiceDetailsFragment serviceDetailsFragment = new ServiceDetailsFragment();
                    replaceFragment(serviceDetailsFragment);
                }else if(item.getTitle().equalsIgnoreCase("Suggestions/Feedback")){
                    SuggestionsFragment suggestionsFragment = new SuggestionsFragment();
                    replaceFragment(suggestionsFragment);
                }else{
                    TrakStatusFragment trakStatusFragment = new TrakStatusFragment();
                    replaceFragment(trakStatusFragment);
                }

               /* FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.service_fragment, serviceDetailsFragment);
                fragmentTransaction.commit();*/
            }
        });
        return rootView;
    }


    private void changeFragment(Fragment targetFragment){

        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
    private void replaceFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
    private void dialogForLogout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure you want to logout ?");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        DmsSharedPreferences.saveUserLoggedInStatus(getActivity(), false);
                        appController.setLoggedInStatus(false);
                        Intent i = new Intent(getActivity(), MailValidationActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        Toast.makeText(getActivity(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class BottomDialogList extends BottomSheetDialogFragment {

        BottomListViewAdapter adapterMessage;
        ArrayList<String> listData = new ArrayList<>();

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet, container, false);
            listData.add("Hardware");
            listData.add("Software");
            listData.add("Service");
            listData.add("Grievance");

            ListView listView = view.findViewById(R.id.bottom_listview);

            listView.setAdapter(new BottomListViewAdapter(getActivity(),listData));

            return view;
        }
    }
}

