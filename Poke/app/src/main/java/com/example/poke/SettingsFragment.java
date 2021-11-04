package com.example.poke;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SettingsFragment extends PreferenceFragmentCompat {

    private DatabaseReference mDatabase;
    private String uid;
    private FirebaseUser user;
    private SwitchPreference pref;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        sharedPreferences = getContext().getSharedPreferences("bibleNotify", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        pref = findPreference("bibleNotify");
        pref.setOnPreferenceChangeListener(preferenceChangeListener);

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        if(key.equals("logout")) {
            FirebaseAuth.getInstance().signOut();
            myStartActivity(LoginActivity.class);
        }

        if(key.equals("pwReset")) {
            myStartActivity(PasswordResetActivity.class);
        }

        if(key.equals("memberRevoke")) {
            revokeAccess();
            Toast.makeText(getContext(), "회원탈퇴를 완료했습니다.", Toast.LENGTH_SHORT).show();
            myStartActivity(LoginActivity.class);
        }

        return super.onPreferenceTreeClick(preference);

    }

    Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean switched = ((SwitchPreference) preference).isChecked();
            if(switched) {
                editor.putString("start","no");
                editor.putString("checked","no");
                editor.putString("flag","no");
                editor.apply();
                editor.commit();
                setAlarm.cancel(getContext(),sharedPreferences);
            } else {
                editor.putString("start","yes");
                editor.putString("checked","yes");
                editor.apply();
                editor.commit();
            }
            return true;
        }
    };

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
        Intent intent = new Intent(getContext(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
