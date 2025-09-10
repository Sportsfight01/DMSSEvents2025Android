package com.digitalminds.dmssevent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.adapters.CustomSpinnerAdapter;
import com.digitalminds.dmssevent.adapters.CustomSpinnerEmploeeAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.DepartmentListModel;
import com.digitalminds.dmssevent.models.EmployeeListBasedOnDepartModel;
import com.digitalminds.dmssevent.models.NominationAwardsModel;
import com.digitalminds.dmssevent.models.NominationListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NominateActivity extends AppCompatActivity implements View.OnClickListener,WebServiceResponseCallBack {
    Toolbar toolbar;
    TextView toolbar_title,retryTextView;
    TextView nominateDetailsTextView;
    TextView nominateHeadingTextView, nominateAmountTextView, nominatePointsTextView, cancelTextView, nominateTextView;
    EditText commentsEditText;
    Spinner deptSpinner, employeeNameSpinner;
    DmsEventsAppController appController;
    ProgressDialog progressDialog;
    LinearLayout emptyElement,lineaLyTotalView,linearLyNomineeLayout;
    ArrayList<DepartmentListModel> departmentListModelArrayList=new ArrayList<DepartmentListModel>();
    CustomSpinnerAdapter customSpinnerAdapter;
    int getDepartID,selectedDepartPosition,selectedEmloyeePosition,getEmloyeeIdNominatedBy;
    int apiCallType;
    NominationAwardsModel nominationAwardsModel;
    ArrayList<EmployeeListBasedOnDepartModel> employeeListBasedOnDepartmentIDArrayList=new ArrayList<EmployeeListBasedOnDepartModel>();
    ArrayList<NominationListModel> nominationListModelArrayList=new ArrayList<NominationListModel>();
    CustomSpinnerEmploeeAdapter customSpinnerEmploeeAdapter;
    public static int selectedNominationPosition;
    public static int selectedNominationCount;
    RelativeLayout spinnerRelativeLy;
    String commentsTextValue;
    int valueForDepartID,valueForEmployeeID;

    public static boolean isDmssLeader = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominate);
        appController = (DmsEventsAppController) getApplicationContext();
        nominationAwardsModel=appController.getSelectedNominationCriteria();
        progressDialog = new ProgressDialog(NominateActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar_title.setText("Nominate Employee");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        emptyElement = (LinearLayout) findViewById(R.id.emptyElementNominate);
        linearLyNomineeLayout = (LinearLayout) findViewById(R.id.linearLyNomineeLayout);
        lineaLyTotalView = (LinearLayout) findViewById(R.id.lineaLyTotalView);
        spinnerRelativeLy = (RelativeLayout) findViewById(R.id.spinnerRelativeLy);
        retryTextView = (TextView) findViewById(R.id.retryTextView);
        nominateHeadingTextView = (TextView) findViewById(R.id.nominateHeadingTextView);
        nominateAmountTextView = (TextView) findViewById(R.id.nominateAmountTextView);
        nominateDetailsTextView = (TextView) findViewById(R.id.nominateDetailsTextView);
        nominatePointsTextView = (TextView) findViewById(R.id.nominatePointsTextView);
        cancelTextView = (TextView) findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(this);
        nominateTextView = (TextView) findViewById(R.id.nominateTextView);
        nominateTextView.setOnClickListener(this);
        commentsEditText = (EditText) findViewById(R.id.commentsEditText);
        deptSpinner = (Spinner) findViewById(R.id.deptSpinner);
        //deptSpinner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        //deptSpinner.setPrompt("Select Department");

        employeeNameSpinner = (Spinner) findViewById(R.id.employeeNameSpinner);
        //employeeNameSpinner.setDropdownHeight(WindowManager.LayoutParams.WRAP_CONTENT);
       // employeeNameSpinner.setDropdownMaxHeight(getResources().getDimension(R.dimen.spinner_max_height));
        nominateHeadingTextView.setText(nominationAwardsModel.getAwardName());
        nominateAmountTextView.setText(nominationAwardsModel.getReward());
        nominateDetailsTextView.setText(nominationAwardsModel.getDescription());
        nominatePointsTextView.setText(nominationAwardsModel.getCriteria());
        if(nominationAwardsModel.getId()==9){
            toolbar_title.setText("Nominate Department");
            spinnerRelativeLy.setVisibility(View.GONE);
            getEmloyeeIdNominatedBy=0;
        }else{
            toolbar_title.setText("Nominate Employee");
            spinnerRelativeLy.setVisibility(View.VISIBLE);
        }
        if(selectedNominationCount==0){
            deptSpinner.setClickable(false);
            employeeNameSpinner.setClickable(false);
            commentsEditText.setClickable(false);
            commentsEditText.setCursorVisible(false);
            commentsEditText.setEnabled(false);
            nominateTextView.setClickable(false);
            linearLyNomineeLayout.setAlpha(0.4f);
            linearLyNomineeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NominateActivity.this, "You cannot apply more than 2 Nominee's", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            commentsEditText.setClickable(true);
            linearLyNomineeLayout.setAlpha(1f);
            deptSpinner.setClickable(true);
            commentsEditText.setEnabled(true);
            commentsEditText.setCursorVisible(false);
            employeeNameSpinner.setClickable(true);
            nominateTextView.setClickable(true);
            commentsEditText.setOnClickListener(editTextClickListener);

            callWebApiToGetDepartmentList();
            deptSpinner.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(NominateActivity.this);
                    return false;
                }
            });
            deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i != 0) {
                        //  selectedDepartPosition = deptSpinner.getSelectedItemPosition();
                        getDepartID = departmentListModelArrayList.get(i).getId();
                        //getDepartID = departmentListModelArrayList.get(i).getId();
                        employeeListBasedOnDepartmentIDArrayList.clear();
                        customSpinnerEmploeeAdapter.notifyDataSetChanged();
                        EmployeeListBasedOnDepartModel employeeSelectString = new EmployeeListBasedOnDepartModel("Select Employee");
                        employeeListBasedOnDepartmentIDArrayList.add(employeeSelectString);
                        customSpinnerEmploeeAdapter = new CustomSpinnerEmploeeAdapter(NominateActivity.this, employeeListBasedOnDepartmentIDArrayList);
                        employeeNameSpinner.setAdapter(customSpinnerEmploeeAdapter);
                        callWebApiToGetEmployeeDetailsByDepartID();
                    } else if (i == 0) {
                        getDepartID = departmentListModelArrayList.get(i).getId();
                        if (customSpinnerEmploeeAdapter != null) {
                            employeeListBasedOnDepartmentIDArrayList.clear();
                            customSpinnerEmploeeAdapter.notifyDataSetChanged();
                            EmployeeListBasedOnDepartModel employeeSelectString = new EmployeeListBasedOnDepartModel("Select Employee");
                            employeeListBasedOnDepartmentIDArrayList.add(employeeSelectString);
                            customSpinnerEmploeeAdapter = new CustomSpinnerEmploeeAdapter(NominateActivity.this, employeeListBasedOnDepartmentIDArrayList);
                            employeeNameSpinner.setAdapter(customSpinnerEmploeeAdapter);
                        }else if(customSpinnerEmploeeAdapter == null){
                            EmployeeListBasedOnDepartModel employeeSelectString = new EmployeeListBasedOnDepartModel("Select Employee");
                            employeeListBasedOnDepartmentIDArrayList.add(employeeSelectString);
                            customSpinnerEmploeeAdapter = new CustomSpinnerEmploeeAdapter(NominateActivity.this, employeeListBasedOnDepartmentIDArrayList);
                            employeeNameSpinner.setAdapter(customSpinnerEmploeeAdapter);
                        }

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            employeeNameSpinner.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(NominateActivity.this);
                    return false;
                }
            });


            employeeNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i != 0) {
                        //  selectedDepartPosition = deptSpinner.getSelectedItemPosition();
                        getEmloyeeIdNominatedBy = employeeListBasedOnDepartmentIDArrayList.get(i).getId();

                    } else if (i == 0) {
                        getEmloyeeIdNominatedBy =employeeListBasedOnDepartmentIDArrayList.get(i).getId();
                        if (customSpinnerEmploeeAdapter != null) {
                           /* employeeListBasedOnDepartmentIDArrayList.clear();
                            EmployeeListBasedOnDepartModel employeeSelectString = new EmployeeListBasedOnDepartModel("Select Employee");
                            employeeListBasedOnDepartmentIDArrayList.add(employeeSelectString);
                            customSpinnerEmploeeAdapter = new CustomSpinnerEmploeeAdapter(NominateActivity.this, employeeListBasedOnDepartmentIDArrayList);
                            employeeNameSpinner.setAdapter(customSpinnerEmploeeAdapter);*/
                            customSpinnerEmploeeAdapter.notifyDataSetChanged();
                        }else if(customSpinnerEmploeeAdapter == null){
                            employeeListBasedOnDepartmentIDArrayList.clear();
                            EmployeeListBasedOnDepartModel employeeSelectString = new EmployeeListBasedOnDepartModel("Select Employee");
                            employeeListBasedOnDepartmentIDArrayList.add(employeeSelectString);
                            customSpinnerEmploeeAdapter = new CustomSpinnerEmploeeAdapter(NominateActivity.this, employeeListBasedOnDepartmentIDArrayList);
                            employeeNameSpinner.setAdapter(customSpinnerEmploeeAdapter);
                            customSpinnerEmploeeAdapter.notifyDataSetChanged();
                        }
                    }

                    //selectedEmloyeePosition = deptSpinner.getSelectedItemPosition();

                    //callWebApiToGetEmployeeDetailsByDepartID();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


    }

    private void callWebApiToGetDepartmentList() {
        if (Utils.isNetworkAvailable(NominateActivity.this)) {
            progressDialog.show();
            apiCallType=1;
            lineaLyTotalView.setVisibility(View.VISIBLE);
            emptyElement.setVisibility(View.GONE);
            String url= ConstantKeys.getDepartmentList;
            appController.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            lineaLyTotalView.setVisibility(View.GONE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiToGetDepartmentList();
                }
            });

        }
    }
    private void callWebApiToGetEmployeeDetailsByDepartID() {
        if (Utils.isNetworkAvailable(NominateActivity.this)) {
            apiCallType=2;
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            lineaLyTotalView.setVisibility(View.VISIBLE);
           // String url = "http://192.168.60.173/DMSSMobileApp.WebApi/api/nomine/getemp"+"/"+getDepartID;


           String url= ConstantKeys.getEmployeeListBasedOnDepart+"/"+getDepartID;
            appController.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            lineaLyTotalView.setVisibility(View.GONE);
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiToGetEmployeeDetailsByDepartID();
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nominateTextView:
                commentsTextValue=commentsEditText.getText().toString().trim();
                if(getDepartID!=-1){
                if(getEmloyeeIdNominatedBy!=-1) {
                    if (commentsTextValue.length() > 0) {
                        if (commentsTextValue.length() >=40){
                            dialogAlertForSendNominee();
                        }else {
                            Toast.makeText(NominateActivity.this, "Please Enter Minimum 40 Characters", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(NominateActivity.this, "Please Add Comments", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(NominateActivity.this, "Please Select Employee", Toast.LENGTH_SHORT).show();
                }
                }else{
                    Toast.makeText(NominateActivity.this, "Please Select Department", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancelTextView:
                hideKeyboard(NominateActivity.this);
                finish();
                break;
        }
    }

    private void dialogAlertForSendNominee() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NominateActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Nominee...");
        if(nominationAwardsModel.getId()==9) {
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to nominate this department?");
        }else{
            alertDialog.setMessage("Are you sure you want to nominate this employee?");
        }
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.icon_nominatefab);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                callWebApiToNominatEmployee();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentsEditText.getWindowToken(), 0);
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void callWebApiToNominatEmployee() {
        if (Utils.isNetworkAvailable(NominateActivity.this)) {
            progressDialog.show();
            apiCallType=3;
            //nomine/addMileStoneNominees
           //String url = "http://192.168.60.173/DMSSMobileApp.WebApi/api/nomine/addMileStoneNominees";
            String url= ConstantKeys.addNomineeUrl;
            appController.getWebService().postData(url,getJsonDataToNominateEmployee(), this);
        } else {
            progressDialog.cancel();
        }
    }
    private String getJsonDataToNominateEmployee() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("NominatedBy", DmsSharedPreferences.getUserDetails(NominateActivity.this).getId());
            jsonObject.put("NominatedTo",getEmloyeeIdNominatedBy);
            jsonObject.put("DepartmentId",getDepartID);
            jsonObject.put("AwardId",nominationAwardsModel.getId());
            jsonObject.put("NominatedComment",commentsTextValue);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonObject.toString();
    }
    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                final boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                if (status) {
                    if(apiCallType==1) {
                        departmentListModelArrayList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                        DepartmentListModel departmentListModelSelectString = new DepartmentListModel("Select Department");
                        departmentListModelArrayList.add(departmentListModelSelectString);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            DepartmentListModel departmentListModel = new DepartmentListModel(jsonArray.getJSONObject(i));

                            if (nominationAwardsModel.getId() == 8) {
                                isDmssLeader = true;
                                if (departmentListModel.getId() == 14 || departmentListModel.getDeptName().equalsIgnoreCase("DMSS Leaders")) {
                                    departmentListModelArrayList.add(departmentListModel);
                                }else {
                                    departmentListModelArrayList.remove(departmentListModel);
                                }
                            }else {
                                isDmssLeader = false;
                                if (departmentListModel.getId() == 14 || departmentListModel.getDeptName().equalsIgnoreCase("DMSS Leaders"))
                                    departmentListModelArrayList.remove(departmentListModel);
                                else {
                                    departmentListModelArrayList.add(departmentListModel);
                                }
                            }
                        }
                        this.runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (customSpinnerAdapter == null) {
                                    customSpinnerAdapter = new CustomSpinnerAdapter(NominateActivity.this, departmentListModelArrayList);
                                    deptSpinner.setAdapter(customSpinnerAdapter);
                                customSpinnerAdapter.notifyDataSetChanged();
                                } else {
                                    customSpinnerAdapter.notifyDataSetChanged();
                                }
                            }
                        }));

                    }else if(apiCallType==2) {
                        employeeListBasedOnDepartmentIDArrayList.clear();
                        JSONArray jsonArrayEmployee = jsonObject.getJSONArray(ConstantKeys.resultKey);
                        EmployeeListBasedOnDepartModel employeeSelectString=new EmployeeListBasedOnDepartModel("Select Employee");
                        employeeListBasedOnDepartmentIDArrayList.add(employeeSelectString);
                        for (int i = 0; i < jsonArrayEmployee.length(); i++) {
                            EmployeeListBasedOnDepartModel employeeListBasedOnDepartModel = new EmployeeListBasedOnDepartModel(jsonArrayEmployee.getJSONObject(i));
                            employeeListBasedOnDepartmentIDArrayList.add(employeeListBasedOnDepartModel);
                        }
                        this.runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (customSpinnerEmploeeAdapter == null) {
                                    customSpinnerEmploeeAdapter = new CustomSpinnerEmploeeAdapter(NominateActivity.this, employeeListBasedOnDepartmentIDArrayList);
                                    employeeNameSpinner.setAdapter(customSpinnerEmploeeAdapter);
                                    customSpinnerEmploeeAdapter.notifyDataSetChanged();
                                } else {
                                    customSpinnerEmploeeAdapter.notifyDataSetChanged();
                                }
                            }
                        }));
                    }else if(apiCallType==3){

                            final String message=jsonObject.getString(ConstantKeys.messageKey);
                             int nominationCount=jsonObject.getInt("FormalNominationCount");
                            final int InformalnominationCount=jsonObject.getInt("InformalNominationCount");
                        JSONArray jsonArrayNomineeList = jsonObject.getJSONArray("FormalNominationList");
                        JSONArray injsonArrayNomineeList = jsonObject.getJSONArray("InformalNominationList");
                        if (!NominationsAwardsActivity.formalAwards){
                            nominationCount = InformalnominationCount;
                            jsonArrayNomineeList = injsonArrayNomineeList;
                        }
                        for (int i = 0; i < jsonArrayNomineeList.length(); i++) {
                            NominationListModel nominationAwardsModel = new NominationListModel(jsonArrayNomineeList.getJSONObject(i));
                            nominationListModelArrayList.add(nominationAwardsModel);
                        }
                        appController.setNominationCount(nominationCount);
                        appController.setNominationListModelArrayList(nominationListModelArrayList);
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NominateActivity.this,message,Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                intent.putExtra("key", "Nominated");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    }
                } else if(status==false){
                    final String message=jsonObject.getString(ConstantKeys.messageKey);
                    Utils.showToast(NominateActivity.this, message);
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
                    Utils.showToast(NominateActivity.this, error);
                    lineaLyTotalView.setVisibility(View.GONE);
                    emptyElement.setVisibility(View.VISIBLE);
                    retryTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callWebApiToGetDepartmentList();
                        }
                    });
                } else {
                    Utils.showToast(NominateActivity.this, "Network Error");
                }
                progressDialog.cancel();
            }
        });
    }
    /**
     * Toolbar widgets on-click actions.
     *
     * @param item A variable of type MenuItem.
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //finishing activity up on click of back arrow button
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentsEditText.getWindowToken(), 0);
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    View.OnClickListener editTextClickListener = new View.OnClickListener()

    {

        public void onClick(View v)
        {
            if (v.getId() == commentsEditText.getId())
            {
                commentsEditText.setCursorVisible(true);
            }

        }
    };

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        /*View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (commentsEditText == null) {
            view = new View(activity);
        }*/
        if (commentsEditText != null) {
            imm.hideSoftInputFromWindow(commentsEditText.getWindowToken(), 0);
        }
    }


}

