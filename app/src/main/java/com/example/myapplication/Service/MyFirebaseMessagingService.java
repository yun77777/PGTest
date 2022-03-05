package com.example.myapplication.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.Activity.WebViewActivity;
import com.example.myapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /*
    {
        "to": "token",
            "priority" :"high",
//            "notification": { //background message
//                    "title": "Postma@n",
//                            "body" : "he@llo worl@d",
//                            "channel_id": "my_channel_02"
//                },
            "data": { //foreground message
            "title": "@",
                    "body" : "he@llo wo2rl1d",
                    "channel_id": "my_channel_02"
            }
    }
    */

    private Switch swc_push;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private static final String TAG = "FirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived");
        SharedPreferences sharedPreferences = getSharedPreferences("shared", 0);
        String isAllowed = sharedPreferences.getString("push", "");

        Log.e("isAllowed: ", isAllowed);

        if(isAllowed.equals("false")) return;

        if(remoteMessage.getNotification() != null) { //foreground
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        } else if(remoteMessage.getData().size() > 0 ) {//background
            sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
        }

        super.onMessageReceived(remoteMessage);
    }

    private void sendNotification(String messageBody, String messageTitle) {

        Intent intent = new Intent(this, WebViewActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        final String CHANNEL_ID = "my_channel_02";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "pushName",
                NotificationManager.IMPORTANCE_HIGH
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.hello) // working well for foreground, data in body(not notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(this).notify(1, notification.build());
    }
}