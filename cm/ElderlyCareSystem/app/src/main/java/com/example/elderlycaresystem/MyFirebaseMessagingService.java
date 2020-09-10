package com.example.elderlycaresystem;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.elderlycaresystem.ui.info.InfoActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.security.cert.CertPathBuilder;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {
    }

    private static final String TAG = "FMS";

    // 새로운 토큰을 확인했을 때 호출되는 메서드
    @Override
    // TODO : 새로운 토큰값 서버로 보내기
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG, "onNewToken() 호출됨: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived() 호출");

        String ekey=null;
        String title=null;
        String body=null;
        String url=null;

        // TODO : null check 넣기
        Map<String,String> data = remoteMessage.getData();
        if (data.get("ekey")!=null){
            ekey = data.get("ekey");
        }
        if (data.get("ekey")!=null){
            title = data.get("title");
        }
        if (data.get("ekey")!=null){
            body = data.get("body");
        }
        if (data.get("ekey")!=null){
            url = data.get("url");
        }
        Log.d(TAG, "Title: " + title + ", body: " + body + ", ekey: " + ekey + ", url: " + url);

        if (title != null && body != null) {
            if (url.equals("null")) {
                if (ekey != null) {
                    sendNotification(title, body,ekey);
                }
            }else{
                homeNotification(title,body,url);

            }
        }
    }

    public void homeNotification(String title, String text, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Log.d(TAG, "homeNotification호출됨");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
//                .setOngoing(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }


    public void sendNotification(String title, String text,String ekey) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ekey",Integer.parseInt(ekey));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(InfoActivity.class);
        stackBuilder.addNextIntent(intent);

        Log.d(TAG, "sendNotification호출됨");
        Log.d(TAG, "ekey:"+ekey);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
//                .setOngoing(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(2 /* ID of notification */, notificationBuilder.build());
    }
}
