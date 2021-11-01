package com.example.poke;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.app.AlarmManager;
import android.util.Log;
import android.app.NotificationChannel;

import static android.content.Context.MODE_PRIVATE;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    // Notification
    String CHANNEL_ID = "ingrenotify";
    NotificationChannel notificationChannel;
    CharSequence name = "Ingre Notify";

    @Override
    public void onReceive(Context context, Intent intent) {
        // build and show notification
        try {
            Log.d("test","show noti");
            showNotification(context);

        } catch (Exception e) {
        }
        // Start a new alarm
        Intent intent1 = new Intent(context, AlarmBroadcastReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent1, 0);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 60 * 24), pendingIntent);
    }

    // build Notification
    public void showNotification(Context context) {

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("flag",true);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        Notification.Builder NotificationBuilder;

        // check Android API and do as needed

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationBuilder = new Notification.Builder(context, CHANNEL_ID);
        } else {
            NotificationBuilder = new Notification.Builder(context);
        }
        Notification.Builder mBuilder = NotificationBuilder;

        mBuilder.setSmallIcon(R.mipmap.ic_launcher_image);
        mBuilder.setContentTitle("POKE");
        mBuilder.setContentText("유통기한이 3일 이하인 재료가 있습니다!!");
        mBuilder.setAutoCancel(true);
        mBuilder.setVibrate(new long[]{0,3000});
        mBuilder.setContentIntent(contentIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID);
        }
        mNotificationManager.notify(1, mBuilder.build());
    }
}