package com.example.poke;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class W2vHttpConn {
    OkHttpClient client = new OkHttpClient();

    public String[] getData(String rid){
        //Request request = new Request.Builder().url("http://34.64.118.37/"+rid).addHeader("Connection", "close").build();
        Request request = new Request.Builder().url("http://192.168.200.116:5000/"+rid).addHeader("Connection", "close").build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string().split(" ");
            //return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
