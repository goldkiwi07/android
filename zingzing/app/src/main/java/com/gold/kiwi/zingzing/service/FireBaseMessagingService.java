package com.gold.kiwi.zingzing.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import android.os.Vibrator;
import android.util.Log;

import com.gold.kiwi.common.LOG;
import com.gold.kiwi.zingzing.activity.ActMain;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.gold.kiwi.zingzing.R;

import java.util.Arrays;

public class FireBaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
        LOG.d("FirebaseInstanceId FCM Log", "Refreshed token: " + token);
    }
    private void sendRegistrationToServer(String token){
        //디바이스 토큰이 생성되거나 재생성 될 시 동작할 코드 작성
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LOG.d("Message data payload: " , remoteMessage.getData() + "");
        //String msg = remoteMessage.getData().get("message");
        String time = remoteMessage.getData().get("time");
        LOG.d("Message data payload time : " , time+ "");
        if("".equals(time)||null == time){
            if (remoteMessage.getNotification() != null) {
                Log.d("FCM Log", "알림 메시지: " + remoteMessage.getNotification().getBody());
                String messageBody = remoteMessage.getNotification().getBody();
                String messageTitle = remoteMessage.getNotification().getTitle();
                Intent intent = new Intent(this, ActMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                String channelId = "Channel ID";
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(messageTitle)
                                .setContentText(messageBody)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelName = "Channel Name";
                    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }
                notificationManager.notify(0, notificationBuilder.build());
            }

            return;
        }
        String[] tmp = time.split(",");
        int s = tmp.length;
        LOG.d("Message data payload s : " , s+ "");
        long[] patternTmp = new long[s+1];
        for(int i = 0; i<tmp.length; i++){
            patternTmp[i] = Long.parseLong(tmp[i]);
        }

        for(int i =0; i<patternTmp.length;i++){
            LOG.d("Message data payload patternTmp : " , patternTmp[i]+ "");
        }
        //Arrays.stream(tmp).mapToInt(Integer::parseInt).toArray();
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        //long[] pattern = {100,300,100,300,100,300,100,300,100}; // miliSecond
        //           대기,진동,대기,진동,....
        // 짝수 인덱스 : 대기시간
        // 홀수 인덱스 : 진동시간
        vibrator.vibrate(patternTmp, -1);
        //vibrator.vibrate(pattern, -1);

        return;
    }
}