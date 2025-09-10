package com.digitalminds;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.digitalminds.dmssevent.FragmentsMainActivity;
import com.digitalminds.dmssevent.MailValidationActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.RemoteControlActivity;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.SportsFightUserModel;
import com.digitalminds.dmssevent.models.UserProfileModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashActivitySportsFight extends Activity implements WebServiceResponseCallBack {
    String loginStatus_SF="";
    SportsFightUserModel sportsFightUserModel;
    DmsEventsAppController dmsEventsAppController;
    int apiCallType=0;
    private ProgressBar progressBar;
    UserProfileModel userProfileModelSplash;
    String sessionToken="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_main);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_cyclic);
        dmsEventsAppController=(DmsEventsAppController)getApplicationContext();
        getSportsFightDetails();
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (DmsSharedPreferences.isUserLoggedIn(SplashActivitySportsFight.this)&&(!loginStatus_SF.equalsIgnoreCase("SportsFight"))) {
                        boolean status = DmsSharedPreferences.isOwnerLoggedIn(SplashActivitySportsFight.this);
                        Intent intent;
                        if (status) {
                            intent = new Intent(SplashActivitySportsFight.this, RemoteControlActivity.class);
                        } else {
                            intent = new Intent(SplashActivitySportsFight.this, FragmentsMainActivity.class);
                        }
                        startActivity(intent);
                        finish();

                    } else if((DmsSharedPreferences.isUserLoggedIn(SplashActivitySportsFight.this)==false)&&(loginStatus_SF.equalsIgnoreCase("SportsFight"))){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callWebApiForSportsLogin();
                                // Stuff that updates the UI

                            }
                        });
                        //Intent intent = new Intent(SplashActivity.this, MailValidationActivity.class);
                        // startActivity(intent);
                    }else if((DmsSharedPreferences.isUserLoggedIn(SplashActivitySportsFight.this))&&(loginStatus_SF.equalsIgnoreCase("SportsFight"))){
                        if(dmsEventsAppController.getSportsFightUserModel().getEmail().equalsIgnoreCase(DmsSharedPreferences.getUserDetails(SplashActivitySportsFight.this).getEmailID())){
                            Intent intent = new Intent(SplashActivitySportsFight.this, FragmentsMainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            DmsSharedPreferences.saveUserLoggedInStatus(SplashActivitySportsFight.this, false);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callWebApiForSportsLogin();
                                }
                            });
                        }

                    }else{
                        Intent intent = new Intent(SplashActivitySportsFight.this, MailValidationActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();
    }

    private void callWebApiForSportsLogin() {
        if (Utils.isNetworkAvailable(SplashActivitySportsFight.this)) {
            apiCallType=1;
            progressBar.setVisibility(View.VISIBLE);
            dmsEventsAppController.getWebService().postData(ConstantKeys.sportsFightloginUrl, getJsonData(), this);
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private String getJsonData() {
        String tokenID="";
        if(DmsSharedPreferences.getFCMToken(SplashActivitySportsFight.this)!=null){
            tokenID=DmsSharedPreferences.getFCMToken(SplashActivitySportsFight.this);
        }else if(dmsEventsAppController.getToken()!=null){
            tokenID=dmsEventsAppController.getToken();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", dmsEventsAppController.getSportsFightUserModel().getEmail());
            jsonObject.put("SFId", dmsEventsAppController.getSportsFightUserModel().getUserId());
            jsonObject.put("DeviceType", "Android");
            jsonObject.put("DeviceToken",tokenID );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
    private void getSportsFightDetails() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    void handleSendText(Intent intent) {
        Bundle loginStatus_SF_bundle=intent.getExtras();
        String sharedText=loginStatus_SF_bundle.getString("Profile");
        dmsEventsAppController.setSportsFightDataString(sharedText);
        loginStatus_SF=loginStatus_SF_bundle.getString("SportsFight");
        sessionToken=loginStatus_SF_bundle.getString("SessionToken");
        dmsEventsAppController.setSessionTokenSF(sessionToken);
        //String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            try {

                JSONObject obj = new JSONObject(sharedText);
                String sportsFightEmail=obj.getString("email");
                String sportsFightUserId=obj.getString("userId");
                sportsFightUserModel=new SportsFightUserModel(obj);
                dmsEventsAppController.setSportsFightUserModel(sportsFightUserModel);
                Log.d("My App", obj.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + sharedText + "\"");
            }
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                // Utils.showToast(LoginActivity.this, jsonObject.getString(ConstantKeys.messageKey));
                if (status) {
                    if(apiCallType==1) {
                        JSONObject jsonResultObject = jsonObject.getJSONObject(ConstantKeys.resultKey);
                        userProfileModelSplash = new UserProfileModel(jsonResultObject);
                        DmsSharedPreferences.saveUserDetails(SplashActivitySportsFight.this, userProfileModelSplash);
                        DmsSharedPreferences.saveUserLoggedInStatus(SplashActivitySportsFight.this, status);
                        DmsSharedPreferences.saveProfilePicUrl(SplashActivitySportsFight.this,userProfileModelSplash.getProfileImageURL());
                        //UserProfileModel userProfileModel1 = DmsSharedPreferences.getUserDetails(LoginActivity.this);
                        dmsEventsAppController.setUserProfileModel(userProfileModelSplash);
                        passIntent();
                        //saveProfilePicUrl(userProfileModelSplash.getId());

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void passIntent() {
        Intent intent = new Intent(SplashActivitySportsFight.this, FragmentsMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onServiceCallFail(final String error) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (error != null) {
                    if(loginStatus_SF.equalsIgnoreCase("SportsFight")){
                        Intent intent = new Intent(SplashActivitySportsFight.this, MailValidationActivity.class);
                        startActivity(intent);
                    }else{
                        Utils.showToast(SplashActivitySportsFight.this, error);
                    }
                } else {
                    Utils.showToast(SplashActivitySportsFight.this, "Network Error");
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}

