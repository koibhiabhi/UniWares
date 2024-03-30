package com.example.uniwares;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFcmService extends FirebaseMessagingService {


    private static final String TAG = "FCM_SERVICE_TAG";

    private static final String ADMIN_CHANNEL_ID = "ADMIN_CHANNEL_ID";





    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String title = ""+remoteMessage.getNotification().getTitle();
        String body = ""+remoteMessage.getNotification().getBody();
        String senderID = ""+remoteMessage.getData().get("senderID");
        String notificationType = ""+remoteMessage.getData().get("notificationType");

        Log.d(TAG,"onMessageReceived: title: "+title);

        Log.d(TAG,"onMessageReceived: body: "+body);

        Log.d(TAG,"onMessageReceived: senderUri: "+senderID);

        Log.d(TAG,"onMessageReceived: notificationType: "+notificationType);




        showChatnotification(title, body, senderID);




    }



    private void showChatnotification(String notificationTitle, String notificationDescription, String senderID) {


        int notificationId = new Random().nextInt(3600);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        setupNotificationChannel(notificationManager);

        Intent intent = new Intent(this, chat_screen.class);

        intent.putExtra("recieptUid", senderID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "" + ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ddddd)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }






    private void setupNotificationChannel(NotificationManager notificationManager){



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel =  new NotificationChannel(
                    ADMIN_CHANNEL_ID,
                    "CHAT_CHANNEL",
                    NotificationManager.IMPORTANCE_HIGH

            );


            notificationChannel.setDescription("Show Chat Notifications.");
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }


    }

}
