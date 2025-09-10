package com.digitalminds.dmssevent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * Created by sandeep.kumar on 07-03-2017.
 */
public class MailValidationActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack {
    TextView buttonNext, txtEmailExt, versionTextView;
    EditText edtxtEmailId;
    DmsEventsAppController controller;
    String emailId;
    ProgressDialog progressDialog;
    LinearLayout linearLyEmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_validation);
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        progressDialog = new ProgressDialog(MailValidationActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        controller = (DmsEventsAppController) getApplicationContext();
        buttonNext = (TextView) findViewById(R.id.buttonNext);
        versionTextView = (TextView) findViewById(R.id.versionTextView);
        linearLyEmailID = (LinearLayout) findViewById(R.id.linearLyEmailID);
        versionTextView.setText(ConstantKeys.versionCode);
        edtxtEmailId = (EditText) findViewById(R.id.edtxtEmailId);
        edtxtEmailId.setText("balaswamy.madanu@digitalminds.solutions");

        //if(DmsSharedPreferences.isUserLoggedIn(MailValidationActivity.this)){
        if (DmsSharedPreferences.getUserDetails(this) != null) {
            if (DmsSharedPreferences.getUserDetails(this).getEmailID().length() > 0) {
                String[] emailId = DmsSharedPreferences.getUserDetails(this).getEmailID().split("@");
                String email = emailId[0];
                edtxtEmailId.setText(email);
                int textLength = edtxtEmailId.getText().length();
                edtxtEmailId.setSelection(textLength, textLength);
            }
        }
        linearLyEmailID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtxtEmailId.requestFocus();
            }
        });
        //}
        txtEmailExt = (TextView) findViewById(R.id.txtEmailExt);
        buttonNext.setOnClickListener(this);
        edtxtEmailId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(buttonNext);
                }
                return false;
            }
        });
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            String emailFromLogin = (String) b.get("EmailId");
            edtxtEmailId.setText(emailFromLogin);
        }
    }
//new
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonNext:

                if(edtxtEmailId.getText().toString().trim().length()>3){
                    progressDialog.show();
                    emailId = edtxtEmailId.getText().toString().trim() ;
                    callWebApiForMailValidation();
                }else{
                    Toast.makeText(MailValidationActivity.this,"Please enter your Email Id",Toast.LENGTH_SHORT).show();
                }

              //  if (controller.getValidation().validateEmail(emailId, MailValidationActivity.this)) {

                /*} else {
                    progressDialog.cancel();
                }*/
                break;
        }
    }

    @Override
    public void actionBarSettings() {

    }

    private void callWebApiForMailValidation() {
        /**
         * Checking whether the network is available or not
         */
        if (Utils.isNetworkAvailable(MailValidationActivity.this)) {
            String url = ConstantKeys.checkUserUrl + "?EmailId=" + emailId;
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
        }
    }

    @Override
    public void onServiceCallSuccess(String result) {
        System.out.println("result:: "+result);

        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                if (status) {
                    JSONObject resultObject = jsonObject.getJSONObject(ConstantKeys.resultKey);
                    controller.setEmailID(emailId);
                    UserProfileModel userProfileModel = new UserProfileModel(resultObject);
                    DmsSharedPreferences.saveUserDetails(MailValidationActivity.this, userProfileModel);
                    boolean isPasswordSet = resultObject.isNull(ConstantKeys.isPasswordSet) ? false : resultObject.getBoolean(ConstantKeys.isPasswordSet);
                    if (!isPasswordSet) {
                        Intent i = new Intent(MailValidationActivity.this, OTPVerification.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(MailValidationActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Utils.showToast(MailValidationActivity.this, jsonObject.getString(ConstantKeys.messageKey));
                    /*Hiding Keyboard */
                    hideSoftKeyboard(this);

                    /*InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String test = result;
        }
        progressDialog.cancel();
    }

    public void hideSoftKeyboard(Activity activity){
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        /*
         * If no view is focused, an NPE will be thrown
         *
         * Maxim Dmitriev
         */
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onServiceCallFail(String error) {
        if (error != null) {
            Utils.showToast(MailValidationActivity.this, error);
        } else {
            Utils.showToast(MailValidationActivity.this, "Network Error");
        }
        progressDialog.cancel();
    }
}
