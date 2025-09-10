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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.digitalminds.dmssevent.common.CircleTransform;
import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsEventsBaseActivity;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.TouchImageView;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;
import com.digitalminds.dmssevent.models.UserProfileModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;

public class ProfileActivity extends DmsEventsBaseActivity implements WebServiceResponseCallBack, View.OnClickListener {

    TextView userNameTextView, userRoleTextView, textViewEmployeeId, userEmailTextView, userDepartmentTextView, userPhoneTextView, textViewApply;
    UserProfileModel userProfileModel, userProfileModelUpdation;
    ImageView addProfilePicImageView;
    ImageView imageViewProfilePic;
    TouchImageView profilePictureOverViewImageView;
    //RelativeLayout overViewLayout;
    ImageView overViewClose;
    Toolbar toolbar;
    TextView toolbar_title;
    Uri filePath;
    final private int PICK_IMAGE = 1;
    public static final int MEDIA_TYPE_IMAGE = 5;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 123;
    final private int REQUEST_CODE_ASK_EXTERNAL_STORAGE_PERMISSIONS = 321;
    boolean cameraPermissionAvailable = false, galleryPermissionAvailable = false;
    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE = 122;
    public static File mediaFile;
    LinearLayout lineaLyAddPhoto;
    Context thisClassContext;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    DmsEventsAppController controller;
    //public static boolean profileCallForSaving = false;
    String uploadedImageUrl;
    boolean overViewVisible = false;
    String imagePath, imageName, encodedImage;
    boolean isImageCaptured = false;
    Dialog dialog;
    Bitmap scaledBitmap = null;
    Bitmap scaled;
    Bitmap scaledGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("My Profile");
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        thisClassContext = ProfileActivity.this;
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        userProfileModel = DmsSharedPreferences.getUserDetails(ProfileActivity.this);
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        lineaLyAddPhoto = (LinearLayout) findViewById(R.id.lineaLyAddPhoto);
        textViewEmployeeId = (TextView) findViewById(R.id.textViewEmployeeId);
        userEmailTextView = (TextView) findViewById(R.id.userEmailTextView);
        userDepartmentTextView = (TextView) findViewById(R.id.userDepartmentTextView);
        userPhoneTextView = (TextView) findViewById(R.id.userPhoneTextView);
        imageViewProfilePic = (ImageView) findViewById(R.id.imageViewProfilePic);
        imageViewProfilePic.setOnClickListener(this);
        userRoleTextView = (TextView) findViewById(R.id.userRoleTextView);
        textViewApply = (TextView) findViewById(R.id.textViewApply);
        addProfilePicImageView = (ImageView) findViewById(R.id.addProfilePicImageView);
        //overViewClose = (ImageView) findViewById(R.id.overViewClose);
        profilePictureOverViewImageView = (TouchImageView) findViewById(R.id.profilePictureOverViewImageView);
        // profilePictureOverViewImageView.bringToFront();
        // overViewLayout = (RelativeLayout) findViewById(R.id.overViewLayout);
        //overViewLayout.setVisibility(View.GONE);
        uploadedImageUrl = DmsSharedPreferences.getProfilePicUrl(ProfileActivity.this);
        if ((uploadedImageUrl != null) || (!uploadedImageUrl.equalsIgnoreCase(""))) {
            imageViewProfilePic.setClickable(true);
            Glide.with(thisClassContext).load(ConstantKeys.getImageUrl + uploadedImageUrl).apply(RequestOptions.circleCropTransform()).into(imageViewProfilePic);
            //Picasso.with(thisClassContext).load(ConstantKeys.getImageUrl + uploadedImageUrl).transform(new CircleTransform()).into(imageViewProfilePic);
            //Picasso.with(thisClassContext).load(ConstantKeys.getImageUrl+uploadedImageUrl).into(profilePictureOverViewImageView);
        } else {
            lineaLyAddPhoto.setVisibility(View.GONE);
        }
        textViewApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImageCaptured) {
                    //upLoadImage();
                    callWebApiToAddProfilePhoto();
                } else {
                    Toast.makeText(ProfileActivity.this, "Please Add Image", Toast.LENGTH_SHORT).show();
                }

            }
        });

        addProfilePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });
        if (userProfileModel != null) {
            userNameTextView.setText(userProfileModel.getDisplayName());
            textViewEmployeeId.setText(userProfileModel.getEmpID());
            userEmailTextView.setText(userProfileModel.getEmailID());
            userDepartmentTextView.setText(userProfileModel.getDepartment());
            if (userProfileModel.getMobile().length() > 0) {
                userPhoneTextView.setText(userProfileModel.getMobile());
            } else {
                userPhoneTextView.setText("Not Available");
            }
            userRoleTextView.setText(userProfileModel.getJobTittle().trim());
            /*if((userProfileModel.getProfilePhoto()!=null)) {
                String displayName=userProfileModel.getDisplayName();
                displayName = displayName.replace(" ", ".");
                String profileUrl="http://applicationserver4/sis/StaffPhotos//"+displayName+".jpg";
                Picasso.with(this).load(profileUrl).transform(new CircleTransform()).into(imageView2);
            }else{
                imageView2.setImageResource(R.drawable.profilepic);
            }*/
            //Picasso.with(this).load("http://applicationserver4/sis/StaffPhotos//Sandeep.Kumar.jpg").placeholder(R.drawable.loading).error(R.drawable.appicon).transform(new CircleTransform()).into(imageView2);
            /*Glide.with(this)
                    .load("http://applicationserver4/sis/StaffPhotos/Sandeep.Kumar.jpg")
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(imageView2);*/
            //Picasso.with(this).load("http://www.dmss.co.in/Images/homepage.jpg").transform(new CircleTransform()).into(imageView2);
        } else {
            userNameTextView.setText("User");
            textViewEmployeeId.setText("Employee Id");
            userEmailTextView.setText("Email Id");
            userDepartmentTextView.setText("Department");
            userPhoneTextView.setText("Phone Number");
            userRoleTextView.setText("Role");
        }
    }


    private void dialogToShowProfilePic() {
        uploadedImageUrl = DmsSharedPreferences.getProfilePicUrl(ProfileActivity.this);
        final Dialog dialogForProfile = new Dialog(ProfileActivity.this);
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



        Glide.with(this).load(ConstantKeys.getImageUrl + uploadedImageUrl).apply(options).into(touchImageView);
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

    private void callWebApiToAddProfilePhoto() {
        if (Utils.isNetworkAvailable(ProfileActivity.this)) {
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
            jsonObject.put("EmailID", DmsSharedPreferences.getUserDetails(ProfileActivity.this).getEmailID());
            jsonObject.put("UserGuid", DmsSharedPreferences.getUserDetails(ProfileActivity.this).getEmpGuid());
            jsonObject.put("ImageName", imageName);
            jsonObject.put("ProfileImageChars", encodedImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewProfilePic:
                if ((uploadedImageUrl != null) || (!uploadedImageUrl.equalsIgnoreCase(""))) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    dialogToShowProfilePic();
                    //Picasso.with(thisClassContext).load(ConstantKeys.getImageUrl+uploadedImageUrl).transform(new CircleTransform()).into(touchImageView);
                } else {
                    Toast.makeText(ProfileActivity.this, "No Image to display", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void actionBarSettings() {

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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                imagePath = filePath.getPath();
                File file = new File(imagePath);
                imageName = file.getName();
                isImageCaptured = true;
                String newPath = Utils.compressImage(ProfileActivity.this,filePath.toString(),mediaFile);
                Bitmap bitmapImage = BitmapFactory.decodeFile(newPath);
                //detectFaces(bitmapImage);
                Uri uri = getImageContentUri(ProfileActivity.this, new File(newPath));
                String newImagePath = Utils.getRealPathFromURI(ProfileActivity.this,filePath.toString(),mediaFile);
                /*try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);*/

                //Bitmap bitmapImage = BitmapFactory.decodeFile(newPath);
                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                encodedImage = encodeFromString(scaled);
                System.gc();
                imageViewProfilePic.bringToFront();
                addProfilePicImageView.bringToFront();
                imageViewProfilePic.setClickable(false);
                Glide.with(thisClassContext).load(uri).apply(RequestOptions.circleCropTransform()).into(imageViewProfilePic);
                //Picasso.with(thisClassContext).load(uri).transform(new CircleTransform()).into(imageViewProfilePic);
                lineaLyAddPhoto.setVisibility(View.VISIBLE);
                Toast.makeText(this, " Picture was taken ", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                lineaLyAddPhoto.setVisibility(View.GONE);
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {
                lineaLyAddPhoto.setVisibility(View.GONE);
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }


        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
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
          /*  try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

                encodedImage = encodeFromString(resizedBitmap);
                //image = ConvertBitmapToString(resizedBitmap);
                //Upload();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

            String newPath1 = Utils.compressImage(ProfileActivity.this,imagePath.toString(),mediaFile);
            Bitmap bitmapImage = BitmapFactory.decodeFile(newPath1);
            //Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath.toString());
            int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
            scaledGallery = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
            encodedImage = encodeFromString(scaledGallery);
            //imageViewProfilePic.setImageBitmap(bitmap);
            addProfilePicImageView.bringToFront();
            Picasso.with(thisClassContext).load(file).transform(new CircleTransform()).into(imageViewProfilePic);
            imageViewProfilePic.setClickable(false);
            lineaLyAddPhoto.setVisibility(View.VISIBLE);
            // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));

        } else {
            lineaLyAddPhoto.setVisibility(View.GONE);
            //Toast.makeText(this, " This Image cannot be stored .please try with some other Image. ", Toast.LENGTH_SHORT).show();
        }
    }

    //method to convert the selected image to base64 encoded string
    public static String encodeFromString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public void upLoadImage() {
        if (filePath != null) {
            progressDialog.show();
            java.util.Date date = new java.util.Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
            String userName = DmsSharedPreferences.getUserDetails(thisClassContext).getFirstName();
            UserProfileModel userProfileModel = DmsSharedPreferences.getUserDetails(ProfileActivity.this);
            final String imageName = "Profile_pics/" + userProfileModel.getEmpID() + "_" + userProfileModel.getDisplayName() + ".jpg";
            StorageReference childRef = controller.getStorageRef().child(imageName);
            //uploading the image
            //compressImage(filePath.getPath());
            //UploadTask uploadTask = childRef.putFile(filePath);
            String newPath = Utils.compressImage(ProfileActivity.this,filePath.toString(),mediaFile);
            Uri uri = getImageContentUri(thisClassContext, new File(newPath));
            UploadTask uploadTask = childRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    uploadedImageUrl = taskSnapshot.getDownloadUrl().toString();
                    if (uploadedImageUrl != null && !uploadedImageUrl.isEmpty()) {
                        Picasso.with(thisClassContext).load(uploadedImageUrl).transform(new CircleTransform()).into(imageViewProfilePic);
                        Picasso.with(thisClassContext).load(uploadedImageUrl).into(profilePictureOverViewImageView);
                    }
                    DmsSharedPreferences.saveProfilePicUrl(ProfileActivity.this, uploadedImageUrl);
                    //getUrlOfUploadedImage(imageName);
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(thisClassContext, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(thisClassContext, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

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

    /**
     * Checks that the application is having the camera and gallery permissions or not.
     * if not asks for the permission.
     **/
    public void checkPermissions() {
        int hasPermission = ActivityCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CAMERA);
        int hasWritePermission = ActivityCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = ActivityCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED && hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE);
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
        dialog = new Dialog(ProfileActivity.this);
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
                        Toast.makeText(ProfileActivity.this,
                                "camera permission denied", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this,
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
                    //controller.setMakeServiceCall(true);
//                } else {
//                    Toast.makeText(ProfileActivity.this,
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
        dialog.dismiss();
        if (controller.isCameraPermissionAvailable() == true) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            filePath = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            Toast.makeText(ProfileActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
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
//

            }

    private Uri getOutputMediaFileUri(int type) {
        if (Build.VERSION.SDK_INT > 23) {
            Uri photoURI = FileProvider.getUriForFile(thisClassContext, ProfileActivity.this.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*if (overViewVisible) {
            overViewLayout.setVisibility(View.GONE);
            overViewVisible = false;
        } else {
            finish();
        }*/
    }

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                final String message = jsonObject.getString(ConstantKeys.messageKey);

                if (status) {
                    JSONObject jsonResultObject = jsonObject.getJSONObject(ConstantKeys.resultKey);
                    userProfileModelUpdation = new UserProfileModel(jsonResultObject);
                    DmsSharedPreferences.saveUserDetails(ProfileActivity.this, userProfileModelUpdation);
                    DmsSharedPreferences.saveProfilePicUrl(ProfileActivity.this, userProfileModelUpdation.getProfileImageURL());
                    controller.setUserProfileModel(userProfileModelUpdation);
                    if (isImageCaptured == true) {
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lineaLyAddPhoto.setVisibility(View.GONE);
                                imageViewProfilePic.setClickable(true);
                                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //  }
                   /* if(apiCallType==2) {
                        Intent i=new Intent(LoginActivity.this,OTPVerification.class);
                        startActivity(i);
                        finish();
                    }*/
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
                    Utils.showToast(ProfileActivity.this, error);
                } else {
                    Utils.showToast(ProfileActivity.this, "Network Error");
                }
                progressDialog.cancel();
            }
        });
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
            Toast.makeText(ProfileActivity.this, "Please give permissions from setting for camera and storage", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();

        } else {
            Toast.makeText(ProfileActivity.this, "Please give permissions from setting for camera and storage", Toast.LENGTH_SHORT).show();
            //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onBackClick(View view) {
        getSupportFragmentManager().popBackStack();
    }
}
