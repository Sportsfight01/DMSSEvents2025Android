package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsEventsBaseActivity;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sandeep.kumar on 07-03-2017.
 */
public class OTPVerification extends DmsEventsBaseActivity implements WebServiceResponseCallBack {
    TextView btnSubmit, extEmailId, btnCancel;
    EditText editTextPassword, edtxtConfirmPassword, edtxtOTP;
    DmsEventsAppController controller;
    String otpValue;
    String password, confirmPassword;
    ProgressDialog progressDialog;
    androidx.appcompat.app.ActionBar actionBar;
    Toolbar toolbar;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verfication);
        //actionBarSettings();
        initializeUIElements();
    }


    @Override
    public void initializeUIElements() {
        progressDialog = new ProgressDialog(OTPVerification.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Create Password");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        controller = (DmsEventsAppController) getApplicationContext();
        btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        btnCancel = (TextView) findViewById(R.id.btnCancel);
        extEmailId = (TextView) findViewById(R.id.extEmailId);
        editTextPassword = (EditText) findViewById(R.id.edtxtPassword);
        edtxtConfirmPassword = (EditText) findViewById(R.id.edtxtConfirmPassword);
        edtxtOTP = (EditText) findViewById(R.id.edtxtOTP);
        extEmailId.setText(controller.getEmailID());
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        edtxtConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(btnSubmit);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                progressDialog.show();
                otpValue = edtxtOTP.getText().toString();
                password = editTextPassword.getText().toString();
                confirmPassword = edtxtConfirmPassword.getText().toString();
                if (validateInputFields()) {
                    callWebApiOnOTPVerification();
                }else{
                    progressDialog.cancel();
                }
                break;
            case R.id.btnCancel:
                Intent i=new Intent(OTPVerification.this,MailValidationActivity.class);
                startActivity(i);
                finish();
                break;
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
        actionBarHeadingTextView.setText("Create Password");
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OTPVerification.this,MailValidationActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public boolean validateInputFields() {
        if (controller.getValidation().isNotNull(otpValue)) {
            if (controller.getValidation().isNotNull(password)) {
                if (controller.getValidation().isNotNull(confirmPassword)) {
                    if (password.equals(confirmPassword)) {
                        return true;
                    } else {
                        Toast.makeText(OTPVerification.this, "New Password and Confirm Password should be equal", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OTPVerification.this, "Confirm Password should not be empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(OTPVerification.this, "New Password should not be empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(OTPVerification.this, "OTP field should not be empty", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void callWebApiOnOTPVerification() {
        /**
         * Checking whether the network is available or not
         */
        if (Utils.isNetworkAvailable(OTPVerification.this)) {
            controller.getWebService().postData(ConstantKeys.createPasswordUrl, getCreatePasswordJsonData(), this);
        }else {
            progressDialog.cancel();
        }
    }

    public String getCreatePasswordJsonData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", controller.getEmailID());
            jsonObject.put("Password", password);
            jsonObject.put("OTP", otpValue);
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
                if (status) {
                    Intent i = new Intent(OTPVerification.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    Utils.showToast(OTPVerification.this, jsonObject.getString(ConstantKeys.messageKey));
                } else {
                    Utils.showToast(OTPVerification.this, jsonObject.getString(ConstantKeys.messageKey));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        progressDialog.cancel();
    }

    @Override
    public void onServiceCallFail(String error) {
        if (error != null) {
            Utils.showToast(OTPVerification.this, error);
        } else {
            Utils.showToast(OTPVerification.this, "Network Error");
        }
        progressDialog.cancel();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
