package com.example.poke;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import java.util.Calendar;
import android.content.Context;
import android.util.Log;

public class setAlarm {
    public static int hour = 19;
    public static int min = 00;
    public static int sec = 00;

    public static void startAlarmBroadcastReceiver(Context context, SharedPreferences sharedPreferences) {

//        if (sharedPreferences.contains("SetTimeH")) {
//            min = sharedPreferences.getInt("SetTimeM", min);
//            hour = sharedPreferences.getInt("SetTimeH", sec);
//        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
            if(sharedPreferences.getString("start","yes").equals("yes")){
                // Start Alarm
                Intent _intent = new Intent(context, AlarmBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 60 * 60 * 24, pendingIntent);
            }
    }

    public static void cancel(Context context,SharedPreferences sharedPreferences){
        Log.d("cancel","cancel!!");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent _intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, _intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        pendingIntent.cancel();
        alarmManager=null;
        pendingIntent=null;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("start","no");
        editor.commit();
    }


}