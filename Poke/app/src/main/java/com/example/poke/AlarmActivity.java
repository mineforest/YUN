package com.example.poke;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AlarmActivity extends AppCompatActivity {
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("bibleNotify",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Switch switchButton = (Switch) findViewById(R.id.alarm_switch);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    Log.d("switch","on");
                    editor.putString("start","yes");
                    editor.putString("checked","yes");
                    editor.apply();
                    editor.commit();
                    setAlarm.startAlarmBroadcastReceiver(mContext,sharedPreferences);
                }else{
                    Log.d("switch","off");
                    editor.putString("start","no");
                    editor.putString("checked","no");
                    editor.apply();
                    editor.commit();
                    setAlarm.cancel(mContext,sharedPreferences);
                }
            }
        });
        if (sharedPreferences.getString("checked","yes").equals("yes")){

            switchButton.setChecked(true);

        }else {

            switchButton.setChecked(false);
        }
    }

}
