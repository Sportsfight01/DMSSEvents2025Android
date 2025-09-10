package com.digitalminds.dmssevent.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import com.digitalminds.dmssevent.AddNewFeedActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Sandeep.Kumar on 18-01-2018.
 */

public class Util {

    public static boolean isNetworkAvailable(Activity act) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            Toast.makeText(act, "Internet Unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static   void makeFolder(String path, String folder) {
        File directory = new File(path, folder);
        if (directory.exists() == false) {
            directory.mkdirs();
        }

    }


    public static void showToast(final Activity act, final String message) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, message, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public static void cancelDialog(final Activity act, final Dialog dialog) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
            }
        });

    }
    public static void showDialog(final Activity act, final Dialog dialog) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });

    }
    public static void startActivityCommon(final Activity act, final Intent in) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                act.startActivity(in);
            }
        });

    }

    public static String getDeviceID(Activity act) {
        String deviceId = "";
        deviceId = Settings.Secure.getString(act.getContentResolver(), Settings.Secure.ANDROID_ID);

        return deviceId;
    }

    public static boolean getStatus(String data) {
        boolean status = false;
        try {
            JSONObject jsonObject = new JSONObject(data);
            status = jsonObject.getBoolean("Status");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return status;
    }



    public static String getMessage(String value) {
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(value);
            message = jsonObject.getString("Message");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return message;
    }

    /* * camera module popup
    *************************************/
    public static void selectImageDialog(final Activity act) {
        final CharSequence[] items = { "Take Photo","Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Profile Pic");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (isDeviceSupportCamera(act)) {
                        captureImage(act);
                    } else {
                        Toast.makeText(act, "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                    }
                } else if (items[item].equals("Choose from gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    act.startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            AddNewFeedActivity.SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    /**
     * Checking device has camera hardware or not
     */
    private static boolean isDeviceSupportCamera(Activity act) {
        if (act.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static void captureImage(Activity act) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Common.imageUri = getOutputMediaFileUri(Common.MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Common.imageUri);

        // start the image capture Intent
        try {
            act.startActivityForResult(intent, AddNewFeedActivity.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    public static Uri getOutputMediaFileUri(int type) {
        File tempFile = getOutputMediaFile(type);
        Uri uri = Uri.fromFile(tempFile);
        return uri;
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Common.sdCardPath);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Common.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        File files = mediaFile;
        return mediaFile;
    }

    public static byte[] getImageByteArray(File inputFile) {
        try {
            FileInputStream input = new FileInputStream(inputFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[65536];
            int l;

            while ((l = input.read(buffer)) > 0)
                output.write(buffer, 0, l);

            input.close();
            output.close();

            return output.toByteArray();

        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }
}

