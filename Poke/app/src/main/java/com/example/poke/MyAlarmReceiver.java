package com.example.poke;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        System.out.println("hello we are here from the alarm reciever method " + currentDateTimeString);

        final PeriodicWorkRequest PeriodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class,1, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance().enqueue(PeriodicWorkRequest);

    }
}