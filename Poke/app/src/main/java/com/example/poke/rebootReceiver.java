package com.example.poke;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.poke.setAlarm;

public class rebootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            final SharedPreferences sharedPreferences = context.getSharedPreferences("ingreNotify", 0);
            setAlarm.startAlarmBroadcastReceiver(context, sharedPreferences);
        }
    }


}