package com.digitalminds.dmssevent.firebase;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.digitalminds.dmssevent.backendservice.WebService;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import org.json.JSONObject;


/**
 * Created by Sandeep.Kumar on 02-02-2018.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
   // AppController controller;
   public String token=null;
   DmsEventsAppController controller;
   Context context;

    @Override
    public void onTokenRefresh() {
        System.out.println("Firebase Token onTokenRefresh::");

        String token = FirebaseInstanceId.getInstance().getToken();
        controller=(DmsEventsAppController)getApplicationContext();
        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());
        System.out.println("Firebase Token::");
        controller.setToken(token);


       /* SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.dmsSharedEventsPreference), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_fcm_token_id),token);
        editor.commit();*/
        DmsSharedPreferences.saveFCMToken(getApplicationContext(),token);
        this.token=token;
        registerToken(token);



    }

    private void registerToken(final String token) {
        final WebService service = new WebService(getApplicationContext());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = service.postData(ConstantKeys.registerDevice, getRegistrationJson(token).toString());
                if ((response != null) && (response.length() > 0) && (response.equalsIgnoreCase("true"))) {
                    showToast() ;
                }

            }
        });
        t.start();
        // Common.FCMToken=token;
        //controller.getPrefManager().setFcmToken(token);
    }
public void showToast(){
    Handler handler = new Handler(Looper.getMainLooper());

    handler.post(new Runnable() {

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "Device Registered Sucessfully on Server", Toast.LENGTH_SHORT).show();
        }
    });
}
    public JSONObject getRegistrationJson(String token) {
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(getApplicationContext().
                TELEPHONY_SERVICE);
        JSONObject jsonObject = new JSONObject();
        System.out.println("controller.getEmailID():: "+controller.getEmailID());
        try {
           // jsonObject.put("DeviceType", "Android");
            jsonObject.put("DeviceToken", token);
            jsonObject.put("Email", controller.getEmailID());
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject;
    }
}

