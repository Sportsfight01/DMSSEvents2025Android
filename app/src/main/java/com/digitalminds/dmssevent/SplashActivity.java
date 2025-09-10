package com.digitalminds.dmssevent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;


/**
 * Created by sandeep.kumar on 14-03-2017.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_main);
        getVersionCode();
        openSplash();
    }

    private void openSplash() {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (DmsSharedPreferences.isUserLoggedIn(SplashActivity.this)) {
                        boolean status = DmsSharedPreferences.isOwnerLoggedIn(SplashActivity.this);
                        Intent intent;
                        if (status) {
                            intent = new Intent(SplashActivity.this, RemoteControlActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, PermissionActivity.class);
                            // intent = new Intent(SplashActivity.this, BookMyGameActivity.class);
                        }
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent = new Intent(SplashActivity.this, MailValidationActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();

    }

    public void getVersionCode() {
        try {
            PackageInfo pInfo = SplashActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            ConstantKeys.versionCode = "Version  " + pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);


    }

}
