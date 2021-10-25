package com.example.poke;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class W2vHttpConn {
    OkHttpClient client = new OkHttpClient();
    String ip_addr = "http://192.168.200.172:5000/";

    public ArrayList<Recipe_get> getRcp(String id, int flag) {
        String dir = "rcp/";
        ArrayList<Recipe_get> arrayList = new ArrayList<>();
        if (flag >= 1)  dir = "user/"; // flag가 1이상이면 유저검색
        Request request = new Request.Builder().url(ip_addr + dir + id).addHeader("Connection", "close").build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray jsonArray = jsonObject.getJSONArray("recipe");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                    Recipe_get recipe_get = new Recipe_get(object.getString("id"), object.getString("score"));
                    arrayList.add(recipe_get);
            }
            return arrayList;
        } catch (IOException | JSONException e) {
            Log.d("IOE","IOEXCEPTION");
        }
        return null;
    }
}
