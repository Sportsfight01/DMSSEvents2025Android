package com.digitalminds.dmssevent;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.adapters.GalleryRecycleViewAdapter;
import com.digitalminds.dmssevent.adapters.GalleryViewPagerAdapter;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsEventsBaseActivity;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.AdapterCallBack;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.AlbumsModel;
import com.digitalminds.dmssevent.models.PhotoDetailsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by sandeep.kumar on 14-03-2017.
 */
public class GalleryActivity extends DmsEventsBaseActivity implements AdapterCallBack, WebServiceResponseCallBack {
    RecyclerView recyclerView;
    ViewPager imageViewPager;
    ArrayList<PhotoDetailsModel> photoDetailsModels = new ArrayList<PhotoDetailsModel>();
    AlbumsModel albumsModel;
    DmsEventsAppController controller;
    GalleryRecycleViewAdapter galleryRecycleViewAdapter;
    GalleryViewPagerAdapter galleryViewPagerAdapter;
    public static final String TAG = "GalleryActivity";
    public static final String EXTRA_NAME = "images";
    androidx.appcompat.app.ActionBar actionBar;
    Context thisContext;
    int selectedPosition = 0, needToDeletePosition;

    Dialog dialog;
    Context context;
    static int AddImageApiCall = 2, DeleteImageApiCAll = 3, UpdateAlbumName = 4;
    int apiCall;
    ProgressDialog progressDialog;
    Dialog deleteImageDialog;
    ImageView shareImageButton, addImageButton, deleteImageButton;
    TextView textViewAlbumName;
    ImageView editImageView;
    String uploadedImageUrl;
    Toolbar toolbar;
    TextView toolbar_title;
    Uri filePath;
    final private int PICK_IMAGE = 1;
    public static final int MEDIA_TYPE_IMAGE = 5;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 123;
    final private int REQUEST_CODE_ASK_EXTERNAL_STORAGE_PERMISSIONS = 321;
    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE = 122;
    String editedAlbumName;
    TextView noImagesTextView;
    public static File mediaFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //actionBarSettings();
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Gallery");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        System.gc();
        progressDialog = new ProgressDialog(GalleryActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        context = GalleryActivity.this;
        controller = (DmsEventsAppController) getApplicationContext();
        thisContext = GalleryActivity.this;
        albumsModel = controller.getSelectedAlbum();
        photoDetailsModels.addAll(albumsModel.getAlbumImageList());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisContext, LinearLayoutManager.HORIZONTAL, false));
        imageViewPager = (ViewPager) findViewById(R.id.imageViewPager);
        shareImageButton = (ImageView) findViewById(R.id.shareImageButton);
        shareImageButton.setOnClickListener(this);
        addImageButton = (ImageView) findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(this);
        deleteImageButton = (ImageView) findViewById(R.id.deleteImageButton);
        deleteImageButton.setOnClickListener(this);
        textViewAlbumName = (TextView) findViewById(R.id.textViewAlbumName);
        noImagesTextView = (TextView) findViewById(R.id.noImagesTextView);
        editImageView = (ImageView) findViewById(R.id.editImageView);
        editImageView.setOnClickListener(this);
        galleryViewPagerAdapter = new GalleryViewPagerAdapter(photoDetailsModels, thisContext);
        galleryRecycleViewAdapter = new GalleryRecycleViewAdapter(photoDetailsModels, thisContext, 0, DmsSharedPreferences.getUserDetails(GalleryActivity.this).getId());
        textViewAlbumName.setText(albumsModel.getAlbumName() + "  (" + albumsModel.getPhotosCount() + ")");
        recyclerView.setAdapter(galleryRecycleViewAdapter);
        imageViewPager.setAdapter(galleryViewPagerAdapter);
        //imageViewPager.setPageTransformer(true,StackTransformer.class);
        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                galleryRecycleViewAdapter.setSelectedPosition(position);
                recyclerView.smoothScrollToPosition(position);
                selectedPosition = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        checkListEmpty();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareImageButton:
                if (photoDetailsModels.size() > 0) {
                    shareImage();
                } else {
                    Utils.showToast(GalleryActivity.this, "No images to share...");
                }
                break;
            case R.id.addImageButton:
                checkPermissions();
                break;
            case R.id.deleteImageButton:
                if (photoDetailsModels.size() > 0) {
                    needToDeletePosition = selectedPosition;
                    boolean owner;
                    if (DmsSharedPreferences.getUserDetails(GalleryActivity.this).getId() == photoDetailsModels.get(needToDeletePosition).getCreatedBy()) {
                        owner = true;
                    } else {
                        owner = false;
                    }
                    displayDeleteImageDialog(owner, false);
                } else {
                    Utils.showToast(GalleryActivity.this, "No images to delete...");
                }

                break;
            case R.id.editImageView:
                boolean owner1;
                if (controller.getUserProfileModel().getId() == controller.getSelectedAlbum().getCreatedBy()) {
                    owner1 = true;
                } else {
                    owner1 = false;
                }
                displayDeleteImageDialog(owner1, true);
                break;
        }
    }

    private void shareImage() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, photoDetailsModels.get(selectedPosition).getImageURL());
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    @Override
    public void actionBarSettings() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        ImageView actionBarBackImageView = (ImageView) view.findViewById(R.id.actionBarBackImageView);
        ImageView imgCreateFolder = (ImageView) view.findViewById(R.id.imgCreateFolder);
        imgCreateFolder.setVisibility(View.GONE);
        actionBarBackImageView.setVisibility(View.VISIBLE);
        actionBarHeadingTextView.setText("Gallery");
        actionBarBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void adapterClickedPosition(int position, boolean subChildItem) {
        if (selectedPosition != position) {
            imageViewPager.setCurrentItem(position, false);
            selectedPosition = position;
        }
    }


    private void callWebApi(int apiCallType, String albumName) {
        /**
         * Checking whether the network is available or not
         */
        if (Utils.isNetworkAvailable(GalleryActivity.this)) {
            if (apiCallType == AddImageApiCall) {
                apiCall = AddImageApiCall;
                controller.getWebService().postData(ConstantKeys.addImagesUrl, getImageJsonData(), this);
            } else if (apiCallType == DeleteImageApiCAll) {
                apiCall = DeleteImageApiCAll;
                String imageId = Integer.toString(photoDetailsModels.get(needToDeletePosition).getId());
                controller.getWebService().deleteData(ConstantKeys.deleteImageUrl + imageId, this);
            } else if (apiCallType == UpdateAlbumName) {
                apiCall = UpdateAlbumName;
                editedAlbumName = albumName;
                controller.getWebService().postData(ConstantKeys.createAlbumUrl, getEditAlbumJsonData(), this);
            }

        } else {
            progressDialog.cancel();
        }
    }

    public String getEditAlbumJsonData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AlbumName", editedAlbumName);
            jsonObject.put("EventId", controller.getSelectedEvent().getId());
            jsonObject.put("ModifiedBy", DmsSharedPreferences.getUserDetails(GalleryActivity.this).getId());
            jsonObject.put("Id", controller.getSelectedAlbum().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String getImageJsonData() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ImageName", " ");
            jsonObject.put("ImageURL", uploadedImageUrl);
            jsonObject.put("AlbumId", controller.getSelectedAlbum().getId());
            jsonObject.put("CreatedBy", controller.getUserProfileModel().getId());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonArray.toString();
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            if (apiCall == AddImageApiCall) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                    Utils.showToast(GalleryActivity.this, jsonObject.getString(ConstantKeys.messageKey));
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.uploadedImagesIdKey);
                        photoDetailsModels.add(new PhotoDetailsModel(" ", uploadedImageUrl, jsonArray.getInt(0), controller.getUserProfileModel().getId()));
                        albumsModel.setPhotosCount(photoDetailsModels.size());
                        //selectedPosition = photoDetailsModels.size()- 1;
                        GalleryActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*galleryRecycleViewAdapter.setSelectedPosition(selectedPosition);*/
                                textViewAlbumName.setText(albumsModel.getAlbumName() + "  (" + albumsModel.getPhotosCount() + ")");
                                galleryRecycleViewAdapter.notifyDataSetChanged();
                                galleryViewPagerAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(photoDetailsModels.size() - 1);
                                Intent intent = getIntent();
                                intent.putExtra("key", "key");
                                setResult(RESULT_OK, intent);
                                //imageViewPager.setCurrentItem(selectedPosition, false);
                            }
                        });

                    } else {
                        deleteUploadedImage(uploadedImageUrl);
                    }
                    /*if (status) {
                        apiCall = AlbumsListApiCall;
                        callWebApi(true);
                    } else {
                        progressDialog.cancel();
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.cancel();
            } else if (apiCall == DeleteImageApiCAll) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                    if (status == true) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                String needToDeleteUrl = photoDetailsModels.get(needToDeletePosition).getImageURL();
                                deleteUploadedImage(needToDeleteUrl);
                                photoDetailsModels.remove(needToDeletePosition);
                                albumsModel.setPhotosCount(photoDetailsModels.size());
                                textViewAlbumName.setText(albumsModel.getAlbumName() + "  (" + albumsModel.getPhotosCount() + ")");
                                galleryViewPagerAdapter.notifyDataSetChanged();
                                galleryRecycleViewAdapter.notifyDataSetChanged();
                                Intent intent = getIntent();
                                intent.putExtra("key", "key");
                                setResult(RESULT_OK, intent);

                            }
                        });
                        controller.setMakeServiceCall(true);
                        Utils.showToast(GalleryActivity.this, jsonObject.getString(ConstantKeys.messageKey));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.cancel();
            } else if (apiCall == UpdateAlbumName) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                    if (status == true) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                albumsModel.setAlbumName(editedAlbumName);
                                textViewAlbumName.setText(albumsModel.getAlbumName() + "  (" + albumsModel.getPhotosCount() + ")");
                                Intent intent = getIntent();
                                intent.putExtra("key", "key");
                                setResult(RESULT_OK, intent);
                            }
                        });
                        Utils.showToast(GalleryActivity.this, jsonObject.getString(ConstantKeys.messageKey));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.cancel();
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    checkListEmpty();
                }
            });


        }
    }

    @Override
    public void onServiceCallFail(String error) {
        if (error != null) {
            Utils.showToast(GalleryActivity.this, error);
        } else {
            Utils.showToast(GalleryActivity.this, "Network Error");
        }
        progressDialog.cancel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                String imgPick = filePath.getPath();
                //filePath = Uri.parse(compressImage(filePath.getPath()));

                upLoadImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(GalleryActivity.this, "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(GalleryActivity.this, "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            filePath = /*Uri.parse(compressImage(*/data.getData()/*.toString()))*/;

            upLoadImage();

            /*String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPick = cursor.getString(columnIndex);
            cursor.close();
            */
            //Toast.makeText(GalleryActivity.this,filePath.getPath(),Toast.LENGTH_SHORT).show();
        }
    }


    public void upLoadImage() {
        if (filePath != null) {
            progressDialog.show();
            java.util.Date date = new java.util.Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
            String userName = DmsSharedPreferences.getUserDetails(GalleryActivity.this).getFirstName();
            final String imageName = ConstantKeys.fBGalleryAlbumName + "/" + controller.getSelectedAlbum().getAlbumName() + "/" + userName + "_" + timeStamp + ".jpg";
            StorageReference childRef = controller.getStorageRef().child(imageName);
            //uploading the image
            //compressImage(filePath.getPath());
            //UploadTask uploadTask = childRef.putFile(filePath);
            String newPath = compressImage(filePath.toString());
            Uri uri = getImageContentUri(GalleryActivity.this, new File(newPath));
            UploadTask uploadTask = childRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    uploadedImageUrl = taskSnapshot.getDownloadUrl().toString();
                    callWebApi(AddImageApiCall, null);
                    //getUrlOfUploadedImage(imageName);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(GalleryActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(GalleryActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    /*public void getUrlOfUploadedImage(String imageName){
        storageRef.child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uploadedImageUrl = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(GalleryActivity.this, "Download Failed" + e, Toast.LENGTH_SHORT).show();

            }
        });
    }*/
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public void deleteUploadedImage(String needToDeleteImage) {
        StorageReference photoRef = controller.getStorage().getReferenceFromUrl(needToDeleteImage);
        photoRef.delete();
    }


    /**
     * Checks that the application is having the camera and gallery permissions or not.
     * if not asks for the permission.
     **/
    public void checkPermissions() {
        int hasPermission = ActivityCompat.checkSelfPermission(GalleryActivity.this, android.Manifest.permission.CAMERA);
        int hasWritePermission = ActivityCompat.checkSelfPermission(GalleryActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = ActivityCompat.checkSelfPermission(GalleryActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED && hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GalleryActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE);
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
        dialog = new Dialog(GalleryActivity.this);
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
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    if (controller.isCameraPermissionAvailable() == true) {
                        callCamera();
                    } else {
                        Toast.makeText(GalleryActivity.this,
                                "camera permission denied", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(GalleryActivity.this,
                            "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
        textViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller.isGalleryPermissionAvailable() == true) {
                    callGallery();
                    //controller.setMakeServiceCall(true);
                } else {
                    Toast.makeText(GalleryActivity.this,
                            "gallery permission denied", Toast.LENGTH_LONG)
                            .show();
                }

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
        dialog.dismiss();
        if (controller.isCameraPermissionAvailable() == true) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            filePath = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            Toast.makeText(GalleryActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void callGallery() {
        dialog.dismiss();
        // Create intent to Open Image applications like Gallery, Google Photos
        if (controller.isGalleryPermissionAvailable() == true) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, PICK_IMAGE);
        } else {
            Toast.makeText(GalleryActivity.this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getOutputMediaFileUri(int type) {
        if (Build.VERSION.SDK_INT > 23) {
            Uri photoURI = FileProvider.getUriForFile(GalleryActivity.this, context.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));
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


        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    public void displayDeleteImageDialog(boolean owner, final boolean edit) {
        deleteImageDialog = new Dialog(GalleryActivity.this);

        deleteImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteImageDialog.setContentView(R.layout.create_album_dialog);

        TextView dialogHeadingTextView = (TextView) deleteImageDialog.findViewById(R.id.dialogHeadingTextView);

        final EditText albumNameEditText = (EditText) deleteImageDialog.findViewById(R.id.albumNameEditText);
        if (edit) {
            albumNameEditText.setVisibility(View.VISIBLE);
        } else {
            albumNameEditText.setVisibility(View.GONE);
        }

        Button createAlbumButton = (Button) deleteImageDialog.findViewById(R.id.createAlbumButton);
        Button deleteImageButton = (Button) deleteImageDialog.findViewById(R.id.deleteImageButton);
        if (owner) {
            if (edit) {
                dialogHeadingTextView.setText("Edit your album name and start adding photos, easy peasy");
                createAlbumButton.setText("Cancel");
                deleteImageButton.setText("Edit");
            } else {
                dialogHeadingTextView.setText("Do you really want to delete this image");
                createAlbumButton.setText("Cancel");
                deleteImageButton.setText("Delete");
            }

            deleteImageButton.setVisibility(View.VISIBLE);
            createAlbumButton.setVisibility(View.VISIBLE);

        } else {
            if (edit) {
                dialogHeadingTextView.setText("You are not owner of this album,you cannot edit the name of this album.");
                albumNameEditText.setVisibility(View.GONE);
            } else {
                dialogHeadingTextView.setText("You are not owner of this picture,you cannot delete this picture");
            }

            deleteImageButton.setVisibility(View.GONE);
            createAlbumButton.setVisibility(View.VISIBLE);
            createAlbumButton.setText("Ok");
        }

        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageDialog.dismiss();
            }
        });

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageDialog.dismiss();
                progressDialog.show();
                if (!edit) {
                    callWebApi(DeleteImageApiCAll, null);
                } else {
                    callWebApi(UpdateAlbumName, albumNameEditText.getText().toString().trim());
                }


            }
        });

        deleteImageDialog.show();
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
//      max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 600.0f;
        float maxWidth = 900.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (index != -1) {
                return cursor.getString(index);
            } else {
                return mediaFile.getAbsolutePath();
            }
        }
    }

    public String getFilename() {
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

        /*File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        //return mediaFile;


        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }*/
        String uriSting = (mediaStorageDir.getAbsolutePath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return uriSting;

    }

    public void checkListEmpty() {
        if (photoDetailsModels.size() > 0) {
            imageViewPager.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            noImagesTextView.setVisibility(View.GONE);
        } else {
            imageViewPager.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noImagesTextView.setVisibility(View.VISIBLE);
        }
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

    /**
     * Callback received when a permissions request has been completed.
     */
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
            Toast.makeText(GalleryActivity.this, "Please give permissions from setting for camera and storage", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();

        } else {
            Toast.makeText(GalleryActivity.this, "Please give permissions from setting for camera and storage", Toast.LENGTH_SHORT).show();
            //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
