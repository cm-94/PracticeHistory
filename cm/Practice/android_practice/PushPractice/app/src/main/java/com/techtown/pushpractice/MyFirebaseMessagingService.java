package com.techtown.pushpractice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {
    }

    private static final String TAG = "FMS";

    // 새로운 토큰을 확인했을 때 호출되는 메서드
    // TODO : 서버로 토큰값 다시 보내기(일단 로그인때랑 똑같이)
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG,"onNewToken() 호출됨: "+s);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"onMessageReceived() 호출");

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String ekey = remoteMessage.getData().get("ekey");
        String ename = remoteMessage.getData().get("ename");

        if (title != null && body != null && ekey != null && ename != null){
            Log.d(TAG,"Title: "+title+", body: "+body+", ekey: "+ekey);

            sendNotification(title,body);
            newActivityIntent(ekey,ename);
        }
    }

    private void newActivityIntent(String key,String name){
        Log.d(TAG, "sendToActivity() 호출됨. ekey: " +key);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);

        intent.putExtra("ekey",Integer.valueOf(key));
        intent.putExtra("ename",name);

        // Service에서 Activity 띄우려면 Intent에 Flag를 주어야 됨..!!
        // MainActivity가 이미 메모리에 올라가 있을경우 MainActivity의 onNewIntent()메서드로 데이터가 전달됨!!
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                Intent.FLAG_ACTIVITY_SINGLE_TOP|
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void sendNotification(String title, String text) {
        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
        Log.d(TAG, "sendNotification호출됨");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent1,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
