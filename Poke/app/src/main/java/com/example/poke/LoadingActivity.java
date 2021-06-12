package com.example.poke;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoadingActivity extends Activity {

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
        ProgressDialog asyncDialog = new ProgressDialog(LoadingActivity.this);

        @Override
        protected void onPreExecute() {
            try{
                Thread.sleep(1000);

            }catch (Exception e){
                e.printStackTrace();
            }
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(asyncDialog.isShowing()) asyncDialog.dismiss();

            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            super.onPostExecute(integer);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            if(asyncDialog.isShowing()) asyncDialog.dismiss();
        }
    }
}
