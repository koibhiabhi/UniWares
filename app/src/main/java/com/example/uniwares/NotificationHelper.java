package com.example.uniwares;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationHelper {

    private static final String CHANNEL_ID = "uniwares_channel_id";
    private static final int NOTIFICATION_ID = 1;

    private Context context;
    private DatabaseReference adsRef, usersRef;
    private FirebaseUser currentUser;

    public NotificationHelper(Context context) {
        this.context = context;
        adsRef = FirebaseDatabase.getInstance().getReference("Ads");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void sendNotificationToSeller(String productTitle, String sellerUid) {
        Log.d("NotificationHelper", "Sending notification to seller for product: " + productTitle);

        adsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot adSnapshot : userSnapshot.getChildren()) {
                        String title = adSnapshot.child("title").getValue(String.class);
                        if (title != null && title.equals(productTitle)) {
                            String sellerUid = adSnapshot.child("uid").getValue(String.class);
                            Log.d("NotificationHelper", " " + productTitle);
                            if (sellerUid != null) {
                                usersRef.child(sellerUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot userSnapshot) {
                                        if (userSnapshot.exists()) {
                                            String sellerFcmToken = userSnapshot.child("fcmToken").getValue(String.class);
                                            Log.d("NotificationHelper", "SellerFCM: " + sellerFcmToken);


                                            String sellerName = userSnapshot.child("username").getValue(String.class);

                                            String buyerUid = currentUser.getUid();
                                            usersRef.child(buyerUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot buyerSnapshot) {
                                                    if (buyerSnapshot.exists()) {
                                                        String buyerName = buyerSnapshot.child("username").getValue(String.class);

                                                        if (sellerFcmToken != null && sellerName != null && buyerName != null) {
                                                            String notificationTitle = "New Order Request";
                                                            String notificationBody = buyerName + " wants to buy your product: " + productTitle;

                                                            Log.d("NotificationHelper", "Sending notification to seller: " + sellerName + " (" + sellerUid + ")");
                                                            sendNotification(notificationTitle, notificationBody, sellerFcmToken);
                                                        } else {
                                                            Log.e("NotificationHelper", "Error: sellerFcmToken, sellerName, or buyerName is null");
                                                        }
                                                    } else {
                                                        Log.e("NotificationHelper", "Error: buyerSnapshot does not exist");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Log.e("NotificationHelper", "Firebase database error: " + databaseError.getMessage());
                                                }
                                            });
                                        } else {
                                            Log.e("NotificationHelper", "Error: userSnapshot does not exist");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("NotificationHelper", "Firebase database error: " + databaseError.getMessage());
                                    }
                                });
                            } else {
                                Log.e("NotificationHelper", "Error: sellerUid is null");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("NotificationHelper", "Firebase database error: " + databaseError.getMessage());
            }
        });

    }






    @SuppressLint("MissingPermission")
//    private void sendNotification(String title, String body, String fcmToken) {
//        Log.d("NotificationHelper", "Sending notification for product: " + title);
//
//        createNotificationChannel();
//
//        Intent intent = new Intent(context, home.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ddddd)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//
//        // Send the notification to the FCM token
//        // Use an FCM library like Firebase Cloud Messaging to send the notification
//    }


    private void sendNotification(String title, String body, String sellerFcmToken) {
        Log.d("NotificationHelper", "Sending notification for product: " + title);

        createNotificationChannel();

        Intent intent = new Intent(context, home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ddddd)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        // Use an FCM library like Firebase Cloud Messaging to send the notification to the seller's FCM token
        // For demonstration purposes, this code shows how you would send the notification
        // using FirebaseMessaging.getInstance().send() method. Replace this with the actual implementation.
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(sellerFcmToken)
                .setMessageId(Integer.toString(NOTIFICATION_ID))
                .addData("title", title)
                .addData("body", body)
                .build());
    }





    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
