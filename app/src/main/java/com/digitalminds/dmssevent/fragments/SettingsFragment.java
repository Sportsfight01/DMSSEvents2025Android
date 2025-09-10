package com.digitalminds.dmssevent.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.FireBaseActivity;
import com.digitalminds.dmssevent.MailValidationActivity;
import com.digitalminds.dmssevent.NominationsSelectionActivity;
import com.digitalminds.dmssevent.ProfileActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.MovableFloatingActionButton;
import com.digitalminds.dmssevent.common.Utils;

/**
 * Created by Sandeep.Kumar on 29-01-2018.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {
    View rootView;
    MovableFloatingActionButton fab;
    TextView textViewProfile, textViewNotifications, textViewLogout,toolbar_title,textViewNominations;
    Toolbar toolbar;
    DmsEventsAppController appController;
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        rootView = inflater.inflate(R.layout.fragment_settings, parent, false);
        appController=(DmsEventsAppController)getActivity().getApplicationContext();
        /*toolbar = (Toolbar)rootView. findViewById(R.id.toolbarSettings);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);*/
        textViewProfile = (TextView) rootView.findViewById(R.id.textViewProfile);
        fab = (MovableFloatingActionButton) rootView.findViewById(R.id.fab);
        textViewNominations = (TextView) rootView.findViewById(R.id.textViewNominations);
        //toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar_title.setText("Settings");
        textViewNotifications = (TextView) rootView.findViewById(R.id.textViewNotifications);
        textViewLogout = (TextView) rootView.findViewById(R.id.textViewLogout);
        textViewNominations.setOnClickListener(this);
        textViewProfile.setOnClickListener(this);
        textViewNotifications.setOnClickListener(this);
        textViewLogout.setOnClickListener(this);
        fab.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewProfile:
                if (Utils.isNetworkAvailable(getActivity())) {
                    Intent i = new Intent(getActivity(), ProfileActivity.class);
                    getActivity().startActivity(i);
                }
                break;
            case R.id.textViewNotifications:
                if (Utils.isNetworkAvailable(getActivity())) {
                    Intent i1 = new Intent(getActivity(), FireBaseActivity.class);
                    getActivity().startActivity(i1);
                }
                break;
            case R.id.textViewNominations:
                //Toast.makeText(getActivity(),"Coming Soon..",Toast.LENGTH_SHORT).show();
                if (Utils.isNetworkAvailable(getActivity())) {
                    Intent i = new Intent(getActivity(), NominationsSelectionActivity.class);
                    getActivity().startActivity(i);
                }
                break;
            case R.id.textViewLogout:
                if (Utils.isNetworkAvailable(getActivity())) {
                    dialogForLogout();
                }
                break;
            case R.id.fab:
              //  Toast.makeText(getActivity(),"Hiiiiii",Toast.LENGTH_SHORT).show();
                break;
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

}

