package com.digitalminds.dmssevent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.MovableFloatingActionButton;
import com.digitalminds.dmssevent.common.Util;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.fragments.GamesFragment;
import com.digitalminds.dmssevent.fragments.NewsFragment;
import com.digitalminds.dmssevent.fragments.SettingsFragment;
import com.digitalminds.dmssevent.interfaces.ApiInterface;
import com.digitalminds.dmssevent.locationservice.LocationUpdatesService;
import com.digitalminds.dmssevent.models.UserProfileModel;
//import com.firebase.jobdispatcher.FirebaseJobDispatcher;
//import com.firebase.jobdispatcher.GooglePlayDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sandeep.Kumar on 29-01-2018.
 */

public class FragmentsMainActivity extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    androidx.appcompat.app.ActionBar actionBar;
    TextView actionBarHeadingTextView, toolbar_title_fragments;
    LinearLayout lineaLyMyGames;
    Toolbar toolbar;
    TextView toolbar_title_Games;
    ImageView imageViewMyGames, imageViewMyBookings;
    boolean doubleBackToExitPressedOnce = false;
    MovableFloatingActionButton fab;
    final String appPackageName = "sportsfight.com.sportsfight";
    DmsEventsAppController controller;
    int selectedFrom = 0;
    RelativeLayout myInvitationsRelativieLy;
    TextView myNotifCountTextView;

    ImageView backButton;
    private static final String TAG = FragmentsMainActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    //GET THE USER DETAILS
    UserProfileModel userProfileModel;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            if (!checkPermissions()) {
                requestPermissions();
            } else {
                mService.requestLocationUpdates();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    DatabaseReference databaseReference;

    String userEmailID = null;
    String id = null;
    String empID = null;
    String empName = null;


    public static final String BASE_URL = "http://www.digitalminds.solutions/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

//    FirebaseJobDispatcher dispatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        setContentView(R.layout.fragments_main);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userProfileModel = DmsSharedPreferences.getUserDetails(FragmentsMainActivity.this);
//        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(FragmentsMainActivity.this));

        if (userProfileModel != null) {
            userEmailID = userProfileModel.getEmailID();
            id = String.valueOf(userProfileModel.getId());
            empID = userProfileModel.getEmpID();
            empName = userProfileModel.getCommonName();
        }

        if (!checkPermissions()) {
            requestPermissions();
        }
        // postLatLongToApi("17.512510","78.352226");
        // apiCall("17.554102","78.361848");

        controller = (DmsEventsAppController) getApplicationContext();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        controller.setScreenWidth(width);
        //setStatusBarGradiant(FragmentsMainActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar_title_fragments = (TextView) toolbar.findViewById(R.id.toolbar_title_fragments);
        toolbar_title_Games = (TextView) toolbar.findViewById(R.id.toolbar_title_Games);
        lineaLyMyGames = (LinearLayout) toolbar.findViewById(R.id.lineaLyMyGames);
        imageViewMyGames = (ImageView) toolbar.findViewById(R.id.imageViewMyGames);
        imageViewMyBookings = (ImageView) toolbar.findViewById(R.id.imageViewMyBookings);
        myInvitationsRelativieLy = (RelativeLayout) toolbar.findViewById(R.id.myInvitationsRelativieLy);
        myInvitationsRelativieLy.setVisibility(View.GONE);
        myNotifCountTextView = (TextView) toolbar.findViewById(R.id.myNotifCountTextView);
        backButton = (ImageView) findViewById(R.id.back_img);

        toolbar_title_fragments.setText("News Feed");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        fab = (MovableFloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
//        backButton.setOnClickListener(this);

        //actionBarSettings();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        // bottomNavigationView.setItemIconTintList(null);
        disableShiftMode(bottomNavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                if (currentFragment instanceof NewsFragment) {
                                    return false;
                                } else {
                                    imageViewMyGames.setVisibility(View.GONE);
                                    myInvitationsRelativieLy.setVisibility(View.GONE);
                                    toolbar_title_fragments.setVisibility(View.VISIBLE);
                                    toolbar_title_Games.setVisibility(View.GONE);
                                    selectedFragment = NewsFragment.newInstance();
                                    toolbar_title_fragments.setText("News Feed");
                                    fragmentNavigation(selectedFragment);
                                }
                                break;
                            case R.id.action_item2:
                                selectedFrom = 0;
                                if (currentFragment instanceof GamesFragment) {
                                    return false;
                                } else {
                                    selectedFragment = GamesFragment.newInstance();
                                    imageViewMyGames.setVisibility(View.VISIBLE);
                                    myInvitationsRelativieLy.setVisibility(View.GONE);
                                    toolbar_title_Games.setVisibility(View.VISIBLE);
                                    toolbar_title_fragments.setVisibility(View.GONE);
                                    imageViewMyGames.setOnClickListener(FragmentsMainActivity.this);
                                    toolbar_title_Games.setText("Games");
                                    fragmentNavigation(selectedFragment);
                                }
                                break;

                            case R.id.action_item4:
                                if (currentFragment instanceof SettingsFragment) {
                                    return false;
                                } else {
                                    imageViewMyGames.setVisibility(View.GONE);
                                    myInvitationsRelativieLy.setVisibility(View.GONE);
                                    toolbar_title_fragments.setVisibility(View.VISIBLE);
                                    selectedFragment = SettingsFragment.newInstance();
                                    toolbar_title_Games.setVisibility(View.GONE);
                                    toolbar_title_fragments.setText("Settings");
                                    fragmentNavigation(selectedFragment);
                                }
                                break;
                        /*    case R.id.action_item3:
                                if (currentFragment instanceof EventsFragment) {
                                    return false;
                                } else {

                                    imageViewMyGames.setVisibility(View.GONE);
                                    myInvitationsRelativieLy.setVisibility(View.GONE);
                                    toolbar_title_fragments.setVisibility(View.VISIBLE);
                                    selectedFragment = EventsFragment.newInstance();
                                    toolbar_title_Games.setVisibility(View.GONE);
                                    toolbar_title_fragments.setText("Events");
                                    fragmentNavigation(selectedFragment);
                                }
                                break;
                            case R.id.action_item6:
                                if (currentFragment instanceof ServicesHomeFragment) {
                                    return false;
                                } else {
                                    imageViewMyGames.setVisibility(View.GONE);
                                    myInvitationsRelativieLy.setVisibility(View.GONE);
                                    toolbar_title_fragments.setVisibility(View.VISIBLE);
                                    selectedFragment = ServicesHomeFragment.newInstance();
                                    toolbar_title_Games.setVisibility(View.GONE);
                                    toolbar_title_fragments.setText("Services");
                                    fragmentNavigation(selectedFragment);
                                }
                                break;
*/
                            /*case R.id.action_item5:
                                selectedFrom=2;
                                if (currentFragment instanceof BookingsFragment) {
                                    return false;
                                }else {
                                    openDialog();
                                    return false;
                                    *//*myInvitationsRelativieLy.setVisibility(View.VISIBLE);
                                    myInvitationsRelativieLy.setOnClickListener(FragmentsMainActivity.this);
                                    //imageViewMyBookings.setImageResource(R.drawable.bell);
                                    imageViewMyGames.setVisibility(View.GONE);
                                    toolbar_title_fragments.setVisibility(View.VISIBLE);
                                    selectedFragment = BookingsFragment.newInstance();
                                    toolbar_title_Games.setVisibility(View.GONE);
                                    toolbar_title_fragments.setText("My Bookings");*//*
                                }
                                //break;*/
                        }
                        return true;
                    }
                });


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, NewsFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(FragmentsMainActivity.this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }

    private void isUserAvailableOrNot() {
        databaseReference.child("DMSSEmployees/" + id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    addUserData();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void addUserData() {
        String currentDate = "";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = null;
        SimpleDateFormat sdfTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            currentDate = sdf.format(c);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("EmpId", empID);
        hashMap.put("EmailId", userEmailID);
        hashMap.put("EmpName", empName);

        databaseReference.child("DMSSEmployees/" + id + "/" + currentDate).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }


    private void addUserLocation(Double lat, Double longitude) {
        Date c = Calendar.getInstance().getTime();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
            HashMap<String, Double> hashmapLatLong = new HashMap<>();
            hashmapLatLong.put("Latitude", lat);
            hashmapLatLong.put("Longitude", longitude);
            databaseReference.child("DMSSEmployees/" + id + "/" + sdf.format(c) + "/Time/" + sdfTime.format(c)).setValue(hashmapLatLong).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }


    }


    public void fragmentNavigation(Fragment selectedFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();

    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                FragmentsMainActivity.this);
        builder.setTitle("Booking functionality under development");
        // builder.setMessage("One Action Button Alert");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //item.setShiftingMode(false);
                //item.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }

/*    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }*/
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }else{
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                fm.popBackStack();
            }
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewMyGames:
                if (Utils.isNetworkAvailable(FragmentsMainActivity.this)) {
                    Intent i = new Intent(FragmentsMainActivity.this, MyGamesTabs.class);
                    startActivity(i);
                }
                break;
           /* case R.id.back_img:
                System.out.println("Back Main:: clicked");

                break;*/
            case R.id.fab:
               /* if (Utils.isNetworkAvailable(FragmentsMainActivity.this)) {
                    //openSportsApp();
                    Intent i = new Intent(FragmentsMainActivity.this, NominationsSelectionActivity.class);
                    startActivity(i);
                }*/
                if (Utils.isNetworkAvailable(FragmentsMainActivity.this)) {
                Intent i = new Intent(FragmentsMainActivity.this, MyGamesTabs.class);
                startActivity(i);
            }

                break;
            case R.id.myInvitationsRelativieLy:
                if (Utils.isNetworkAvailable(FragmentsMainActivity.this)) {
                    Intent i = new Intent(FragmentsMainActivity.this, BookingNotifications.class);
                    startActivity(i);
                }
                break;
        }
    }

    private void openSportsApp() {

        //final String appPackageName = getApplicationContext().getPackageName();
        String data = Utils.getSharedPrefs(FragmentsMainActivity.this);
        Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
        if (intent != null) {
            String details = "";
            String sessionToken = "";
            if (controller.getSportsFightDataString().length() > 0) {
                details = controller.getSportsFightDataString();
                sessionToken = controller.getSessionTokenSF();
            } else {
                details = controller.getUserProfileModel().getEmailID();
                sessionToken = "";
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Profile", details);
            intent.putExtra("email", controller.getUserProfileModel().getEmailID());
            intent.putExtra("SessionToken", sessionToken);
            startActivity(intent);

        } else {

            onGoToAnotherInAppStore(intent, appPackageName);

        }

    }

    public void onGoToAnotherInAppStore(Intent intent, String appPackageName) {
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + appPackageName));
            startActivity(intent);

        } catch (android.content.ActivityNotFoundException anfe) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
            startActivity(intent);

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        controller.setCallFromNotification(true);


    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(FragmentsMainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(FragmentsMainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                // setButtonsState(false);
                Snackbar.make(
                                findViewById(R.id.activity_main),
                                R.string.permission_denied_explanation,
                                Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
               if( Util.isNetworkAvailable(FragmentsMainActivity.this) ){
                    apiCall(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
                }

            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            /*setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));*/
        }
    }

    public void apiCall(String longitude, String latitude) {
        ApiInterface apiService =
                getClient().create(ApiInterface.class);

        Date c = Calendar.getInstance().getTime();
        String date = "";
        String time = "";
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
            date = sdf.format(c);
            //time = sdfTime.format(c);


        }
        String name = "";
        try {
            name = Build.MANUFACTURER + " " + Build.BRAND;
        } catch (Exception ignored) {
            name = "";

        }

        HashMap<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("EmpId", empID);
        jsonResponse.put("Email", userEmailID);
        jsonResponse.put("Date", date);
        jsonResponse.put("DateTime", date);
        jsonResponse.put("longitude", longitude);
        jsonResponse.put("latitude", latitude);
        jsonResponse.put("Device", name);

        Call<ResponseBody> call = apiService.postLocationLatLong(jsonResponse);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "Success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    public void checkPermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mService.requestLocationUpdates();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FragmentsMainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}

