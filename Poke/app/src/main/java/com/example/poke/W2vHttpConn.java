package com.example.poke;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class W2vHttpConn {
    OkHttpClient client = new OkHttpClient();

    public String[] getData(String rid) {
            //Request request = new Request.Builder().url("http://34.64.118.37/"+rid).addHeader("Connection", "close").build();
            Request request = new Request.Builder().url("http://192.168.200.163:5000/"+rid).addHeader("Connection", "close").build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string().split(" ");
//            } catch (SocketTimeoutException e) {
//                Log.d("SOCKET","SOCKET ERROR");
            } catch (IOException e) {
                Log.d("IOE","IOEXCEPTION");
            }
            return null;
    }
}
