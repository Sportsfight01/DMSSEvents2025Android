package com.digitalminds.dmssevent.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.common.DmsEventsAppController;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

/**
 * Created by Sandeep.Kumar on 01-03-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    NotificationCompat.Builder notificationBuilder;
    DmsEventsAppController appController;
    String imageUrl = "";
    String message = "";
    String title = "";
    Notification notification;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        appController = (DmsEventsAppController) getApplicationContext();
        appController.setFromNotification(true);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Map<String, String> data = remoteMessage.getData();
        String myCustomKey = data.get("my_custom_key");
//        Log.d(TAG, "From: " + data.toString());
        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//        handleDataMessage(remoteMessage);
        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.

        //message will contain the Push Message
        //String title = remoteMessage.getData().get("title").toString();


       /* try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());
            handleDataMessage(json);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }*/

        /*try {
            imageUrl = remoteMessage.getNotification().getIcon();
             title =remoteMessage.getNotification().getTitle();
             message= remoteMessage.getNotification().getBody();
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        //imageUri will contain URL of the image to be displayed with Notification
        //String imageUri = remoteMessage.getData().get("image");
        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
        if ( imageUrl!=null) {
            //To get a Bitmap image from the URL received
            bitmap = getBitmapfromUrl(imageUrl);
            NotificationsDetailView.bmp = bitmap;
            NotificationsDetailView.messageValue = message;
            NotificationsDetailView.titleValue=title;
            generateNotification( title,message);
            //StaticPage.bmp = bitmap;
          //  sendNotification(message, bitmap, TrueOrFlase);
        }else{
            NotificationsDetailView.messageValue = message;
            NotificationsDetailView.titleValue=title;
            generateNotification( title,message);
            //Check if Notn are Enabled?
            *//*if (getPrefernceHelperInstace().getBoolean(getApplicationContext(), ENABLE_NOTIFICATION, true)) {
                generateNotification( title,message);
                //Show Notification
            } else {
                Log.e(TAG, "ReactFireBaseMessagingService: Notifications Are Disabled by User");

            }*//*

        }*/


}

    private void handleDataMessage(RemoteMessage remoteMessage) {
        try {
            imageUrl = remoteMessage.getNotification().getIcon();
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        //String timestamp = data.getString("timestamp");
        //JSONObject payload = data.getJSONObject("payload");
        if(imageUrl!=null){
        if (imageUrl.length() > 0) {
            //To get a Bitmap image from the URL received
            bitmap = getBitmapfromUrl(imageUrl);
            NotificationsDetailView.imageUrl = imageUrl;
            NotificationsDetailView.messageValue = message;
            NotificationsDetailView.titleValue = title;
            if (appController.isLoggedInStatus()) {
                generateNotification(title, message, bitmap);
            }
            //StaticPage.bmp = bitmap;
            //  sendNotification(message, bitmap, TrueOrFlase);
        }
    }else{
                NotificationsDetailView.messageValue = message;
                NotificationsDetailView.titleValue=title;
                if(appController.isLoggedInStatus()){
                    generateNotification( title,message);
                }
                //Check if Notn are Enabled?
          /*  if (getPrefernceHelperInstace().getBoolean(getApplicationContext(), ENABLE_NOTIFICATION, true)) {
                generateNotification( title,message);
                //Show Notification
            } else {
                Log.e(TAG, "ReactFireBaseMessagingService: Notifications Are Disabled by User");

            }*/

            }
           /* Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);
*/

          /*  if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }*/

    }
//    private void sendNotification(String messageBody, Bitmap image, String TrueOrFalse) {
//        Intent intent = new Intent(this, StaticPage.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("AnotherActivity", TrueOrFalse);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,PendingIntent.FLAG_ONE_SHOT);
//        Bitmap bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo_s);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setLargeIcon(bitmap)/*Notification icon image*/
//                .setSmallIcon(R.drawable.logo_s)
//                .setContentTitle(messageBody)
//                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))/*Notification with Image*/
//                .setAutoCancel(true)
//                .setDefaults( Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        /*try {
            URL url = new URL(imageUrl);
            InputStream in = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in,1024*8);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int len=0;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer)) != -1){
                out.write(buffer, 0, len);
            }
            out.close();
            bis.close();

            byte[] data = out.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitmap;
            //imageView.setImageBitmap(bitmap);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }*/
    }

    public void generateNotification(String title, String message) {
        Intent intent = new Intent(this, NotificationsDetailView.class);
        intent.putExtra("NotificationCall","NotificationCall");
        //FragmentsMainActivity.callFromNotifications=true;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager, title, message);
        }
         int notificationId = new Random().nextInt(60000);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri defaultSoundUri1 = Uri.parse("android.resource://"+"com.skooladvisor"+"/"+R.raw.furrow);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            notificationBuilder= new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.app_icon_main)
           .setColor(getResources().getColor(R.color.colorBlue))
           .setContentTitle(title)
            .setContentText(message)
                    //.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)

                    .setVibrate(new long[] {0, 1000, 200,1000 })
                    .setLights(Color.MAGENTA, 500, 500)
                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setPriority((Notification.PRIORITY_MAX))
                            //.setSound(defaultSoundUri)
                    .setAutoCancel(true)
                   /* .addAction(R.drawable.icon_login_logo, "SkoolNoficationClick", pendingIntent)*/
                    .setContentIntent(pendingIntent);
        }else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            notificationBuilder= new NotificationCompat.Builder(this)
                    /*.setLargeIcon(bitmap)*/
                    .setSmallIcon(R.drawable.app_icon_main)
                    .setContentTitle(title)
                    .setColor(getResources().getColor(R.color.colorBlue))
                    .setContentText(message)
                   // .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                   .setSound(defaultSoundUri)
                    .setPriority((Notification.PRIORITY_MAX))
                    .setVibrate(new long[] {0, 1000, 200,1000 })
                    .setLights(Color.MAGENTA, 500, 500)
                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        }
//        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

    }
    public void generateNotification(String title, String message,Bitmap bitmap) {
        Intent intent = new Intent(this, NotificationsDetailView.class);
        intent.putExtra("NotificationCall","NotificationCall");
        //FragmentsMainActivity.callFromNotifications=true;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager, title, message);
        }
        int notificationId = new Random().nextInt(60000);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri defaultSoundUri1 = Uri.parse("android.resource://"+"com.skooladvisor"+"/"+R.raw.furrow);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
            bigPictureStyle.bigPicture(bitmap);

           /* NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            notification = mBuilder.setSmallIcon(R.drawable.app_icon_main).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(pendingIntent)
                    .setSound(defaultSoundUri)
                    .setColor(getResources().getColor(R.color.colorBlue))
                    .setStyle(bigPictureStyle)
                    *//*.setWhen(getTimeMilliSec(timeStamp))*//*
                    .setSmallIcon(R.drawable.app_icon_main)
                    *//*.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))*//*
                    .setLargeIcon(bitmap)
                    .setContentText(message)
                    .build();*/

            notificationBuilder= new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.app_icon_main)
           .setColor(getResources().getColor(R.color.colorBlue))
           .setContentTitle(title)
            .setContentText(message)
                    //.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)

                    .setVibrate(new long[] {0, 1000, 200,1000 })
                    .setLights(Color.MAGENTA, 500, 500)
                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setPriority((Notification.PRIORITY_MAX))
                            //.setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setStyle(bigPictureStyle)
                    //.addAction(R.drawable.icon_login_logo, "SkoolNoficationClick", pendingIntent)
                    .setContentIntent(pendingIntent);
        }else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
            bigPictureStyle.bigPicture(bitmap);

            /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);



            notification = mBuilder.setSmallIcon(R.drawable.app_icon_main).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(pendingIntent)
                    .setSound(defaultSoundUri)
                    .setColor(getResources().getColor(R.color.colorBlue))
                    .setStyle(bigPictureStyle)
                    *//*.setWhen(getTimeMilliSec(timeStamp))*//*
                    .setSmallIcon(R.drawable.app_icon_main)
                    *//*.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))*//*
                    .setLargeIcon(bitmap)
                    .setContentText(message)
                    .build();*/
            notificationBuilder= new NotificationCompat.Builder(this)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.app_icon_main)
                    .setContentTitle(title)
                    .setColor(getResources().getColor(R.color.colorBlue))
                    .setContentText(message)

                   // .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                   .setSound(defaultSoundUri)
                    .setStyle(bigPictureStyle)
                    .setPriority((Notification.PRIORITY_MAX))
                    .setVibrate(new long[] {0, 1000, 200,1000 })
                    .setLights(Color.MAGENTA, 500, 500)
                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        }
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager, String title, String message) {
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel("12345", title, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(message);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}

