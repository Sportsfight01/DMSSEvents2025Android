package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.util.AreaData;
import com.digitalminds.dmssevent.util.Category;
import com.digitalminds.dmssevent.util.ComplaintType;
import com.digitalminds.dmssevent.util.SaveStatusRes;
import com.digitalminds.dmssevent.util.StatusListData;
import com.digitalminds.dmssevent.util.SubCategory;
import com.digitalminds.dmssevent.util.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComplaintsActivity extends AppCompatActivity implements  WebServiceResponseCallBack {
    ProgressDialog progressDialog;
    DmsEventsAppController controller;
    AppCompatButton submit,clear;
    TextInputEditText ed_description;
    TextView tv_myComplaints;
    AutoCompleteTextView complaintTypeDropdown,categoryDropDown,subCategoryDropDown,dropDownAreaType;
    List<SubCategory> filterSubCategoriesList = new ArrayList<>();
    List<Category> filterCategoriesList = new ArrayList<>();

    int selectedComplaintTypeId =0;
    int selectedAreaID=0;
    int selectedCategoryId =0;
    int selectedSubCategoryId =0;
    String serviceRequest="";
    String mDescription="";
    int userID=0;
    ImageView tv_back;
    String complaintsFor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_complaints);
        dropDownAreaType = (AutoCompleteTextView)findViewById(R.id.drop_down_area_type);
        complaintTypeDropdown = (AutoCompleteTextView)findViewById(R.id.drop_down_type);
        categoryDropDown = (AutoCompleteTextView)findViewById(R.id.drop_down_category);
        subCategoryDropDown = (AutoCompleteTextView)findViewById(R.id.drop_down_sub_category);
        submit = (AppCompatButton)findViewById(R.id.submit);
        clear = (AppCompatButton)findViewById(R.id.clear);

        ed_description = (TextInputEditText)findViewById(R.id.ed_description);
        tv_myComplaints = (TextView)findViewById(R.id.tv_myComplaints);
        tv_back = findViewById(R.id.back);
        complaintsFor = getIntent().getStringExtra(getString(R.string.get_complaints_for));

        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(ComplaintsActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        serviceRequest=ConstantKeys.getComplaintsMasterData;
         userID = DmsSharedPreferences.getUserDetails(ComplaintsActivity.this).getId();
        System.out.println("userID::"+userID);

        callWebApiComplaintMasterData( ConstantKeys.getComplaintsMasterData);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveComplaintData();



            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaintTypeDropdown.setText("");
                selectedComplaintTypeId=0;
                subCategoryDropDown.setText("");
                selectedSubCategoryId=0;
                categoryDropDown.setText("");
                selectedCategoryId=0;
                ed_description.setText("");

            }
        });
        tv_myComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ComplaintsActivity.this,MyComplaintActivity.class);
                intent.putExtra(getString(R.string.get_complaints_for),complaintsFor);
                startActivity(intent);
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveComplaintData(){

         mDescription= ed_description.getText().toString();

        if(selectedComplaintTypeId==0){
            Toast.makeText(this,"Please select Complaint",Toast.LENGTH_SHORT).show();

        }
        else if(selectedCategoryId==0){
            Toast.makeText(this,"Please select Category",Toast.LENGTH_SHORT).show();


        }else if(selectedSubCategoryId==0){
            Toast.makeText(this,"Please select Sub Category",Toast.LENGTH_SHORT).show();
        }else{
//            System.out.println("selectedComplaintTypeId:: "+selectedComplaintTypeId+" selectedCategoryId:: "
//                    +selectedCategoryId+" selectedSubCategoryId:: "+selectedSubCategoryId+" mDescription:: "+mDescription);
            callWebApiComplaintMasterData( ConstantKeys.saveComplaint);
            new Utils().showConfirmationDialog(ComplaintsActivity.this,"Do you want to send "+complaintTypeDropdown.getText().toString()+" ?", new Utils.CallbackListner() {
                @Override
                public void onReturn(String response) {
                    System.out.println("showConfirmationDialog:: " + response);
                    if(response.equalsIgnoreCase("Success")){
                        if (com.digitalminds.dmssevent.common.Utils.isNetworkAvailable(ComplaintsActivity.this)) {
                            //callWebApiType = 1;
                            serviceRequest=ConstantKeys.saveComplaint;

                            progressDialog.show();
                            //emptyElement.setVisibility(View.GONE);
                            //String url = "http://192.168.100.92:1010/api/events/eventawards";
                            String requestObject=getComplaintJsonData();
                            System.out.println("requestObject:: "+requestObject);
                            controller.getWebService().postData(ConstantKeys.saveComplaint, requestObject,ComplaintsActivity.this);
                        } else {
                            progressDialog.cancel();

                        }
                    }

                }
            });

        }

    }

    private void callWebApiComplaintMasterData(String url) {
        if (com.digitalminds.dmssevent.common.Utils.isNetworkAvailable(ComplaintsActivity.this)) {
            //callWebApiType = 1;
            progressDialog.show();
            //emptyElement.setVisibility(View.GONE);
            //String url = "http://192.168.100.92:1010/api/events/eventawards";
            controller.getWebService().getData(url, this);
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

    @Override
    public void onServiceCallSuccess(String result) {
        progressDialog.cancel();
        System.out.println("Success result:: "+result);
        if (serviceRequest.equalsIgnoreCase(ConstantKeys.saveComplaint))
        {
            System.out.println("Success result:: Save Complaints:: "+result);
            runOnUiThread(() -> {
                if(result!=null) {

                    Gson gson = new Gson();
                    SaveStatusRes saveStatusRes = gson.fromJson(result, SaveStatusRes.class);
                    String successMessage=saveStatusRes.getMessage().get("Message").getAsString();
                    System.out.println(successMessage);
                    if(successMessage!=null) {
                         Utils.showSuccessDialog(ComplaintsActivity.this, successMessage, new Utils.CallbackListner() {
                            @Override
                            public void onReturn(String response) {
                                Intent intent = new Intent(ComplaintsActivity.this,MyComplaintActivity.class);
                                intent.putExtra(getString(R.string.get_complaints_for),complaintsFor);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            });
        }else{
        getData(result);
        }

    }

    @Override
    public void onServiceCallFail(String error) {
        progressDialog.cancel();

        System.out.println("error result:: "+error);

    }


    public String getComplaintJsonData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryID", selectedCategoryId);
            jsonObject.put("Description", mDescription);
            jsonObject.put("StatusId", 1);
            jsonObject.put("AreaId", 1);
            jsonObject.put("Userid", userID);
            jsonObject.put("SubCategoryId", selectedSubCategoryId);
            jsonObject.put("ReceiverId", Utils.AdminId); // Admin Id(Venkat)
            jsonObject.put("ComplaintTypeId", selectedComplaintTypeId);
            System.out.println("jsonObject:: "+jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    private void getData(String result){
        HashMap<String, List<?>> masterDataMap= Utils.parseMasterData(result);

        List<ComplaintType> complaintTypeList= (List<ComplaintType>) masterDataMap.get(Utils.ComplaintTypes);
        List<Category> categoryList= (List<Category>) masterDataMap.get(Utils.CategoryList);
        List<SubCategory> subCategoryList= (List<SubCategory>) masterDataMap.get(Utils.SubCategories);
        List<AreaData> areaList= (List<AreaData>) masterDataMap.get(Utils.AreaList);

        System.out.println("Success CategoryList:: "+complaintTypeList);

        runOnUiThread(() -> {
            ArrayAdapter complaintTypeArrayAdapter= new ArrayAdapter( this, R.layout.dropdown_item, complaintTypeList);
//            ArrayAdapter categoryListAdapter= new ArrayAdapter( this, R.layout.dropdown_item, categoryList);
            ArrayAdapter areatypeAdapter= new ArrayAdapter( this, R.layout.dropdown_item, areaList);
            dropDownAreaType.setAdapter(areatypeAdapter);
            complaintTypeDropdown.setAdapter(complaintTypeArrayAdapter);
//            categoryDropDown.setAdapter(categoryListAdapter);

            dropDownAreaType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    filterCategoriesList.clear();

                    AreaData selectedCategory = (AreaData) parent.getItemAtPosition(position);
                    selectedAreaID = selectedCategory.getCategoryId();
                    selectedCategoryId=0;
                    subCategoryDropDown.setText("");
                    selectedSubCategoryId=0;
                    categoryDropDown.setText("");
                    filterCategoriesList= Utils.filterCategories(categoryList,selectedAreaID);
                    ArrayAdapter categoryListAdapter= new ArrayAdapter( ComplaintsActivity.this, R.layout.dropdown_item, filterCategoriesList);
                    categoryDropDown.setAdapter(categoryListAdapter);
                }
            });
            complaintTypeDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    ComplaintType selectedComplaintType = (ComplaintType) parent.getItemAtPosition(position);
                    selectedComplaintTypeId = selectedComplaintType.getComplaintTypeId();
                    String complaintTypeIdName = selectedComplaintType.toString();
                    System.out.println("complaintTypeId:: "+ selectedComplaintTypeId +"  complaintTypeIdName:: "+complaintTypeIdName);
                    subCategoryDropDown.setText("");
                    selectedSubCategoryId=0;
                    categoryDropDown.setText("");
                    selectedCategoryId=0;

                }
            });

            categoryDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    filterSubCategoriesList.clear();
                    subCategoryDropDown.setText("");
                    selectedSubCategoryId=0;
                    Category selectedCategory = (Category) parent.getItemAtPosition(position);
                    selectedCategoryId = selectedCategory.getCategoryId();
                    String categoryName = selectedCategory.toString();
                    System.out.println("category:: "+selectedCategoryId+"  categoryName:: "+categoryName);
                    filterSubCategoriesList= Utils.filterSubCategories(subCategoryList,selectedCategoryId);
                    ArrayAdapter subategoryListAdapter= new ArrayAdapter( ComplaintsActivity.this, R.layout.dropdown_item, filterSubCategoriesList);
                    subCategoryDropDown.setAdapter(subategoryListAdapter);

                }
            });
            subCategoryDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    SubCategory selectedCategory = (SubCategory) parent.getItemAtPosition(position);
                    selectedCategoryId = selectedCategory.getCategoryId();
                    selectedSubCategoryId = selectedCategory.getSubCategoryId();

                    String subcategoryName = selectedCategory.toString();
                    System.out.println("category:: "+selectedCategoryId+"  selectedSubCategoryId:: "+selectedSubCategoryId+" subcategoryName:: "+subcategoryName);

                }
            });
           /* subCategoryDropDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(filterSubCategoriesList!=null || filterSubCategoriesList.size()==0){
                        Toast.makeText(ComplaintsActivity.this,"No sub categories available",Toast.LENGTH_SHORT).show();

                    }

                }
            });*/

        });
    }

}
