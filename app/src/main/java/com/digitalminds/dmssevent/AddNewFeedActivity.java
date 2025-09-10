package com.digitalminds.dmssevent;

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
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalminds.dmssevent.common.ConstantKeys;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.digitalminds.dmssevent.common.DmsSharedPreferences;
import com.digitalminds.dmssevent.common.Utils;
import com.digitalminds.dmssevent.interfaces.WebServiceResponseCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Sandeep.Kumar on 06-02-2018.
 */

public class AddNewFeedActivity extends AppCompatActivity implements View.OnClickListener, WebServiceResponseCallBack {
    String uploadedImageUrl;
    TextView textViewAddImage, textViewUploadImage, textViewCancel;
    ImageView imageViewForPreview, imageViewForPreview1;
    RelativeLayout relativeLyAddImage;
    Uri filePath;
    final private int PICK_IMAGE = 1;
    public static final int MEDIA_TYPE_IMAGE = 5;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    Context context;
    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE = 122;
    public static File mediaFile;
    DmsEventsAppController controller;
    ProgressDialog progressDialog;
    EditText editTextAddTitle;
    Toolbar toolbar;
    TextView toolbar_title;
    LinearLayout emptyElement;
    TextView retryTextView;
    final public static int SELECT_FILE = 02;
    String imagePath, imageName;
    boolean isImageCaptured = false;
    String encodedImage = "";
    int permissionCheck;
    androidx.appcompat.app.AlertDialog.Builder alert;
    Dialog dialog;
    Bitmap scaledBitmap = null;
    Bitmap scaled;
    Bitmap scaledGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feed);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbarGamesList);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("What's in your mind");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        intializeUIElements();
    }

    private void intializeUIElements() {
        System.gc();
        progressDialog = new ProgressDialog(AddNewFeedActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        controller = (DmsEventsAppController) getApplicationContext();
        context = AddNewFeedActivity.this;
        permissionCheck = ContextCompat.checkSelfPermission(AddNewFeedActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        emptyElement = (LinearLayout) findViewById(R.id.emptyElement);
        imageViewForPreview = (ImageView) findViewById(R.id.imageViewForPreview);
        imageViewForPreview1 = (ImageView) findViewById(R.id.imageViewForPreview1);
        textViewAddImage = (TextView) findViewById(R.id.textViewAddImage);
        textViewUploadImage = (TextView) findViewById(R.id.textViewUploadImage);
        textViewCancel = (TextView) findViewById(R.id.textViewCancel);
        editTextAddTitle = (EditText) findViewById(R.id.editTextAddTitle);
        relativeLyAddImage = (RelativeLayout) findViewById(R.id.relativeLyAddImage);
        relativeLyAddImage.setOnClickListener(this);
        textViewUploadImage.setOnClickListener(this);
        textViewCancel.setOnClickListener(this);
    }

    /**
     * Checks that the application is having the camera and gallery permissions or not.
     * if not asks for the permission.
     **/
    public void checkPermissions() {
        int hasPermission = ActivityCompat.checkSelfPermission(AddNewFeedActivity.this, android.Manifest.permission.CAMERA);
        int hasWritePermission = ActivityCompat.checkSelfPermission(AddNewFeedActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = ActivityCompat.checkSelfPermission(AddNewFeedActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED && hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNewFeedActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS_AND_EXTERNAL_STORAGE);
        } else if (hasPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission == PackageManager.PERMISSION_GRANTED) {
            controller.setCameraPermissionAvailable(false);
            controller.setGalleryPermissionAvailable(true);
            captureImage();
        } else if (hasPermission == PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED) {
            controller.setCameraPermissionAvailable(true);
            controller.setGalleryPermissionAvailable(false);
            captureImage();
        } else {
            controller.setCameraPermissionAvailable(true);
            controller.setGalleryPermissionAvailable(true);
            captureImage();
        }
    }

    public void captureImage() {
        dialog = new Dialog(AddNewFeedActivity.this);
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
                        Toast.makeText(AddNewFeedActivity.this,
                                "camera permission denied", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(AddNewFeedActivity.this,
                            "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
        textViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGallery();
/*

                if (controller.isGalleryPermissionAvailable() == true) {
                    callGallery();
                    //controller.setMakeServiceCall(true);
                } else {
                    Toast.makeText(AddNewFeedActivity.this,
                            "gallery permission denied", Toast.LENGTH_LONG)
                            .show();
                }
*/

            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
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
            Toast.makeText(AddNewFeedActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
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
       /* } else {
            Toast.makeText(AddNewFeedActivity.this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
        }*/
    }

    private Uri getOutputMediaFileUri(int type) {
        if (Build.VERSION.SDK_INT > 23) {
            Uri photoURI = FileProvider.getUriForFile(AddNewFeedActivity.this, context.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));
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

    public String compressImage(String imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        scaledBitmap = null;
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

        /*float maxHeight = 1280.0f;
        float maxWidth = 1280.0f;*/
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
// load the bitmap from its path
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
        //canvas.drawBitmap(bmp, new Rect(100,100,100,0), new Rect(100,100,100,0), null);
        //canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

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
        String removeColonString = timeStamp.replace(":", "-");
        String uriSting = (mediaStorageDir.getAbsolutePath() + File.separator + "IMG_" + removeColonString + ".jpg");
        return uriSting;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            System.gc();
            if (resultCode == RESULT_OK) {
                imagePath = filePath.getPath();
                File file = new File(imagePath);
                imageName = file.getName();
                isImageCaptured = true;
                String newPath = compressImage(filePath.toString());
                Bitmap bitmapImage = BitmapFactory.decodeFile(newPath);
                Uri uri = getImageContentUri(AddNewFeedActivity.this, new File(newPath));
                String newImagePath = getRealPathFromURI(filePath.toString());
                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                encodedImage = encodeFromString(scaled);
                System.gc();
                imageViewForPreview.setImageURI(uri);
                Toast.makeText(this, " Picture was taken ", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
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
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            String newPath1 = compressImage(imagePath.toString());
            Bitmap bitmapImage = BitmapFactory.decodeFile(newPath1);
            //Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath.toString());
            int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
            scaledGallery = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
            encodedImage = encodeFromString(scaledGallery);
            imageViewForPreview.setImageBitmap(scaledGallery);
        } else {
            // Toast.makeText(this, " This Image cannot be stored .please try with some other Image. ", Toast.LENGTH_SHORT).show();
        }
    }

    //method to convert the selected image to base64 encoded string
    public static String encodeFromString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLyAddImage:
                checkPermissions();
                break;
            case R.id.textViewCancel:
                editTextAddTitle.setText("");
                isImageCaptured = false;
                imageViewForPreview.setImageResource(android.R.color.transparent);
                break;
            case R.id.textViewUploadImage:

                if (editTextAddTitle.length() > 0 || isImageCaptured) {
                    callWebApiForAddNewFeed();
                } else {
                    Toast.makeText(this, "Please Add Image or Title", Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }

    private void callWebApiForAddNewFeed() {
        if (Utils.isNetworkAvailable(AddNewFeedActivity.this)) {
            progressDialog.show();
            emptyElement.setVisibility(View.GONE);
            String url = ConstantKeys.addNewFeed;
            controller.getWebService().postData(url, getJsonDataAddNewFeed(), this);
        } else {
            progressDialog.cancel();
            emptyElement.setVisibility(View.VISIBLE);
            retryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callWebApiForAddNewFeed();
                }
            });
        }
    }

    private String getJsonDataAddNewFeed() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ImageName", imageName);
            jsonObject.put("ImageChars", encodedImage);
            jsonObject.put("EmployeeId", DmsSharedPreferences.getUserDetails(AddNewFeedActivity.this).getEmpID());
            jsonObject.put("Title", editTextAddTitle.getText().toString().trim());
            jsonObject.put("EmpId", DmsSharedPreferences.getUserDetails(AddNewFeedActivity.this).getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject.toString();
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

    @Override
    public void onServiceCallSuccess(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean status = jsonObject.getBoolean(ConstantKeys.statusJsonKey);
                final String message = jsonObject.getString(ConstantKeys.messageKey);
                //JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeys.resultKey);
                if (status) {
                    this.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(AddNewFeedActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            intent.putExtra("key", "Success");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }));

                } else {
                    Utils.showToast(AddNewFeedActivity.this, "Server Error");
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
            Utils.showToast(AddNewFeedActivity.this, error);
        } else {
            Utils.showToast(AddNewFeedActivity.this, "Network Error");
        }
        progressDialog.cancel();
    }

    // *
    //  * Callback received when a permissions request has been completed.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            Toast.makeText(AddNewFeedActivity.this, "Please give permissions from setting for camera and storage", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(AddNewFeedActivity.this, "Please give permissions from setting for camera and storage", Toast.LENGTH_SHORT).show();
        }
    }

}
