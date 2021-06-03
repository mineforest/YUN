package com.example.poke;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CallAsyncTask();
    }

    private void CallAsyncTask(){
        SplashActivity.MyAsyncTask myAsyncTask = new SplashActivity.MyAsyncTask();
        myAsyncTask.execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Integer>{
        ProgressDialog asyncDialog = new ProgressDialog(SplashActivity.this);

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
