package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.util.ComplaintStatusRes;
import com.digitalminds.dmssevent.util.ComplaintType;
import com.digitalminds.dmssevent.util.ComplaintsData;
import com.digitalminds.dmssevent.util.ComplaintsResponse;
import com.digitalminds.dmssevent.util.SaveStatusRes;
import com.digitalminds.dmssevent.util.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ComplaintsDetailsActivity extends AppCompatActivity implements WebServiceResponseCallBack {
    TextView tv_complaint_type, tv_category_type,tv_su_category_type,tv_status,
            tv_description,tv_email_id,tv_employee_id,tv_ticket_id,current_status,tv_header_ticketId,tv_complaint_area,tv_update_time;
    AutoCompleteTextView drop_down_status;
    int selectedComplaintStatusId=0;
    ProgressDialog progressDialog;
    DmsEventsAppController controller;
    ComplaintsData complaintsData=null;
    Button update_statue;
    ImageView tv_back;
    LinearLayout ll_update_stutus;
    String complaintsFor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_details);

        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(ComplaintsDetailsActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
       initView();
    }
    private void initView() {
        tv_back = findViewById(R.id.back);
        complaintsFor = getIntent().getStringExtra(getString(R.string.get_complaints_for));

        tv_complaint_type = findViewById(R.id.tv_complaint_type);
        tv_category_type = findViewById(R.id.tv_category_type);
        tv_su_category_type = findViewById(R.id.tv_su_category_type);
        tv_description = findViewById(R.id.tv_description);
        tv_update_time = findViewById(R.id.tv_update_time);

        drop_down_status = findViewById(R.id.drop_down_status);
        tv_email_id = findViewById(R.id.tv_email_id);
        tv_employee_id = findViewById(R.id.tv_employee_id);
        tv_ticket_id = findViewById(R.id.tv_ticket_id);
        tv_complaint_area = findViewById(R.id.tv_complaint_area);
        tv_header_ticketId = findViewById(R.id.tv_header_ticketId);
        update_statue = findViewById(R.id.update_statue);
        ll_update_stutus = findViewById(R.id.ll_update_stutus);
        current_status = findViewById(R.id.current_status);
        complaintsData = (ComplaintsData) getIntent().getSerializableExtra("complaint_data");
        tv_complaint_type.setText(complaintsData.getComplaintType());
        tv_category_type.setText(complaintsData.getCategoryName());
        tv_su_category_type.setText(complaintsData.getSubCategoryName());
        tv_category_type.setText(complaintsData.getCategoryName());
        tv_description.setText(complaintsData.getDescription());
        tv_email_id.setText(complaintsData.getEmailId());
        tv_employee_id.setText(complaintsData.getEmployeeName());
        tv_ticket_id.setText(complaintsData.getTicketId());
        tv_header_ticketId.setText(complaintsData.getTicketId());
        current_status.setText(complaintsData.getCurrentStatus());
        drop_down_status.setText(complaintsData.getCurrentStatus());
        tv_complaint_area.setText(complaintsData.getAreaName());
        tv_update_time.setText(complaintsData.getLastUpdatedOn());
        if(complaintsFor.equalsIgnoreCase(getString(R.string.get_complaints_for_all_emps))) {
            ll_update_stutus.setVisibility(View.VISIBLE);
            update_statue.setVisibility(View.VISIBLE);
        }else{
            ll_update_stutus.setVisibility(View.GONE);
            update_statue.setVisibility(View.GONE);

        }
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update_statue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedComplaintStatusId==0) {
                    Toast.makeText(ComplaintsDetailsActivity.this,"Please select status",Toast.LENGTH_SHORT).show();
                }else{
                    updateStatus();

                }
            }
        });
        ArrayAdapter statusListAdapter = new ArrayAdapter(this, R.layout.dropdown_item, Utils.parseComplaintsStatusData());

        drop_down_status.setAdapter(statusListAdapter);

        drop_down_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                ComplaintStatusRes selectedComplaintStatusType = (ComplaintStatusRes) parent.getItemAtPosition(position);

                selectedComplaintStatusId = selectedComplaintStatusType.getStatusId();
                String complaintStatusName = selectedComplaintStatusType.toString();
                System.out.println("complaintTypeId:: " + selectedComplaintStatusId + "  complaintTypeIdName:: " + selectedComplaintStatusType);

            }
        });
    }
        private void updateStatus(){
            if (com.digitalminds.dmssevent.common.Utils.isNetworkAvailable(ComplaintsDetailsActivity.this)) {
                //callWebApiType = 1;
               String serviceRequest= ConstantKeys.saveComplaintStatus;

                progressDialog.show();
                //emptyElement.setVisibility(View.GONE);
                //String url = "http://192.168.100.92:1010/api/events/eventawards";
                controller.getWebService().postData(ConstantKeys.saveComplaintStatus, getUpdateStatusResqJsonData(),this);
            } else {
                progressDialog.cancel();
            /*emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForGetAllNewsFeed();
                }
            });*/
            }
        }

    public String getUpdateStatusResqJsonData() {
        String userID = Integer.toString(DmsSharedPreferences.getUserDetails(this).getId());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("StatusId", selectedComplaintStatusId);
            jsonObject.put("id", complaintsData.getTicketId());
            jsonObject.put("AdminId", Utils.AdminId);


            System.out.println("jsonObject:: "+jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onServiceCallSuccess(String result) {
        progressDialog.cancel();
        System.out.println("onServiceCall:: Success:: "+result);

        runOnUiThread(() -> {
            if(result!=null) {

                Gson gson = new Gson();
                SaveStatusRes saveStatusRes = gson.fromJson(result, SaveStatusRes.class);
                String successMessage=saveStatusRes.getMessage().get("Message").getAsString();
                System.out.println(successMessage);
                if(successMessage!=null) {
                     Utils.showSuccessDialog(ComplaintsDetailsActivity.this, successMessage, new Utils.CallbackListner() {
                        @Override
                        public void onReturn(String response) {
                            System.out.println("showConfirmationDialog:: " + response);

                            finish();

                        }
                    });
                }
            }
                });
    }

    @Override
    public void onServiceCallFail(String error) {
        progressDialog.cancel();

        System.out.println("onServiceCall:: error:: " + error);
    }


}
