package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalminds.dmssevent.adapters.DropdownAdapter;
import com.digitalminds.dmssevent.adapters.MyComplaintsAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.util.ComplaintsData;
import com.digitalminds.dmssevent.util.Utils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyComplaintActivity  extends AppCompatActivity implements WebServiceResponseCallBack {
    ProgressDialog progressDialog;
    DmsEventsAppController controller;
    RecyclerView rv_complaints;
    TextView tv_no_complaint,tv_header;
    ImageView iv_filter;
    EditText ed_search;
    int userID=0;
    ImageView tv_back;
    String complaintsFor;
    MyComplaintsAdapter adapter =null;
    int selectedIndex = 0; // Default selected item
    DropdownAdapter dropdownAdapteradapter=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actvity_my_complaints);
        rv_complaints = (RecyclerView)findViewById(R.id.rv_complaints);
        tv_no_complaint = findViewById(R.id.tv_no_complaint);
        iv_filter = findViewById(R.id.iv_filter);
        tv_header = findViewById(R.id.tv_header);

        ed_search = findViewById(R.id.ed_search);

        controller = (DmsEventsAppController) getApplicationContext();
        progressDialog = new ProgressDialog(MyComplaintActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
//        userID = DmsSharedPreferences.getUserDetails(MyComplaintActivity.this).getEmpID().replace("DM","");
        userID = DmsSharedPreferences.getUserDetails(MyComplaintActivity.this).getId();

        tv_back = findViewById(R.id.back);
        complaintsFor = getIntent().getStringExtra(getString(R.string.get_complaints_for));
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ed_search.setText("");
        if(complaintsFor.equalsIgnoreCase(getString(R.string.get_complaints_for_all_emps))) {
            callWebApiComplaintMasterData(ConstantKeys.getallemployeecomplaints);
            tv_header.setText("All Tickets");

        }else{

            callWebApiComplaintMasterData(ConstantKeys.getEmployeecomplaints +userID);
            tv_header.setText("My Tickets");

        }

    }

    private void initView(){




      /*  iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MyComplaintActivity.this, iv_filter);
                String[] statusArr = {"All","Open", "InProgress", "Pending","Closed"};
                for(int i=0;i<statusArr.length;i++){

                    popupMenu.getMenu().add(Menu.NONE, i, i, statusArr[i]);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(adapter!=null) {
                            if(item.getTitle().toString().equalsIgnoreCase("All")){
                                ed_search.setText("");

                            }else {
                                adapter.filter(item.getTitle().toString());
                            }

                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });*/

        String[] statusArr = {"All","Open", "InProgress", "Pending", "Closed"};
        ListPopupWindow listPopupWindow = new ListPopupWindow(MyComplaintActivity.this);

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownAdapteradapter = new DropdownAdapter(MyComplaintActivity.this, statusArr, selectedIndex);
                listPopupWindow.setAdapter(dropdownAdapteradapter);
                listPopupWindow.setAnchorView(iv_filter);
                listPopupWindow.setWidth(400); // Adjust width as needed
                listPopupWindow.setModal(true);

                int anchorWidth = ed_search.getWidth();
//                System.out.println();
                int anchorHeight = ed_search.getHeight();

//                listPopupWindow.setHorizontalOffset(anchorWidth - listPopupWindow.getWidth());
                listPopupWindow.setVerticalOffset(33);

                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedIndex = position;
//                        Toast.makeText(MyComplaintActivity.this, "Selected: " + statusArr[position], Toast.LENGTH_SHORT).show();
                        listPopupWindow.dismiss();


                        if(adapter!=null) {
                            if(statusArr[position].equalsIgnoreCase("All")){
                                ed_search.setText("");

                            }else {
                                adapter.filter(statusArr[position].toString());
                            }

                        }
                    }
                });

                listPopupWindow.show();
            }
        });

        ed_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable drawableEnd = ed_search.getCompoundDrawables()[2]; // Right drawable
                    if (drawableEnd != null) {
                        int drawableWidth = drawableEnd.getBounds().width();
                        int touchAreaStart = ed_search.getWidth() - ed_search.getPaddingRight() - drawableWidth;

                        if (event.getX() >= touchAreaStart) {
                            // Action when drawableEnd is clicked
                            ed_search.setText("");
                            selectedIndex=0;
                            dropdownAdapteradapter = new DropdownAdapter(MyComplaintActivity.this, statusArr, selectedIndex);
                            listPopupWindow.setAdapter(dropdownAdapteradapter);
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter!=null) {
                    adapter.filter(s.toString());
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

    }
    private void callWebApiComplaintMasterData(String url) {
        if (com.digitalminds.dmssevent.common.Utils.isNetworkAvailable(MyComplaintActivity.this)) {
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

        progressDialog.dismiss();
        System.out.println("result:: "+result);
        runOnUiThread(() -> {

            try {
//                if(result!=null)
                JSONObject jsonObject = new JSONObject(result);

                String message=jsonObject.getString("message");

                if(message!=null && message.contains("successfully")) {

                    List<ComplaintsData> complaintsDataList = Utils.parseMyComplaintsData(result);

                    if (complaintsDataList != null && complaintsDataList.size() > 0) {
                        tv_no_complaint.setVisibility(View.GONE);
                        rv_complaints.setVisibility(View.VISIBLE);
                        ed_search.setVisibility(View.VISIBLE);


                        adapter = new MyComplaintsAdapter(complaintsDataList, item -> {
//                        Toast.makeText(this, "Clicked: " + item.getComplaintTypeId(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MyComplaintActivity.this, ComplaintsDetailsActivity.class);
                            intent.putExtra("complaint_data", item);
                            intent.putExtra(getString(R.string.get_complaints_for), complaintsFor);
// complaintsData is an instance of ComplaintsData
                            startActivity(intent);

                        });

                        rv_complaints.setLayoutManager(new LinearLayoutManager(this));
                        rv_complaints.setAdapter(adapter);
                    } else {
                        tv_no_complaint.setVisibility(View.VISIBLE);
                        rv_complaints.setVisibility(View.GONE);
                        ed_search.setVisibility(View.GONE);
                        tv_no_complaint.setText("No Tickets Available");

                    }
                }else{
                    tv_no_complaint.setText("No Tickets Available");
                    tv_no_complaint.setVisibility(View.VISIBLE);
                    rv_complaints.setVisibility(View.GONE);
                    ed_search.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });


    }

    @Override
    public void onServiceCallFail(String error) {
        runOnUiThread(() -> {

            progressDialog.dismiss();
            tv_no_complaint.setVisibility(View.VISIBLE);
            ed_search.setVisibility(View.GONE);

            tv_no_complaint.setText("Unable to Connect Server.Please try again..");
        });


    }
}
