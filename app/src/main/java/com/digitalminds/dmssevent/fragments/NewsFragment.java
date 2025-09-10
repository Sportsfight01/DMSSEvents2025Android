package com.digitalminds.dmssevent.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.AddNewFeedActivity;
import com.digitalminds.dmssevent.CommentsActivity;
import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.adapters.NewsAdapter;
import com.digitalminds.dmssevent.common.CircleTransform;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.TouchImageView;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.AdaterClickCallBackForFavourite;
import com.digitalminds.dmssevent.interfaces.AdaterClickCallBackForPosition;
import com.digitalminds.dmssevent.interfaces.SubAdapterCallBackInterface;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.GetAllNewsFeedModel;
import com.digitalminds.dmssevent.models.NewsModel;
import com.digitalminds.dmssevent.models.UserProfileModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Sandeep.Kumar on 29-01-2018.
 */

public class NewsFragment extends Fragment implements WebServiceResponseCallBack, AdaterClickCallBackForPosition,
        AdaterClickCallBackForFavourite,View.OnClickListener,SubAdapterCallBackInterface {
    View rootView;
    NewsAdapter newsAdapter;

    boolean serviceCallStarted = false;
    boolean progressDialogHide = false;
    LinearLayout img_arrow;
    NewsModel newsModel;
    ListView listViewNews;
    ArrayList<NewsModel> newsModels = new ArrayList<NewsModel>();
    LinearLayout lineaLyNewFeed;
    ImageView actionBarImageView, imageViewSendComment;
    ProgressDialog progressDialog;
    DmsEventsAppController controller;
    LinearLayout emptyElement, awardDescription;
    TextView retryTextView, textViewNoResults;
    public static final int REQUEST_CODE = 201,REQUEST_FOR_COMMENTS_COUNT=2;
    ArrayList<GetAllNewsFeedModel> allNewsFeedModelArrayList = new ArrayList<GetAllNewsFeedModel>();
    String profilePic = "", userName;
    GetAllNewsFeedModel getAllNewsFeedModel;
    int callWebApiType = 0, newsID, commentSelectedPosition;
    boolean IsLikeActivity = false, favouriteStatusValue;
    Dialog dialog;
    int favouritePosition,updateLikesCount;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout lineaLayoutTotalNewFeeds,lineaLyAddPhoto;
    String uploadedImageUrl;
    String imagePath, imageName, encodedImage;
    boolean isImageCaptured = false;
    TextView textViewApply;
    UserProfileModel userProfileModelUpdation;
    ImageView imageViewProfilePic,addProfilePicImageView,imageViewClose;
    Uri filePath;
    final private int PICK_IMAGE = 1;
    public static final int MEDIA_TYPE_IMAGE = 5;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 123;
    final private int REQUEST_CODE_ASK_EXTERNAL_STORAGE_PERMISSIONS = 321;
    boolean cameraPermissionAvailable = false, galleryPermissionAvailable = false;
    public static File mediaFile;
    Bitmap bitmap;
    Dialog dialogToAddProfile;
    UserProfileModel userProfileModel;
    int totalSchoolListCount, presentListCount = 1;
    ProgressBar progressBar;
    ProgressBar more_progress;
    View loadItemsLayout_recyclerView;

    View footer;
    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE = 122;
    TextView toolbar_title,textViewEmployeeId,userEmailTextView,userDepartmentTextView,userNameTextView,userRoleTextView;
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        controller = (DmsEventsAppController) getActivity().getApplicationContext();
        //Utils.darkenStatusBar(getActivity(), R.drawable.applying_gradientcolor);
        userProfileModel = DmsSharedPreferences.getUserDetails(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        emptyElement = (LinearLayout) rootView.findViewById(R.id.emptyElement);
        lineaLayoutTotalNewFeeds=(LinearLayout) rootView.findViewById(R.id.lineaLayoutTotalNewFeeds);
        retryTextView = (TextView) rootView.findViewById(R.id.retryTextView);
        listViewNews = (ListView) rootView.findViewById(R.id.listViewNews);

        img_arrow = (LinearLayout)rootView. findViewById(R.id.img_arrow);
        //footer = getActivity().getLayoutInflater().inflate(R.layout.list_footer, null);
        //footer.setVisibility(View.GONE);
        // Find the progressbar within footer
        progressBar = (ProgressBar)rootView. findViewById(R.id.load_progress);
        more_progress=(ProgressBar)rootView.findViewById(R.id.more_progress);
        loadItemsLayout_recyclerView=(View)rootView.findViewById(R.id.loadItemsLayout_recyclerView);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(more_progress.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            more_progress.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            more_progress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        }
        //listViewNews.addFooterView(footer);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView. findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presentListCount=1;
                allNewsFeedModelArrayList.clear();
                callWebApiForGetAllNewsFeed();
                mSwipeRefreshLayout.setRefreshing(false);
                // Configure the refreshing colors
                mSwipeRefreshLayout.setColorSchemeResources(R.color.fbBlue);

            }
        });

        lineaLyNewFeed = (LinearLayout) rootView.findViewById(R.id.lineaLyNewFeed);
        actionBarImageView = (ImageView) rootView.findViewById(R.id.actionBarImageView);
         uploadedImageUrl = DmsSharedPreferences.getProfilePicUrl(getActivity());
        if (uploadedImageUrl != null && uploadedImageUrl.length() > 0) {
            String url=ConstantKeys.getImageUrl+uploadedImageUrl.trim();
            //Picasso.with(getActivity()).load("http://192.168.100.92:1010/docs/UserProfileimages/7961fc09-368c-4817-b12c-b3fd5e92e1c2_images.jpeg").transform(new CircleTransform()).into(actionBarImageView);
           // Picasso.with(getActivity()).load(url).transform(new CircleTransform()).into(actionBarImageView);
            Glide.with(getActivity()).load(url).apply(RequestOptions.circleCropTransform()).into(actionBarImageView);

            //Glide.with(this).load(url).transform(new CircleTransform()).into(actionBarImageView);

            actionBarImageView.setClickable(true);
           // callWebApiForGetAllNewsFeed();
            //Picasso.with(getActivity()).load(uploadedImageUrl).into(profilePictureOverViewImageView);
        }else{
            dialogToAddProfilePic();
            actionBarImageView.setClickable(false);

        }
        actionBarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(getActivity())) {
                    dialogToShowProfilePic();
                }
            }
        });
        lineaLyNewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetworkAvailable(getActivity())) {
                    Intent i = new Intent(getActivity(), AddNewFeedActivity.class);
                    startActivityForResult(i, REQUEST_CODE);
                }else{
                    //Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(DmsSharedPreferences.isUserLoggedIn(getActivity())){
            controller.setLoggedInStatus(DmsSharedPreferences.isUserLoggedIn(getActivity()));
        }
        callWebApiForGetAllNewsFeed();

        listViewNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastPosition = firstVisibleItem + visibleItemCount;

                if (lastPosition > 4) {
                    img_arrow.setVisibility(View.VISIBLE);
                } else {
                    img_arrow.setVisibility(View.GONE);
                }
                img_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listViewNews.setSelection(0);

                        // listViewAllSchools.setSelection(0);
                    }
                });
                if (presentListCount < totalSchoolListCount) {
                    if (presentListCount != 0 && presentListCount == lastPosition && lastPosition != totalSchoolListCount) {
                        if (!serviceCallStarted) {
                            progressDialogHide = true;
                            //showProgressBar();
                            callWebApiForGetAllNewsFeed();
                        } else {
                            progressDialogHide = false;
                            //showProgressBar();
                            hideProgressBar();
                        }

                        //servicesListview.smoothScrollToPosition(lastPosition);
                    } else {
                        progressDialogHide = false;
                        hideProgressBar();
                        //listViewNews.removeFooterView(footer);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(controller.isCallFromNotification()){
            presentListCount=1;
            allNewsFeedModelArrayList.clear();
            callWebApiForGetAllNewsFeed();
            controller.setCallFromNotification(false);
        }else if(controller.isNotificationCalled()){
            presentListCount=1;
            allNewsFeedModelArrayList.clear();
            callWebApiForGetAllNewsFeed();
            controller.setNotificationCalled(false);
        }
    }

    // Hide progress
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
    public void showProgressBar() {

        progressBar.setVisibility(View.VISIBLE);
    }
    private void callWebApiForGetAllNewsFeed() {
        if (Utils.isNetworkAvailable(getActivity())) {
            callWebApiType = 1;
            serviceCallStarted = true;
            emptyElement.setVisibility(View.GONE);
          /*  if (progressDialogHide) {
                //progressBar.setVisibility(View.VISIBLE);
                progressDialog.show();
            } else {
                progressDialog.show();
                hideProgressBar();
            }*/

            if (progressDialogHide) {
                loadItemsLayout_recyclerView.setVisibility(View.VISIBLE);
                //dialogProgress=Utils.showPogress(SeeAllSchoolsActivity.this);
                //progressDialog.show();
            } else {
                //progressDialog.show();
                loadItemsLayout_recyclerView.setVisibility(View.GONE);
                progressDialog.show();
                hideProgressBar();
            }
            lineaLayoutTotalNewFeeds.setVisibility(View.VISIBLE);
            String url = ConstantKeys.getAllNewsFeed + DmsSharedPreferences.getUserDetails(getActivity()).getEmpID()+"/"+Integer.toString(presentListCount);
            controller.getWebService().getData(url, this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            lineaLayoutTotalNewFeeds.setVisibility(View.GONE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForGetAllNewsFeed();
                }
            });
        }

    }

    private void dialogToAddProfilePic() {
        // custom dialog
        dialogToAddProfile = new Dialog(getActivity());
        dialogToAddProfile.setContentView(R.layout.dialog_for_profile);
        dialogToAddProfile.setCanceledOnTouchOutside(false);
        dialogToAddProfile.setCancelable(false);
        // set the custom dialog components - text, image and button
        lineaLyAddPhoto=(LinearLayout) dialogToAddProfile.findViewById(R.id.lineaLyAddPhoto);
        textViewApply=(TextView) dialogToAddProfile.findViewById(R.id.textViewApply);
        imageViewProfilePic=(ImageView) dialogToAddProfile.findViewById(R.id.imageViewProfilePic);
        imageViewClose=(ImageView) dialogToAddProfile.findViewById(R.id.imageViewClose);
        addProfilePicImageView=(ImageView) dialogToAddProfile.findViewById(R.id.addProfilePicImageView);
       // Toolbar toolbaDialouge =(Toolbar) dialogToAddProfile.findViewById(R.id.toolbarGamesList);
       // toolbar_title = (TextView) toolbaDialouge.findViewById(R.id.toolbar_title);
        textViewEmployeeId = (TextView)dialogToAddProfile. findViewById(R.id.textViewEmployeeId);
        userNameTextView = (TextView)dialogToAddProfile. findViewById(R.id.userNameTextView);
        userEmailTextView = (TextView) dialogToAddProfile.findViewById(R.id.userEmailTextView);
        userRoleTextView = (TextView) dialogToAddProfile.findViewById(R.id.userRoleTextView);
        userDepartmentTextView = (TextView) dialogToAddProfile.findViewById(R.id.userDepartmentTextView);
        //toolbar_title.setText("Add Photo");
        textViewApply.setOnClickListener(this);
        imageViewProfilePic.setOnClickListener(this);
        addProfilePicImageView.setOnClickListener(this);
        imageViewClose.setOnClickListener(this);
        if(userProfileModel!=null){
            userNameTextView.setText(userProfileModel.getDisplayName());
            textViewEmployeeId.setText(userProfileModel.getEmpID());
            userEmailTextView.setText(userProfileModel.getEmailID());
            userDepartmentTextView.setText(userProfileModel.getDepartment());
            userRoleTextView.setText(userProfileModel.getUserRole());
        }

        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialogToAddProfile.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogToAddProfile.show();// I was told to call show first I am not sure if this it to cause layout to happen so that we can override width?
        dialogToAddProfile.getWindow().setAttributes(lWindowParams);


       // dialogToAddProfile.show();
    }

    private void dialogToShowProfilePic() {
        uploadedImageUrl = DmsSharedPreferences.getProfilePicUrl(getActivity());
        final Dialog dialogForProfile = new Dialog(getActivity());
        dialogForProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForProfile.setContentView(R.layout.profile_dialog);

        dialogForProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        // set the custom dialog components - text, image and button
        TouchImageView touchImageView = (TouchImageView) dialogForProfile.findViewById(R.id.profilePictureOverViewImageView);
        ImageView imageViewClose = (ImageView) dialogForProfile.findViewById(R.id.imageViewClose);
        //Picasso.with(getActivity()).load(ConstantKeys.getImageUrl + uploadedImageUrl).into(touchImageView);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image);



        Glide.with(getActivity()).load(ConstantKeys.getImageUrl + uploadedImageUrl).apply(options).into(touchImageView);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForProfile.cancel();
            }
        });

        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialogForProfile.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogForProfile.show();
        // Apply the newly created layout parameters to the alert dialog window
        dialogForProfile.getWindow().setAttributes(lWindowParams);
    }

    private void dialogToShowWallpaper(String imageUrl) {
        final Dialog dialogForWallpaper = new Dialog(getActivity());
        dialogForWallpaper.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForWallpaper.setContentView(R.layout.profile_dialog);

        dialogForWallpaper.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        // set the custom dialog components - text, image and button
        TouchImageView touchImageView = (TouchImageView) dialogForWallpaper.findViewById(R.id.profilePictureOverViewImageView);
        ImageView imageViewClose = (ImageView) dialogForWallpaper.findViewById(R.id.imageViewClose);
        //Picasso.with(getActivity()).load(ConstantKeys.getImageUrl + uploadedImageUrl).into(touchImageView);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image);
        Glide.with(getActivity()).load(imageUrl).apply(options).into(touchImageView);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForWallpaper.cancel();
            }
        });
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialogForWallpaper.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogForWallpaper.show();// I was told to call show first I am not sure if this it to cause layout to happen so that we can override width?
        dialogForWallpaper.getWindow().setAttributes(lWindowParams);
    }
    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                if (status) {
                    if (callWebApiType == 1) {
                        //allNewsFeedModelArrayList.clear();
                        //JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                        JSONObject jsonResultObject = jsonObject.getJSONObject(ConstantKeys.resultKey);
                        JSONArray jsonArray = jsonResultObject.getJSONArray("NewsList");
                        totalSchoolListCount = jsonResultObject.getInt("TotalListSize");
                        //allNewsFeedModelArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            getAllNewsFeedModel = new GetAllNewsFeedModel(jsonArray.getJSONObject(i));
                            allNewsFeedModelArrayList.add(getAllNewsFeedModel);
                            userName = getAllNewsFeedModel.getEmployeeName();
                        }
                        if (getActivity() != null) {
                            //do stuff
                            getActivity().runOnUiThread(new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    loadItemsLayout_recyclerView.setVisibility(View.GONE);
                                    presentListCount = allNewsFeedModelArrayList.size();
                                    serviceCallStarted = false;
                                    if (newsAdapter == null) {
                                        newsAdapter = new NewsAdapter(getActivity(), allNewsFeedModelArrayList, NewsFragment.this, NewsFragment.this, NewsFragment.this);
                                        listViewNews.setAdapter(newsAdapter);
                                        newsAdapter.notifyDataSetChanged();
                                        if(allNewsFeedModelArrayList.size()==0){
                                            listViewNews.setEmptyView(rootView.findViewById(R.id.emptynoresult));
                                        }
                                    } else {
                                        newsAdapter.notifyDataSetChanged();
                                    }
                                }
                            }));

                        }
                    } else if (callWebApiType == 2) {
                        final String message = jsonObject.getString(ConstantKeys.messageKey);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(favouriteStatusValue==true){
                                        GetAllNewsFeedModel updateFavValue=allNewsFeedModelArrayList.get(favouritePosition);
                                        updateFavValue.setFavourite(true);
                                        updateFavValue.setLikesCount(updateLikesCount+1);
                                        newsAdapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    }else {
                                        GetAllNewsFeedModel updateFavValue1=allNewsFeedModelArrayList.get(favouritePosition);
                                        updateFavValue1.setFavourite(false);
                                        updateFavValue1.setLikesCount(updateLikesCount-1);
                                        newsAdapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    }else if(callWebApiType==3){
                        final String message = jsonObject.getString(ConstantKeys.messageKey);
                        final JSONObject jsonResultObject = jsonObject.getJSONObject(ConstantKeys.resultKey);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userProfileModelUpdation = new UserProfileModel(jsonResultObject);
                                DmsSharedPreferences.saveUserDetails(getActivity(), userProfileModelUpdation);
                                DmsSharedPreferences.saveProfilePicUrl(getActivity(),userProfileModelUpdation.getProfileImageURL());
                                controller.setUserProfileModel(userProfileModelUpdation);
                                uploadedImageUrl = DmsSharedPreferences.getProfilePicUrl(getActivity());
                                Picasso.with(getActivity()).load(ConstantKeys.getImageUrl+uploadedImageUrl).transform(new CircleTransform()).into(actionBarImageView);
                                dialogToAddProfile.dismiss();
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                } else {
                    Utils.showToast(getActivity(), "Server Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(progressDialog!=null){
            progressDialog.cancel();
        }
    }

    public String saveProfilePicUrl(String id) {
        profilePic = "";
        final String imageName = ConstantKeys.fBProfilePicsAlbumName + "/" + id + "_" + userName + ".jpg";
        StorageReference childRef = controller.getStorageRef().child(imageName);
        childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                profilePic = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                profilePic = "";
            }
        });
        return profilePic;
    }

    @Override
    public void onServiceCallFail(final String error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (error != null) {
                    Utils.showToast(getActivity(), error);
                    emptyElement.setVisibility(View.VISIBLE);
                    lineaLayoutTotalNewFeeds.setVisibility(View.GONE);
                    retryTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callWebApiForGetAllNewsFeed();
                        }
                    });
                } else {
                    Utils.showToast(getActivity(), "Network Error");
                    emptyElement.setVisibility(View.VISIBLE);
                    //emptynoresult.setVisibility(View.GONE);
                    lineaLayoutTotalNewFeeds.setVisibility(View.GONE);
                    retryTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callWebApiForGetAllNewsFeed();
                        }
                    });
                }
                progressDialog.cancel();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE && resultCode  == RESULT_OK) {
                String isNewFeedAddedOrNot = data.getStringExtra("key");
                if (isNewFeedAddedOrNot.equalsIgnoreCase("Success")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            presentListCount=1;
                            allNewsFeedModelArrayList.clear();
                            callWebApiForGetAllNewsFeed();
                        }
                    });
                }
            }else if (requestCode == REQUEST_FOR_COMMENTS_COUNT && resultCode == RESULT_OK ){
                String getCommentsCount = data.getStringExtra("Key");
                String commentsValuesNew=data.getStringExtra("CommentsSize");
                int countValue=Integer.parseInt(commentsValuesNew);
                if(getCommentsCount.equalsIgnoreCase("refreshCommentsCount")){
                    GetAllNewsFeedModel updateCommentsData=allNewsFeedModelArrayList.get(commentSelectedPosition);
                    updateCommentsData.setCommentsCount(countValue);
                    newsAdapter.notifyDataSetChanged();
                }
            }else  if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    imagePath = filePath.getPath();
                    File file = new File(imagePath);
                    imageName = file.getName();
                    isImageCaptured = true;
                    //showDialog();
                    Bitmap bitmap;
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(filePath));
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

                        encodedImage = encodeFromString(resizedBitmap);
                        //image = ConvertBitmapToString(resizedBitmap);
                        //Upload();

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.gc();
                    //String newPath = compressImage(filePath.toString());
                    //Uri uri = getImageContentUri(getActivity(), new File(imagePath));
                    //Picasso.with(thisClassContext).load(uploadedImageUrl).transform(new CircleTransform()).into(imageView2);
                    //Picasso.with(thisClassContext).load(uploadedImageUrl).into(profilePictureOverViewImageView);

                    //imageViewProfilePic.setImageURI(filePath);
                    Glide.with(getActivity()).load(filePath).apply(RequestOptions.circleCropTransform()).into(imageViewProfilePic);

                   // Picasso.with(getActivity()).load(filePath).transform(new CircleTransform()).into(imageViewProfilePic);
                    Toast.makeText(getActivity(), " Picture was taken ", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getActivity(), " Picture was not taken ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), " Picture was not taken ", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                imagePath = c.getString(columnIndex);
                File file = new File(imagePath);
                imageName = file.getName();
                c.close();
                isImageCaptured = true;
                //showDialog();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                //Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

                    encodedImage = encodeFromString(resizedBitmap);
                    //image = ConvertBitmapToString(resizedBitmap);
                    //Upload();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //imageViewProfilePic.setImageBitmap(bitmap);
                Glide.with(getActivity()).load(file).apply(RequestOptions.circleCropTransform()).into(imageViewProfilePic);

                //Picasso.with(getActivity()).load(file).transform(new CircleTransform()).into(imageViewProfilePic);

                // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));

            } else {
                //Toast.makeText(getActivity(), " This Image cannot be stored .please try with some other Image. ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
    //method to convert the selected image to base64 encoded string
    public static String encodeFromString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    @Override
    public void itemClickFavourite(View view, int position, boolean favouriteStatus, int newsId,int likesCount) {
        IsLikeActivity = true;
        if (favouriteStatus) {
            favouriteStatusValue = false;
            favouritePosition=position;
            newsID = newsId;
            updateLikesCount=likesCount;
            //imageViewLike = (ImageView) view.findViewById(R.id.imageViewLike);
            callWebApiForFavouriteClick();
        } else {
            favouriteStatusValue = true;
            newsID = newsId;
            favouritePosition=position;
            updateLikesCount=likesCount;
            callWebApiForFavouriteClick();
        }
    }

    @Override
    public void itemClick(View view, int position, int newsId,String name) {
        IsLikeActivity = false;
        newsID = newsId;
        commentSelectedPosition = position;
        CommentsActivity.newsID = newsID;
        CommentsActivity.toolbarTitle = name;
        Intent i = new Intent(getActivity(), CommentsActivity.class);
        startActivityForResult(i,REQUEST_FOR_COMMENTS_COUNT);
    }

    private void callWebApiForFavouriteClick() {
        if (Utils.isNetworkAvailable(getActivity())) {
            callWebApiType = 2;
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            String url = ConstantKeys.clickFavourite;
            controller.getWebService().postData(url, getJsonForAddFavourite(), this);
        } else {
            progressDialog.cancel();
        }

    }

    private String getJsonForAddFavourite() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("NewsID", newsID);
            jsonObject.put("EmployeeId", DmsSharedPreferences.getUserDetails(getActivity()).getEmpID());
            jsonObject.put("Liked", favouriteStatusValue);
            jsonObject.put("Comment", null);
            jsonObject.put("IsLikeActivity", true);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewApply:
                if (isImageCaptured) {
                    //upLoadImage();
                    callWebApiType=3;
                    callWebApiToAddProfilePhoto();
                }  else {
                    Toast.makeText(getActivity(), "Please add your photo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addProfilePicImageView:
                checkPermissions();
                break;
            case R.id.imageViewClose:
                if(isImageCaptured==false){
                    Toast.makeText(getActivity(),"Please add your profile pic and save photo",Toast.LENGTH_SHORT).show();
                }else if(isImageCaptured==true){
                    Toast.makeText(getActivity(),"Please Save your details",Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }
    private void callWebApiToAddProfilePhoto() {
        if (Utils.isNetworkAvailable(getActivity())) {
            // apiCallType=1;
            progressDialog.show();
            controller.getWebService().postData(ConstantKeys.addingProfilePhotoUrl, addProfilePhotoJsonData(), this);
        } else {
            progressDialog.cancel();
        }
    }
    private String addProfilePhotoJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmailID", DmsSharedPreferences.getUserDetails(getActivity()).getEmailID());
            jsonObject.put("UserGuid", DmsSharedPreferences.getUserDetails(getActivity()).getEmpGuid());
            jsonObject.put("ImageName", imageName);
            jsonObject.put("ProfileImageChars", encodedImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * Checks that the application is having the camera and gallery permissions or not.
     * if not asks for the permission.
     **/
    public void checkPermissions() {
        int hasPermission = ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
        int hasWritePermission = ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED && hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE);
        } else if(hasPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission == PackageManager.PERMISSION_GRANTED ) {
            controller.setCameraPermissionAvailable(false);
            controller.setGalleryPermissionAvailable(true);
            captureImage();
        }else if(hasPermission == PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED){
            controller.setCameraPermissionAvailable(true);
            controller.setGalleryPermissionAvailable(false);
            captureImage();
        }
        else {
            controller.setCameraPermissionAvailable(true);
            controller.setGalleryPermissionAvailable(true);
            captureImage();
        }
        //galleryPermissionAvailable = true;
        //cameraPermissionAvailable = true;
        //captureImage();
    }

    public void captureImage() {
        dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_for_permissions);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button
        TextView textViewCamera = (TextView) dialog.findViewById(R.id.textViewCamera);
        TextView textViewGallery = (TextView) dialog.findViewById(R.id.textViewGallery);
        // if decline button is clicked, close the custom dialog
        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking device has camera hardware or not
                if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    if (controller.isCameraPermissionAvailable() == true) {
                        callCamera();
                    } else {
                        Toast.makeText(getActivity(),
                                "camera permission denied", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
        textViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (controller.isGalleryPermissionAvailable() == true) {
                    callGallery();
//                    //controller.setMakeServiceCall(true);
//                } else {
//                    Toast.makeText(getActivity(),
//                            "gallery permission denied", Toast.LENGTH_LONG)
//                            .show();
//                }

            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        // dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();// I was told to call show first I am not sure if this it to cause layout to happen so that we can override width?
        dialog.getWindow().setAttributes(lWindowParams);

    }


    public void callCamera() {
       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/
        dialog.dismiss();
        if (controller.isCameraPermissionAvailable() == true) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            filePath = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void callGallery() {
        dialog.dismiss();
        // Create intent to Open Image applications like Gallery, Google Photos
//        if (controller.isGalleryPermissionAvailable() == true) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, PICK_IMAGE);
//        } else {
//            Toast.makeText(getActivity(), "Gallery permission denied", Toast.LENGTH_SHORT).show();
//        }
    }

    private Uri getOutputMediaFileUri(int type) {
        if (Build.VERSION.SDK_INT > 23) {
            Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));
            return photoURI;
        } else {
            return Uri.fromFile(getOutputMediaFile(type));
        }
    }

    private static File getOutputMediaFile(int type) {
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "DmssImages");

        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                Log.d("DmssImages", "Failed to create directory BurbankImages.");
                return null;
            }
        }

        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("dd:MM:yyyy_HH:mm:ss").format(date.getTime());
        String removeColonString = timeStamp.replace(":", "-");


        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + removeColonString + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    // *
    //  * Callback received when a permissions request has been completed.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            controller.setCameraPermissionAvailable(true);
            controller.setGalleryPermissionAvailable(true);
            captureImage();
        } else if (requestCode == REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            controller.setCameraPermissionAvailable(true);
            controller.setGalleryPermissionAvailable(false);
            captureImage();
        } else if (requestCode == REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            controller.setCameraPermissionAvailable(false);
            controller.setGalleryPermissionAvailable(true);
            captureImage();
        } else if (requestCode == REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            controller.setCameraPermissionAvailable(false);
            controller.setGalleryPermissionAvailable(false);
            Toast.makeText(getActivity(), "Please give permissions from setting for camera and storage", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();

        } else {
            Toast.makeText(getActivity(), "Please give permissions from setting for camera and storage", Toast.LENGTH_SHORT).show();
            //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void SubAdapterCallBack(String url) {
        if(url.length()>0){
            dialogToShowWallpaper(url);
        }
    }

    /*@Override
    public void teamsItemLick(View view, int position) {
        String url=ConstantKeys.getImageUrl+allNewsFeedModelArrayList.get(position).getFBImageUrl();
        dialogToShowWallpaper(url);
    }*/
}
