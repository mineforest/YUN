package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SettingActivity extends AppCompatActivity {
    private Context mContext;
    private DatabaseReference mDatabase;
    private TextView logoutBtn;
    private TextView pwReset;
    private TextView memberRevoke;
    private SwitchMaterial switchButton;
    private SwitchMaterial timerSoundOneTwoDropTheBeat;
    private String uid;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        logoutBtn = findViewById(R.id.logoutBtn);
        pwReset = findViewById(R.id.pwReset);
        memberRevoke = findViewById(R.id.memberRevoke);
        switchButton = findViewById(R.id.alarm_switch);
        timerSoundOneTwoDropTheBeat = findViewById(R.id.timerSetting);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("bibleNotify",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switchButton.setChecked(sharedPreferences.getString("checked", "yes").equals("yes"));
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putString("start","yes");
                    editor.putString("checked","yes");
                    editor.apply();
                    editor.commit();
                    //setAlarm.startAlarmBroadcastReceiver(mContext,sharedPreferences);
                }else{
                    editor.putString("start","no");
                    editor.putString("checked","no");
                    editor.putString("flag","no");
                    editor.apply();
                    editor.commit();
                    setAlarm.cancel(mContext,sharedPreferences);
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                myStartActivity(LoginActivity.class);
            }
        });

        pwReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(PasswordResetActivity.class);
            }
        });

        memberRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
                Toast.makeText(SettingActivity.this, "회원탈퇴를 완료했습니다.", Toast.LENGTH_SHORT).show();
                myStartActivity(LoginActivity.class);
            }
        });

    }

    private void revokeAccess() {
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.child(DataBaseCategory.allergy.toString()).child(uid).removeValue();
                        mDatabase.child(DataBaseCategory.dips.toString()).child(uid).removeValue();
                        mDatabase.child(DataBaseCategory.history.toString()).child(uid).removeValue();
                        mDatabase.child(DataBaseCategory.ingredient.toString()).child(uid).removeValue();
                        mDatabase.child(DataBaseCategory.preference.toString()).child(uid).removeValue();
                        mDatabase.child(DataBaseCategory.users.toString()).child(uid).removeValue();
                    }
                });
                t.start();
                try{
                    t.sleep(500);
                }catch(Exception ignored){}
            }
        });
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(SettingActivity.this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
