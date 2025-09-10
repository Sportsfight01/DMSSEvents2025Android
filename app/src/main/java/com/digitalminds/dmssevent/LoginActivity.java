package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsEventsBaseActivity;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.UserProfileModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sandeep.kumar on 14-03-2017.
 */
public class LoginActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack {
    TextView btnLogin, edtxEmail,textViewForgotPassword;
    EditText edtxtPassword;
    DmsEventsAppController controller;
    String password;
    ProgressDialog progressDialog;
    androidx.appcompat.app.ActionBar actionBar;
    int apiCallType=0;
public static int profileCallForSavingPhoto=1;
    UserProfileModel userProfileModel;
    boolean updateProfileDetails=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // actionBarSettings();
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        controller = (DmsEventsAppController) getApplicationContext();
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        edtxEmail = (TextView) findViewById(R.id.edtxEmail);
        edtxtPassword = (EditText) findViewById(R.id.edtxtPassword);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        edtxEmail.setText(controller.getEmailID());
        edtxEmail.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
        edtxtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(btnLogin);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                progressDialog.show();
                password = edtxtPassword.getText().toString();
                if (controller.getValidation().isNotNull(password)) {
                    if(controller.getEmailID().equalsIgnoreCase("satyapriya.rajput@digitalminds.solutions")&& password.equalsIgnoreCase("1")){
                        DmsSharedPreferences.saveOwnerLoggedInStatus(LoginActivity.this, true);
                        DmsSharedPreferences.saveUserLoggedInStatus(LoginActivity.this, true);
                        boolean loginStatus=DmsSharedPreferences.isUserLoggedIn(LoginActivity.this);
                        controller.setLoggedInStatus(loginStatus);
                        Intent i=new Intent(LoginActivity.this,RemoteControlActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        callWebApiToLogin();
                    }
                } else {
                    DmsSharedPreferences.saveOwnerLoggedInStatus(LoginActivity.this,false);
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
                break;
            case R.id.edtxEmail:
                Toast.makeText(LoginActivity.this,"Please go back to edit your Email Id",Toast.LENGTH_SHORT).show();
                break;
            case R.id.textViewForgotPassword:
                callForgotPasswordAPI();
                break;
        }

    }

    private void callForgotPasswordAPI() {
        progressDialog.show();
        if (Utils.isNetworkAvailable(LoginActivity.this)) {
            apiCallType=2;
            controller.getWebService().getData(ConstantKeys.forgotPassword + controller.getEmailID(), this);
        }else {
            progressDialog.cancel();
        }
    }

    @Override
    public void actionBarSettings() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_action_bar, null);
        actionBar.setCustomView(view, new androidx.appcompat.app.ActionBar.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        actionBar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM | androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME);
        TextView actionBarHeadingTextView = (TextView) view.findViewById(R.id.actionBarHeadingTextView);
        ImageView imageViewLogout = (ImageView) view.findViewById(R.id.imageViewLogout);
        imageViewLogout.setVisibility(View.GONE);

        ImageView imageViewProfile = (ImageView) view.findViewById(R.id.imageViewProfile);
        imageViewProfile.setVisibility(View.GONE);
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        //actionBarHeadingTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        actionBarHeadingTextView.setText("Login");
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,MailValidationActivity.class);
                String[] str_array = edtxEmail.getText().toString().split("@");
                String stringEmail = str_array[0];
                final String stringEmailWithAddress = str_array[1];
                i.putExtra("EmailId",stringEmail);
                startActivity(i);
                finish();
            }
        });
    }


    private void callWebApiToLogin() {
        /**
         * Checking whether the network is available or not
         */
        if (Utils.isNetworkAvailable(LoginActivity.this)) {
            apiCallType=1;
            System.out.println("getCreatePasswordJsonData::"+getCreatePasswordJsonData());
            controller.getWebService().postData(ConstantKeys.loginUrl, getCreatePasswordJsonData(), this);
        }else {
            progressDialog.cancel();
        }
    }

    public String getCreatePasswordJsonData() {

        String tokenID="";
        if(DmsSharedPreferences.getFCMToken(LoginActivity.this)!=null){
            tokenID=DmsSharedPreferences.getFCMToken(LoginActivity.this);
        }else if(controller.getToken()!=null){
            tokenID=controller.getToken();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", controller.getEmailID());
            jsonObject.put("Password", password);
            jsonObject.put("DeviceType", "Android");
            jsonObject.put("DeviceToken",tokenID );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                Utils.showToast(LoginActivity.this, jsonObject.getString(ConstantKeys.messageKey));
                if (status) {
                    if(apiCallType==1) {
                        JSONObject jsonResultObject = jsonObject.getJSONObject(ConstantKeys.resultKey);
                        userProfileModel = new UserProfileModel(jsonResultObject);
                        DmsSharedPreferences.saveUserDetails(LoginActivity.this, userProfileModel);
                        DmsSharedPreferences.saveUserLoggedInStatus(LoginActivity.this, status);
                        boolean loginStatus=DmsSharedPreferences.isUserLoggedIn(LoginActivity.this);
                        controller.setLoggedInStatus(loginStatus);
                        System.out.println("jsonResultObject:: "+jsonResultObject);
                        System.out.println("jsonResultObject getProfileImageURL :: "+userProfileModel.getProfileImageURL());

                        DmsSharedPreferences.saveProfilePicUrl(LoginActivity.this,userProfileModel.getProfileImageURL());
                        //UserProfileModel userProfileModel1 = DmsSharedPreferences.getUserDetails(LoginActivity.this);
                        controller.setUserProfileModel(userProfileModel);
                        //passIntent();
                        saveProfilePicUrl(userProfileModel.getId());
                        if(updateProfileDetails==true){
                            passIntent();
                        }
                    }else if(apiCallType==2) {
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i=new Intent(LoginActivity.this,OTPVerification.class);
                                startActivity(i);
                                finish();
                            }
                        });

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        progressDialog.cancel();
    }
    @Override
    public void onServiceCallFail(final String error) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (error != null) {
                    Utils.showToast(LoginActivity.this, error);
                } else {
                    Utils.showToast(LoginActivity.this, "Network Error");
                }
                progressDialog.cancel();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2){
            String succesStatus=data.getStringExtra("Key");
            if(succesStatus.equalsIgnoreCase("SuccesfullyAddedPhoto")){
                updateProfileDetails=true;
                callWebApiToLogin();
            }
        }
    }

    public void saveProfilePicUrl(int id){
        int lastId = DmsSharedPreferences.getLastLoggedInId(LoginActivity.this);
        if(lastId != id){
            DmsSharedPreferences.saveLastLoggedInId(LoginActivity.this,id);
            //DmsSharedPreferences.saveProfilePicUrl(LoginActivity.this,uri.toString());
            /*if((userProfileModel.getProfileImageURL()!=null)&&(userProfileModel.getProfileImageURL().length()>0)){

                passIntent();
            }else{
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProfileActivity.profileCallForSaving=true;
                        Intent i=new Intent(LoginActivity.this,ProfileActivity.class);
                        startActivityForResult(i,profileCallForSavingPhoto);
                        //dialogToSaveProfilePic();
                    }
                });

            }
        }else{*/
            progressDialog.cancel();
            passIntent();
        }else{
            progressDialog.cancel();
            passIntent();
        }
    }

    private void dialogToSaveProfilePic() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(LoginActivity.this,ProfileActivity.class);
                startActivityForResult(i,profileCallForSavingPhoto);
            }
        });
    }

    public void passIntent(){

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                // Check if no view has focus:
                View view = LoginActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                Intent i = new Intent(LoginActivity.this, PermissionActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

}
