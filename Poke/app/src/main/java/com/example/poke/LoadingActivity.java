package com.example.poke;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoadingActivity extends Activity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CallAsyncTask();
    }

    private void CallAsyncTask(){
        LoadingActivity.MyAsyncTask myAsyncTask = new LoadingActivity.MyAsyncTask();
        myAsyncTask.execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected void onPreExecute() {
            try{
                Thread.sleep(500);

            }catch (Exception e){
                e.printStackTrace();
            }

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try{
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                if(user == null) {
                    myStartActivity(LoginActivity.class);
                }
                else{
                    uid = user.getUid();
                    nullStartActivity(uid);
                }
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            try{
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(integer);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }

    private void nullStartActivity(String uid){
        mDatabase.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if(!dataSnapshot.child("users").child(uid).exists()){
                    myStartActivity(MemberInitActivity.class);
                }
                else if(!dataSnapshot.child("preference").child(uid).exists()){
                    myStartActivity(PreferenceActivity.class);
                }
                else{
                    myStartActivity(MainActivity.class);
                }
            }
            else {
                Log.e("firebase", "Error getting data", task.getException());
            }
        });
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(getApplicationContext(),c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
