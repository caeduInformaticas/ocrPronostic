package com.aasana.caedu.ocrprognostic.controller.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.aasana.caedu.ocrprognostic.R;

public class NotificationHelper extends ContextWrapper {
    public static String chanelId = "chanel_id";
    public static String chanelName ="chanel_name";
    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= (Build.VERSION_CODES.O)){
            createChanels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChanels() {
        NotificationChannel channel = new NotificationChannel(chanelId,chanelName,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
//        channel.getSound();
        channel.setLightColor(R.color.md_blue_400);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);

    }
    public NotificationManager getManager(){
        if (mManager==null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message){
        Intent intent = new Intent(this,NotificationActivity.class);
        PendingIntent resulPending = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(),chanelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setAutoCancel(true)
                .setContentIntent(resulPending);
    }
}
