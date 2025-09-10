package com.digitalminds.dmssevent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.adapters.CommentListAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.CommentsListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep.Kumar on 15-02-2018.
 */

public class CommentsActivity extends AppCompatActivity implements WebServiceResponseCallBack {
    ListView listViewComments;
    TextView textViewNoResults;
    ImageView imageViewSendComment;
    EditText editTextComment;
    String editCommentValue="";
    DmsEventsAppController controller;
    ArrayList<CommentsListModel> commentsListModelArrayList=new ArrayList<CommentsListModel>();
    CommentsListModel commentsListModel;
    CommentListAdapter commentListAdapter;
    int callWebApiType;
    ProgressDialog progressDialog;
    public static int newsID;
    public static String toolbarTitle;
    Toolbar toolbar;
    TextView toolbar_title;
    boolean callWebApiCallingAfterUserAddedNewComment=false;
    int commentsSize;
    Intent intent;
    String updatedCommentsSize;
    LinearLayout lineaLySendComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_comments);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        controller = (DmsEventsAppController) getApplicationContext();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Comments");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        listViewComments = (ListView) findViewById(R.id.listViewComments);
        textViewNoResults = (TextView) findViewById(R.id.textViewNoResults);
        imageViewSendComment = (ImageView) findViewById(R.id.imageViewSendComment);
        lineaLySendComment = (LinearLayout) findViewById(R.id.lineaLySendComment);
        editTextComment = (EditText) findViewById(R.id.editTextComment);
        // set the custom dialog components - text, image and button
        callWebApiForGetComments();
        lineaLySendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCommentValue = editTextComment.getText().toString().trim();
                if (editCommentValue.length() > 0) {
                    callWebApiType=2;
                    callWebApiForAddComment();
                } else {
                    Toast.makeText(CommentsActivity.this, "Please add your comments", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void callWebApiForGetComments() {
        if (Utils.isNetworkAvailable(this)) {
            callWebApiType = 1;
            progressDialog.show();
            String url = ConstantKeys.clickComment;
            controller.getWebService().getData(url+newsID, this);
        } else {
            progressDialog.cancel();
        }
    }
    private void callWebApiForAddComment() {
        if (Utils.isNetworkAvailable(this)) {
            callWebApiType = 2;
            progressDialog.show();
            //emptyElement.setVisibility(View.GONE);
            String url = ConstantKeys.clickFavourite;
            controller.getWebService().postData(url,getJsonForAddComment(), this);
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
    private String getJsonForAddComment() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("NewsID", newsID);
            jsonObject.put("EmployeeId", DmsSharedPreferences.getUserDetails(CommentsActivity.this).getEmpID());
            jsonObject.put("Liked", false);
            jsonObject.put("Comment", editCommentValue);
            jsonObject.put("IsLikeActivity", false);

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                //JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                if (status) {
                    if (callWebApiType == 1) {
                        commentsListModelArrayList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                        if(jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                commentsListModel = new CommentsListModel(jsonArray.getJSONObject(i));
                                commentsListModelArrayList.add(commentsListModel);
                            }
                            if(callWebApiCallingAfterUserAddedNewComment==true) {
                                commentsSize = commentsListModelArrayList.size();
                                updatedCommentsSize=String.valueOf(commentsSize);
                                intent = getIntent();
                                intent.putExtra("Key", "refreshCommentsCount");
                                intent.putExtra("CommentsSize",updatedCommentsSize );
                                setResult(RESULT_OK, intent);

                            }
                            this.runOnUiThread(new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (commentListAdapter == null) {
                                        textViewNoResults.setVisibility(View.GONE);
                                        listViewComments.setVisibility(View.VISIBLE);
                                        commentListAdapter = new CommentListAdapter(CommentsActivity.this, commentsListModelArrayList);
                                        listViewComments.setAdapter(commentListAdapter);
                                    } else {
                                        commentListAdapter.notifyDataSetChanged();
                                    }
                                }
                            }));

                        }else{
                            this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewNoResults.setVisibility(View.VISIBLE);
                                    listViewComments.setVisibility(View.GONE);
                                }
                            });
                        }

                    }else if(callWebApiType == 2){
                        final String message = jsonObject.getString(ConstantKeys.messageKey);
                        //do stuff
                        this.runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                callWebApiCallingAfterUserAddedNewComment=true;
                                callWebApiForGetComments();
                                editTextComment.setText("");
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                                Toast.makeText(CommentsActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }));
                    }

                } else {
                    Utils.showToast(CommentsActivity.this, "Server Error");
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
                    Utils.showToast(CommentsActivity.this, error);
                } else {
                    Utils.showToast(CommentsActivity.this, "Network Error");
                }
                progressDialog.cancel();
            }
        });

        // swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
